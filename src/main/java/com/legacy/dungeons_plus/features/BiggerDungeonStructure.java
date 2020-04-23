package com.legacy.dungeons_plus.features;

import java.util.function.Function;

import com.legacy.dungeons_plus.DungeonsPlus;
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

public class BiggerDungeonStructure extends ScatteredStructure<NoFeatureConfig>
{

	public BiggerDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator)
	{
		return 15;
	}

	protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator)
	{
		return 4;
	}

	@Override
	protected int getSeedModifier()
	{
		return 420691337;
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

	public static class Start extends StructureStart
	{

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			BlockPos pos = new BlockPos(chunkX * 16, 5 + rand.nextInt(30), chunkZ * 16);

			BiggerDungeonPieces.init(generator, templateManagerIn, pos, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
