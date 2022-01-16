package com.legacy.dungeons_plus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nullable;

import com.legacy.structure_gel.api.block_entity.SpawnerAccessHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class DPUtil
{
	public static void createChest(ICreateChest chestCreator, ServerLevelAccessor level, BoundingBox bounds, Random rand, BlockPos pos, ResourceLocation lootTable, Rotation rotation, String[] data)
	{
		Direction facing = data.length > 1 ? Direction.byName(data[1]) : Direction.NORTH;
		ChestType chestType = data.length > 2 ? data[2].equals(ChestType.LEFT.getSerializedName()) ? ChestType.LEFT : (data[2].equals(ChestType.RIGHT.getSerializedName()) ? ChestType.RIGHT : ChestType.SINGLE) : ChestType.SINGLE;
		createChest(chestCreator, level, bounds, rand, pos, lootTable, rotation, facing, chestType);
	}

	public static void createChest(ICreateChest chestCreator, ServerLevelAccessor level, BoundingBox bounds, Random rand, BlockPos pos, ResourceLocation lootTable, Rotation rotation, Direction facing, ChestType chestType)
	{
		boolean waterlog = level.getFluidState(pos).getType() == Fluids.WATER;
		BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing).setValue(ChestBlock.TYPE, chestType).setValue(ChestBlock.WATERLOGGED, waterlog).rotate(level, pos, rotation);
		/*
		 * Set to air first since tile entities are silly
		 */
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		/*
		 * This part is so quark can use custom chests
		 */
		chestCreator.createChest(level, bounds, rand, pos, lootTable, chest);
		/*
		 * Only happens if lootr is loaded. This already happens once, so it's redund
		 */
		if (DungeonsPlus.isLootrLoaded)
			RandomizableContainerBlockEntity.setLootTable(level, rand, pos, lootTable);
	}

	public static void placeSpawner(String entity, ServerLevelAccessor level, BlockPos pos)
	{
		placeSpawner(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity)), level, pos);
	}

	public static void placeSpawner(EntityType<?> entity, ServerLevelAccessor level, BlockPos pos)
	{
		placeSpawner(SpawnerAccessHelper.createSpawnerEntity(entity), level, pos);
	}

	public static void placeSpawner(SpawnData spawnerEntity, ServerLevelAccessor level, BlockPos pos)
	{
		placeSpawner(Arrays.asList(spawnerEntity), level, pos);
	}

	public static void placeSpawner(List<SpawnData> spawnerEntities, ServerLevelAccessor level, BlockPos pos)
	{
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3);
		if (level.getBlockEntity(pos)instanceof SpawnerBlockEntity spawner)
		{
			SpawnerAccessHelper.setSpawnPotentials(spawner, level.getLevel(), pos, spawnerEntities.toArray(SpawnData[]::new));
		}
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

	public static interface ICreateChest
	{
		boolean createChest(ServerLevelAccessor level, BoundingBox bounds, Random rand, BlockPos pos, ResourceLocation lootTable, @Nullable BlockState chest);
	}
}
