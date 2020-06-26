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
import com.legacy.dungeons_plus.features.SnowyTemplePieces;
import com.legacy.dungeons_plus.features.SnowyTempleStructure;
import com.legacy.dungeons_plus.features.TowerPieces;
import com.legacy.dungeons_plus.features.TowerStructure;
import com.legacy.structure_gel.access_helpers.JigsawAccessHelper;
import com.legacy.structure_gel.util.RegistryHelper;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
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

@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String NAME = "Dungeons Plus";
	public static final String MODID = "dungeons_plus";
	public static final String VERSION = "1.0.1";
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
			
			RegistryHelper.addFeature(biome, Decoration.SURFACE_STRUCTURES, Features.TOWER.getFirst());
			RegistryHelper.addFeature(biome, Decoration.UNDERGROUND_STRUCTURES, Features.BIGGER_DUNGEON.getFirst());
			RegistryHelper.addFeature(biome, Decoration.SURFACE_STRUCTURES, Features.LEVIATHAN.getFirst());
			RegistryHelper.addFeature(biome, Decoration.SURFACE_STRUCTURES, Features.SNOWY_TEMPLE.getFirst());
			RegistryHelper.addFeature(biome, Decoration.SURFACE_STRUCTURES, Features.END_RUINS.getFirst());

			if (DungeonsConfig.COMMON.tower.isBiomeAllowed(biome))
				RegistryHelper.addStructure(biome, Features.TOWER.getFirst());

			/**
			 * Should only add to the overworld biomes. If you get it in your dimension, I'm
			 * sorry, but there is a blacklist in the config. - Silver_David
			 */
			if (types.contains(BiomeDictionary.Type.OVERWORLD) && DungeonsConfig.COMMON.biggerDungeon.isBiomeAllowed(biome))
				RegistryHelper.addStructure(biome, Features.BIGGER_DUNGEON.getFirst());

			if (DungeonsConfig.COMMON.leviathan.isBiomeAllowed(biome))
				RegistryHelper.addStructure(biome, Features.LEVIATHAN.getFirst());
			
			if (DungeonsConfig.COMMON.snowyTemple.isBiomeAllowed(biome))
				RegistryHelper.addStructure(biome, Features.SNOWY_TEMPLE.getFirst());
			
			if (DungeonsConfig.COMMON.endRuins.isBiomeAllowed(biome))
				RegistryHelper.addStructure(biome, Features.END_RUINS.getFirst());
		}
	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}

	@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Features
	{
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> TOWER;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> LEVIATHAN;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> SNOWY_TEMPLE;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> BIGGER_DUNGEON;
		public static Pair<Structure<NoFeatureConfig>, IStructurePieceType> END_RUINS;

		@SubscribeEvent
		public static void onRegistry(final RegistryEvent.Register<Feature<?>> event)
		{
			IForgeRegistry<Feature<?>> registry = event.getRegistry();
			TOWER = RegistryHelper.registerStructureAndPiece(registry, locate("tower"), new TowerStructure(NoFeatureConfig::deserialize, DungeonsConfig.COMMON.tower), TowerPieces.Piece::new);
			LEVIATHAN = RegistryHelper.registerStructureAndPiece(registry, locate("leviathan"), new LeviathanStructure(NoFeatureConfig::deserialize, DungeonsConfig.COMMON.leviathan), LeviathanPieces.Piece::new);
			SNOWY_TEMPLE = RegistryHelper.registerStructureAndPiece(registry, locate("snowy_temple"), new SnowyTempleStructure(NoFeatureConfig::deserialize, DungeonsConfig.COMMON.snowyTemple), SnowyTemplePieces.Piece::new);
			BIGGER_DUNGEON = RegistryHelper.registerStructureAndPiece(registry, locate("bigger_dungeon"), new BiggerDungeonStructure(NoFeatureConfig::deserialize, DungeonsConfig.COMMON.biggerDungeon), BiggerDungeonPieces.Piece::new);
			END_RUINS = RegistryHelper.registerStructureAndPiece(registry, locate("end_ruins"), new EndRuinsStructure(NoFeatureConfig::deserialize, DungeonsConfig.COMMON.endRuins), EndRuinsPieces.Piece::new);

			JigsawAccessHelper.addIllagerStructures(TOWER.getFirst(), SNOWY_TEMPLE.getFirst());
		}
	}
}
