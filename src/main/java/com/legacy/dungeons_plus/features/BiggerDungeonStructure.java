package com.legacy.dungeons_plus.features;

import java.util.function.Function;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BiggerDungeonStructure extends ScatteredStructure<NoFeatureConfig>
{

	public BiggerDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator)
	{
		return 20;
	}

	protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator)
	{
		return 4;
	}

	@Override
	protected int getSeedModifier()
	{
		return 4321334;
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
