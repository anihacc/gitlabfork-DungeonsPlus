package com.legacy.dungeons_plus.structures;

import com.legacy.dungeons_plus.pieces.SoulPrisonPieces;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.worldgen.structure.GelConfigStructure;
import com.legacy.structure_gel.worldgen.structure.GelStructureStart;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SoulPrisonStructure extends GelConfigStructure<NoFeatureConfig>
{
	public SoulPrisonStructure(Codec<NoFeatureConfig> codec, StructureConfig config)
	{
		super(codec, config);
	}

	@Override
	protected boolean func_230363_a_(ChunkGenerator chunkGen, BiomeProvider biomeProvider, long seed, SharedSeedRandom sharedSeedRand, int chunkPosX, int chunkPosZ, Biome biomeIn, ChunkPos chunkPos, NoFeatureConfig config)
	{
		IBlockReader minPos = chunkGen.func_230348_a_(chunkPos.getXStart() + 3, chunkPos.getZStart() + 3);
		IBlockReader maxPos = chunkGen.func_230348_a_(chunkPos.getXStart() + 22, chunkPos.getZStart() + 22);
		if (minPos.getBlockState(chunkPos.asBlockPos().up(29)).getBlock() == Blocks.LAVA && maxPos.getBlockState(chunkPos.asBlockPos().up(29)).getBlock() == Blocks.LAVA)
			return super.func_230363_a_(chunkGen, biomeProvider, seed, sharedSeedRand, chunkPosX, chunkPosZ, biomeIn, chunkPos, config);
		return false;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}

	public static class Start extends GelStructureStart<NoFeatureConfig>
	{
		public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
		}

		// init
		@Override
		public void func_230364_a_(DynamicRegistries registry, ChunkGenerator chunkGen, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig configIn)
		{
			SoulPrisonPieces.assemble(templateManagerIn, new BlockPos(chunkX * 16, 29, chunkZ * 16), Rotation.randomRotation(this.rand), this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
