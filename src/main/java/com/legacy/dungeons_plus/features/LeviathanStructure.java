package com.legacy.dungeons_plus.features;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.legacy.dungeons_plus.DungeonsConfig;
import com.legacy.structure_gel.structures.GelStructure;
import com.legacy.structure_gel.structures.GelStructureStart;
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

public class LeviathanStructure extends GelStructure<NoFeatureConfig>
{
	public static final List<SpawnListEntry> SPAWNS = Lists.newArrayList(new SpawnListEntry(EntityType.HUSK, 1, 4, 4));
	
	public LeviathanStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	@Override
	public List<SpawnListEntry> getSpawnList()
	{
		return SPAWNS;
	}

	@Override
	public int getSeed()
	{
		return 719643;
	}

	@Override
	public double getProbability()
	{
		return DungeonsConfig.COMMON.leviathan.getProbability();
	}

	@Override
	public int getSpacing()
	{
		return DungeonsConfig.COMMON.leviathan.getSpacing();
	}

	@Override
	public int getOffset()
	{
		return DungeonsConfig.COMMON.leviathan.getOffset();
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return LeviathanStructure.Start::new;
	}

	@Override
	public int getSize()
	{
		return 4;
	}

	public static class Start extends GelStructureStart
	{

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
		{
			super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			LeviathanPieces.assemble(generator, templateManagerIn, new BlockPos(chunkX * 16 + rand.nextInt(15), 90, chunkZ * 16 + rand.nextInt(15)), this.components, this.rand);
			this.recalculateStructureSize();

			this.components.forEach(c -> c.offset(0, -1, 0));
		}
	}
}
