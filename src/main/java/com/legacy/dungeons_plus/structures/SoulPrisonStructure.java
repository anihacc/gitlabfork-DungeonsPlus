package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.structures.pieces.SoulPrisonPieces;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigStructure;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SoulPrisonStructure extends GelConfigStructure<NoneFeatureConfiguration>
{
	public SoulPrisonStructure(Codec<NoneFeatureConfiguration> codec, StructureConfig config)
	{
		super(codec, config, PieceGeneratorSupplier.simple(context -> context.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG) && isValidPos(context), SoulPrisonStructure::generatePieces));
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		ChunkPos chunkPos = context.chunkPos();
		Random rand = context.random();
		SoulPrisonPieces.assemble(context.structureManager(), new BlockPos((chunkPos.x << 4) + 9, 29, (chunkPos.z << 4) + 9), Rotation.getRandom(rand), builder, rand);
	}

	private static boolean isValidPos(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context)
	{
		ChunkGenerator chunkGen = context.chunkGenerator();
		ChunkPos chunkPos = context.chunkPos();
		NoiseColumn minCorner = chunkGen.getBaseColumn(chunkPos.getMinBlockX() + 3, chunkPos.getMinBlockZ() + 3, context.heightAccessor());
		NoiseColumn maxCorner = chunkGen.getBaseColumn(chunkPos.getMinBlockX() + 22, chunkPos.getMinBlockZ() + 22, context.heightAccessor());
		return minCorner.getBlock(29).getBlock() == Blocks.LAVA && maxCorner.getBlock(29).getBlock() == Blocks.LAVA;
	}
}
