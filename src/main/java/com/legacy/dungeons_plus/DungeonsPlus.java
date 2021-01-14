package com.legacy.dungeons_plus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.legacy.dungeons_plus.pieces.SoulPrisonPieces;
import com.legacy.dungeons_plus.pieces.WarpedGardenPieces;
import com.legacy.dungeons_plus.pools.BiggerDungeonPools;
import com.legacy.dungeons_plus.pools.EndRuinsPools;
import com.legacy.dungeons_plus.pools.LeviathanPools;
import com.legacy.dungeons_plus.pools.SnowyTemplePools;
import com.legacy.dungeons_plus.pools.TowerPools;
import com.legacy.dungeons_plus.structures.BiggerDungeonStructure;
import com.legacy.dungeons_plus.structures.EndRuinsStructure;
import com.legacy.dungeons_plus.structures.LeviathanStructure;
import com.legacy.dungeons_plus.structures.SnowyTempleStructure;
import com.legacy.dungeons_plus.structures.SoulPrisonStructure;
import com.legacy.dungeons_plus.structures.TowerStructure;
import com.legacy.dungeons_plus.structures.WarpedGardenStructure;
import com.legacy.structure_gel.access_helpers.BiomeAccessHelper;
import com.legacy.structure_gel.access_helpers.JigsawAccessHelper;
import com.legacy.structure_gel.registrars.StructureRegistrar;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String MODID = "dungeons_plus";
	public static final Logger LOGGER = LogManager.getLogger();

	public DungeonsPlus()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DPConfig.COMMON_SPEC);
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(DungeonsPlus::biomeLoad);
	}

	protected static void biomeLoad(final BiomeLoadingEvent event)
	{
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.TOWER.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.BIGGER_DUNGEON.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.LEVIATHAN.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.SNOWY_TEMPLE.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.END_RUINS.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.WARPED_GARDEN.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.SOUL_PRISON.getStructureFeature());
	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}

	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Structures
	{
		public static StructureRegistrar<VillageConfig, TowerStructure> TOWER = StructureRegistrar.of(locate("tower"), new TowerStructure(VillageConfig.CODEC, DPConfig.COMMON.tower), TowerStructure.Piece::new, new VillageConfig(() -> TowerPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle();
		public static StructureRegistrar<VillageConfig, BiggerDungeonStructure> BIGGER_DUNGEON = StructureRegistrar.of(locate("bigger_dungeon"), new BiggerDungeonStructure(VillageConfig.CODEC, DPConfig.COMMON.biggerDungeon), BiggerDungeonStructure.Piece::new, new VillageConfig(() -> BiggerDungeonPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle();
		public static StructureRegistrar<VillageConfig, LeviathanStructure> LEVIATHAN = StructureRegistrar.of(locate("leviathan"), new LeviathanStructure(VillageConfig.CODEC, DPConfig.COMMON.leviathan), LeviathanStructure.Piece::new, new VillageConfig(() -> LeviathanPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle();
		public static StructureRegistrar<VillageConfig, SnowyTempleStructure> SNOWY_TEMPLE = StructureRegistrar.of(locate("snowy_temple"), new SnowyTempleStructure(VillageConfig.CODEC, DPConfig.COMMON.snowyTemple), SnowyTempleStructure.Piece::new, new VillageConfig(() -> SnowyTemplePools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle();
		public static StructureRegistrar<VillageConfig, EndRuinsStructure> END_RUINS = StructureRegistrar.of(locate("end_ruins"), new EndRuinsStructure(VillageConfig.CODEC, DPConfig.COMMON.endRuins), EndRuinsStructure.Piece::new, new VillageConfig(() -> EndRuinsPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle();
		public static StructureRegistrar<NoFeatureConfig, WarpedGardenStructure> WARPED_GARDEN = StructureRegistrar.of(locate("warped_garden"), new WarpedGardenStructure(NoFeatureConfig.CODEC, DPConfig.COMMON.warpedGarden), WarpedGardenPieces.Piece::new, NoFeatureConfig.NO_FEATURE_CONFIG, Decoration.SURFACE_STRUCTURES).handle();
		public static StructureRegistrar<NoFeatureConfig, SoulPrisonStructure> SOUL_PRISON = StructureRegistrar.of(locate("soul_prison"), new SoulPrisonStructure(NoFeatureConfig.CODEC, DPConfig.COMMON.soulPrison), SoulPrisonPieces.Piece::new, NoFeatureConfig.NO_FEATURE_CONFIG, Decoration.SURFACE_STRUCTURES).handle();

		@SubscribeEvent
		protected static void onRegistry(final RegistryEvent.Register<Structure<?>> event)
		{
			IForgeRegistry<Structure<?>> registry = event.getRegistry();
			TOWER.handleForge(registry);
			BIGGER_DUNGEON.handleForge(registry);
			LEVIATHAN.handleForge(registry);
			SNOWY_TEMPLE.handleForge(registry);
			END_RUINS.handleForge(registry);
			WARPED_GARDEN.handleForge(registry);
			SOUL_PRISON.handleForge(registry);

			JigsawAccessHelper.addIllagerStructures(TOWER.getStructure(), SNOWY_TEMPLE.getStructure());

			TowerPools.init();
			BiggerDungeonPools.init();
			LeviathanPools.init();
			SnowyTemplePools.init();
			EndRuinsPools.init();
		}
	}
}
