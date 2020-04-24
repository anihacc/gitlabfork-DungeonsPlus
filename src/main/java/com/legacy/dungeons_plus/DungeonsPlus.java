package com.legacy.dungeons_plus;

import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.features.BiggerDungeonPieces;
import com.legacy.dungeons_plus.features.BiggerDungeonStructure;
import com.legacy.dungeons_plus.features.TowerPieces;
import com.legacy.dungeons_plus.features.TowerStructure;
import com.legacy.structure_gel.StructureGelMod;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
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

	public DungeonsPlus()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsConfig.COMMON_SPEC);
	}

	private void commonInit(final FMLCommonSetupEvent event)
	{
		for (Biome biome : ForgeRegistries.BIOMES.getValues())
		{
			Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
			
			if (types.contains(BiomeDictionary.Type.OVERWORLD))
			{
				biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Features.TOWER.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
				biome.addFeature(Decoration.UNDERGROUND_STRUCTURES, Biome.createDecoratedFeature(Features.BIGGER_DUNGEON.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

				if (ImmutableList.of(Biomes.PLAINS, Biomes.DARK_FOREST, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.MOUNTAINS).contains(biome))
				{
					biome.addStructure(Features.TOWER.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG);
				}
								
				biome.addStructure(Features.BIGGER_DUNGEON.getFirst(), IFeatureConfig.NO_FEATURE_CONFIG);
			}
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
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> BIGGER_DUNGEON;
		
		@SubscribeEvent
		public static void onRegistry(final RegistryEvent.Register<Feature<?>> event)
		{
			TOWER = structure(event, "tower", new TowerStructure(NoFeatureConfig::deserialize), TowerPieces.Piece::new);
			BIGGER_DUNGEON = structure(event, "bigger_dungeon", new BiggerDungeonStructure(NoFeatureConfig::deserialize), BiggerDungeonPieces.Piece::new);
			
			StructureGelMod.addIllagerStructures(TOWER.getFirst());
		}

		@SuppressWarnings("deprecation")
		private static <C extends IFeatureConfig> Pair<Structure<C>, IStructurePieceType> structure(RegistryEvent.Register<Feature<?>> event, String key, Structure<C> structure, IStructurePieceType pieceType)
		{
			register(event.getRegistry(), key, structure);
			Registry.register(Registry.STRUCTURE_FEATURE, locate(key.toLowerCase()), structure);
			Feature.STRUCTURES.put(locate(key.toLowerCase()).toString(), structure);
			Registry.register(Registry.STRUCTURE_PIECE, locate(key.toLowerCase()).toString(), pieceType);
			return Pair.of(structure, pieceType);
		}
	}
}
