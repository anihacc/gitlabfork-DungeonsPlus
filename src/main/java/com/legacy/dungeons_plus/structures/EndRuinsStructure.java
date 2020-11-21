package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.worldgen.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.worldgen.jigsaw.GelConfigJigsawStructure;
import com.legacy.structure_gel.worldgen.jigsaw.GelJigsawStructure;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager.IPieceFactory;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class EndRuinsStructure extends GelConfigJigsawStructure
{
	public EndRuinsStructure(Codec<VillageConfig> codec, StructureConfig config)
	{
		super(codec, config, 0, true, true);
	}

	@Override
	public int getSeed()
	{
		return 843152;
	}

	@Override
	public IPieceFactory getPieceType()
	{
		return Piece::new;
	}

	@Override
	public void handleStartFactory(GelJigsawStructure.Start start, DynamicRegistries dynamicRegistries, ChunkGenerator chunkGen, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, VillageConfig config)
	{
		int x = (chunkX * 16) + 7;
		int z = (chunkZ * 16) + 7;
		int y = chunkGen.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
		if (y >= 60)
		{
			super.handleStartFactory(start, dynamicRegistries, chunkGen, templateManager, chunkX, chunkZ, biome, config);
			start.getComponents().removeIf(c -> c.getBoundingBox().minY < 5);
			start.recalculateStructureSize();
		}
	}

	public static class Piece extends AbstractGelStructurePiece
	{
		public Piece(TemplateManager template, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox boundingBox)
		{
			super(template, jigsawPiece, pos, groundLevelDelta, rotation, boundingBox);
		}

		public Piece(TemplateManager template, CompoundNBT nbt)
		{
			super(template, nbt);
		}

		@Override
		public IStructurePieceType getStructurePieceType()
		{
			return DungeonsPlus.Structures.END_RUINS.getPieceType();
		}

		/**
		 * Places end stone underneath the structure in case it generates with overhang,
		 * and obsidian under the pylons just in case.
		 */
		@Override
		public boolean func_237001_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator chunkGen, Random rand, MutableBoundingBox bounds, BlockPos pos, boolean isLegacy)
		{
			if (super.func_237001_a_(seedReader, structureManager, chunkGen, rand, bounds, pos, isLegacy))
			{
				if (this.getLocation().toString().contains("end_ruins/tower/base_"))
					this.extendDown(seedReader, Blocks.END_STONE.getDefaultState(), bounds, this.rotation, rand);
				if (this.getLocation().toString().contains("end_ruins/pylon/"))
					this.extendDown(seedReader, Blocks.OBSIDIAN.getDefaultState(), bounds, this.rotation, rand);
				return true;
			}
			return false;
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("spawner"))
			{
				String[] data = key.split("-");
				DPUtil.placeSpawner(data[1], world, pos);
			}
		}
	}
}