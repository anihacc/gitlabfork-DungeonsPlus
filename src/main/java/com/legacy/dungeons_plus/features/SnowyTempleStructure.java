package com.legacy.dungeons_plus.features;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.legacy.structure_gel.structures.GelConfigStructure;
import com.legacy.structure_gel.structures.GelStructureStart;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.mojang.datafixers.Dynamic;

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

	public SnowyTempleStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn, StructureConfig config)
	{
		super(configFactoryIn, config);
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
	public IStartFactory getStartFactory()
	{
		return SnowyTempleStructure.Start::new;
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
			SnowyTemplePieces.assemble(generator, templateManagerIn, new BlockPos(chunkX * 16 + rand.nextInt(15), 90, chunkZ * 16 + rand.nextInt(15)), this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
