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
import com.legacy.dungeons_plus.structures.snowy_temple.SnowyTemplePieces;
import com.legacy.dungeons_plus.structures.snowy_temple.SnowyTempleStructure;
import com.legacy.dungeons_plus.structures.soul_prison.SoulPrisonPieces;
import com.legacy.dungeons_plus.structures.soul_prison.SoulPrisonStructure;
import com.legacy.dungeons_plus.structures.tower.TowerPieces;
import com.legacy.dungeons_plus.structures.tower.TowerStructure;
import com.legacy.dungeons_plus.structures.warped_garden.WarpedGardenPools;
import com.legacy.dungeons_plus.structures.warped_garden.WarpedGardenStructure;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride.BoundingBoxType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// TODO Future: Sculk
// TODO Future: Copper
// TODO Future: Slime
@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPStructures
{	
	//@formatter:off
	public static final StructureRegistrar<NoneFeatureConfiguration, TowerStructure> TOWER = 
			StructureRegistrar.builder(DungeonsPlus.locate("tower"), () -> new TowerStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.tower))
				.addPiece(TowerPieces.Piece::new)
				.pushConfigured(NoneFeatureConfiguration.INSTANCE)
					.biomes(DPConfig.COMMON.tower.getConfigured())
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.build();

	public static final StructureRegistrar<ReanimatedRuinsStructure.Configuration, ReanimatedRuinsStructure> REANIMATED_RUINS = 
			StructureRegistrar.builder(DungeonsPlus.locate("reanimated_ruins"), () -> new ReanimatedRuinsStructure(ReanimatedRuinsStructure.Configuration.CODEC, DPConfig.COMMON.reanimatedRuins))
				.addPiece(ReanimatedRuinsStructure.Piece::new)
				.pushConfigured(ReanimatedRuinsType.MOSSY.toString(), new ReanimatedRuinsStructure.Configuration(ReanimatedRuinsPools.MOSSY_ROOT, 10, ReanimatedRuinsType.MOSSY))
					.biomes(DPConfig.COMMON.reanimatedRuins.getConfigured(ReanimatedRuinsType.MOSSY.toString()))
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.pushConfigured(ReanimatedRuinsType.MESA.toString(), new ReanimatedRuinsStructure.Configuration(ReanimatedRuinsPools.MESA_ROOT, 10, ReanimatedRuinsType.MESA))
					.biomes(DPConfig.COMMON.reanimatedRuins.getConfigured(ReanimatedRuinsType.MESA.toString()))
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.pushConfigured(ReanimatedRuinsType.FROZEN.toString(), new ReanimatedRuinsStructure.Configuration(ReanimatedRuinsPools.FROZEN_ROOT, 10, ReanimatedRuinsType.FROZEN))
					.biomes(DPConfig.COMMON.reanimatedRuins.getConfigured(ReanimatedRuinsType.FROZEN.toString()))
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.build();

	public static final StructureRegistrar<JigsawConfiguration, LeviathanStructure> LEVIATHAN = 
			StructureRegistrar.builder(DungeonsPlus.locate("leviathan"), () -> new LeviathanStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.leviathan))
				.addPiece(LeviathanStructure.Piece::new)
				.pushConfigured(new JigsawConfiguration(LeviathanPools.ROOT, 7))
					.biomes(DPConfig.COMMON.leviathan.getConfigured())
					.noSpawns(BoundingBoxType.PIECE)
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, new SpawnerData(EntityType.HUSK, 1, 1, 4))
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.build();

	public static final StructureRegistrar<NoneFeatureConfiguration, SnowyTempleStructure> SNOWY_TEMPLE = 
			StructureRegistrar.builder(DungeonsPlus.locate("snowy_temple"), () -> new SnowyTempleStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.snowyTemple))
				.addPiece(SnowyTemplePieces.Piece::new)
				.pushConfigured(NoneFeatureConfiguration.NONE)
					.biomes(DPConfig.COMMON.snowyTemple.getConfigured())
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, new SpawnerData(EntityType.STRAY, 1, 1, 4))
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.build();

	public static final StructureRegistrar<JigsawConfiguration, WarpedGardenStructure> WARPED_GARDEN = 
			StructureRegistrar.builder(DungeonsPlus.locate("warped_garden"), () -> new WarpedGardenStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.warpedGarden))
				.addPiece(WarpedGardenStructure.Piece::new)
				.pushConfigured(new JigsawConfiguration(WarpedGardenPools.ROOT, 4))
					.biomes(DPConfig.COMMON.warpedGarden.getConfigured())
					.spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, new SpawnerData(EntityType.DROWNED, 1, 1, 4))
					.dimensions(Level.OVERWORLD)
				.popConfigured()
				.build();

	public static final StructureRegistrar<JigsawConfiguration, EndRuinsStructure> END_RUINS = 
			StructureRegistrar.builder(DungeonsPlus.locate("end_ruins"), () -> new EndRuinsStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.endRuins))
				.addPiece(EndRuinsStructure.Piece::new)
				.pushConfigured(new JigsawConfiguration(EndRuinsPools.ROOT, 7))
					.biomes(DPConfig.COMMON.endRuins.getConfigured())
					.dimensions(Level.END)
				.popConfigured()
				.build();
	
	public static final StructureRegistrar<NoneFeatureConfiguration, SoulPrisonStructure> SOUL_PRISON = 
			StructureRegistrar.builder(DungeonsPlus.locate("soul_prison"), () -> new SoulPrisonStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.soulPrison))
				.addPiece(SoulPrisonPieces.Piece::new)
				.pushConfigured(NoneFeatureConfiguration.NONE)
					.biomes(DPConfig.COMMON.soulPrison.getConfigured())
					.spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, new SpawnerData(EntityType.SKELETON, 5, 1, 4), new SpawnerData(EntityType.GHAST, 1, 1, 2))
					.dimensions(Level.NETHER)
				.popConfigured()
				.build();

	//@formatter:on

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<StructureFeature<?>> event)
	{
	}
}