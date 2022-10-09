package com.legacy.dungeons_plus.registry;

import java.util.List;

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
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure;
import com.legacy.structure_gel.api.structure.GridStructurePlacement;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride.BoundingBoxType;

// TODO Future: Sculk
// TODO Future: Copper
// TODO Future: Slime
public class DPStructures
{
	//@formatter:off
	public static final StructureRegistrar<TowerStructure> TOWER = 
			StructureRegistrar.builder(DungeonsPlus.locate("tower"), () -> () -> TowerStructure.CODEC)
				.placement(() -> GridStructurePlacement.builder().spacing(31).config(() -> DPConfig.COMMON.tower).build(DPStructures.TOWER))
				.addPiece(() -> TowerPieces.Piece::new)
				.pushStructure(TowerStructure::new)
					.config(() -> DPConfig.COMMON.tower.getStructure())
					.terrainAdjustment(TerrainAdjustment.BEARD_THIN)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> REANIMATED_RUINS = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("reanimated_ruins"))
				.placement(() -> GridStructurePlacement.builder().spacing(36).config(() -> DPConfig.COMMON.reanimatedRuins).build(DPStructures.REANIMATED_RUINS))
				.addPiece(() -> ReanimatedRuinsStructure.Piece::new)
				.pushStructure(ReanimatedRuinsType.MOSSY.toString(), s -> ExtendedJigsawStructure.builder(s, ReanimatedRuinsPools.MOSSY_ROOT).maxDepth(10).startHeight(-15, 15).capability(new ReanimatedRuinsStructure.Capability(ReanimatedRuinsType.MOSSY)).build())
					.config(() -> DPConfig.COMMON.reanimatedRuins.getStructure(ReanimatedRuinsType.MOSSY.toString()))
					.terrainAdjustment(TerrainAdjustment.BURY)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.pushStructure(ReanimatedRuinsType.MESA.toString(), s -> ExtendedJigsawStructure.builder(s, ReanimatedRuinsPools.MESA_ROOT).maxDepth(10).startHeight(-15, 15).capability(new ReanimatedRuinsStructure.Capability(ReanimatedRuinsType.MESA)).build())
					.config(() -> DPConfig.COMMON.reanimatedRuins.getStructure(ReanimatedRuinsType.MESA.toString()))
					.terrainAdjustment(TerrainAdjustment.BURY)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.pushStructure(ReanimatedRuinsType.FROZEN.toString(), s -> ExtendedJigsawStructure.builder(s, ReanimatedRuinsPools.FROZEN_ROOT).maxDepth(10).startHeight(-15, 15).capability(new ReanimatedRuinsStructure.Capability(ReanimatedRuinsType.FROZEN)).build())
					.config(() -> DPConfig.COMMON.reanimatedRuins.getStructure(ReanimatedRuinsType.FROZEN.toString()))
					.terrainAdjustment(TerrainAdjustment.BURY)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> LEVIATHAN = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("leviathan"))
				.placement(() -> GridStructurePlacement.builder().spacing(36).config(() -> DPConfig.COMMON.leviathan).build(DPStructures.LEVIATHAN))
				.addPiece(() -> LeviathanStructure.Piece::new)
				.pushStructure(s -> ExtendedJigsawStructure.builder(s, LeviathanPools.ROOT).onSurface().startHeight(-3, 0).capability(LeviathanStructure.Capability.INSTANCE).build())
					.config(() -> DPConfig.COMMON.leviathan.getStructure())
					.noSpawns(BoundingBoxType.PIECE)
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, () -> List.of(new SpawnerData(EntityType.HUSK, 1, 1, 4)))
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<SnowyTempleStructure> SNOWY_TEMPLE = 
			StructureRegistrar.builder(DungeonsPlus.locate("snowy_temple"), () -> () -> SnowyTempleStructure.CODEC)
				.placement(() -> GridStructurePlacement.builder().spacing(36).config(() -> DPConfig.COMMON.snowyTemple).build(DPStructures.SNOWY_TEMPLE))
				.addPiece(() -> SnowyTemplePieces.Piece::new)
				.pushStructure(SnowyTempleStructure::new)
					.config(() -> DPConfig.COMMON.snowyTemple.getStructure())
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, () -> List.of(new SpawnerData(EntityType.STRAY, 1, 1, 4)))
					.terrainAdjustment(TerrainAdjustment.BEARD_THIN)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> WARPED_GARDEN = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("warped_garden"))
				.placement(() -> GridStructurePlacement.builder().spacing(36).config(() -> DPConfig.COMMON.warpedGarden).build(DPStructures.WARPED_GARDEN))
				.addPiece(() -> WarpedGardenStructure.Piece::new)
				.pushStructure(s -> ExtendedJigsawStructure.builder(s, WarpedGardenPools.ROOT).maxDepth(4).startHeight(36).capability(WarpedGardenStructure.Capability.INSTANCE).build())
					.config(() -> DPConfig.COMMON.warpedGarden.getStructure())
					.spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, () -> List.of(new SpawnerData(EntityType.DROWNED, 1, 1, 4)))
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();
	
	public static final StructureRegistrar<SoulPrisonStructure> SOUL_PRISON = 
			StructureRegistrar.builder(DungeonsPlus.locate("soul_prison"), () -> () -> SoulPrisonStructure.CODEC)
				.placement(() -> GridStructurePlacement.builder().spacing(20).config(() -> DPConfig.COMMON.soulPrison).build(DPStructures.SOUL_PRISON))
				.addPiece(() -> SoulPrisonPieces.Piece::new)
				.pushStructure(SoulPrisonStructure::new)
					.config(() -> DPConfig.COMMON.soulPrison.getStructure())
					.spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, () -> List.of(new SpawnerData(EntityType.SKELETON, 5, 1, 4), new SpawnerData(EntityType.GHAST, 1, 1, 2)))
					.dimensions(Level.NETHER)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> END_RUINS = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("end_ruins"))
				.placement(() -> GridStructurePlacement.builder().spacing(27).config(() -> DPConfig.COMMON.endRuins).build(DPStructures.END_RUINS))
				.addPiece(() -> EndRuinsStructure.Piece::new)
				.pushStructure(s -> ExtendedJigsawStructure.builder(s, EndRuinsPools.ROOT).onSurface().capability(EndRuinsStructure.Capability.INSTANCE).build())
					.config(() -> DPConfig.COMMON.endRuins.getStructure())
					.dimensions(Level.END)
				.popStructure()
				.build();
	
	//@formatter:on
}