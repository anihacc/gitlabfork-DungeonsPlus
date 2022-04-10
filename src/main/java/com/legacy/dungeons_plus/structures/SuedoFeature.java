package com.legacy.dungeons_plus.structures;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.TriPredicate;

@FunctionalInterface
public interface SuedoFeature
{
	void place(ServerLevelAccessor level, BlockPos pos, Random rand);

	public static void fillBlob(ServerLevelAccessor level, BlockPos pos, int range, BlockState state, Random rand, float chance, TriPredicate<ServerLevelAccessor, BlockPos, BlockState> replace)
	{
		for (int x = -range; x <= range; x++)
		{
			for (int y = -range; y <= range; y++)
			{
				for (int z = -range; z <= range; z++)
				{
					if (rand.nextFloat() < chance)
					{
						BlockPos placePos = pos.offset(x, y, z);
						if (replace.test(level, placePos, level.getBlockState(placePos)))
							level.setBlock(placePos, state, 2);
					}
				}
			}
		}
	}
}