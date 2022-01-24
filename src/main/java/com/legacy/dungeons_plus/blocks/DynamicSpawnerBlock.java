package com.legacy.dungeons_plus.blocks;

import com.legacy.dungeons_plus.block_entities.DynamicSpawnerBlockEntity;
import com.legacy.dungeons_plus.registry.DPBlockEntities;
import com.legacy.dungeons_plus.registry.DPBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DynamicSpawnerBlock extends BaseEntityBlock
{
	public DynamicSpawnerBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new DynamicSpawnerBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, DPBlockEntities.DYNAMIC_SPAWNER, level.isClientSide ? DynamicSpawnerBlockEntity::clientTick : DynamicSpawnerBlockEntity::serverTick);
	}

	@Override
	public int getExpDrop(BlockState state, LevelReader level, BlockPos pos, int fortune, int silktouch)
	{
		return 15 + this.RANDOM.nextInt(15) + this.RANDOM.nextInt(15);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_56794_)
	{
		return RenderShape.MODEL;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
	{
		return new ItemStack(DPBlocks.DYNAMIC_SPAWNER);
	}
}
