package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.loot.NamedLootItem;
import com.legacy.dungeons_plus.loot.OptionalLootEntry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DPLoot
{
	public static final DeferredRegister<LootPoolEntryType> LOOT_POOLS_TYPES = DeferredRegister.create(Registry.LOOT_POOL_ENTRY_TYPE.key(), DungeonsPlus.MODID);

	public static final RegistryObject<LootPoolEntryType> NAMED_ITEM = LOOT_POOLS_TYPES.register("named_item", () -> new LootPoolEntryType(new NamedLootItem.Serializer()));
	public static final RegistryObject<LootPoolEntryType> OPTIONAL_ENTRY = LOOT_POOLS_TYPES.register("optional_entry", () -> new LootPoolEntryType(new OptionalLootEntry.Serializer()));

	public static final class Tower
	{
		public static final ResourceLocation CHEST_COMMON = chest("tower/common");
		public static final ResourceLocation CHEST_BARREL = chest("tower/barrel");
		public static final ResourceLocation CHEST_VEX = chest("tower/vex");
		public static final ResourceLocation CHEST_VEX_MAP = chest("tower/vex_map");

		public static final ResourceLocation ENTITY_SKELETON = entity("tower/skeleton");
		public static final ResourceLocation ENTITY_SPIDER = entity("tower/spider");
		public static final ResourceLocation ENTITY_ZOMBIE = entity("tower/zombie");
	}

	public static final class ReanimatedRuins
	{
		public static final ResourceLocation CHEST_COMMON = chest("reanimated_ruins/common");
		public static final ResourceLocation CHEST_DESERT = chest("reanimated_ruins/desert");
		public static final ResourceLocation CHEST_DESERT_MAP = chest("reanimated_ruins/desert_map");
		public static final ResourceLocation CHEST_FROZEN = chest("reanimated_ruins/frozen");
		public static final ResourceLocation CHEST_FROZEN_MAP = chest("reanimated_ruins/frozen_map");
		public static final ResourceLocation CHEST_MOSSY = chest("reanimated_ruins/mossy");
		public static final ResourceLocation CHEST_MOSSY_MAP = chest("reanimated_ruins/mossy_map");

		public static final ResourceLocation ENTITY_SKELETON = entity("reanimated_ruins/skeleton");
		public static final ResourceLocation ENTITY_ZOMBIE = entity("reanimated_ruins/zombie");
	}

	public static final class Leviathan
	{
		public static final ResourceLocation CHEST_COMMON = chest("leviathan/common");
		public static final ResourceLocation CHEST_RARE = chest("leviathan/rare");

		public static final ResourceLocation ENTITY_HUSK = entity("leviathan/husk");
	}

	public static final class SnowyTemple
	{
		public static final ResourceLocation CHEST_COMMON = chest("snowy_temple/common");
		public static final ResourceLocation CHEST_RARE = chest("snowy_temple/rare");

		public static final ResourceLocation ENTITY_STRAY = entity("snowy_temple/stray");
	}

	public static final class WarpedGarden
	{
		public static final ResourceLocation CHEST_COMMON = chest("warped_garden/common");
		public static final ResourceLocation CHEST_RARE = chest("warped_garden/rare");

	}

	public static final class SoulPrison
	{
		public static final ResourceLocation CHEST_COMMON = chest("soul_prison/common");
		public static final ResourceLocation CHEST_RARE = chest("soul_prison/rare");
		public static final ResourceLocation CHEST_GOLDEN_ARMOR = chest("soul_prison/golden_armor");

	}

	private static ResourceLocation chest(String key)
	{
		return DungeonsPlus.locate("chests/" + key);
	}

	private static ResourceLocation entity(String key)
	{
		return DungeonsPlus.locate("entities/" + key);
	}
}
