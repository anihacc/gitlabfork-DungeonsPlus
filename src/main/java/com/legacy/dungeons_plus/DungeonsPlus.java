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
import com.legacy.structure_gel.data.BiomeDictionary;
import com.legacy.structure_gel.data.BiomeDictionary.BiomeType;
import com.legacy.structure_gel.data.BiomeDictionaryEvent;
import com.legacy.structure_gel.util.RegistryHelper;
import com.legacy.structure_gel.util.StructureRegistrar;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String MODID = "dungeons_plus";
	public static final Logger LOGGER = LogManager.getLogger();

	public DungeonsPlus()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DPConfig.COMMON_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBiomeDict);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
	}

	@SuppressWarnings("unchecked")
	public void registerBiomeDict(final BiomeDictionaryEvent event)
	{
		event.register(BiomeType.create(locate("tower_biomes")).biomes(Biomes.PLAINS, Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.MOUNTAINS));
		event.register(BiomeType.create(locate("leviathan_biomes")).biomes(Biomes.DESERT));
		event.register(BiomeType.create(locate("snowy_temple_biomes")).biomes(Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TAIGA));
		event.register(BiomeType.create(locate("bigger_dungeon_biomes")).parents(BiomeDictionary.OVERWORLD));
		event.register(BiomeType.create(locate("end_ruins_biomes")).parents(BiomeDictionary.OUTER_END_ISLAND));
	}

	public void commonInit(final FMLCommonSetupEvent event)
	{
		for (Biome biome : ForgeRegistries.BIOMES.getValues())
		{
			if (DPConfig.COMMON.tower.isBiomeAllowed(biome))
				BiomeAccessHelper.addStructure(biome, Structures.TOWER.getStructureFeature());

			if (DPConfig.COMMON.biggerDungeon.isBiomeAllowed(biome))
				BiomeAccessHelper.addStructure(biome, Structures.BIGGER_DUNGEON.getStructureFeature());

			if (DPConfig.COMMON.leviathan.isBiomeAllowed(biome))
				BiomeAccessHelper.addStructure(biome, Structures.LEVIATHAN.getStructureFeature());

			if (DPConfig.COMMON.snowyTemple.isBiomeAllowed(biome))
				BiomeAccessHelper.addStructure(biome, Structures.SNOWY_TEMPLE.getStructureFeature());

			if (DPConfig.COMMON.endRuins.isBiomeAllowed(biome))
				BiomeAccessHelper.addStructure(biome, Structures.END_RUINS.getStructureFeature());
		}
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
			TOWER = RegistryHelper.handleRegistrar(registry, locate("tower"), new TowerStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.tower), Decoration.SURFACE_STRUCTURES, TowerStructure.Piece::new, new VillageConfig(() -> TowerPools.ROOT, 7));
			BIGGER_DUNGEON = RegistryHelper.handleRegistrar(registry, locate("bigger_dungeon"), new BiggerDungeonStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.biggerDungeon), Decoration.UNDERGROUND_STRUCTURES, BiggerDungeonStructure.Piece::new, new VillageConfig(() -> BiggerDungeonPools.ROOT, 7));
			LEVIATHAN = RegistryHelper.handleRegistrar(registry, locate("leviathan"), new LeviathanStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.tower), Decoration.SURFACE_STRUCTURES, LeviathanStructure.Piece::new, new VillageConfig(() -> LeviathanPools.ROOT, 7));
			SNOWY_TEMPLE = RegistryHelper.handleRegistrar(registry, locate("snowy_temple"), new SnowyTempleStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.tower), Decoration.SURFACE_STRUCTURES, SnowyTempleStructure.Piece::new, new VillageConfig(() -> SnowyTemplePools.ROOT, 7));
			END_RUINS = RegistryHelper.handleRegistrar(registry, locate("end_ruins"), new EndRuinsStructure(VillageConfig.field_236533_a_, DPConfig.COMMON.tower), Decoration.SURFACE_STRUCTURES, EndRuinsStructure.Piece::new, new VillageConfig(() -> EndRuinsPools.ROOT, 7));

			JigsawAccessHelper.addIllagerStructures(TOWER.getStructure(), SNOWY_TEMPLE.getStructure());

			TowerPools.init();
			BiggerDungeonPools.init();
			LeviathanPools.init();
			SnowyTemplePools.init();
			EndRuinsPools.init();
		}
	}
}
