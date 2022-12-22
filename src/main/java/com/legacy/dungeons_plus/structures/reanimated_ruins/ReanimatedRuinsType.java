package com.legacy.dungeons_plus.structures.reanimated_ruins;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPSpawners;
import com.legacy.dungeons_plus.structures.BlockModifierMap;
import com.legacy.dungeons_plus.structures.PsuedoFeature;
import com.legacy.dungeons_plus.structures.PsuedoFeature.IPlacement;
import com.legacy.structure_gel.api.dynamic_spawner.DynamicSpawnerType;
import com.legacy.structure_gel.api.structure.base.IModifyState;
import com.mojang.serialization.Codec;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public enum ReanimatedRuinsType implements StringRepresentable
{
	MOSSY("mossy", DPLoot.ReanimatedRuins.CHEST_MOSSY, DPSpawners.REANIMATED_RUINS_MOSSY, mossyBlockModifier(), ReanimatedRuinsType::mossyFeatures),
	MESA("mesa", DPLoot.ReanimatedRuins.CHEST_DESERT, DPSpawners.REANIMATED_RUINS_DESERT, desertBlockModifier(), ReanimatedRuinsType::desertFeatures),
	FROZEN("frozen", DPLoot.ReanimatedRuins.CHEST_FROZEN, DPSpawners.REANIMATED_RUINS_FROZEN, frozenBlockModifier(), ReanimatedRuinsType::frozenFeatures);

	public static final Codec<ReanimatedRuinsType> CODEC = StringRepresentable.fromEnum(ReanimatedRuinsType::values);
	private final String name;
	public final ResourceLocation loot;
	public final DynamicSpawnerType spawner;
	public final BlockModifierMap modifierMap;
	private final PsuedoFeature[] features;

	ReanimatedRuinsType(String name, ResourceLocation loot, DynamicSpawnerType spawner, BlockModifierMap modifierMap, Consumer<List<PsuedoFeature>> features)
	{
		this.name = name;
		this.loot = loot;
		this.spawner = spawner;
		this.modifierMap = modifierMap;
		List<PsuedoFeature> featuresList = new ArrayList<>();
		features.accept(featuresList);
		this.features = featuresList.toArray(PsuedoFeature[]::new);
	}

	@Override
	public String getSerializedName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return this.getSerializedName();
	}

	public static ReanimatedRuinsType byName(@Nullable String name)
	{
		for (ReanimatedRuinsType type : ReanimatedRuinsType.values())
			if (type.name.equals(name))
				return type;
		return MOSSY;
	}

	public void decorate(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		for (var f : this.features)
			f.place(level, pos, rand);
	}

	private static void mossyFeatures(List<PsuedoFeature> list)
	{
		list.add(new PsuedoFeature(0, 2, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::puddle));
		list.add(new PsuedoFeature(6, 9, IPlacement.NOOP, ReanimatedRuinsType::mossyStone));
		list.add(new PsuedoFeature(2, 5, IPlacement.NOOP, ReanimatedRuinsType::grassFloor));
		list.add(new PsuedoFeature(2, 5, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::dripleaf));
		list.add(new PsuedoFeature(2, 6, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::tallGrass));
		list.add(new PsuedoFeature(8, 15, IPlacement.FIND_HIGHEST_AIR, ReanimatedRuinsType::caveVines));
	}

	private static void desertFeatures(List<PsuedoFeature> list)
	{
		list.add(new PsuedoFeature(2, 6, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::deadBush));
		list.add(new PsuedoFeature(0, 6, IPlacement.FIND_HIGHEST_AIR, ReanimatedRuinsType::dripstone));
	}

	private static void frozenFeatures(List<PsuedoFeature> list)
	{
		list.add(new PsuedoFeature(0, 2, IPlacement.FIND_HIGHEST_AIR, ReanimatedRuinsType::ice));
		list.add(new PsuedoFeature(0, 2, IPlacement.NOOP, ReanimatedRuinsType::diorite));
		list.add(new PsuedoFeature(2, 5, IPlacement.NOOP, ReanimatedRuinsType::calciteFloor));
		list.add(new PsuedoFeature(2, 6, IPlacement.FIND_LOWEST_AIR, ReanimatedRuinsType::snowPatch));
		list.add(new PsuedoFeature(2, 6, IPlacement.NOOP, ReanimatedRuinsType::glowLichen));
	}

	private static void puddle(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
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

	private static void mossyStone(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		DPUtil.fillBlob(level, pos, 4, (l, p, r) ->
		{
			BlockState s = level.getBlockState(p);
			if (s.is(Blocks.COBBLESTONE))
				return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
			else if (s.is(Blocks.STONE_BRICKS))
				return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
			else if (s.is(Blocks.INFESTED_STONE_BRICKS))
				return Blocks.INFESTED_MOSSY_STONE_BRICKS.defaultBlockState();
			else if (s.is(Blocks.STONE_BRICK_SLAB))
				return IModifyState.mergeStates(Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState(), s);
			else if (s.is(Blocks.STONE_BRICK_STAIRS))
				return IModifyState.mergeStates(Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState(), s);
			return null;
		}, rand, 0.20F);
	}

	private static void grassFloor(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		DPUtil.fillBlob(level, pos, 4, Blocks.GRASS_BLOCK.defaultBlockState(), (l, p, s) -> s.is(Blocks.STONE) && l.getBlockState(p.above()).isAir(), rand, 0.60F);
	}

	private static void dripleaf(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		BlockState ground = level.getBlockState(pos.below());
		if (ground.is(BlockTags.BIG_DRIPLEAF_PLACEABLE) || ground.is(BlockTags.SMALL_DRIPLEAF_PLACEABLE))
		{
			placeFeature(level, pos, rand, CaveFeatures.DRIPLEAF);
		}
	}

	private static void tallGrass(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		BlockState grass = Blocks.GRASS.defaultBlockState();
		DPUtil.fillBlob(level, pos, 3, grass, (l, p, s) -> s.isAir() && grass.canSurvive(l, p), rand, 0.5F);
	}

	private static void caveVines(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		if (Blocks.CAVE_VINES.defaultBlockState().canSurvive(level, pos))
		{
			placeFeature(level, pos, rand, CaveFeatures.CAVE_VINE);
		}
	}

	private static void deadBush(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		BlockState deadBush = Blocks.DEAD_BUSH.defaultBlockState();
		DPUtil.fillBlob(level, pos, 3, deadBush, (l, p, s) -> s.isAir() && deadBush.canSurvive(l, p), rand, 0.2F);
	}

	private static void dripstone(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		placeFeature(level, pos, rand, CaveFeatures.DRIPSTONE_CLUSTER);
	}

	private static void diorite(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		DPUtil.fillBlob(level, pos, 4, (l, p, r) ->
		{
			BlockState s = level.getBlockState(p);
			if (s.is(Blocks.COBBLESTONE))
				return Blocks.DIORITE.defaultBlockState();
			return null;
		}, rand, 0.85F);
	}

	private static void calciteFloor(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		DPUtil.fillBlob(level, pos, 4, Blocks.CALCITE.defaultBlockState(), (l, p, s) -> s.is(Blocks.STONE) && l.getBlockState(p.above()).isAir(), rand, 0.60F);
	}

	private static void ice(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		DPUtil.fillBlob(level, pos, 4, Blocks.PACKED_ICE.defaultBlockState(), (l, p, s) -> s.is(Blocks.COBBLESTONE) && l.getBlockState(p.below()).isAir(), rand, 0.75F);
	}

	private static void snowPatch(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		BlockState snow = Blocks.SNOW.defaultBlockState();
		DPUtil.fillBlob(level, pos, 3, snow, (l, p, s) -> s.isAir() && snow.canSurvive(l, p), rand, 0.75F);
	}

	private static void glowLichen(ServerLevelAccessor level, BlockPos pos, RandomSource rand)
	{
		placeFeature(level, pos, rand, CaveFeatures.GLOW_LICHEN);
	}

	private static void placeFeature(ServerLevelAccessor level, BlockPos pos, RandomSource rand, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey)
	{
		if (level instanceof WorldGenLevel wgLevel)
			level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(configuredFeatureKey).ifPresent(f -> f.value().place(wgLevel, wgLevel.getLevel().getChunkSource().getGenerator(), rand, pos));
	}

	private static BlockModifierMap mossyBlockModifier()
	{
		return Util.make(new BlockModifierMap(), map ->
		{
			map.put(Blocks.CANDLE, candleFunc(Blocks.CYAN_CANDLE, false));

			map.put(Blocks.AZALEA_LEAVES, (s, r) -> r.nextFloat() < 0.10F ? IModifyState.mergeStates(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), s) : s);
		});
	}

	private static BlockModifierMap desertBlockModifier()
	{
		return Util.make(new BlockModifierMap(), map ->
		{
			map.put(Blocks.CANDLE, candleFunc(Blocks.RED_CANDLE, true));

			map.put(Blocks.COBBLESTONE, Blocks.GRANITE);
			map.put(Blocks.INFESTED_COBBLESTONE, Blocks.GRANITE);
			map.merge(Blocks.COBBLESTONE_SLAB, Blocks.GRANITE_SLAB);
			map.merge(Blocks.COBBLESTONE_STAIRS, Blocks.GRANITE_STAIRS);
			map.merge(Blocks.COBBLESTONE_WALL, Blocks.GRANITE_WALL);

			map.put(Blocks.STONE, Blocks.TERRACOTTA);
			map.merge(Blocks.STONE_SLAB, Blocks.POLISHED_GRANITE_SLAB);
			map.merge(Blocks.STONE_STAIRS, Blocks.POLISHED_GRANITE_STAIRS);

			map.put(Blocks.STONE_BRICKS, Blocks.CUT_RED_SANDSTONE);
			map.put(Blocks.INFESTED_STONE_BRICKS, Blocks.CUT_RED_SANDSTONE);
			map.put(Blocks.CRACKED_STONE_BRICKS, Blocks.RED_SANDSTONE);
			map.put(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.RED_SANDSTONE);
			map.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_RED_SANDSTONE);
			map.put(Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_RED_SANDSTONE);
			map.merge(Blocks.STONE_BRICK_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB);
			map.merge(Blocks.STONE_BRICK_STAIRS, Blocks.RED_SANDSTONE_STAIRS);
			map.merge(Blocks.STONE_BRICK_WALL, Blocks.RED_SANDSTONE_WALL);
		});
	}

	private static BlockModifierMap frozenBlockModifier()
	{
		return Util.make(new BlockModifierMap(), map ->
		{
			map.put(Blocks.CANDLE, candleFunc(Blocks.WHITE_CANDLE, false));
		});
	}

	private static BiFunction<BlockState, RandomSource, BlockState> candleFunc(Block candle, boolean lit)
	{
		return (s, r) -> r.nextFloat() < 0.65F ? IModifyState.mergeStates(candle.defaultBlockState(), s).setValue(CandleBlock.LIT, lit) : Blocks.AIR.defaultBlockState();
	}
}