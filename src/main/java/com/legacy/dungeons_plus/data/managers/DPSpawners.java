package com.legacy.dungeons_plus.data.managers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * This class exists identically for clients and servers. Add listeners during
 * {@link FMLCommonSetupEvent}.
 * 
 * @author Silver_David
 *
 */
public class DPSpawners
{
	public static final SpawnerType TOWER_ZOMBIE = SpawnerType.register("tower_zombie", DPSpawners::towerZombie);;

	public static void init()
	{
	}

	private static void towerZombie(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		s.getSpawner().setEntityId(EntityType.ZOMBIE);
	}

	public static class Spawner extends BaseSpawner implements Cloneable
	{
		public Spawner()
		{
		}

		@Override
		public void broadcastEvent(Level level, BlockPos pos, int param)
		{
			level.blockEvent(pos, Blocks.SPAWNER, param, 0);
		}

		@Override
		public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData spawnData)
		{
			super.setNextSpawnData(level, pos, spawnData);
			if (level != null)
			{
				BlockState state = level.getBlockState(pos);
				level.sendBlockUpdated(pos, state, state, 4);
			}
		}

		@Nullable
		@Override
		public BlockEntity getSpawnerBlockEntity()
		{
			return null;
		}

		@Override
		protected Spawner clone()
		{
			Spawner clone = new Spawner();
			clone.load(null, BlockPos.ZERO, this.save(new CompoundTag()));
			return clone;
		}
	}

	public static class SpawnerType
	{
		private static final Map<ResourceLocation, SpawnerType> REGISTRY = new HashMap<>();
		public static final SpawnerType DEFAULT = register("default", (spawner, level, pos) ->
		{
		});

		private final SpawnerMaker spawner;
		private final ResourceLocation name;

		public SpawnerType(ResourceLocation name, SpawnerMaker spawner)
		{
			this.spawner = spawner;
			this.name = name;
		}

		public static SpawnerType register(ResourceLocation name, SpawnerModifier spawnerModifier)
		{
			if (!REGISTRY.containsKey(name))
			{
				SpawnerType type = new SpawnerType(name, (level, pos) -> SpawnerMaker.makeSpawner(spawnerModifier, level, pos));
				REGISTRY.put(name, type);
				return type;
			}
			else
			{
				DungeonsPlus.LOGGER.warn("Attempted to register a spawner under an existing name: {}", name);
				return REGISTRY.get(name);
			}
		}

		public static SpawnerType register(String name, SpawnerModifier spawnerModifier)
		{
			return register(DungeonsPlus.locate(name), spawnerModifier);
		}

		public static SpawnerType get(ResourceLocation name)
		{
			return REGISTRY.getOrDefault(name, DEFAULT);
		}

		public Spawner getSpawner(@Nullable Level level, BlockPos pos)
		{
			return this.spawner.make(level, pos);
		}

		public ResourceLocation getTypeName()
		{
			return this.name;
		}
	}
	
	public static interface SpawnerModifier
	{
		void modify(SpawnerBlockEntity spawner, @Nullable Level level, BlockPos pos);
	}
	
	private static interface SpawnerMaker
	{
		Spawner make(@Nullable Level level, BlockPos pos);
		
		public static Spawner makeSpawner(SpawnerModifier modifier, @Nullable Level level, BlockPos pos)
		{
			BlockState state = level != null ? level.getBlockState(pos) : DPBlocks.DYNAMIC_SPAWNER.defaultBlockState();
			if (!state.is(DPBlocks.DYNAMIC_SPAWNER))
				state = DPBlocks.DYNAMIC_SPAWNER.defaultBlockState();
			SpawnerBlockEntity spawnerBE = new SpawnerBlockEntity(pos, state);
			modifier.modify(spawnerBE, level, pos);
			Spawner spawner = new Spawner();
			spawner.load(level, pos, spawnerBE.getSpawner().save(new CompoundTag()));
			return spawner;
		}
	}
}
