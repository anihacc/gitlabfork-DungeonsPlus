package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DPConfig;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.EndRuinsStructure;
import com.legacy.dungeons_plus.structures.LeviathanStructure;
import com.legacy.dungeons_plus.structures.ReanimatedRuinsStructure;
import com.legacy.dungeons_plus.structures.SnowyTempleStructure;
import com.legacy.dungeons_plus.structures.SoulPrisonStructure;
import com.legacy.dungeons_plus.structures.TowerStructure;
import com.legacy.dungeons_plus.structures.WarpedGardenStructure;
import com.legacy.dungeons_plus.structures.pieces.SoulPrisonPieces;
import com.legacy.dungeons_plus.structures.pieces.TowerPieces;
import com.legacy.dungeons_plus.structures.pieces.WarpedGardenPieces;
import com.legacy.dungeons_plus.structures.pools.EndRuinsPools;
import com.legacy.dungeons_plus.structures.pools.LeviathanPools;
import com.legacy.dungeons_plus.structures.pools.ReanimatedRuinsPools;
import com.legacy.dungeons_plus.structures.pools.SnowyTemplePools;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.legacy.structure_gel.api.structure.DummyStructure;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;

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
	//@formatter:off
	public static final StructureRegistrar<NoneFeatureConfiguration, TowerStructure> TOWER = 
			StructureRegistrar.builder(DungeonsPlus.locate("tower"), new TowerStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.tower))
				.addPiece(TowerPieces.Piece::new)
				.addConfigured(NoneFeatureConfiguration.INSTANCE)
				.build();

	public static final StructureRegistrar<JigsawConfiguration, ReanimatedRuinsStructure> REANIMATED_RUINS = 
			StructureRegistrar.builder(DungeonsPlus.locate("reanimated_ruins"), new ReanimatedRuinsStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.reanimatedRuins))
				.addPiece(ReanimatedRuinsStructure.Piece::new)
				.addConfigured(new JigsawConfiguration(() -> ReanimatedRuinsPools.ROOT, 12))
				.build();

	public static final StructureRegistrar<JigsawConfiguration, LeviathanStructure> LEVIATHAN = 
			StructureRegistrar.builder(DungeonsPlus.locate("leviathan"), new LeviathanStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.leviathan))
				.addPiece(LeviathanStructure.Piece::new)
				.addConfigured(new JigsawConfiguration(() -> LeviathanPools.ROOT, 7))
				.build();

	public static final StructureRegistrar<JigsawConfiguration, SnowyTempleStructure> SNOWY_TEMPLE = 
			StructureRegistrar.builder(DungeonsPlus.locate("snowy_temple"), new SnowyTempleStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.snowyTemple))
				.addPiece(SnowyTempleStructure.Piece::new)
				.addConfigured(new JigsawConfiguration(() -> SnowyTemplePools.ROOT, 7))
				.build();

	public static final StructureRegistrar<JigsawConfiguration, EndRuinsStructure> END_RUINS = 
			StructureRegistrar.builder(DungeonsPlus.locate("end_ruins"), new EndRuinsStructure(JigsawConfiguration.CODEC, DPConfig.COMMON.endRuins))
				.addPiece(EndRuinsStructure.Piece::new)
				.addConfigured(new JigsawConfiguration(() -> EndRuinsPools.ROOT, 7))
				.build();

	public static final StructureRegistrar<NoneFeatureConfiguration, WarpedGardenStructure> WARPED_GARDEN = 
			StructureRegistrar.builder(DungeonsPlus.locate("warped_garden"), new WarpedGardenStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.warpedGarden))
				.addPiece(WarpedGardenPieces.Piece::new)
				.addConfigured(NoneFeatureConfiguration.NONE)
				.build();

	public static final StructureRegistrar<NoneFeatureConfiguration, SoulPrisonStructure> SOUL_PRISON = 
			StructureRegistrar.builder(DungeonsPlus.locate("soul_prison"), new SoulPrisonStructure(NoneFeatureConfiguration.CODEC, DPConfig.COMMON.soulPrison))
				.addPiece(SoulPrisonPieces.Piece::new)
				.addConfigured(NoneFeatureConfiguration.NONE)
				.build();

	//@formatter:on

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<StructureFeature<?>> event)
	{
		IForgeRegistry<StructureFeature<?>> registry = event.getRegistry();
		TOWER.handleForge(registry);
		REANIMATED_RUINS.handleForge(registry);
		LEVIATHAN.handleForge(registry);
		SNOWY_TEMPLE.handleForge(registry);
		END_RUINS.handleForge(registry);
		WARPED_GARDEN.handleForge(registry);
		SOUL_PRISON.handleForge(registry);

		StructureAccessHelper.addNoiseAffectingStructures(SNOWY_TEMPLE.getStructure());

		ReanimatedRuinsPools.init();
		LeviathanPools.init();
		SnowyTemplePools.init();
		EndRuinsPools.init();

		DummyStructure.createDummy("dungeons_plus:bigger_dungeon");
	}
}