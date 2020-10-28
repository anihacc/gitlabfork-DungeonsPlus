package com.legacy.dungeons_plus.structures;

import com.legacy.dungeons_plus.pieces.WarpedGardenPieces;
import com.legacy.structure_gel.worldgen.structure.GelStructure;
import com.legacy.structure_gel.worldgen.structure.GelStructureStart;
import com.mojang.serialization.Codec;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WarpedGardenStructure extends GelStructure<NoFeatureConfig>
{
	public WarpedGardenStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	protected boolean func_230365_b_()
	{
		return false;
	}

	@Override
	public double getProbability()
	{
		return 1.0;
	}

	@Override
	public int getSpacing()
	{
		return 10;
	}

	@Override
	public int getOffset()
	{
		return 0;
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
			WarpedGardenPieces.assemble(templateManagerIn, new BlockPos(chunkX * 16, 90, chunkZ * 16), Rotation.randomRotation(this.rand), this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
