package com.legacy.dungeons_plus;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.legacy.structure_gel.access_helpers.SpawnerAccessHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;

public class DPUtil
{
	public static void createChest(ICreateChest chestCreator, IServerWorld world, MutableBoundingBox bounds, Random rand, BlockPos pos, ResourceLocation lootTable, Rotation rotation, String[] data)
	{
		Direction facing = data.length > 1 ? Direction.byName(data[1]) : Direction.NORTH;
		ChestType chestType = data.length > 2 ? data[2].equals(ChestType.LEFT.getSerializedName()) ? ChestType.LEFT : (data[2].equals(ChestType.RIGHT.getSerializedName()) ? ChestType.RIGHT : ChestType.SINGLE) : ChestType.SINGLE;
		createChest(chestCreator, world, bounds, rand, pos, lootTable, rotation, facing, chestType);
	}

	public static void createChest(ICreateChest chestCreator, IServerWorld world, MutableBoundingBox bounds, Random rand, BlockPos pos, ResourceLocation lootTable, Rotation rotation, Direction facing, ChestType chestType)
	{
		boolean waterlog = world.getFluidState(pos).getType() == Fluids.WATER;
		BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing).setValue(ChestBlock.TYPE, chestType).setValue(ChestBlock.WATERLOGGED, waterlog).rotate(world, pos, rotation);
		/*
		 * Set to air first since tile entities are silly
		 */
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		/*
		 * This part is so quark can use custom chests
		 */
		chestCreator.createChest(world, bounds, rand, pos, lootTable, chest);
		/*
		 * Only happens if lootr is loaded. This already happens once, so it's redund
		 */
		if (DungeonsPlus.isLootrLoaded)
			LockableLootTileEntity.setLootTable(world, rand, pos, lootTable);
	}

	public static void placeSpawner(String entity, IWorld world, BlockPos pos)
	{
		placeSpawner(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity)), world, pos);
	}

	public static void placeSpawner(EntityType<?> entity, IWorld world, BlockPos pos)
	{
		placeSpawner(SpawnerAccessHelper.createSpawnerEntity(entity), world, pos);
	}

	public static void placeSpawner(WeightedSpawnerEntity spawnerEntity, IWorld world, BlockPos pos)
	{
		placeSpawner(Arrays.asList(spawnerEntity), world, pos);
	}

	public static void placeSpawner(List<WeightedSpawnerEntity> spawnerEntities, IWorld world, BlockPos pos)
	{
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3);
		if (world.getBlockEntity(pos) instanceof MobSpawnerTileEntity)
			SpawnerAccessHelper.setSpawnPotentials((MobSpawnerTileEntity) world.getBlockEntity(pos), spawnerEntities);
	}

	public static void placeMonsterBox(IServerWorld world, BlockPos pos, Random rand)
	{
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		if (DungeonsPlus.isQuarkLoaded && rand.nextInt(100) < DPConfig.COMMON.biggerDungeonMonsterBoxChance.get())
		{
			ResourceLocation boxLocation = new ResourceLocation("quark", "monster_box");
			if (ForgeRegistries.BLOCKS.containsKey(boxLocation))
			{
				try
				{
					BlockState monsterBox = ForgeRegistries.BLOCKS.getValue(boxLocation).defaultBlockState();
					world.setBlock(pos, monsterBox, 2);
				}
				catch (Throwable t)
				{
					DungeonsPlus.LOGGER.error(String.format("Failed to place monster box at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
					DungeonsPlus.LOGGER.error(t);
				}
			}
		}
	}

	public static void placeWaystone(IServerWorld world, BlockPos pos, Random rand, @Nullable Block defaultBlock)
	{
		if (DungeonsPlus.isWaystonesLoaded && rand.nextInt(100) < DPConfig.COMMON.towerWaystoneChance.get())
		{
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

			ResourceLocation waystoneLocation = new ResourceLocation("waystones", "waystone");
			if (ForgeRegistries.FEATURES.containsKey(waystoneLocation))
			{
				try
				{
					if (world instanceof ISeedReader)
					{
						world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
						@SuppressWarnings("unchecked")
						Feature<NoFeatureConfig> waystoneFeature = (Feature<NoFeatureConfig>) ForgeRegistries.FEATURES.getValue(waystoneLocation);
						waystoneFeature.place((ISeedReader) world, world.getLevel().getChunkSource().getGenerator(), rand, pos, NoFeatureConfig.INSTANCE);
					}
					return;
				}
				catch (Throwable t)
				{
					DungeonsPlus.LOGGER.error(String.format("Failed to place waystone at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ()));
					DungeonsPlus.LOGGER.error(t);
				}
			}
		}

		world.setBlock(pos, (defaultBlock != null ? defaultBlock : Blocks.AIR).defaultBlockState(), 2);

	}

	public static interface ICreateChest
	{
		boolean createChest(IServerWorld world, MutableBoundingBox bounds, Random rand, BlockPos pos, ResourceLocation lootTable, @Nullable BlockState chest);
	}
}
