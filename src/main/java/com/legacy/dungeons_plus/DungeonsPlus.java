package com.legacy.dungeons_plus;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.legacy.dungeons_plus.features.BiggerDungeonPieces;
import com.legacy.dungeons_plus.features.BiggerDungeonStructure;
import com.legacy.dungeons_plus.features.EndRuinsPieces;
import com.legacy.dungeons_plus.features.EndRuinsStructure;
import com.legacy.dungeons_plus.features.LeviathanPieces;
import com.legacy.dungeons_plus.features.LeviathanStructure;
import com.legacy.dungeons_plus.features.TowerPieces;
import com.legacy.dungeons_plus.features.TowerStructure;
import com.legacy.structure_gel.structures.jigsaw.JigsawAccessHelper;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String NAME = "Dungeons Plus";
	public static final String MODID = "dungeons_plus";
	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger();

	public DungeonsPlus()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsConfig.COMMON_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
	}

	public void commonInit(final FMLCommonSetupEvent event)
	{
		for (Biome biome : ForgeRegistries.BIOMES.getValues())
		{
			Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);

			biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Features.TOWER.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			biome.addFeature(Decoration.UNDERGROUND_STRUCTURES, Biome.createDecoratedFeature(Features.BIGGER_DUNGEON.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Features.LEVIATHAN.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Features.END_RUINS.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

			if (DungeonsConfig.COMMON.tower.getBiomes().contains(biome))
				biome.addStructure(Features.TOWER.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG);

			/**
			 * Should only add to the overworld biomes. If you get it in your dimension, I'm
			 * sorry. - Silver_David
			 */
			if (types.contains(BiomeDictionary.Type.OVERWORLD))
				biome.addStructure(Features.BIGGER_DUNGEON.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG);

			if (DungeonsConfig.COMMON.leviathan.getBiomes().contains(biome))
				biome.addStructure(Features.LEVIATHAN.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG);

			if (DungeonsConfig.COMMON.endRuins.getBiomes().contains(biome))
				biome.addStructure(Features.END_RUINS.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG);
		}
	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}

	public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, String name, T object)
	{
		object.setRegistryName(DungeonsPlus.locate(name));
		registry.register(object);
		return object;
	}

	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Features
	{
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> TOWER;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> LEVIATHAN;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> BIGGER_DUNGEON;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> END_RUINS;

		@SubscribeEvent
		public static void onRegistry(final RegistryEvent.Register<Feature<?>> event)
		{
			TOWER = structure(event, "tower", new TowerStructure(NoFeatureConfig::deserialize), TowerPieces.Piece::new);
			LEVIATHAN = structure(event, "leviathan", new LeviathanStructure(NoFeatureConfig::deserialize), LeviathanPieces.Piece::new);
			BIGGER_DUNGEON = structure(event, "bigger_dungeon", new BiggerDungeonStructure(NoFeatureConfig::deserialize), BiggerDungeonPieces.Piece::new);
			END_RUINS = structure(event, "end_ruins", new EndRuinsStructure(NoFeatureConfig::deserialize), EndRuinsPieces.Piece::new);

			JigsawAccessHelper.addIllagerStructures(TOWER.getFirst());
		}

		@SuppressWarnings("deprecation")
		private static <C extends IFeatureConfig> Pair<Structure<C>, IStructurePieceType> structure(RegistryEvent.Register<Feature<?>> event, String key, Structure<C> structure, IStructurePieceType pieceType)
		{
			Structure<C> struc = Registry.register(Registry.STRUCTURE_FEATURE, locate(key.toLowerCase()), structure);
			register(event.getRegistry(), key, struc);
			return Pair.of(struc, Registry.register(Registry.STRUCTURE_PIECE, locate(key.toLowerCase()), pieceType));
		}
	}
}
