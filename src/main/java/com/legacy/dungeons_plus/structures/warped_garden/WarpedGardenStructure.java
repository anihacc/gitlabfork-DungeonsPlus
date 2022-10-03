package com.legacy.dungeons_plus.structures.warped_garden;

import java.util.Random;

import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class WarpedGardenStructure extends GelConfigJigsawStructure<JigsawConfiguration>
{
	public WarpedGardenStructure(Codec<JigsawConfiguration> codec, StructureConfig config)
	{
		super(codec, config, 36, true, false, context -> true, Piece::new);
	}

	@Override
	public int getSpacing()
	{
		return 36;
	}

	@Override
	public int getOffset()
	{
		return this.getSpacing();
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
			return DPStructures.WARPED_GARDEN.getPieceType();
		}

		@Override
		public BlockState modifyState(ServerLevelAccessor level, Random rand, BlockPos pos, BlockState originalState)
		{
			if (originalState.is(Blocks.BLUE_WOOL))
				return Blocks.WATER.defaultBlockState();
			if (originalState.is(Blocks.OBSIDIAN) && rand.nextFloat() < 0.35F)
				return Blocks.CRYING_OBSIDIAN.defaultBlockState();
			return originalState;
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
		}
	}
}
