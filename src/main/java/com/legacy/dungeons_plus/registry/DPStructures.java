package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DPConfig;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.end_ruins.EndRuinsPools;
import com.legacy.dungeons_plus.structures.end_ruins.EndRuinsStructure;
import com.legacy.dungeons_plus.structures.leviathan.LeviathanPools;
import com.legacy.dungeons_plus.structures.leviathan.LeviathanStructure;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsPools;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsStructure;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsType;
import com.legacy.dungeons_plus.structures.snowy_temple.SnowyTemplePools;
import com.legacy.dungeons_plus.structures.snowy_temple.SnowyTempleStructure;
import com.legacy.dungeons_plus.structures.soul_prison.SoulPrisonPieces;
import com.legacy.dungeons_plus.structures.soul_prison.SoulPrisonStructure;
import com.legacy.dungeons_plus.structures.tower.TowerPieces;
import com.legacy.dungeons_plus.structures.tower.TowerStructure;
import com.legacy.dungeons_plus.structures.warped_garden.WarpedGardenPieces;
import com.legacy.dungeons_plus.structures.warped_garden.WarpedGardenStructure;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride.BoundingBoxType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPStructures
{
	//@formatter:off
	public static final StructureRegistrar<NoneFeatureConfiguration, TowerStructure> TOWER = 
			StructureRegistrar.builder(DungeonsPlus.locate("tower"), () -> new TowerStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.tower))
				.addPiece(TowerPieces.Piece::new)
				.pushConfigured(NoneFeatureConfiguration.INSTANCE)
					.biomes(DPConfig.COMMON.tower.getConfigured())
				.popConfigured()
				.build();

	public static final StructureRegistrar<ReanimatedRuinsStructure.Configuration, ReanimatedRuinsStructure> REANIMATED_RUINS = 
			StructureRegistrar.builder(DungeonsPlus.locate("reanimated_ruins"), () -> new ReanimatedRuinsStructure(ReanimatedRuinsStructure.Configuration.CODEC, DPConfig.COMMON.reanimatedRuins))
				.addPiece(ReanimatedRuinsStructure.Piece::new)
				.pushConfigured("mossy", new ReanimatedRuinsStructure.Configuration(ReanimatedRuinsPools.MOSSY_ROOT, 10, ReanimatedRuinsType.MOSSY))
					.biomes(Biomes.SWAMP)
				.popConfigured()
				.pushConfigured("desert", new ReanimatedRuinsStructure.Configuration(ReanimatedRuinsPools.DESERT_ROOT, 10, ReanimatedRuinsType.DESERT))
					.biomes(Biomes.DESERT)
				.popConfigured()
				.pushConfigured("frozen", new ReanimatedRuinsStructure.Configuration(ReanimatedRuinsPools.FROZEN_ROOT, 10, ReanimatedRuinsType.FROZEN))
					.biomes(Biomes.SNOWY_PLAINS)
				.popConfigured()
				.build();

	public static final StructureRegistrar<JigsawConfiguration, LeviathanStructure> LEVIATHAN = 
			StructureRegistrar.builder(DungeonsPlus.locate("leviathan"), () -> new LeviathanStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.leviathan))
				.addPiece(LeviathanStructure.Piece::new)
				.pushConfigured(new JigsawConfiguration(LeviathanPools.ROOT, 7))
					.biomes(DPConfig.COMMON.leviathan.getConfigured())
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, new SpawnerData(EntityType.HUSK, 1, 1, 4))
				.popConfigured()
				.build();

	public static final StructureRegistrar<JigsawConfiguration, SnowyTempleStructure> SNOWY_TEMPLE = 
			StructureRegistrar.builder(DungeonsPlus.locate("snowy_temple"), () -> new SnowyTempleStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.snowyTemple))
				.addPiece(SnowyTempleStructure.Piece::new)
				.pushConfigured(new JigsawConfiguration(SnowyTemplePools.ROOT, 7))
					.biomes(DPConfig.COMMON.snowyTemple.getConfigured())
					.adaptNoise()
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, new SpawnerData(EntityType.STRAY, 1, 1, 4))
				.popConfigured()
				.build();

	public static final StructureRegistrar<JigsawConfiguration, EndRuinsStructure> END_RUINS = 
			StructureRegistrar.builder(DungeonsPlus.locate("end_ruins"), () -> new EndRuinsStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.endRuins))
				.addPiece(EndRuinsStructure.Piece::new)
				.pushConfigured(new JigsawConfiguration(EndRuinsPools.ROOT, 7))
					.biomes(DPConfig.COMMON.endRuins.getConfigured())
				.popConfigured()
				.build();

	public static final StructureRegistrar<NoneFeatureConfiguration, WarpedGardenStructure> WARPED_GARDEN = 
			StructureRegistrar.builder(DungeonsPlus.locate("warped_garden"), () -> new WarpedGardenStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.warpedGarden))
				.addPiece(WarpedGardenPieces.Piece::new)
				.pushConfigured(NoneFeatureConfiguration.NONE)
					.biomes(DPConfig.COMMON.warpedGarden.getConfigured())
				.popConfigured()
				.build();

	public static final StructureRegistrar<NoneFeatureConfiguration, SoulPrisonStructure> SOUL_PRISON = 
			StructureRegistrar.builder(DungeonsPlus.locate("soul_prison"), () -> new SoulPrisonStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.soulPrison))
				.addPiece(SoulPrisonPieces.Piece::new)
				.pushConfigured(NoneFeatureConfiguration.NONE)
					.biomes(DPConfig.COMMON.soulPrison.getConfigured())
				.popConfigured()
				.build();

	//@formatter:on

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<StructureFeature<?>> event)
	{
		// TODO "init" maybe not needed anymore?
		ReanimatedRuinsPools.init();
		LeviathanPools.init();
		SnowyTemplePools.init();
		EndRuinsPools.init();

		// TODO DummyStructure.createDummy("dungeons_plus:bigger_dungeon");
	}
}