package com.legacy.dungeons_plus.structures;

import com.legacy.dungeons_plus.DPUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;

public class PsuedoFeature
{
	private final int minPlacements, maxPlacements;
	private final IPlacement placement;
	private final IPlacer placer;

	public PsuedoFeature(int minPlacements, int maxPlacements, IPlacement placement, IPlacer placer) throws IllegalArgumentException
	{
		this.minPlacements = minPlacements;
		this.maxPlacements = maxPlacements;
		this.placement = placement;
		this.placer = placer;

		if (minPlacements < 0)
			throw new IllegalArgumentException("SuedoFeature minPlacements cannot be negative. Value = " + minPlacements);
		if (maxPlacements < minPlacements)
			throw new IllegalArgumentException("SuedoFeature maxPlacements is less than minPlacements. " + maxPlacements + " < " + minPlacements);
	}

	public void place(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		for (int i = rand.nextInt(this.maxPlacements + 1 - this.minPlacements) + this.minPlacements; i > -1; i--)
		{
			this.placer.place(level, this.placement.mutatePos(DPUtil.randOffset(pos, 6, rand), level, rand), rand);
		}
	}

	@FunctionalInterface
	public static interface IPlacement
	{
		final IPlacement NOOP = (pos, level, rand) ->
		{
			return pos;
		};
		final IPlacement FIND_LOWEST_AIR = (pos, level, rand) ->
		{
			var mutPos = pos.mutable();
			for (int dist = 0; level.getBlockState(mutPos).isAir() && dist < 16; dist++)
				mutPos.move(Direction.DOWN);
			return mutPos.move(Direction.UP).immutable();
		};
		final IPlacement FIND_HIGHEST_AIR = (pos, level, rand) ->
		{
			var mutPos = pos.mutable();
			for (int dist = 0; level.getBlockState(mutPos).isAir() && dist < 16; dist++)
				mutPos.move(Direction.UP);
			return mutPos.move(Direction.DOWN).immutable();
		};

		abstract BlockPos mutatePos(BlockPos pos, ServerLevelAccessor level, RandomSource rand);
	}

	@FunctionalInterface
	public static interface IPlacer
	{
		void place(ServerLevelAccessor level, BlockPos pos, RandomSource rand);
	}
}