package com.legacy.dungeons_plus.block_entities;

import com.legacy.dungeons_plus.data.managers.DPSpawners.Spawner;
import com.legacy.dungeons_plus.data.managers.DPSpawners.SpawnerType;
import com.legacy.dungeons_plus.registry.DPBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DynamicSpawnerBlockEntity extends DPBlockEntity
{
	private static final String SPAWNER_ID_KEY = "SpawnerID";

	private ResourceLocation spawnerID = SpawnerType.DEFAULT.getTypeName();
	private Spawner spawner = SpawnerType.DEFAULT.getSpawner(this.level, this.worldPosition);

	public DynamicSpawnerBlockEntity(BlockPos pos, BlockState state)
	{
		super(DPBlockEntities.DYNAMIC_SPAWNER, pos, state);
	}

	public void setSpawner(SpawnerType type)
	{
		ResourceLocation newName = type.getTypeName();
		if (!newName.equals(this.spawnerID))
		{
			this.spawnerID = newName;
			this.spawner = type.getSpawner(this.level, this.worldPosition);
		}
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		if (tag.contains(SPAWNER_ID_KEY))
		{
			this.setSpawner(SpawnerType.get(new ResourceLocation(tag.getString(SPAWNER_ID_KEY))));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		if (this.spawnerID != null)
		{
			tag.putString(SPAWNER_ID_KEY, this.spawnerID.toString());
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, DynamicSpawnerBlockEntity blockEntity)
	{
		blockEntity.spawner.clientTick(level, pos);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, DynamicSpawnerBlockEntity blockEntity)
	{
		blockEntity.spawner.serverTick((ServerLevel) level, pos);
	}

	@Override
	public boolean triggerEvent(int paramA, int paramB)
	{
		return this.spawner.onEventTriggered(this.level, paramA) ? true : super.triggerEvent(paramA, paramB);
	}

	@Override
	public boolean onlyOpCanSetNbt()
	{
		return true;
	}

	public Spawner getSpawner()
	{
		return this.spawner;
	}
}