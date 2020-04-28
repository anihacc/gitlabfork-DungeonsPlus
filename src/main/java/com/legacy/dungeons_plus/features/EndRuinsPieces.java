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
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.registries.ForgeRegistries;

public class EndRuinsPieces
{
	public static void assemble(ChunkGenerator<?> chunkGen, TemplateManager template, BlockPos pos, List<StructurePiece> pieces, SharedSeedRandom seed)
	{
		JigsawManager.func_214889_a(DungeonsPlus.locate("end_ruins/base_plate"), 7, EndRuinsPieces.Piece::new, chunkGen, template, pos, pieces, seed);
	}

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "end_ruins/");

		/**
		 * In this case, root is a simple flat structure with jigsaws in it to generate
		 * other structures on the ground off of it, similar to how the pillager outpost
		 * works.
		 */
		registry.register("base_plate", registry.builder().names("base_plate").build(), PlacementBehaviour.TERRAIN_MATCHING);
		registry.register("pylon_plate", registry.builder().names("pylon_plate").build(), PlacementBehaviour.TERRAIN_MATCHING);

		/**
		 * By using the .weight(int) method, I can set the weights of all structures in
		 * the pool to the same value. Combined with the JigsawPoolBuilder.collect
		 * method, I can set the weights of all normal towers to 2 and all broken towers
		 * to 1 (default).
		 */
		registry.register("pylon", JigsawPoolBuilder.collect(registry.builder().names("pylon/tall", "pylon/medium", "pylon/small"), registry.builder().weight(2).names("pylon/tall_broken", "pylon/medium_broken", "pylon/small_broken")));

		registry.register("pylon/debris", registry.builder().names("pylon/debris_1", "pylon/debris_2", "pylon/debris_3", "pylon/debris_4").build());

		/**
		 * Since all of the next registry entries will start with "end_ruins/tower/" I'm
		 * changing the prefix. Using setPrefix creates a clone of the registry, so I'm
		 * creating a new registry for this prefix. In practice, this allows me to use
		 * the old registry and it's prefix along with this one.
		 */
		JigsawRegistryHelper towerRegistry = registry.setPrefix("end_ruins/tower/");
		/**
		 * By using the .clone() method, I can use the same settings for each pool
		 * builder. All tower pools will use the RandomBlockSwapProcessor to replace end
		 * stone bricks with end stone.
		 */
		JigsawPoolBuilder towerPieces = towerRegistry.builder().processors(new RandomBlockSwapProcessor(Blocks.END_STONE_BRICKS, 0.1F, Blocks.END_STONE.getDefaultState()));
		towerRegistry.register("base", towerPieces.clone().names("base_1", "base_2").build());
		towerRegistry.register("mid", towerPieces.clone().names("mid_1", "mid_2").build());
		towerRegistry.register("top", towerPieces.clone().names("top_1", "top_2").build());

		towerRegistry.register("spine", towerRegistry.builder().names("spine").build());
		towerRegistry.register("block_pile", towerRegistry.builder().names(ImmutableMap.of("block_pile_1", 2, "block_pile_2", 2, "block_pile_3", 2, "block_pile_4", 1)).build());

	}

	public static class Piece extends GelStructurePiece
	{
		public Piece(TemplateManager template, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox boundingBox)
		{
			super(DungeonsPlus.Features.END_RUINS.getSecond(), template, jigsawPiece, pos, groundLevelDelta, rotation, boundingBox);
		}

		public Piece(TemplateManager template, CompoundNBT nbt)
		{
			super(template, nbt, DungeonsPlus.Features.END_RUINS.getSecond());
		}

		/**
		 * Places end stone underneath the structure in case it generates with overhang, and obsidian under the pylons just in case.
		 */
		@Override
		public boolean func_225577_a_(IWorld world, ChunkGenerator<?> chunkGen, Random rand, MutableBoundingBox bounds, ChunkPos chunkPos)
		{
			if (super.func_225577_a_(world, chunkGen, rand, bounds, chunkPos))
			{
				if (this.getLocation().toString().contains("end_ruins/tower/base_"))
					this.extendDown(world, Blocks.END_STONE.getDefaultState(), bounds, this.rotation, rand);
				if (this.getLocation().toString().contains("end_ruins/pylon/"))
					this.extendDown(world, Blocks.OBSIDIAN.getDefaultState(), bounds, this.rotation, rand);
			}
			return false;
		}

		@Override
		public void handleDataMarker(String key, IWorld worldIn, BlockPos pos, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("spawner"))
			{
				this.setAir(worldIn, pos);
				String[] data = key.split("-");
				EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(data[1]));

				if (entityType == EntityType.PHANTOM || entityType == EntityType.ENDERMAN)
				{
					worldIn.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
					if (worldIn.getTileEntity(pos) instanceof MobSpawnerTileEntity)
					{
						MobSpawnerTileEntity tile = (MobSpawnerTileEntity) worldIn.getTileEntity(pos);					
						tile.getSpawnerBaseLogic().setEntityType(entityType);
					}
				}
				else
				{
					worldIn.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState(), 3);
				}
			}
		}
	}
}
