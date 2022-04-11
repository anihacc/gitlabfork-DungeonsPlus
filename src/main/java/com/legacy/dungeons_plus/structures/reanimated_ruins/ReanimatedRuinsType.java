package com.legacy.dungeons_plus.structures.reanimated_ruins;

import java.util.Random;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPSpawners;
import com.legacy.dungeons_plus.structures.PsuedoFeature;
import com.legacy.dungeons_plus.structures.PsuedoFeature.IPlacement;
import com.legacy.structure_gel.api.dynamic_spawner.DynamicSpawnerType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public enum ReanimatedRuinsType implements StringRepresentable
{
	MOSSY("mossy", DPLoot.ReanimatedRuins.CHEST_MOSSY, DPSpawners.REANIMATED_RUINS_MOSSY, ReanimatedRuinsType::mossyFeatures),
	DESERT("desert", DPLoot.ReanimatedRuins.CHEST_DESERT, DPSpawners.REANIMATED_RUINS_DESERT, ReanimatedRuinsType::desertFeatures),
	FROZEN("frozen", DPLoot.ReanimatedRuins.CHEST_FROZEN, DPSpawners.REANIMATED_RUINS_FROZEN, ReanimatedRuinsType::frozenFeatures);

	private final String name;
	public final ResourceLocation loot;
	public final DynamicSpawnerType spawner;
	private final ImmutableList<PsuedoFeature> features;
	//TODO 
	Consumer<ImmutableList.Builder<PsuedoFeature>> featureCon;

	ReanimatedRuinsType(String name, ResourceLocation loot, DynamicSpawnerType spawner, Consumer<ImmutableList.Builder<PsuedoFeature>> features)
	{
		this.name = name;
		this.loot = loot;
		this.spawner = spawner;
		ImmutableList.Builder<PsuedoFeature> builder = ImmutableList.builder();
		features.accept(builder);
		this.features = builder.build();
		// TODO
		this.featureCon = features;
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

	public ImmutableList<PsuedoFeature> getFeatures()
	{
		// TODO
		ImmutableList.Builder<PsuedoFeature> builder = ImmutableList.builder();
		featureCon.accept(builder);
		return builder.build();
		//return this.features;
	}

	public void decorate(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		for (var f : this.getFeatures())
			f.place(level, pos, rand);
	}

	private static void mossyFeatures(ImmutableList.Builder<PsuedoFeature> list)
	{
		list.add(new PsuedoFeature(0, 2, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::puddle));
		list.add(new PsuedoFeature(6, 9, IPlacement.NOOP, ReanimatedRuinsType::mossyCobble));
		list.add(new PsuedoFeature(2, 5, IPlacement.NOOP, ReanimatedRuinsType::grassFloor));
		list.add(new PsuedoFeature(2, 5, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::dripleaf));
		list.add(new PsuedoFeature(2, 6, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::tallGrass));
		list.add(new PsuedoFeature(8, 15, IPlacement.FIND_HIGHEST_AIR, ReanimatedRuinsType::caveVines));
	}

	private static void desertFeatures(ImmutableList.Builder<PsuedoFeature> list)
	{
		
	}
	
	private static void frozenFeatures(ImmutableList.Builder<PsuedoFeature> list)
	{
		
	}
	
	private static void puddle(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		pos = pos.below();
		placeWater(level, pos);
		for (Direction dir : DPUtil.HORIZONTAL_DIR)
			if (rand.nextBoolean())
				placeWater(level, pos.relative(dir));
	}

	private static void placeWater(ServerLevelAccessor level, BlockPos pos)
	{
		if (level.getBlockState(pos).is(Blocks.STONE) && level.getBlockState(pos.above()).isAir())
		{
			level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
			level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
		}
	}

	private static void mossyCobble(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		DPUtil.fillBlob(level, pos, 4, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), (l, p, s) -> s.is(Blocks.COBBLESTONE), rand, 0.20F);
	}

	private static void grassFloor(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		DPUtil.fillBlob(level, pos, 4, Blocks.GRASS_BLOCK.defaultBlockState(), (l, p, s) -> s.is(Blocks.STONE) && l.getBlockState(p.above()).isAir(), rand, 0.60F);
	}

	private static void dripleaf(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		BlockState ground = level.getBlockState(pos.below());
		if (ground.is(BlockTags.BIG_DRIPLEAF_PLACEABLE) || ground.is(BlockTags.SMALL_DRIPLEAF_PLACEABLE))
		{
			ServerLevel serverLev = level.getLevel();
			CaveFeatures.DRIPLEAF.value().place(serverLev, serverLev.getChunkSource().getGenerator(), rand, pos);
		}
	}

	private static void tallGrass(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		BlockState grass = Blocks.GRASS.defaultBlockState();
		DPUtil.fillBlob(level, pos, 3, grass, (l, p, s) -> s.isAir() && grass.canSurvive(l, p), rand, 0.5F);
	}

	private static void caveVines(ServerLevelAccessor level, BlockPos pos, Random rand)
	{
		if (Blocks.CAVE_VINES.defaultBlockState().canSurvive(level, pos))
		{
			ServerLevel serverLev = level.getLevel();
			CaveFeatures.CAVE_VINE.value().place(serverLev, serverLev.getChunkSource().getGenerator(), rand, pos);
		}
	}
}