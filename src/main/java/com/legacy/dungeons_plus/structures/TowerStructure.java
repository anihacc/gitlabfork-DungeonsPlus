package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.structures.pieces.TowerPieces;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigStructure;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class TowerStructure extends GelConfigStructure<NoneFeatureConfiguration>
{
	public TowerStructure(Codec<NoneFeatureConfiguration> codec, StructureConfig config)
	{
		super(codec, config, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), TowerStructure::generatePieces), TowerStructure::afterPlace);
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		ChunkPos chunkPos = context.chunkPos();
		Random rand = context.random();
		int samples = 1;
		int y = context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(8), chunkPos.getBlockZ(8), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
		int width = 13;
		for (int x = 0; x < 1; x++)
		{
			for (int z = 0; z < 1; z++)
			{
				y += context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(x * width), chunkPos.getBlockZ(z * width), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
				samples++;
			}
		}
		BlockPos pos = new BlockPos((chunkPos.x << 4) + 8, y / samples, (chunkPos.z << 4) + 8);
		TowerPieces.assemble(context.structureManager(), pos, Rotation.getRandom(rand), builder, rand);
	}

	private static void afterPlace(WorldGenLevel level, StructureFeatureManager structureManager, ChunkGenerator chunkGen, Random rand, BoundingBox bounds, ChunkPos chunkPos, PiecesContainer pieces)
	{
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		int minHeight = level.getMinBuildHeight();
		BoundingBox pieceBounds = pieces.calculateBoundingBox();
		int maxHeight = pieceBounds.minY();

		for (int x = bounds.minX(); x <= bounds.maxX(); ++x)
		{
			for (int z = bounds.minZ(); z <= bounds.maxZ(); ++z)
			{
				pos.set(x, maxHeight, z);
				if (!level.isEmptyBlock(pos) && pieceBounds.isInside(pos) && pieces.isInsidePiece(pos))
				{
					for (int y = maxHeight - 1; y > minHeight; --y)
					{
						pos.setY(y);
						if (!level.isEmptyBlock(pos) && !level.getBlockState(pos).getMaterial().isLiquid())
							break;

						level.setBlock(pos, (rand.nextFloat() < 0.2 ? Blocks.MOSSY_COBBLESTONE : Blocks.COBBLESTONE).defaultBlockState(), 2);
					}
				}
			}
		}
	}
}
