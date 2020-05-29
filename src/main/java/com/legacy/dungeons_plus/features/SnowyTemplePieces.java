package com.legacy.dungeons_plus.features;

import java.util.List;
import java.util.Random;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.structures.GelStructurePiece;
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
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.registries.ForgeRegistries;

public class SnowyTemplePieces
{
	public static void assemble(ChunkGenerator<?> chunkGen, TemplateManager template, BlockPos pos, List<StructurePiece> pieces, SharedSeedRandom seed)
	{
		JigsawManager.addPieces(DungeonsPlus.locate("snowy_temple/temple/outside"), 7, SnowyTemplePieces.Piece::new, chunkGen, template, pos, pieces, seed);
	}

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "snowy_temple/");
		
		RandomBlockSwapProcessor iceToBlue = new RandomBlockSwapProcessor(Blocks.PACKED_ICE, 0.07F, Blocks.BLUE_ICE.getDefaultState());
		registry.register("temple/outside", registry.builder().names("temple/outside").processors(iceToBlue).build());
		registry.register("temple/inside", registry.builder().names("temple/inside_1", "temple/inside_2").processors(iceToBlue).build());
		registry.register("road", registry.builder().names("road_1", "road_2").build(), PlacementBehaviour.TERRAIN_MATCHING);
	}
	
	public static class Piece extends GelStructurePiece
	{
		public Piece(TemplateManager template, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox boundingBox)
		{
			super(DungeonsPlus.Features.SNOWY_TEMPLE.getSecond(), template, jigsawPiece, pos, groundLevelDelta, rotation, boundingBox);
		}

		public Piece(TemplateManager template, CompoundNBT nbt)
		{
			super(template, nbt, DungeonsPlus.Features.SNOWY_TEMPLE.getSecond());
		}

		@Override
		public void handleDataMarker(String key, IWorld world, BlockPos pos, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				this.setAir(world, pos);
				String[] data = key.split("-");

				world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.byName(data[1])).rotate(this.rotation), 3);
				if (world.getTileEntity(pos) instanceof ChestTileEntity)
					((ChestTileEntity) world.getTileEntity(pos)).setLootTable(LootTables.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
			}
			if (key.contains("spawner"))
			{
				this.setAir(world, pos);
				String[] data = key.split("-");
				
				world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
				if (world.getTileEntity(pos) instanceof MobSpawnerTileEntity)
				{
					((MobSpawnerTileEntity) world.getTileEntity(pos)).getSpawnerBaseLogic().setEntityType(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(data[1])));
				}
			}
		}
	}
}
