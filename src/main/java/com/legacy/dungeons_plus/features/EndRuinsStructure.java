package com.legacy.dungeons_plus.features;

import java.util.function.Function;

import com.legacy.dungeons_plus.DungeonsConfig;
import com.legacy.structure_gel.structures.GelStructure;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class EndRuinsStructure extends GelStructure<NoFeatureConfig>
{
	public EndRuinsStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public int getSeed()
	{
		return 843152;
	}

	@Override
	public double getProbability()
	{
		return DungeonsConfig.COMMON.endRuins.getProbability();
	}

	@Override
	public int getSpacing()
	{
		return DungeonsConfig.COMMON.endRuins.getSpacing();
	}

	@Override
	public int getOffset()
	{
		return DungeonsConfig.COMMON.endRuins.getOffset();
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return EndRuinsStructure.Start::new;
	}

	@Override
	public int getSize()
	{
		return 5;
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
			int x = (chunkX * 16) + 7;
			int z = (chunkZ * 16) + 7;
			int y = generator.func_222529_a(x, z, Heightmap.Type.WORLD_SURFACE_WG);
			if (y >= 60)
			{
				EndRuinsPieces.assemble(generator, templateManagerIn, new BlockPos(x, 0, z), this.components, this.rand);
				this.recalculateStructureSize();

				/**
				 * Removes components if they generate below y level 5. In this case, basically
				 * the parts that generate over the void.
				 */
				this.components.removeIf(c -> c.getBoundingBox().minY < 5);
			}
		}
	}
}