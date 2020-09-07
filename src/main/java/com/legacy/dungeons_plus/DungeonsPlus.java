package com.legacy.dungeons_plus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.legacy.dungeons_plus.features.BiggerDungeonPools;
import com.legacy.dungeons_plus.features.BiggerDungeonStructure;
import com.legacy.dungeons_plus.features.EndRuinsPools;
import com.legacy.dungeons_plus.features.EndRuinsStructure;
import com.legacy.dungeons_plus.features.LeviathanPools;
import com.legacy.dungeons_plus.features.LeviathanStructure;
import com.legacy.dungeons_plus.features.SnowyTemplePools;
import com.legacy.dungeons_plus.features.SnowyTempleStructure;
import com.legacy.dungeons_plus.features.TowerPools;
import com.legacy.dungeons_plus.features.TowerStructure;
import com.legacy.structure_gel.access_helpers.BiomeAccessHelper;
import com.legacy.structure_gel.access_helpers.JigsawAccessHelper;
import com.legacy.structure_gel.registrars.StructureRegistrar;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String MODID = "dungeons_plus";
	public static final Logger LOGGER = LogManager.getLogger();

	public DungeonsPlus()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DPConfig.COMMON_SPEC);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::commonInit);
	}

	public void commonInit(final FMLCommonSetupEvent event)
	{
		BiomeAccessHelper.addStructureToBiomes(Structures.TOWER.getStructureFeature());
		BiomeAccessHelper.addStructureToBiomes(Structures.BIGGER_DUNGEON.getStructureFeature());
		BiomeAccessHelper.addStructureToBiomes(Structures.LEVIATHAN.getStructureFeature());
		BiomeAccessHelper.addStructureToBiomes(Structures.SNOWY_TEMPLE.getStructureFeature());
		BiomeAccessHelper.addStructureToBiomes(Structures.END_RUINS.getStructureFeature());
	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}

	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Structures
	{
		public static StructureRegistrar<VillageConfig, TowerStructure> TOWER;
		public static StructureRegistrar<VillageConfig, BiggerDungeonStructure> BIGGER_DUNGEON;
		public static StructureRegistrar<VillageConfig, LeviathanStructure> LEVIATHAN;
		public static StructureRegistrar<VillageConfig, SnowyTempleStructure> SNOWY_TEMPLE;
		public static StructureRegistrar<VillageConfig, EndRuinsStructure> END_RUINS;

		@SubscribeEvent
		public static void onRegistry(final RegistryEvent.Register<Structure<?>> event)
		{
			IForgeRegistry<Structure<?>> registry = event.getRegistry();
			TOWER = StructureRegistrar.of(locate("tower"), new TowerStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.tower), TowerStructure.Piece::new, new VillageConfig(() -> TowerPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle().handleForge(registry);
			BIGGER_DUNGEON = StructureRegistrar.of(locate("bigger_dungeon"), new BiggerDungeonStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.biggerDungeon), BiggerDungeonStructure.Piece::new, new VillageConfig(() -> BiggerDungeonPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle().handleForge(registry);
			LEVIATHAN = StructureRegistrar.of(locate("leviathan"), new LeviathanStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.leviathan), LeviathanStructure.Piece::new, new VillageConfig(() -> LeviathanPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle().handleForge(registry);
			SNOWY_TEMPLE = StructureRegistrar.of(locate("snowy_temple"), new SnowyTempleStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.snowyTemple), SnowyTempleStructure.Piece::new, new VillageConfig(() -> SnowyTemplePools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle().handleForge(registry);
			END_RUINS = StructureRegistrar.of(locate("end_ruins"), new EndRuinsStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.endRuins), EndRuinsStructure.Piece::new, new VillageConfig(() -> EndRuinsPools.ROOT, 7), Decoration.SURFACE_STRUCTURES).handle().handleForge(registry);

			JigsawAccessHelper.addIllagerStructures(TOWER.getStructure(), SNOWY_TEMPLE.getStructure());

			TowerPools.init();
			BiggerDungeonPools.init();
			LeviathanPools.init();
			SnowyTemplePools.init();
			EndRuinsPools.init();
		}
	}
}
