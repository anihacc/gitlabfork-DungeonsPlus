package com.legacy.dungeons_plus.features;

import java.util.function.Function;

import com.legacy.structure_gel.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.structures.GelConfigStructure;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BiggerDungeonStructure extends GelConfigStructure<NoFeatureConfig>
{
	public BiggerDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn, StructureConfig config)
	{
		super(configFactoryIn, config);
	}

	@Override
	public int getSeed()
	{
		return 973181;
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return BiggerDungeonStructure.Start::new;
	}

	@Override
	public int getSize()
	{
		return 3;
	}

	public static class Start extends GelStructureStart
	{

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			BiggerDungeonPieces.assemble(generator, templateManagerIn, new BlockPos(chunkX * 16 + rand.nextInt(15), 90, chunkZ * 16 + rand.nextInt(15)), this.components, this.rand);
			this.recalculateStructureSize();

			/**
			 * Generates the structure between y level 5 and 45 at max. If the max value
			 * ends up being smaller than the min, it gets increased automatically. The same
			 * goes for the min being bigger than the max.
			 */
			this.setHeight(5, rand.nextInt(45));
		}
	}
}
