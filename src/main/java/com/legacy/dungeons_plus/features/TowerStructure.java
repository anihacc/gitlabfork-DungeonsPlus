package com.legacy.dungeons_plus.features;

import java.util.function.Function;

import com.legacy.structure_gel.structures.GelConfigStructure;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class TowerStructure extends GelConfigStructure<NoFeatureConfig>
{
	public TowerStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn, StructureConfig config)
	{
		super(configFactoryIn, config);
	}

	@Override
	public int getSeed()
	{
		return 155166;
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return TowerStructure.Start::new;
	}

	@Override
	public int getSize()
	{
		return 3;
	}

	public static class Start extends StructureStart
	{

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			BlockPos pos = new BlockPos(chunkX * 16 + this.rand.nextInt(5), 90, chunkZ * 16 + this.rand.nextInt(5));
			TowerPieces.assemble(generator, templateManagerIn, pos, this.components, this.rand);
			this.recalculateStructureSize();
		}

	}
}
