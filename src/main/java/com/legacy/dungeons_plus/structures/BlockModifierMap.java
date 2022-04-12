package com.legacy.dungeons_plus.structures;

import java.util.HashMap;
import java.util.Random;
import java.util.function.BiFunction;

import com.legacy.structure_gel.api.structure.base.IModifyState;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockModifierMap extends HashMap<Block, BiFunction<BlockState, Random, BlockState>>
{
	private static final long serialVersionUID = 9048061696297205268L;

	public BlockModifierMap()
	{
		super();
	}

	public BiFunction<BlockState, Random, BlockState> put(Block key, Block value)
	{
		return super.put(key, (s, r) -> value.defaultBlockState());
	}

	public BiFunction<BlockState, Random, BlockState> put(Block key, BlockState value)
	{
		return super.put(key, (s, r) -> value);
	}

	public BiFunction<BlockState, Random, BlockState> merge(Block key, Block value)
	{
		return super.put(key, (s, r) -> IModifyState.mergeStates(value.defaultBlockState(), s));
	}

	public BiFunction<BlockState, Random, BlockState> merge(Block key, BlockState value)
	{
		return super.put(key, (s, r) -> IModifyState.mergeStates(value, s));
	}

	public BlockState modifiy(BlockState input, Random rand)
	{
		var func = this.get(input.getBlock());
		return func != null ? func.apply(input, rand) : input;
	}
}