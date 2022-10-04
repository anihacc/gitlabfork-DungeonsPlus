package com.legacy.dungeons_plus;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.function.TriFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.util.TriPredicate;

public class DPUtil
{
	public static final Direction[] HORIZONTAL_DIR = Arrays.stream(Direction.values()).filter(d -> d.getAxis().isHorizontal()).toArray(Direction[]::new);

	public static boolean isInEllipsoid(double w, double h, double d, double x, double y, double z)
	{
		x = w - (x / 2);
		y = h - (y / 2);
		z = d - (z / 2);
		return (((x - w) * (x - w)) / w) + (((y - h) * (y - h)) / h) + (((z - d) * (z - d)) / d) < 1;
	}

	public static void fillBlob(ServerLevelAccessor level, BlockPos pos, int range, TriFunction<ServerLevelAccessor, BlockPos, Random, BlockState> stateFunc, Random rand, float chance)
	{
		for (int x = -range; x <= range; x++)
		{
			for (int y = -range; y <= range; y++)
			{
				for (int z = -range; z <= range; z++)
				{
					if (isInEllipsoid(range, range, range, x, y, z) && rand.nextFloat() < chance)
					{
						BlockPos placePos = pos.offset(x, y, z);
						BlockState state = stateFunc.apply(level, placePos, rand);
						if (state != null)
							level.setBlock(placePos, state, 2);
					}
				}
			}
		}
	}

	public static void fillBlob(ServerLevelAccessor level, BlockPos pos, int range, BlockState state, TriPredicate<ServerLevelAccessor, BlockPos, BlockState> canPlace, Random rand, float chance)
	{
		fillBlob(level, pos, range, (l, p, r) -> canPlace.test(l, p, l.getBlockState(p)) ? state : null, rand, chance);
	}

	public static BlockPos randOffset(BlockPos pos, int range, Random rand)
	{
		return pos.offset(range(range, rand), range(range, rand), range(range, rand));
	}

	public static int range(int range, Random rand)
	{
		return rand.nextInt(range * 2) - range;
	}

	@SuppressWarnings("unchecked")
	public static void placeWaystone(ServerLevelAccessor level, BlockPos pos, Random rand, @Nullable Block defaultBlock)
	{
		if (DungeonsPlus.isWaystonesLoaded && rand.nextFloat() < DPConfig.COMMON.towerWaystoneChance.get())
		{
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
			try
			{
				Feature<NoneFeatureConfiguration> waystoneFeature = (Feature<NoneFeatureConfiguration>) level.registryAccess().registryOrThrow(Registry.FEATURE_REGISTRY).get(new ResourceLocation("waystones", "waystone"));
				if (waystoneFeature != null)
				{
					level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
					WorldGenLevel worldGenRegion = level instanceof WorldGenLevel ? (WorldGenLevel) level : level.getLevel();
					waystoneFeature.place(new FeaturePlaceContext<NoneFeatureConfiguration>(Optional.empty(), worldGenRegion, level.getLevel().getChunkSource().getGenerator(), rand, pos, NoneFeatureConfiguration.INSTANCE));
					return;
				}
			}
			catch (Throwable t)
			{
				DungeonsPlus.LOGGER.error(String.format("Failed to place waystone at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
				DungeonsPlus.LOGGER.error(t);
			}
		}

		level.setBlock(pos, (defaultBlock != null ? defaultBlock : Blocks.AIR).defaultBlockState(), 2);
	}
}
