package com.legacy.dungeons_plus.features;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.structures.GelStructurePiece;
import com.legacy.structure_gel.structures.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.structures.jigsaw.JigsawRegistryHelper;
import com.legacy.structure_gel.structures.processors.RandomBlockSwapProcessor;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;

public class BiggerDungeonPieces
{
	public static void assemble(ChunkGenerator<?> chunkGen, TemplateManager template, BlockPos pos, List<StructurePiece> pieces, SharedSeedRandom seed)
	{
		JigsawManager.func_214889_a(DungeonsPlus.locate("bigger_dungeon/root"), 7, BiggerDungeonPieces.Piece::new, chunkGen, template, pos, pieces, seed);
	}

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "bigger_dungeon/");

		/**
		 * Check TowerPieces for information on the JigsawRegistryHelper and
		 * JigsawPoolBuilder.
		 * 
		 * Jigsaw structures generate at surface level by default. To prevent this, have
		 * whatever structure starts the generation have generateAtSurface set to false.
		 * You'll set the y level in the structure start like normal. In this case, it's
		 * in BiggerDungeonStructure.Start.init
		 */
		registry.register("root", registry.builder().names("root").maintainWater(false).build());

		JigsawPoolBuilder basicPoolBuilder = registry.builder().maintainWater(false).processors(new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.MOSSY_COBBLESTONE.getDefaultState()));
		registry.register("main_room", basicPoolBuilder.clone().names("main_room").build());

		/**
		 * Using multiple JigsawPoolBuilders, I can collect them into one pool and build
		 * that, while keeping the settings of each pool builder in their own
		 * structures. In this, I'll have a zombie, skeleton, husk, and stray room with
		 * the zombie and skeleton having a weight of 4.
		 * 
		 * Since basicRooms is cloning from basicPoolBuilder, they will have the
		 * processor that converts some cobblestone to mossy cobble. Note: We have to
		 * clone it otherwise we end up changing the instance, which affects all
		 * builders associated with it.
		 */
		JigsawPoolBuilder basicRooms = basicPoolBuilder.clone().names(ImmutableMap.of("side_room/skeleton", 4, "side_room/zombie", 4));
		JigsawPoolBuilder strayRoom = registry.builder().names("side_room/stray").maintainWater(false).processors(new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.PACKED_ICE.getDefaultState()));
		JigsawPoolBuilder huskRoom = registry.builder().names("side_room/husk").maintainWater(false).processors(new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.TERRACOTTA.getDefaultState()));
		registry.register("side_room", JigsawPoolBuilder.collect(basicRooms, strayRoom, huskRoom));

	}

	public static class Piece extends GelStructurePiece
	{
		public Piece(TemplateManager template, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox boundingBox)
		{
			super(DungeonsPlus.Features.BIGGER_DUNGEON.getSecond(), template, jigsawPiece, pos, groundLevelDelta, rotation, boundingBox);
		}

		public Piece(TemplateManager template, CompoundNBT nbt)
		{
			super(template, nbt, DungeonsPlus.Features.BIGGER_DUNGEON.getSecond());
		}

		@Override
		public void handleDataMarker(String key, IWorld worldIn, BlockPos pos, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				this.setAir(worldIn, pos);
				if (rand.nextBoolean())
				{
					String[] data = key.split("-");

					worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.byName(data[1])).rotate(this.rotation), 3);
					if (worldIn.getTileEntity(pos) instanceof ChestTileEntity)
						((ChestTileEntity) worldIn.getTileEntity(pos)).setLootTable(LootTables.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
				}
			}
			if (key.contains("spawner"))
			{
				this.setAir(worldIn, pos);
				String[] data = key.split("-");

				worldIn.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
				if (worldIn.getTileEntity(pos) instanceof MobSpawnerTileEntity)
					((MobSpawnerTileEntity) worldIn.getTileEntity(pos)).getSpawnerBaseLogic().setEntityType(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(data[1])));

			}
		}
	}
}
