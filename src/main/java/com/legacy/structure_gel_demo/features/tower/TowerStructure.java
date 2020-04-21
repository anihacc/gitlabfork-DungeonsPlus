package com.legacy.structure_gel_demo.features.tower;

import java.util.function.Function;

import com.legacy.structure_gel_demo.SGDemoMod;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class TowerStructure extends ScatteredStructure<NoFeatureConfig>
{

	public TowerStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator)
	{
		return 16;
	}

	protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator)
	{
		return 8;
	}

	@Override
	protected int getSeedModifier()
	{
		return 143112;
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return TowerStructure.Start::new;
	}

	@Override
	public String getStructureName()
	{
		return SGDemoMod.locate("tower").toString();
	}

	@Override
	public int getSize()
	{
		return 3;
	}

	public static class Start extends StructureStart
	{

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			TowerPieces.init(generator, templateManagerIn, new BlockPos(chunkX * 16, 90, chunkZ * 16), this.components, this.rand);
			this.recalculateStructureSize();
		}

	}
}
