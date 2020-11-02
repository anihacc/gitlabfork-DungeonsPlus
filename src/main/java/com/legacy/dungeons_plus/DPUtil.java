package com.legacy.dungeons_plus;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.legacy.structure_gel.access_helpers.SpawnerAccessHelper;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class DPUtil
{
	public static void placeLootChest(ResourceLocation lootTable, IWorld world, Random rand, BlockPos pos, Rotation rotation, String facing)
	{
		placeLootChest(lootTable, world, rand, pos, rotation, Direction.byName(facing), ChestType.SINGLE);
	}

	public static void placeLootChest(ResourceLocation lootTable, IWorld world, Random rand, BlockPos pos, Rotation rotation, String facing, String chestType)
	{
		placeLootChest(lootTable, world, rand, pos, rotation, Direction.byName(facing), chestType.equals(ChestType.LEFT.getString()) ? ChestType.LEFT : (chestType.equals(ChestType.RIGHT.getString()) ? ChestType.RIGHT : ChestType.SINGLE));
	}

	public static void placeLootChest(ResourceLocation lootTable, IWorld world, Random rand, BlockPos pos, Rotation rotation, Direction facing)
	{
		placeLootChest(lootTable, world, rand, pos, rotation, facing, ChestType.SINGLE);
	}

	public static void placeLootChest(ResourceLocation lootTable, IWorld world, Random rand, BlockPos pos, Rotation rotation, Direction facing, ChestType chestType)
	{
		boolean waterlog = world.getFluidState(pos).getFluid() == Fluids.WATER;
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, facing).with(ChestBlock.TYPE, chestType).with(ChestBlock.WATERLOGGED, waterlog).rotate(world, pos, rotation), 3);
		if (world.getTileEntity(pos) instanceof ChestTileEntity)
			((ChestTileEntity) world.getTileEntity(pos)).setLootTable(lootTable, rand.nextLong());
	}

	public static void placeSpawner(String entity, IWorld world, Random rand, BlockPos pos)
	{
		placeSpawner(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity)), world, rand, pos);
	}

	public static void placeSpawner(EntityType<?> entity, IWorld world, Random rand, BlockPos pos)
	{
		placeSpawner(SpawnerAccessHelper.createSpawnerEntity(entity), world, rand, pos);
	}

	public static void placeSpawner(WeightedSpawnerEntity spawnerEntity, IWorld world, Random rand, BlockPos pos)
	{
		placeSpawner(Arrays.asList(spawnerEntity), world, rand, pos);
	}

	public static void placeSpawner(List<WeightedSpawnerEntity> spawnerEntities, IWorld world, Random rand, BlockPos pos)
	{
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
		if (world.getTileEntity(pos) instanceof MobSpawnerTileEntity)
			SpawnerAccessHelper.setSpawnPotentials((MobSpawnerTileEntity) world.getTileEntity(pos), spawnerEntities);
	}
}