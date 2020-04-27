package com.legacy.dungeons_plus.features;

import java.util.List;
import java.util.Random;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.structures.GelStructurePiece;
import com.legacy.structure_gel.structures.jigsaw.JigsawRegistryHelper;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;

public class LeviathanPieces
{
	public static void assemble(ChunkGenerator<?> chunkGen, TemplateManager template, BlockPos pos, List<StructurePiece> pieces, SharedSeedRandom seed)
	{
		JigsawManager.func_214889_a(DungeonsPlus.locate("leviathan/spine"), 7, LeviathanPieces.Piece::new, chunkGen, template, pos, pieces, seed);
	}

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "leviathan/");
		registry.register("spine", registry.builder().names("spine_front_1", "spine_front_2").build());
		registry.register("spine_back", registry.builder().names("spine_back_1", "spine_back_2").build());
		registry.register("skull", registry.builder().names("skull_1", "skull_2").build());
		registry.register("tail", registry.builder().names("tail_1", "tail_2").build());
		registry.register("room", registry.builder().names("room").build());
	}

	public static class Piece extends GelStructurePiece
	{
		public Piece(TemplateManager template, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox boundingBox)
		{
			super(DungeonsPlus.Features.LEVIATHAN.getSecond(), template, jigsawPiece, pos, groundLevelDelta, rotation, boundingBox);
		}

		public Piece(TemplateManager template, CompoundNBT nbt)
		{
			super(template, nbt, DungeonsPlus.Features.LEVIATHAN.getSecond());
		}

		@Override
		public void handleDataMarker(String key, IWorld world, BlockPos pos, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				this.setAir(world, pos);
				if (rand.nextBoolean())
				{
					String[] data = key.split("-");

					world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.byName(data[1])).rotate(this.rotation), 3);
					if (world.getTileEntity(pos) instanceof ChestTileEntity)
						((ChestTileEntity) world.getTileEntity(pos)).setLootTable(LootTables.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
				}
			}
			if (key.equals("spawner"))
			{
				this.setAir(world, pos);

				world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
				if (world.getTileEntity(pos) instanceof MobSpawnerTileEntity)
				{
					((MobSpawnerTileEntity) world.getTileEntity(pos)).getSpawnerBaseLogic().setEntityType(EntityType.HUSK);
				}
			}
		}
	}
}