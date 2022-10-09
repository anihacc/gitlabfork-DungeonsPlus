package com.legacy.dungeons_plus.structures.leviathan;

import com.legacy.dungeons_plus.registry.DPJigsawTypes;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure.IPieceFactory;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure.JigsawContext;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.IJigsawCapability;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.JigsawType;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class LeviathanStructure
{
	public static class Capability implements IJigsawCapability
	{
		public static final Capability INSTANCE = new Capability();
		public static final Codec<Capability> CODEC = Codec.unit(INSTANCE);

		@Override
		public JigsawType<?> getType()
		{
			return DPJigsawTypes.LEVIATHAN;
		}

		@Override
		public boolean canPlace(GenerationContext generationContext, BlockPos placementPos, JigsawContext jigsawContext)
		{
			ChunkPos chunkPos = generationContext.chunkPos();
			int[] xz = new int[] { -16, 0, 16 };
			for (int x : xz)
				for (int z : xz)
					if (generationContext.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(x), chunkPos.getBlockZ(z), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()) < 65)
						return false;
			return true;
		}

		@Override
		public IPieceFactory getPieceFactory()
		{
			return Piece::new;
		}
	}

	public static class Piece extends AbstractGelStructurePiece
	{
		public Piece(StructureTemplateManager template, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds)
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
			return DPStructures.LEVIATHAN.getPieceType().get();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds)
		{
		}
	}
}
