package com.legacy.dungeons_plus.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.block_entity.SpawnerAccessHelper;
import com.legacy.structure_gel.api.dynamic_spawner.DynamicSpawnerType;
import com.legacy.structure_gel.api.events.RegisterDynamicSpawnerEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.SpawnData.CustomSpawnRules;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * This class exists identically for clients and servers. Add listeners during
 * {@link FMLCommonSetupEvent}.
 * 
 * @author Silver_David
 *
 */
@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPSpawners
{
	private static List<DynamicSpawnerType> objs = new ArrayList<>();
	public static final Optional<SpawnData.CustomSpawnRules> SPAWN_IN_SKYLIGHT = Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 7), new InclusiveRange<>(0, 15)));
	public static final Optional<SpawnData.CustomSpawnRules> SPAWN_IN_PARTIAL_DARK = Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 7), new InclusiveRange<>(0, 7)));

	private static final float DROP_CHANCE = Mob.DEFAULT_EQUIPMENT_DROP_CHANCE;

	public static final DynamicSpawnerType TOWER_ZOMBIE = create("tower_zombie", DPSpawners::towerZombie);
	public static final DynamicSpawnerType TOWER_SKELETON = create("tower_skeleton", DPSpawners::towerSkeleton);
	public static final DynamicSpawnerType TOWER_SPIDER = create("tower_spider", DPSpawners::towerSpider);
	public static final DynamicSpawnerType TOWER_VEX = create("tower_vex", DPSpawners::towerVex);

	public static final DynamicSpawnerType REANIMATED_RUINS_MOSSY = create("reanimated_ruins_mossy", DPSpawners::reanRuinsMossy);
	public static final DynamicSpawnerType REANIMATED_RUINS_DESERT = create("reanimated_ruins_desert", DPSpawners::reanRuinsMesa);
	public static final DynamicSpawnerType REANIMATED_RUINS_FROZEN = create("reanimated_ruins_frozen", DPSpawners::reanRuinsFrozen);

	public static final DynamicSpawnerType LEVIATHAN_HUSK = create("leviathan_husk", DPSpawners::leviathanHusk);

	public static final DynamicSpawnerType SNOWY_TEMPLE_STRAY = create("snowy_temple_stray", DPSpawners::snowyTempleStray);

	public static final DynamicSpawnerType WARPED_GARDEN_DROWNED = create("warped_garden_drowned", DPSpawners::warpedGardenDrowned);

	public static final DynamicSpawnerType SOUL_PRISON_GHAST = create("soul_prison_ghast", DPSpawners::soulPrisonGhast);

	public static final DynamicSpawnerType END_RUINS_ENDERMAN = create("end_ruins_enderman", DPSpawners::endRuinsEnderman);
	public static final DynamicSpawnerType END_RUINS_PHANTOM = create("end_ruins_phantom", DPSpawners::endRuinsPhantom);

	@SubscribeEvent
	protected static void registerDynamicSpawners(final RegisterDynamicSpawnerEvent event)
	{
		objs.forEach(event::register);
		objs = null;
	}

	private static DynamicSpawnerType create(String name, DynamicSpawnerType.SpawnerModifier spawnerModifier)
	{
		var dynSpawner = new DynamicSpawnerType(DungeonsPlus.locate(name), spawnerModifier);
		objs.add(dynSpawner);
		return dynSpawner;
	}

	private static void towerZombie(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, basicMob(s, level, pos, EntityType.ZOMBIE, DPLoot.Tower.ENTITY_ZOMBIE, SPAWN_IN_SKYLIGHT));
	}

	private static void towerSkeleton(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, basicMob(s, level, pos, EntityType.SKELETON, DPLoot.Tower.ENTITY_SKELETON, SPAWN_IN_SKYLIGHT, tag ->
		{
			handItems(tag, new ItemStack(Items.BOW), DROP_CHANCE);
		}));
	}

	private static void towerSpider(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, basicMob(s, level, pos, EntityType.SPIDER, DPLoot.Tower.ENTITY_SPIDER, SPAWN_IN_SKYLIGHT));
	}

	private static void towerVex(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SpawnData spawnData = SpawnerAccessHelper.createSpawnerEntity(EntityType.VEX, new CompoundTag(), SPAWN_IN_SKYLIGHT);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, spawnData);
		SpawnerAccessHelper.setMinSpawnDelay(s, level, pos, 100);
		SpawnerAccessHelper.setMaxSpawnDelay(s, level, pos, 400);
		SpawnerAccessHelper.setSpawnCount(s, level, pos, 2);
	}

	private static void reanRuinsMossy(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SimpleWeightedRandomList.Builder<SpawnData> builder = SimpleWeightedRandomList.builder();
		builder.add(basicMob(s, level, pos, EntityType.ZOMBIE, DPLoot.ReanimatedRuins.ENTITY_ZOMBIE, SPAWN_IN_PARTIAL_DARK), 2);
		builder.add(basicMob(s, level, pos, EntityType.DROWNED, null, SPAWN_IN_PARTIAL_DARK, tag ->
		{
			handItems(tag, new ItemStack(Items.STONE_SWORD), DROP_CHANCE, ItemStack.EMPTY, DROP_CHANCE);
		}), 1);
		builder.add(basicMob(s, level, pos, EntityType.SLIME, null, SPAWN_IN_PARTIAL_DARK, tag ->
		{
			tag.putInt("Size", 2);
		}), 1);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, builder.build());
		SpawnerAccessHelper.setSpawnCount(s, level, pos, 3);
	}

	private static void reanRuinsMesa(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SimpleWeightedRandomList.Builder<SpawnData> builder = SimpleWeightedRandomList.builder();
		builder.add(basicMob(s, level, pos, EntityType.ZOMBIE, DPLoot.ReanimatedRuins.ENTITY_ZOMBIE, Optional.empty()), 2);
		builder.add(basicMob(s, level, pos, EntityType.HUSK, null, Optional.empty()), 2);
		builder.add(basicMob(s, level, pos, EntityType.SKELETON, DPLoot.ReanimatedRuins.ENTITY_SKELETON, Optional.empty(), tag ->
		{
			handItems(tag, new ItemStack(Items.GOLDEN_PICKAXE), DROP_CHANCE);
		}), 1);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, builder.build());
		SpawnerAccessHelper.setSpawnCount(s, level, pos, 3);
	}

	private static void reanRuinsFrozen(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SimpleWeightedRandomList.Builder<SpawnData> builder = SimpleWeightedRandomList.builder();
		builder.add(basicMob(s, level, pos, EntityType.SKELETON, DPLoot.ReanimatedRuins.ENTITY_SKELETON, Optional.empty(), tag ->
		{
			handItems(tag, new ItemStack(Items.BOW), DROP_CHANCE);
		}), 3);
		builder.add(basicMob(s, level, pos, EntityType.STRAY, null, Optional.empty()), 2);
		builder.add(basicMob(s, level, pos, EntityType.CAVE_SPIDER, null, Optional.empty()), 1);
		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, builder.build());
		SpawnerAccessHelper.setSpawnCount(s, level, pos, 3);
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
		CompoundTag bowTag = new CompoundTag();
		lootTable(bowTag, DPLoot.SnowyTemple.ENTITY_STRAY);
		handItems(bowTag, Items.BOW.getDefaultInstance(), DROP_CHANCE);
		SpawnData strayBow = SpawnerAccessHelper.createSpawnerEntity(EntityType.STRAY, bowTag, SPAWN_IN_SKYLIGHT);

		CompoundTag swordTag = new CompoundTag();
		lootTable(swordTag, DPLoot.SnowyTemple.ENTITY_STRAY);
		handItems(swordTag, Items.IRON_SWORD.getDefaultInstance(), DROP_CHANCE);
		SpawnData straySword = SpawnerAccessHelper.createSpawnerEntity(EntityType.STRAY, swordTag, SPAWN_IN_SKYLIGHT);

		SpawnerAccessHelper.setSpawnPotentials(s, level, pos, strayBow, straySword);
	}

	private static void warpedGardenDrowned(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos)
	{
		SimpleWeightedRandomList.Builder<SpawnData> spawns = new SimpleWeightedRandomList.Builder<>();

		ItemStack goldAxe = new ItemStack(Items.GOLDEN_AXE);
		goldAxe.setDamageValue(10);
		CompoundTag entityTag = new CompoundTag();
		handItems(entityTag, goldAxe, DROP_CHANCE);
		spawns.add(SpawnerAccessHelper.createSpawnerEntity(EntityType.DROWNED, entityTag, Optional.empty()), 1);
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
		handItems(tag, mainHand, mainHandDropChance, ItemStack.EMPTY, DROP_CHANCE);
	}

	private static SpawnData basicMob(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos, EntityType<?> type, @Nullable ResourceLocation lootTable, Optional<CustomSpawnRules> customSpawnRules, Consumer<CompoundTag> tagFunc)
	{
		CompoundTag tag = new CompoundTag();
		tagFunc.accept(tag);
		if (lootTable != null)
			lootTable(tag, lootTable);
		return SpawnerAccessHelper.createSpawnerEntity(type, tag, customSpawnRules);
	}

	private static SpawnData basicMob(SpawnerBlockEntity s, @Nullable Level level, BlockPos pos, EntityType<?> type, @Nullable ResourceLocation lootTable, Optional<CustomSpawnRules> customSpawnRules)
	{
		return basicMob(s, level, pos, type, lootTable, customSpawnRules, tag ->
		{
		});
	}
}
