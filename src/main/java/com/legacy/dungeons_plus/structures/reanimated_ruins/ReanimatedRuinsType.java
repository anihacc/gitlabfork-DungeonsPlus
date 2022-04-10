package com.legacy.dungeons_plus.structures.reanimated_ruins;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPSpawners;
import com.legacy.dungeons_plus.structures.SuedoFeature;
import com.legacy.structure_gel.api.dynamic_spawner.DynamicSpawnerType;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public enum ReanimatedRuinsType implements StringRepresentable
{	
	MOSSY("mossy", DPLoot.ReanimatedRuins.CHEST_MOSSY, DPSpawners.REANIMATED_RUINS_MOSSY, list ->
	{
		BlockState grass = Blocks.GRASS.defaultBlockState();
		
		list.accept((level, pos, rand) ->
		{
			if (level.getBlockState(pos).is(Blocks.STONE) && level.getBlockState(pos.above()).isAir())
				level.setBlock(pos, Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM).setValue(SlabBlock.WATERLOGGED, true), 2);
		}, 0.01F);
		list.accept((level, pos, rand) ->
		{
			SuedoFeature.fillBlob(level, pos, 3, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), rand, 0.22F, (l, p, s) -> s.is(Blocks.COBBLESTONE));
		}, 0.01F);
		list.accept((level, pos, rand) ->
		{
			SuedoFeature.fillBlob(level, pos, 3, Blocks.GRASS_BLOCK.defaultBlockState(), rand, 0.22F, (l, p, s) -> s.is(Blocks.STONE) && l.getBlockState(p.above()).isAir());
		}, 0.01F);
		list.accept((level, pos, rand) ->
		{
			BlockState ground = level.getBlockState(pos.below());
			if (ground.is(BlockTags.BIG_DRIPLEAF_PLACEABLE) || ground.is(BlockTags.SMALL_DRIPLEAF_PLACEABLE))
				CaveFeatures.DRIPLEAF.value().place(level.getLevel(), level.getLevel().getChunkSource().getGenerator(), rand, pos);
		}, 0.025F);
		list.accept((level, pos, rand) ->
		{
			SuedoFeature.fillBlob(level, pos, 2, grass, rand, 0.18F, (l, p, s) -> s.isAir() && grass.canSurvive(l, p));
		}, 0.01F);
		list.accept((level, pos, rand) ->
		{
			if (level.getBlockState(pos).isAir() && Blocks.CAVE_VINES.defaultBlockState().canSurvive(level, pos))
				CaveFeatures.CAVE_VINE.value().place(level.getLevel(), level.getLevel().getChunkSource().getGenerator(), rand, pos);
		}, 0.07F);
	}),
	DESERT("desert", DPLoot.ReanimatedRuins.CHEST_DESERT, DPSpawners.REANIMATED_RUINS_DESERT, list ->
	{
	}),
	FROZEN("frozen", DPLoot.ReanimatedRuins.CHEST_FROZEN, DPSpawners.REANIMATED_RUINS_FROZEN, list ->
	{
	});

	private final String name;
	public final ResourceLocation loot;
	public final DynamicSpawnerType spawner;
	private final ImmutableList<Pair<SuedoFeature, Float>> features;

	ReanimatedRuinsType(String name, ResourceLocation loot, DynamicSpawnerType spawner, Consumer<BiConsumer<SuedoFeature, Float>> features)
	{
		this.name = name;
		this.loot = loot;
		this.spawner = spawner;
		ImmutableList.Builder<Pair<SuedoFeature, Float>> builder = ImmutableList.builder();
		features.accept((s, c) -> builder.add(Pair.of(s, c)));
		this.features = builder.build();
	}

	@Override
	public String getSerializedName()
	{
		return this.name;
	}

	public static ReanimatedRuinsType byName(@Nullable String name)
	{
		for (ReanimatedRuinsType type : ReanimatedRuinsType.values())
			if (type.name.equals(name))
				return type;
		return MOSSY;
	}

	public void decorate(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		int range = 5;
		for (var f : this.features)
		{
			for (int x = -range; x <= range; x++)
			{
				for (int y = -range; y <= range; y++)
				{
					for (int z = -range; z <= range; z++)
					{
						if (rand.nextFloat() < f.getValue())
							f.getKey().place(level, pos.offset(x, y, z), rand);
					}
				}
			}
		}
	}
}