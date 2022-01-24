package com.legacy.dungeons_plus.events;

import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPLoot;
import com.legacy.dungeons_plus.data.managers.DPSpawners;
import com.legacy.dungeons_plus.data.providers.DPAdvancementProv;
import com.legacy.dungeons_plus.data.providers.DPLangProvider;
import com.legacy.dungeons_plus.data.providers.DPLootProv;
import com.legacy.dungeons_plus.data.providers.DPTagProv;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.entity.EntityAccessHelper;
import com.legacy.structure_gel.api.events.AddStructureToBiomeEvent;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class DPEvents
{
	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBus
	{
		private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> TOWER_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.Tower.ZOMBIE, EntityType.SPIDER, DPLoot.Tower.SPIDER, EntityType.SKELETON, DPLoot.Tower.SKELETON));
		private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> BURIED_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.BiggerDungeon.ZOMBIE, EntityType.SKELETON, DPLoot.BiggerDungeon.SKELETON));

		@SubscribeEvent
		protected static void onEntitySpawn(final EntityJoinWorldEvent event)
		{
			Level level = event.getWorld();
			if (!level.isClientSide)
			{
				Entity entity = event.getEntity();

				TOWER_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DPStructures.TOWER, e -> EntityAccessHelper.setDeathLootTable((Mob) e, loot)));
				BURIED_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DPStructures.BIGGER_DUNGEON, e -> EntityAccessHelper.setDeathLootTable((Mob) e, loot)));
				ifInStructure(entity, EntityType.HUSK, DPStructures.LEVIATHAN, e -> EntityAccessHelper.setDeathLootTable((Mob) e, DPLoot.Leviathan.HUSK));
				ifInStructure(entity, EntityType.STRAY, DPStructures.SNOWY_TEMPLE, e -> EntityAccessHelper.setDeathLootTable((Mob) e, DPLoot.SnowyTemple.STRAY));
				ifInStructure(entity, EntityType.ENDERMAN, DPStructures.END_RUINS, e -> ((EnderMan) e).targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((EnderMan) e, Player.class, true, false)));
				ifInStructure(entity, EntityType.GHAST, DPStructures.SOUL_PRISON, e -> ((Ghast) e).targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((Ghast) e, Player.class, true, false)));
			}
		}

		private static void ifInStructure(Entity entity, EntityType<?> entityTest, StructureRegistrar<?, ?> structure, Consumer<Entity> consumer)
		{
			if (entity.getType().equals(entityTest) && entity.level instanceof ServerLevel && ((ServerLevel) entity.level).structureFeatureManager().getStructureWithPieceAt(entity.blockPosition(), structure.getStructure()).isValid())
				consumer.accept(entity);
		}

		@SubscribeEvent
		protected static void addStructureToBiome(final AddStructureToBiomeEvent event)
		{
			event.register(DPStructures.TOWER);
			event.register(DPStructures.BIGGER_DUNGEON);
			event.register(DPStructures.LEVIATHAN);
			event.register(DPStructures.SNOWY_TEMPLE);
			event.register(DPStructures.WARPED_GARDEN);

			event.register(DPStructures.END_RUINS);
			event.register(DPStructures.SOUL_PRISON);
		}
	}

	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBus
	{
		@SubscribeEvent
		protected static void commonInit(final FMLCommonSetupEvent event)
		{
			ModList modList = ModList.get();
			DungeonsPlus.isLootrLoaded = modList.isLoaded("lootr");
			DungeonsPlus.isWaystonesLoaded = modList.isLoaded("waystones");
			DungeonsPlus.isQuarkLoaded = modList.isLoaded("quark");
			
			DPSpawners.init();
		}

		@SubscribeEvent
		public static void gatherData(final GatherDataEvent event)
		{
			DataGenerator gen = event.getGenerator();
			ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

			// data
			BlockTagsProvider blockTagProv = new DPTagProv.Block(gen, event.getExistingFileHelper());
			gen.addProvider(blockTagProv);
			gen.addProvider(new DPTagProv.Item(gen, blockTagProv, existingFileHelper));
			gen.addProvider(new DPAdvancementProv(gen));
			gen.addProvider(new DPLootProv(gen));

			// assets
			gen.addProvider(new DPLangProvider(gen));
		}
	}
}
