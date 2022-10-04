package com.legacy.dungeons_plus.structures.end_ruins;

import java.util.List;

import com.legacy.dungeons_plus.registry.DPJigsawTypes;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure.IPieceFactory;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure.JigsawContext;
import com.legacy.structure_gel.api.structure.base.IPieceBuilderModifier;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.IJigsawCapability;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.JigsawType;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class EndRuinsStructure
{
	public static class Capability implements IJigsawCapability
	{
		public static final Capability INSTANCE = new Capability();
		public static final Codec<Capability> CODEC = Codec.unit(INSTANCE);

		@Override
		public JigsawType<?> getType()
		{
			return DPJigsawTypes.END_RUINS;
		}

		@Override
		public IPieceFactory getPieceFactory()
		{
			return Piece::new;
		}

		@Override
		public boolean canPlace(GenerationContext generationContext, BlockPos placementPos, JigsawContext jigsawContext)
		{
			ChunkPos chunkPos = generationContext.chunkPos();
			int[] xz = new int[] { -8, 8 };
			for (int x : xz)
				for (int z : xz)
					if (generationContext.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(x), chunkPos.getBlockZ(z), Heightmap.Types.WORLD_SURFACE_WG, generationContext.heightAccessor(), generationContext.randomState()) < 56)
						return false;
			return true;
		}

		@Override
		public void modifyPieceBuilder(StructurePiecesBuilder pieceBuilder, Structure.GenerationContext context)
		{
			List<StructurePiece> pieces = IPieceBuilderModifier.getPieces(pieceBuilder);

			pieces.removeIf(p ->
			{
				// Removes pieces that generate in the void
				var bounds = p.getBoundingBox();
				if (bounds.minY() < 20)
					return true;

				// Removes pieces hanging over the void
				int[] zs = new int[] { bounds.minZ(), bounds.maxZ() };
				for (int x : new int[] { bounds.minX(), bounds.maxX() })
					for (int z : zs)
						if (context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()) < 20)
							return true;

				return false;
			});
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
			return DPStructures.END_RUINS.getPieceType().get();
		}

		/**
		 * Places end stone underneath the structure in case it generates with overhang,
		 * and obsidian under the pylons just in case.
		 */
		@Override
		public void place(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource rand, BoundingBox bounds, BlockPos pos, boolean isLegacy)
		{
			if (pos.getY() > 5)
			{
				super.place(level, structureManager, chunkGen, rand, bounds, pos, isLegacy);
				String name = this.getLocation().toString();
				if (name.contains("end_ruins/tower/base_"))
					this.extendDown(level, Blocks.END_STONE.defaultBlockState(), bounds, this.rotation, rand);
			}
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds)
		{
		}
	}
}