package com.legacy.dungeons_plus.registry;

import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.structures.end_ruins.EndRuinsStructure;
import com.legacy.dungeons_plus.structures.leviathan.LeviathanStructure;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsStructure;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsType;
import com.legacy.dungeons_plus.structures.snowy_temple.SnowyTemplePieces;
import com.legacy.dungeons_plus.structures.snowy_temple.SnowyTempleStructure;
import com.legacy.dungeons_plus.structures.soul_prison.SoulPrisonPieces;
import com.legacy.dungeons_plus.structures.soul_prison.SoulPrisonStructure;
import com.legacy.dungeons_plus.structures.tower.TowerPieces;
import com.legacy.dungeons_plus.structures.tower.TowerStructure;
import com.legacy.dungeons_plus.structures.warped_garden.WarpedGardenStructure;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure;
import com.legacy.structure_gel.api.structure.GridStructurePlacement;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride.BoundingBoxType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

// TODO Future: Sculk
// TODO Future: Copper
// TODO Future: Slime
public class DPStructures
{
	//@formatter:off
	public static final StructureRegistrar<TowerStructure> TOWER = 
			StructureRegistrar.builder(DungeonsPlus.locate("tower"), () -> () -> TowerStructure.CODEC)
				.placement(() -> GridStructurePlacement.builder().spacing(31).probability(0.75F).build(DPStructures.TOWER))
				.addPiece(() -> TowerPieces.Piece::new)
				.pushStructure(TowerStructure::new)
					.biomes(DPTags.Biomes.HAS_TOWER)
					.terrainAdjustment(TerrainAdjustment.BEARD_THIN)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> REANIMATED_RUINS = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("reanimated_ruins"))
				.placement(() -> GridStructurePlacement.builder().spacing(36).probability(0.75F).build(DPStructures.REANIMATED_RUINS))
				.addPiece(() -> ReanimatedRuinsStructure.Piece::new)
				.pushStructure(ReanimatedRuinsType.MOSSY.toString(), (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(DPTemplatePools.REANIMATED_RUINS_MOSSY)).maxDepth(10).startHeight(-15, 15).capability(new ReanimatedRuinsStructure.Capability(ReanimatedRuinsType.MOSSY)).build())
					.biomes(DPTags.Biomes.HAS_REANIMATED_RUINS_MOSSY)
					.terrainAdjustment(TerrainAdjustment.BURY)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.pushStructure(ReanimatedRuinsType.MESA.toString(), (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(DPTemplatePools.REANIMATED_RUINS_MESA)).maxDepth(10).startHeight(-15, 15).capability(new ReanimatedRuinsStructure.Capability(ReanimatedRuinsType.MESA)).build())
					.biomes(DPTags.Biomes.HAS_REANIMATED_RUINS_MESA)
					.terrainAdjustment(TerrainAdjustment.BURY)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.pushStructure(ReanimatedRuinsType.FROZEN.toString(), (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(DPTemplatePools.REANIMATED_RUINS_FROZEN)).maxDepth(10).startHeight(-15, 15).capability(new ReanimatedRuinsStructure.Capability(ReanimatedRuinsType.FROZEN)).build())
					.biomes(DPTags.Biomes.HAS_REANIMATED_RUINS_FROZEN)
					.terrainAdjustment(TerrainAdjustment.BURY)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> LEVIATHAN = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("leviathan"))
				.placement(() -> GridStructurePlacement.builder().spacing(36).probability(0.75F).build(DPStructures.LEVIATHAN))
				.addPiece(() -> LeviathanStructure.Piece::new)
				.pushStructure((c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(DPTemplatePools.LEVIATHAN)).onSurface().startHeight(-3, 0).capability(LeviathanStructure.Capability.INSTANCE).build())
					.biomes(DPTags.Biomes.HAS_LEVIATHAN)
					.noSpawns(BoundingBoxType.PIECE)
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, () -> List.of(new SpawnerData(EntityType.HUSK, 1, 1, 4)))
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<SnowyTempleStructure> SNOWY_TEMPLE = 
			StructureRegistrar.builder(DungeonsPlus.locate("snowy_temple"), () -> () -> SnowyTempleStructure.CODEC)
				.placement(() -> GridStructurePlacement.builder().spacing(36).probability(0.75F).build(DPStructures.SNOWY_TEMPLE))
				.addPiece(() -> SnowyTemplePieces.Piece::new)
				.pushStructure(SnowyTempleStructure::new)
					.biomes(DPTags.Biomes.HAS_SNOWY_TEMPLE)
					.spawns(MobCategory.MONSTER, BoundingBoxType.STRUCTURE, () -> List.of(new SpawnerData(EntityType.STRAY, 1, 1, 4)))
					.terrainAdjustment(TerrainAdjustment.BEARD_THIN)
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> WARPED_GARDEN = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("warped_garden"))
				.placement(() -> GridStructurePlacement.builder().spacing(36).probability(0.75F).build(DPStructures.WARPED_GARDEN))
				.addPiece(() -> WarpedGardenStructure.Piece::new)
				.pushStructure((c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(DPTemplatePools.WARPED_GARDEN)).maxDepth(4).startHeight(36).capability(WarpedGardenStructure.Capability.INSTANCE).build())
					.biomes(DPTags.Biomes.HAS_WARPED_GARDEN)
					.spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, () -> List.of(new SpawnerData(EntityType.DROWNED, 1, 1, 4)))
					.dimensions(Level.OVERWORLD)
				.popStructure()
				.build();
	
	public static final StructureRegistrar<SoulPrisonStructure> SOUL_PRISON = 
			StructureRegistrar.builder(DungeonsPlus.locate("soul_prison"), () -> () -> SoulPrisonStructure.CODEC)
				.placement(() -> GridStructurePlacement.builder().spacing(20).probability(0.75F).build(DPStructures.SOUL_PRISON))
				.addPiece(() -> SoulPrisonPieces.Piece::new)
				.pushStructure(SoulPrisonStructure::new)
					.biomes(DPTags.Biomes.HAS_SOUL_PRISON)
					.spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, () -> List.of(new SpawnerData(EntityType.SKELETON, 5, 1, 4), new SpawnerData(EntityType.GHAST, 1, 1, 2)))
					.dimensions(Level.NETHER)
				.popStructure()
				.build();

	public static final StructureRegistrar<ExtendedJigsawStructure> END_RUINS = 
			StructureRegistrar.jigsawBuilder(DungeonsPlus.locate("end_ruins"))
				.placement(() -> GridStructurePlacement.builder().spacing(27).probability(0.75F).build(DPStructures.END_RUINS))
				.addPiece(() -> EndRuinsStructure.Piece::new)
				.pushStructure((c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(DPTemplatePools.END_RUINS)).onSurface().capability(EndRuinsStructure.Capability.INSTANCE).build())
					.biomes(DPTags.Biomes.HAS_END_RUINS)
					.dimensions(Level.END)
				.popStructure()
				.build();
	
	//@formatter:on
}