package com.legacy.dungeons_plus.data.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPBlocks;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.structure_gel.api.block_entity.SpawnerAccessHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * This class exists identically for clients and servers. Add listeners during
 * {@link FMLCommonSetupEvent}.
 * 
 * @author Silver_David
 *
 */
public class DPSpawners
{
	public static final Optional<SpawnData.CustomSpawnRules> SPAWN_IN_SKYLIGHT = Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 8), new InclusiveRange<>(0, 15)));

	public static final SpawnerType TOWER_ZOMBIE = SpawnerType.register("tower_zombie", DPSpawners::towerZombie);
	public static final SpawnerType TOWER_SKELETON = SpawnerType.register("tower_skeleton", DPSpawners::towerSkeleton);
	public static final SpawnerType TOWER_SPIDER = SpawnerType.register("tower_spider", DPSpawners::towerSpider);
	public static final SpawnerType TOWER_VEX = SpawnerType.register("tower_vex", DPSpawners::towerVex);

	public static final SpawnerType LEVIATHAN_HUSK = SpawnerType.register("leviathan_husk", DPSpawners::leviathanHusk);

	public static final SpawnerType SNOWY_TEMPLE_STRAY = SpawnerType.register("snowy_temple_stray", DPSpawners::snowyTempleStray);

	public static final SpawnerType WARPED_GARDEN_DROWNED = SpawnerType.register("warped_garden_drowned", DPSpawners::warpedGardenDrowned);

	public static final SpawnerType SOUL_PRISON_GHAST = SpawnerType.register("soul_prison_ghast", DPSpawners::soulPrisonGhast);

	public static final SpawnerType END_RUINS_ENDERMAN = SpawnerType.register("end_ruins_enderman", DPSpawners::endRuinsEnderman);
	public static final SpawnerType END_RUINS_PHANTOM = SpawnerType.register("end_ruins_phantom", DPSpawners::endRuinsPhantom);

	public static void init()
	{
	}

	private static void towerZombie(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, towerMob(s, level, pos, EntityType.ZOMBIE, DPLoot.Tower.ENTITY_ZOMBIE));
	}

	private static void towerSkeleton(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, towerMob(s, level, pos, EntityType.SKELETON, DPLoot.Tower.ENTITY_SKELETON));
	}

	private static void towerSpider(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, towerMob(s, level, pos, EntityType.SPIDER, DPLoot.Tower.ENTITY_SPIDER));
	}

	private static void towerVex(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnData spawnData = SpawnerAccessHelper.createSpawnerEntity(EntityType.VEX, new CompoundTag(), SPAWN_IN_SKYLIGHT);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, spawnData);
	}

	private static SpawnData towerMob(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos, EntityType<?> type, ResourceLocation lootTable)
	{
		CompoundTag tag = new CompoundTag();
		lootTable(tag, lootTable);
		return SpawnerAccessHelper.createSpawnerEntity(type, tag, SPAWN_IN_SKYLIGHT);
	}

	private static void leviathanHusk(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		CompoundTag tag = new CompoundTag();
		lootTable(tag, DPLoot.Leviathan.ENTITY_HUSK);
		SpawnData spawnData = SpawnerAccessHelper.createSpawnerEntity(EntityType.HUSK, tag, SPAWN_IN_SKYLIGHT);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, spawnData);
	}

	private static void snowyTempleStray(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		CompoundTag tag = new CompoundTag();
		lootTable(tag, DPLoot.SnowyTemple.ENTITY_STRAY);
		SpawnData spawnData = SpawnerAccessHelper.createSpawnerEntity(EntityType.STRAY, tag, SPAWN_IN_SKYLIGHT);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, spawnData);
	}

	private static void warpedGardenDrowned(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SimpleWeightedRandomList.Builder<SpawnData> spawns = new SimpleWeightedRandomList.Builder<>();

		ItemStack goldAxe = new ItemStack(Items.GOLDEN_AXE);
		goldAxe.setDamageValue(10);
		List<Block> corals = BlockTags.CORAL_BLOCKS.getValues();
		for (Block coral : corals)
		{
			CompoundTag entityTag = new CompoundTag();
			handItems(entityTag, goldAxe, 0.085F, new ItemStack(coral), 1.0F);
			spawns.add(SpawnerAccessHelper.createSpawnerEntity(EntityType.DROWNED, entityTag, Optional.empty()), 1);
		}

		CompoundTag entityTag = new CompoundTag();
		handItems(entityTag, goldAxe, 0.085F);
		spawns.add(SpawnerAccessHelper.createSpawnerEntity(EntityType.DROWNED, entityTag, Optional.empty()), corals.size() / 2);

		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, spawns.build());
	}

	private static void soulPrisonGhast(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setRequiredPlayerRange(s, level, pos, 32);
		SpawnerAccessHelper.setMaxNearbyEntities(s, level, pos, 10);
		SpawnerAccessHelper.setSpawnCount(s, level, pos, 5);
		SpawnerAccessHelper.setSpawnRange(s, level, pos, 16);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, EntityType.GHAST);
	}

	private static void endRuinsEnderman(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, EntityType.ENDERMAN);
	}

	private static void endRuinsPhantom(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, EntityType.PHANTOM);
	}

	public static void lootTable(CompoundTag tag, ResourceLocation lootTable)
	{
		tag.putString("DeathLootTable", lootTable.toString());
	}

	public static void handItems(CompoundTag tag, ItemStack mainHand, float mainHandDropChance, ItemStack offHand, float offHandDropChance)
	{
		ListTag handItems = new ListTag();
		handItems.add(mainHand.save(new CompoundTag())); // Main hand
		handItems.add(offHand.save(new CompoundTag()));
		tag.put("HandItems", handItems);

		ListTag handDropChances = new ListTag();
		handDropChances.add(FloatTag.valueOf(mainHandDropChance)); // Main hand
		handDropChances.add(FloatTag.valueOf(offHandDropChance)); // Off hand
		tag.put("HandDropChances", handDropChances);
	}

	public static void handItems(CompoundTag tag, ItemStack mainHand, float mainHandDropChance)
	{
		handItems(tag, mainHand, mainHandDropChance, ItemStack.EMPTY, 0);
	}

	public static class Spawner extends BaseSpawner implements Cloneable
	{
		public Spawner()
		{
		}

		@Override
		public void broadcastEvent(Level level, BlockPos pos, int param)
		{
			level.blockEvent(pos, Blocks.SPAWNER, param, 0);
		}

		@Override
		public void setNextSpawnData(@Nullable Level level, BlockPos pos, SpawnData spawnData)
		{
			super.setNextSpawnData(level, pos, spawnData);
			if (level != null)
			{
				BlockState state = level.getBlockState(pos);
				level.sendBlockUpdated(pos, state, state, 4);
			}
		}

		@Nullable
		@Override
		public BlockEntity getSpawnerBlockEntity()
		{
			return null;
		}

		@Override
		protected Spawner clone()
		{
			Spawner clone = new Spawner();
			clone.load(null, BlockPos.ZERO, this.save(new CompoundTag()));
			return clone;
		}
	}

	public static class SpawnerType
	{
		private static final Map<ResourceLocation, SpawnerType> REGISTRY = new HashMap<>();
		public static final SpawnerType DEFAULT = register("default", (spawner, level, pos) ->
		{
		});

		private final SpawnerMaker spawner;
		private final ResourceLocation name;

		public SpawnerType(ResourceLocation name, SpawnerMaker spawner)
		{
			this.spawner = spawner;
			this.name = name;
		}

		public static SpawnerType register(ResourceLocation name, SpawnerModifier spawnerModifier)
		{
			if (!REGISTRY.containsKey(name))
			{
				SpawnerType type = new SpawnerType(name, (level, pos) -> SpawnerMaker.makeSpawner(spawnerModifier, level, pos));
				REGISTRY.put(name, type);
				return type;
			}
			else
			{
				DungeonsPlus.LOGGER.warn("Attempted to register a spawner under an existing name: {}", name);
				return REGISTRY.get(name);
			}
		}

		public static SpawnerType register(String name, SpawnerModifier spawnerModifier)
		{
			return register(DungeonsPlus.locate(name), spawnerModifier);
		}

		public static SpawnerType get(ResourceLocation name)
		{
			return REGISTRY.getOrDefault(name, DEFAULT);
		}

		public Spawner getSpawner(@Nullable Level level, BlockPos pos)
		{
			return this.spawner.make(level, pos);
		}

		public ResourceLocation getTypeName()
		{
			return this.name;
		}
	}

	public static interface SpawnerModifier
	{
		void modify(SpawnerBlockEntity spawner, @Nullable Level level, BlockPos pos);
	}

	private static interface SpawnerMaker
	{
		Spawner make(@Nullable Level level, BlockPos pos);

		public static Spawner makeSpawner(SpawnerModifier modifier, @Nullable Level level, BlockPos pos)
		{
			BlockState state = level != null ? level.getBlockState(pos) : DPBlocks.DYNAMIC_SPAWNER.defaultBlockState();
			if (!state.is(DPBlocks.DYNAMIC_SPAWNER))
				state = DPBlocks.DYNAMIC_SPAWNER.defaultBlockState();
			SpawnerBlockEntity spawnerBE = new SpawnerBlockEntity(pos, state);
			modifier.modify(spawnerBE, level, pos);
			Spawner spawner = new Spawner();
			spawner.load(level, pos, spawnerBE.getSpawner().save(new CompoundTag()));
			return spawner;
		}
	}
}
