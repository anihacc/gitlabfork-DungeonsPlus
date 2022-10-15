package com.legacy.dungeons_plus.events;

import static com.legacy.dungeons_plus.DungeonsPlus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.legacy.dungeons_plus.DPConfig;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.advancement.ThrownItemHitBlockTrigger;
import com.legacy.dungeons_plus.data.providers.DPAdvancementProv;
import com.legacy.dungeons_plus.data.providers.DPLangProvider;
import com.legacy.dungeons_plus.data.providers.DPLootProv;
import com.legacy.dungeons_plus.data.providers.DPTagProv;
import com.legacy.dungeons_plus.registry.DPItems;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.entity.EntityAccessHelper;
import com.legacy.structure_gel.api.events.RegisterLootTableAliasEvent;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class DPCommonEvents
{
	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBus
	{
		public static void onEntitySpawn(Mob entity)
		{
			Level level = entity.getLevel();
			if (!level.isClientSide)
			{
				ifInStructure(entity, EntityType.HUSK, DPStructures.LEVIATHAN, e ->
				{
					if (DPConfig.COMMON.husksDropSand.get())
						EntityAccessHelper.setDeathLootTable(e, DPLoot.Leviathan.ENTITY_HUSK);
					RandomSource rand = e.getRandom();
					if (rand.nextFloat() < DPConfig.COMMON.huskLeviathanBladeChance.get())
					{
						var stack = DPItems.LEVIATHAN_BLADE.get().getDefaultInstance();
						stack.setDamageValue(rand.nextInt(stack.getItem().getMaxDamage(stack)));
						e.setItemSlot(EquipmentSlot.MAINHAND, stack);
						e.setDropChance(EquipmentSlot.MAINHAND, 0.12F);
					}
				});
				ifInStructure(entity, EntityType.STRAY, DPStructures.SNOWY_TEMPLE, e ->
				{
					if (DPConfig.COMMON.straysDropIce.get())
						EntityAccessHelper.setDeathLootTable(e, DPLoot.SnowyTemple.ENTITY_STRAY);
					RandomSource rand = e.getRandom();
					if (rand.nextFloat() < DPConfig.COMMON.strayFrostedCowlChance.get())
					{
						var stack = DPItems.FROSTED_COWL.get().getDefaultInstance();
						stack.setDamageValue(rand.nextInt(stack.getItem().getMaxDamage(stack)));
						e.setItemSlot(EquipmentSlot.HEAD, stack);
						e.setDropChance(EquipmentSlot.HEAD, 0.12F);
					}
				});
				ifInStructure(entity, EntityType.DROWNED, DPStructures.WARPED_GARDEN, e ->
				{
					RandomSource rand = e.getRandom();
					if (rand.nextFloat() < DPConfig.COMMON.drownedWarpedAxeChance.get())
					{
						var stack = DPItems.WARPED_AXE.get().getDefaultInstance();
						stack.setDamageValue(rand.nextInt(stack.getItem().getMaxDamage(stack)));
						e.setItemSlot(EquipmentSlot.MAINHAND, stack);
						e.setDropChance(EquipmentSlot.MAINHAND, 0.12F);
					}
					if (rand.nextFloat() < DPConfig.COMMON.drownedCoralChance.get())
					{
						var opTag = level.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY).getTag(BlockTags.CORAL_BLOCKS);
						if (opTag.isPresent() && opTag.get().size() > 0)
						{
							e.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(opTag.get().getRandomElement(rand).get().value()));
							e.setDropChance(EquipmentSlot.OFFHAND, 1.0F);
						}
					}
				});
				ifInStructure(entity, EntityType.SKELETON, DPStructures.SOUL_PRISON, e ->
				{
					RandomSource rand = e.getRandom();
					if (rand.nextFloat() < DPConfig.COMMON.skeletonSoulCannonChance.get())
					{
						var stack = DPItems.SOUL_CANNON.get().getDefaultInstance();
						stack.setDamageValue(rand.nextInt(stack.getItem().getMaxDamage(stack)));
						e.setItemSlot(EquipmentSlot.OFFHAND, stack);
						e.setDropChance(EquipmentSlot.OFFHAND, 0.30F);
					}
				});
				ifInStructure(entity, EntityType.GHAST, DPStructures.SOUL_PRISON, e ->
				{
					e.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(e, Player.class, true, false));
				});
				ifInStructure(entity, EntityType.ENDERMAN, DPStructures.END_RUINS, e ->
				{
					e.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(e, Player.class, true, false));
				});
			}
		}

		@SuppressWarnings("unchecked")
		private static <T extends Entity> void ifInStructure(Entity entity, EntityType<T> entityTest, StructureRegistrar<?> structure, Consumer<T> consumer)
		{
			if (entity.getType().equals(entityTest) && entity.level instanceof ServerLevel serverLevel && StructureAccessHelper.isInStructurePiece(serverLevel, structure.getType(), entity.blockPosition()))
				consumer.accept((T) entity);
		}

		@SubscribeEvent
		protected static void onEffectApply(final MobEffectEvent.Applicable event)
		{
			if (event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN)
			{
				List<ItemStack> strayArmors = new ArrayList<>(1);
				LivingEntity entity = event.getEntity();
				for (ItemStack stack : entity.getArmorSlots())
					if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == DPItems.DPArmors.STRAY)
						strayArmors.add(stack);

				int size = strayArmors.size();
				if (size > 0)
				{
					event.setResult(Result.DENY);
					if (entity instanceof ServerPlayer serverPlayer)
					{
						RandomSource rand = serverPlayer.getRandom();
						strayArmors.get(rand.nextInt(size)).hurt(2, rand, serverPlayer);
					}
				}
			}
		}
	}

	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBus
	{
		@SubscribeEvent
		protected static void commonInit(final FMLCommonSetupEvent event)
		{
			ModList modList = ModList.get();
			DungeonsPlus.isWaystonesLoaded = modList.isLoaded("waystones");

			event.enqueueWork(() ->
			{
				CriteriaTriggers.register(ThrownItemHitBlockTrigger.TRIGGER);
			});
		}

		@SubscribeEvent
		protected static void registerLootTableAlias(final RegisterLootTableAliasEvent event)
		{
			// Tower
			event.register(locate("tower/common"), DPLoot.Tower.CHEST_COMMON);
			event.register(locate("tower/barrel"), DPLoot.Tower.CHEST_BARREL);
			event.register(locate("tower/vex"), DPLoot.Tower.CHEST_VEX);
			event.register(locate("tower/vex_map"), DPLoot.Tower.CHEST_VEX_MAP);

			event.register(locate("tower/skeleton"), DPLoot.Tower.ENTITY_SKELETON);
			event.register(locate("tower/spider"), DPLoot.Tower.ENTITY_SPIDER);
			event.register(locate("tower/zombie"), DPLoot.Tower.ENTITY_ZOMBIE);

			// Reanimated ruins
			event.register(locate("reanimated_ruins/common"), DPLoot.ReanimatedRuins.CHEST_COMMON);
			event.register(locate("reanimated_ruins/desert"), DPLoot.ReanimatedRuins.CHEST_DESERT);
			event.register(locate("reanimated_ruins/desert_map"), DPLoot.ReanimatedRuins.CHEST_DESERT_MAP);
			event.register(locate("reanimated_ruins/frozen"), DPLoot.ReanimatedRuins.CHEST_FROZEN);
			event.register(locate("reanimated_ruins/frozen_map"), DPLoot.ReanimatedRuins.CHEST_FROZEN_MAP);
			event.register(locate("reanimated_ruins/mossy"), DPLoot.ReanimatedRuins.CHEST_MOSSY);
			event.register(locate("reanimated_ruins/mossy_map"), DPLoot.ReanimatedRuins.CHEST_MOSSY_MAP);

			event.register(locate("reanimated_ruins/skeleton"), DPLoot.ReanimatedRuins.ENTITY_SKELETON);
			event.register(locate("reanimated_ruins/zombie"), DPLoot.ReanimatedRuins.ENTITY_ZOMBIE);

			// Leviathan
			event.register(locate("leviathan/common"), DPLoot.Leviathan.CHEST_COMMON);
			event.register(locate("leviathan/rare"), DPLoot.Leviathan.CHEST_RARE);

			event.register(locate("leviathan/husk"), DPLoot.Leviathan.ENTITY_HUSK);

			// Snowy Temple
			event.register(locate("snowy_temple/common"), DPLoot.SnowyTemple.CHEST_COMMON);
			event.register(locate("snowy_temple/rare"), DPLoot.SnowyTemple.CHEST_RARE);

			event.register(locate("snowy_temple/stray"), DPLoot.SnowyTemple.ENTITY_STRAY);

			// Warped Garden
			event.register(locate("warped_garden/common"), DPLoot.WarpedGarden.CHEST_COMMON);
			event.register(locate("warped_garden/rare"), DPLoot.WarpedGarden.CHEST_RARE);

			// Soul Prison
			event.register(locate("soul_prison/common"), DPLoot.SoulPrison.CHEST_COMMON);
			event.register(locate("soul_prison/rare"), DPLoot.SoulPrison.CHEST_RARE);
			event.register(locate("soul_prison/golden_armor"), DPLoot.SoulPrison.CHEST_GOLDEN_ARMOR);
		}

		@SubscribeEvent
		protected static void gatherData(final GatherDataEvent event)
		{
			DataGenerator gen = event.getGenerator();
			ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

			// data
			BlockTagsProvider blockTagProv = new DPTagProv.BlockProv(gen, event.getExistingFileHelper());
			gen.addProvider(true, blockTagProv);
			gen.addProvider(true, new DPTagProv.ItemProv(gen, blockTagProv, existingFileHelper));
			gen.addProvider(true, new DPTagProv.StructureProv(gen, existingFileHelper));
			gen.addProvider(true, new DPTagProv.EntityTypeProv(gen, existingFileHelper));
			gen.addProvider(true, new DPTagProv.BiomeProv(gen, existingFileHelper));
			gen.addProvider(true, new DPTagProv.EnchantmentProv(gen, existingFileHelper));

			gen.addProvider(true, new DPAdvancementProv(gen));
			gen.addProvider(true, new DPLootProv(gen));

			// assets
			gen.addProvider(true, new DPLangProvider(gen));
		}
	}
}
