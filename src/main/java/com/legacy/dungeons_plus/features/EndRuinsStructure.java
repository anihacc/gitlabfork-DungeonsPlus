package com.legacy.dungeons_plus.features;

import com.legacy.structure_gel.structures.GelConfigStructure;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class EndRuinsStructure extends GelConfigStructure<NoFeatureConfig>
{
	public EndRuinsStructure(Codec<NoFeatureConfig> codec, StructureConfig config)
	{
		super(codec, config);
	}

	@Override
	public int getSeed()
	{
		return 843152;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return EndRuinsStructure.Start::new;
	}

	public static class Start extends GelStructureStart<NoFeatureConfig>
	{

		public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
		}

		@Override
		public void func_230364_a_(ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config)
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