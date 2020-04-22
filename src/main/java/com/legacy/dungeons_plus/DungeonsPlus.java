package com.legacy.dungeons_plus;

import com.legacy.dungeons_plus.features.tower.TowerPieces;
import com.legacy.dungeons_plus.features.tower.TowerStructure;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
	}

	private void commonInit(final FMLCommonSetupEvent event)
	{
		ForgeRegistries.BIOMES.getValues().forEach(biome -> {
			biome.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Features.TOWER.getFirst(), new NoFeatureConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			biome.addStructure(Features.TOWER.getFirst(), new NoFeatureConfig());
		});
		
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
		
		@SubscribeEvent
		public static void onRegistry(final RegistryEvent.Register<Feature<?>> event)
		{
			TOWER = structure(event, "tower", new TowerStructure(NoFeatureConfig::deserialize), TowerPieces.Piece::new);
			
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
