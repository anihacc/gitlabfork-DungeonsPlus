package com.legacy.dungeons_plus;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

public class DPUtil
{
	public static boolean isInStructure(ServerLevel serverLevel, StructureFeature<?> structure, BlockPos pos)
	{
		return ChunkGenerator.allConfigurations(serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY), structure).anyMatch(configured ->
		{
			return serverLevel.structureFeatureManager().getStructureWithPieceAt(pos, configured).isValid();
		});
	}

	public static void placeMonsterBox(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		if (DungeonsPlus.isQuarkLoaded && rand.nextInt(100) < DPConfig.COMMON.biggerDungeonMonsterBoxChance.get())
		{
			ResourceLocation boxLocation = new ResourceLocation("quark", "monster_box");
			if (ForgeRegistries.BLOCKS.containsKey(boxLocation))
			{
				try
				{
					// TODO test
					BlockState monsterBox = ForgeRegistries.BLOCKS.getValue(boxLocation).defaultBlockState();
					level.setBlock(pos, monsterBox, 2);
				}
				catch (Throwable t)
				{
					DungeonsPlus.LOGGER.error(String.format("Failed to place monster box at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
					DungeonsPlus.LOGGER.error(t);
				}
			}
		}
	}

	public static void placeWaystone(ServerLevelAccessor level, BlockPos pos, Random rand, @Nullable Block defaultBlock)
	{
		if (DungeonsPlus.isWaystonesLoaded && rand.nextInt(100) < DPConfig.COMMON.towerWaystoneChance.get())
		{
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

			ResourceLocation waystoneLocation = new ResourceLocation("waystones", "waystone");
			if (ForgeRegistries.FEATURES.containsKey(waystoneLocation))
			{
				try
				{
					// TODO test
					level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
					@SuppressWarnings("unchecked")
					Feature<NoneFeatureConfiguration> waystoneFeature = (Feature<NoneFeatureConfiguration>) ForgeRegistries.FEATURES.getValue(waystoneLocation);
					waystoneFeature.place(new FeaturePlaceContext<NoneFeatureConfiguration>(Optional.empty(), level.getLevel(), level.getLevel().getChunkSource().getGenerator(), rand, pos, NoneFeatureConfiguration.INSTANCE));
				}
				catch (Throwable t)
				{
					DungeonsPlus.LOGGER.error(String.format("Failed to place waystone at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
					DungeonsPlus.LOGGER.error(t);
				}
			}
		}

		level.setBlock(pos, (defaultBlock != null ? defaultBlock : Blocks.AIR).defaultBlockState(), 2);
	}
}
