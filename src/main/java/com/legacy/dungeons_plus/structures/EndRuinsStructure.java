package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class EndRuinsStructure extends GelConfigJigsawStructure
{
	public EndRuinsStructure(Codec<JigsawConfiguration> codec, StructureConfig config)
	{
		super(codec, config, 0, true, true, EndRuinsStructure::isValidHeight, Piece::new);
	}

	private static boolean isValidHeight(PieceGeneratorSupplier.Context<JigsawConfiguration> context)
	{
		ChunkPos chunkPos = context.chunkPos();
		int y = context.chunkGenerator().getBaseHeight(chunkPos.x << 4 + 7, chunkPos.z << 4 + 7, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
		return y >= 60;
	}

	public static class Piece extends AbstractGelStructurePiece
	{
		public Piece(StructureManager template, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds)
		{
			super(template, piece, pos, groundLevelDelta, rotation, bounds);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(context, tag);
		}

		@Override
		public StructurePieceType getType()
		{
			return DungeonsPlus.Structures.END_RUINS.getPieceType();
		}

		/**
		 * Places end stone underneath the structure in case it generates with overhang,
		 * and obsidian under the pylons just in case.
		 */
		@Override
		public void place(WorldGenLevel level, StructureFeatureManager structureManager, ChunkGenerator chunkGen, Random rand, BoundingBox bounds, BlockPos pos, boolean isLegacy)
		{
			if (pos.getY() > 5)
			{
				super.place(level, structureManager, chunkGen, rand, bounds, pos, isLegacy);
				String name = this.getLocation().toString();
				if (name.contains("end_ruins/tower/base_"))
					this.extendDown(level, Blocks.END_STONE.defaultBlockState(), bounds, this.rotation, rand);
				if (name.contains("end_ruins/pylon/"))
					this.extendDown(level, Blocks.OBSIDIAN.defaultBlockState(), bounds, this.rotation, rand);
			}
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.contains("spawner"))
			{
				String[] data = key.split("-");
				DPUtil.placeSpawner(data[1], level, pos);
			}
		}
	}
}