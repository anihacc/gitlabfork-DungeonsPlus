package com.legacy.dungeons_plus.structures.snowy_temple;

import java.util.Optional;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SnowyTempleStructure extends Structure
{
	public static final Codec<SnowyTempleStructure> CODEC = simpleCodec(SnowyTempleStructure::new);

	public SnowyTempleStructure(StructureSettings settings)
	{
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, pieces -> generatePieces(pieces, context));
	}

	private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		ChunkPos chunkPos = context.chunkPos();
		RandomSource rand = context.random();
		int samples = 1;
		int y = context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(8), chunkPos.getBlockZ(8), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		int width = 25;
		for (int x = 0; x < 1; x++)
		{
			for (int z = 0; z < 1; z++)
			{
				y += context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(x * width), chunkPos.getBlockZ(z * width), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
				samples++;
			}
		}
		BlockPos pos = new BlockPos(chunkPos.x << 4, y / samples, chunkPos.z << 4);
		SnowyTemplePieces.assemble(context.structureTemplateManager(), pos, Rotation.getRandom(rand), builder, rand);
	}

	@Override
	public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource rand, BoundingBox bounds, ChunkPos chunkPos, PiecesContainer pieces)
	{
		DPUtil.fillBelow(level, rand, bounds, pieces, (b, r) -> b);
	}

	@Override
	public StructureType<?> type()
	{
		return DPStructures.SNOWY_TEMPLE.getType();
	}
}
