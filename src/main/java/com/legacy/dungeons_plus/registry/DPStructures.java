package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DPConfig;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.BiggerDungeonStructure;
import com.legacy.dungeons_plus.structures.EndRuinsStructure;
import com.legacy.dungeons_plus.structures.LeviathanStructure;
import com.legacy.dungeons_plus.structures.SnowyTempleStructure;
import com.legacy.dungeons_plus.structures.SoulPrisonStructure;
import com.legacy.dungeons_plus.structures.TowerStructure;
import com.legacy.dungeons_plus.structures.WarpedGardenStructure;
import com.legacy.dungeons_plus.structures.pieces.SoulPrisonPieces;
import com.legacy.dungeons_plus.structures.pieces.WarpedGardenPieces;
import com.legacy.dungeons_plus.structures.pools.BiggerDungeonPools;
import com.legacy.dungeons_plus.structures.pools.EndRuinsPools;
import com.legacy.dungeons_plus.structures.pools.LeviathanPools;
import com.legacy.dungeons_plus.structures.pools.SnowyTemplePools;
import com.legacy.dungeons_plus.structures.pools.TowerPools;
import com.legacy.structure_gel.api.registry.registrar.GelStructureRegistrar;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;

import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPStructures
{
	public static final GelStructureRegistrar<JigsawConfiguration, TowerStructure> TOWER = GelStructureRegistrar.of(DungeonsPlus.locate("tower"), new TowerStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.tower), TowerStructure.Piece::new, new JigsawConfiguration(() -> TowerPools.ROOT, 7), Decoration.SURFACE_STRUCTURES);
	public static final GelStructureRegistrar<JigsawConfiguration, BiggerDungeonStructure> BIGGER_DUNGEON = GelStructureRegistrar.of(DungeonsPlus.locate("bigger_dungeon"), new BiggerDungeonStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.biggerDungeon), BiggerDungeonStructure.Piece::new, new JigsawConfiguration(() -> BiggerDungeonPools.ROOT, 7), Decoration.SURFACE_STRUCTURES);
	public static final GelStructureRegistrar<JigsawConfiguration, LeviathanStructure> LEVIATHAN = GelStructureRegistrar.of(DungeonsPlus.locate("leviathan"), new LeviathanStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.leviathan), LeviathanStructure.Piece::new, new JigsawConfiguration(() -> LeviathanPools.ROOT, 7), Decoration.SURFACE_STRUCTURES);
	public static final GelStructureRegistrar<JigsawConfiguration, SnowyTempleStructure> SNOWY_TEMPLE = GelStructureRegistrar.of(DungeonsPlus.locate("snowy_temple"), new SnowyTempleStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.snowyTemple), SnowyTempleStructure.Piece::new, new JigsawConfiguration(() -> SnowyTemplePools.ROOT, 7), Decoration.SURFACE_STRUCTURES);
	public static final GelStructureRegistrar<JigsawConfiguration, EndRuinsStructure> END_RUINS = GelStructureRegistrar.of(DungeonsPlus.locate("end_ruins"), new EndRuinsStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.endRuins), EndRuinsStructure.Piece::new, new JigsawConfiguration(() -> EndRuinsPools.ROOT, 7), Decoration.SURFACE_STRUCTURES);
	public static final GelStructureRegistrar<NoneFeatureConfiguration, WarpedGardenStructure> WARPED_GARDEN = GelStructureRegistrar.of(DungeonsPlus.locate("warped_garden"), new WarpedGardenStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.warpedGarden), WarpedGardenPieces.Piece::new, NoneFeatureConfiguration.NONE, Decoration.SURFACE_STRUCTURES);
	public static final GelStructureRegistrar<NoneFeatureConfiguration, SoulPrisonStructure> SOUL_PRISON = GelStructureRegistrar.of(DungeonsPlus.locate("soul_prison"), new SoulPrisonStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.soulPrison), SoulPrisonPieces.Piece::new, NoneFeatureConfiguration.NONE, Decoration.SURFACE_STRUCTURES);

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<StructureFeature<?>> event)
	{
		IForgeRegistry<StructureFeature<?>> registry = event.getRegistry();
		TOWER.handleForge(registry);
		BIGGER_DUNGEON.handleForge(registry);
		LEVIATHAN.handleForge(registry);
		SNOWY_TEMPLE.handleForge(registry);
		END_RUINS.handleForge(registry);
		WARPED_GARDEN.handleForge(registry);
		SOUL_PRISON.handleForge(registry);

		StructureAccessHelper.addNoiseAffectingStructures(TOWER.getStructure(), SNOWY_TEMPLE.getStructure());

		TowerPools.init();
		BiggerDungeonPools.init();
		LeviathanPools.init();
		SnowyTemplePools.init();
		EndRuinsPools.init();
	}
}