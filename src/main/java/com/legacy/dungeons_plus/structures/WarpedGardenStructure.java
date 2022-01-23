package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.structures.pieces.WarpedGardenPieces;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigStructure;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class WarpedGardenStructure extends GelConfigStructure<NoneFeatureConfiguration>
{
	public WarpedGardenStructure(Codec<NoneFeatureConfiguration> codec, StructureConfig config)
	{
		super(codec, config, WarpedGardenStructure::generatePieces);
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		ChunkPos chunkPos = context.chunkPos();
		Random rand = context.random();
		WarpedGardenPieces.assemble(context.structureManager(), new BlockPos((chunkPos.x << 4) + 9, 90, (chunkPos.z << 4) + 9), Rotation.getRandom(rand), builder, rand);
	}
}
