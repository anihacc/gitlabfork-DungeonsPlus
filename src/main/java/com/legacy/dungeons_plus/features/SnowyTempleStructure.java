package com.legacy.dungeons_plus.features;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.legacy.structure_gel.structures.GelConfigStructure;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SnowyTempleStructure extends GelConfigStructure<NoFeatureConfig>
{
	public static final List<SpawnListEntry> SPAWNS = ImmutableList.of(new SpawnListEntry(EntityType.STRAY, 1, 2, 4));

	public SnowyTempleStructure(Codec<NoFeatureConfig> codec, StructureConfig config)
	{
		super(codec, config);
	}

	@Override
	public List<SpawnListEntry> getSpawnList()
	{
		return SnowyTempleStructure.SPAWNS;
	}

	@Override
	public int getSeed()
	{
		return 943137831;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return SnowyTempleStructure.Start::new;
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
			SnowyTemplePieces.assemble(generator, templateManagerIn, new BlockPos(chunkX * 16 + rand.nextInt(15), 90, chunkZ * 16 + rand.nextInt(15)), this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
