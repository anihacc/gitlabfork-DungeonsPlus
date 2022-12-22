package com.legacy.dungeons_plus.structures.warped_garden;

import com.legacy.dungeons_plus.registry.DPJigsawTypes;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.jigsaw.ExtendedJigsawStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.IPieceFactory;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.IJigsawCapability;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.JigsawType;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class WarpedGardenStructure
{
	public static class Capability implements IJigsawCapability
	{
		public static final Capability INSTANCE = new Capability();
		public static final Codec<Capability> CODEC = Codec.unit(INSTANCE);

		@Override
		public JigsawType<?> getType()
		{
			return DPJigsawTypes.WARPED_GARDEN;
		}
		
		@Override
		public IPieceFactory getPieceFactory()
		{
			return Piece::new;
		}
	}

	public static class Piece extends ExtendedJigsawStructurePiece
	{
		public Piece(IPieceFactory.Context context)
		{
			super(context);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(context, tag);
		}

		@Override
		public StructurePieceType getType()
		{
			return DPStructures.WARPED_GARDEN.getPieceType().get();
		}

		@Override
		public BlockState modifyState(ServerLevelAccessor level, RandomSource rand, BlockPos pos, BlockState originalState)
		{
			if (originalState.is(Blocks.BLUE_WOOL))
				return Blocks.WATER.defaultBlockState();
			if (originalState.is(Blocks.OBSIDIAN) && rand.nextFloat() < 0.35F)
				return Blocks.CRYING_OBSIDIAN.defaultBlockState();
			return originalState;
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds)
		{
		}
	}
}
