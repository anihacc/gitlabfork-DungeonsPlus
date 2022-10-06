package com.legacy.dungeons_plus.structures.tower;

import java.util.Optional;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class TowerStructure extends Structure
{
	public static final Codec<TowerStructure> CODEC = simpleCodec(TowerStructure::new);

	public TowerStructure(StructureSettings settings)
	{
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, pieceBuilder -> this.generatePieces(pieceBuilder, context));
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		ChunkPos chunkPos = context.chunkPos();
		RandomSource rand = context.random();
		int samples = 1;
		int y = context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(8), chunkPos.getBlockZ(8), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int width = 13;
		for (int x = 0; x < 1; x++)
		{
			for (int z = 0; z < 1; z++)
			{
				y += context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(x * width), chunkPos.getBlockZ(z * width), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
				samples++;
			}
		}
		BlockPos pos = new BlockPos(chunkPos.x << 4, y / samples, chunkPos.z << 4);
		TowerPieces.assemble(context.structureTemplateManager(), pos, Rotation.getRandom(rand), builder, rand);
	}

	@Override
	public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource rand, BoundingBox bounds, ChunkPos chunkPos, PiecesContainer pieces)
	{
		DPUtil.fillBelow(level, rand, bounds, pieces, (b, r) -> (r.nextFloat() < 0.2 ? Blocks.MOSSY_COBBLESTONE : Blocks.COBBLESTONE).defaultBlockState());
	}

	@Override
	public StructureType<?> type()
	{
		return DPStructures.TOWER.getType();
	}
}
