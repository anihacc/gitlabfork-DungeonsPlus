package com.legacy.dungeons_plus.structures.snowy_temple;

import java.util.Random;

import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigStructure;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SnowyTempleStructure extends GelConfigStructure<NoneFeatureConfiguration>
{
	public SnowyTempleStructure(Codec<NoneFeatureConfiguration> codec, StructureConfig config)
	{
		super(codec, config, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), SnowyTempleStructure::generatePieces));
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

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context)
	{
		ChunkPos chunkPos = context.chunkPos();
		Random rand = context.random();
		int samples = 1;
		int y = context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(8), chunkPos.getBlockZ(8), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
		int width = 25;
		for (int x = 0; x < 1; x++)
		{
			for (int z = 0; z < 1; z++)
			{
				y += context.chunkGenerator().getFirstOccupiedHeight(chunkPos.getBlockX(x * width), chunkPos.getBlockZ(z * width), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
				samples++;
			}
		}
		BlockPos pos = new BlockPos(chunkPos.x << 4, y / samples, chunkPos.z << 4);
		SnowyTemplePieces.assemble(context.structureManager(), pos, Rotation.getRandom(rand), builder, rand);
	}
}
