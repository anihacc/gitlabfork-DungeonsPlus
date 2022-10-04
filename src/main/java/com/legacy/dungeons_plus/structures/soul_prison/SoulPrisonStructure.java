package com.legacy.dungeons_plus.structures.soul_prison;

import java.util.Optional;

import com.legacy.dungeons_plus.registry.DPStructures;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SoulPrisonStructure extends Structure
{
	public static final Codec<SoulPrisonStructure> CODEC = simpleCodec(SoulPrisonStructure::new);

	public SoulPrisonStructure(StructureSettings settings)
	{
		super(settings);
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
	{
		ChunkPos chunkPos = context.chunkPos();
		RandomSource rand = context.random();
		SoulPrisonPieces.assemble(context.structureTemplateManager(), new BlockPos((chunkPos.x << 4) + 9, 29, (chunkPos.z << 4) + 9), Rotation.getRandom(rand), builder, rand);
	}

	private boolean isValidPos(GenerationContext context)
	{
		ChunkGenerator chunkGen = context.chunkGenerator();
		ChunkPos chunkPos = context.chunkPos();
		NoiseColumn minCorner = chunkGen.getBaseColumn(chunkPos.getMinBlockX() + 3, chunkPos.getMinBlockZ() + 3, context.heightAccessor(), context.randomState());
		NoiseColumn maxCorner = chunkGen.getBaseColumn(chunkPos.getMinBlockX() + 22, chunkPos.getMinBlockZ() + 22, context.heightAccessor(), context.randomState());
		return minCorner.getBlock(29).getBlock() == Blocks.LAVA && maxCorner.getBlock(29).getBlock() == Blocks.LAVA;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
	{
		if (isValidPos(context))
			return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, pieces -> generatePieces(pieces, context));
		return Optional.empty();
	}

	@Override
	public StructureType<?> type()
	{
		return DPStructures.SOUL_PRISON.getType();
	}
}
