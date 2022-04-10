package com.legacy.dungeons_plus.events;

import static com.legacy.dungeons_plus.DungeonsPlus.*;

import java.util.function.Consumer;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.providers.DPAdvancementProv;
import com.legacy.dungeons_plus.data.providers.DPLangProvider;
import com.legacy.dungeons_plus.data.providers.DPLootProv;
import com.legacy.dungeons_plus.data.providers.DPTagProv;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.entity.EntityAccessHelper;
import com.legacy.structure_gel.api.events.RegisterLootTableAliasEvent;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class DPCommonEvents
{
	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBus
	{
		@SubscribeEvent
		protected static void onEntitySpawn(final EntityJoinWorldEvent event)
		{
			Level level = event.getWorld();
			if (!level.isClientSide)
			{
				Entity entity = event.getEntity();

				ifInStructure(entity, EntityType.HUSK, DPStructures.LEVIATHAN, e -> EntityAccessHelper.setDeathLootTable(e, DPLoot.Leviathan.ENTITY_HUSK));
				ifInStructure(entity, EntityType.STRAY, DPStructures.SNOWY_TEMPLE, e -> EntityAccessHelper.setDeathLootTable(e, DPLoot.SnowyTemple.ENTITY_STRAY));
				ifInStructure(entity, EntityType.ENDERMAN, DPStructures.END_RUINS, e -> e.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((EnderMan) e, Player.class, true, false)));
				ifInStructure(entity, EntityType.GHAST, DPStructures.SOUL_PRISON, e -> e.targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((Ghast) e, Player.class, true, false)));
			}
		}

		@SuppressWarnings("unchecked")
		private static <T extends Entity> void ifInStructure(Entity entity, EntityType<T> entityTest, StructureRegistrar<?, ?> structure, Consumer<T> consumer)
		{
			if (entity.getType().equals(entityTest) && entity.level instanceof ServerLevel serverLevel && StructureAccessHelper.isInStructurePiece(serverLevel, structure.getStructure(), entity.blockPosition()))
				consumer.accept((T) entity);
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
			DungeonsPlus.isQuarkLoaded = modList.isLoaded("quark");
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

			event.register(locate("leviathan/husk"), DPLoot.Leviathan.ENTITY_HUSK);

			// Snowy Temple
			event.register(locate("snowy_temple/common"), DPLoot.SnowyTemple.CHEST_COMMON);

			event.register(locate("snowy_temple/stray"), DPLoot.SnowyTemple.ENTITY_STRAY);

			// Warped Garden
			event.register(locate("warped_garden/common"), DPLoot.WarpedGarden.CHEST_COMMON);

			// Soul Prison
			event.register(locate("soul_prison/common"), DPLoot.SoulPrison.CHEST_COMMON);
			event.register(locate("soul_prison/golden_armor"), DPLoot.SoulPrison.CHEST_GOLDEN_ARMOR);
		}

		@SubscribeEvent
		protected static void gatherData(final GatherDataEvent event)
		{			
			DataGenerator gen = event.getGenerator();
			ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

			// data
			BlockTagsProvider blockTagProv = new DPTagProv.BlockProv(gen, event.getExistingFileHelper());
			gen.addProvider(blockTagProv);
			gen.addProvider(new DPTagProv.ItemProv(gen, blockTagProv, existingFileHelper));
			gen.addProvider(new DPTagProv.ConfiguredStructureFeatureProv(gen, existingFileHelper));
			gen.addProvider(new DPAdvancementProv(gen));
			gen.addProvider(new DPLootProv(gen));

			// assets
			gen.addProvider(new DPLangProvider(gen));
		}
	}
}
