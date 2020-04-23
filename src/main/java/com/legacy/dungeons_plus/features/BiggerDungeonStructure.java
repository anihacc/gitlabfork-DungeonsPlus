package com.legacy.dungeons_plus.features;

import java.util.Random;
import java.util.function.Function;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.DungeonsPlusConfig;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BiggerDungeonStructure extends Structure<NoFeatureConfig>
{
	public BiggerDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ)
	{
		Biome biome = chunkGen.getBiomeProvider().getBiome(new BlockPos((chunkPosX << 4) + 9, 0, (chunkPosZ << 4) + 9));
		if (chunkGen.hasStructure(biome, this))
		{
			((SharedSeedRandom) rand).setLargeFeatureSeedWithSalt(chunkGen.getSeed(), chunkPosX, chunkPosZ, 10387320);
			return rand.nextDouble() < DungeonsPlusConfig.COMMON.biggerDungeonProbability.get();
		}
		else
			return false;
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return BiggerDungeonStructure.Start::new;
	}

	@Override
	public String getStructureName()
	{
		return DungeonsPlus.locate("bigger_dungeon").toString();
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
			BiggerDungeonPieces.init(generator, templateManagerIn, new BlockPos(chunkX * 16, 90, chunkZ * 16), this.components, this.rand);
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
