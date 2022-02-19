package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.loot.NamedLootItem;
import com.legacy.dungeons_plus.loot.OptionalLootEntry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;

public class DPLoot
{
	public static final LootPoolEntryType NAMED_ITEM = registerEntry("named_item", new NamedLootItem.Serializer());
	public static final LootPoolEntryType OPTIONAL_ENTRY = registerEntry("optional_entry", new OptionalLootEntry.Serializer());

	public static void init()
	{
	}

	private static LootPoolEntryType registerEntry(String name, Serializer<? extends LootPoolEntryContainer> serializer)
	{
		return Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, DungeonsPlus.locate(name), new LootPoolEntryType(serializer));
	}

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
		public static final ResourceLocation CHEST_HUSK = chest("reanimated_ruins/husk");
		public static final ResourceLocation CHEST_HUSK_MAP = chest("reanimated_ruins/husk_map");
		public static final ResourceLocation CHEST_STRAY = chest("reanimated_ruins/stray");
		public static final ResourceLocation CHEST_STRAY_MAP = chest("reanimated_ruins/stray_map");

		public static final ResourceLocation ENTITY_SKELETON = entity("reanimated_ruins/skeleton");
		public static final ResourceLocation ENTITY_ZOMBIE = entity("reanimated_ruins/zombie");
	}

	public static final class Leviathan
	{
		public static final ResourceLocation CHEST_COMMON = chest("leviathan/common");

		public static final ResourceLocation ENTITY_HUSK = entity("leviathan/husk");
	}

	public static final class SnowyTemple
	{
		public static final ResourceLocation CHEST_COMMON = chest("snowy_temple/common");

		public static final ResourceLocation ENTITY_STRAY = entity("snowy_temple/stray");
	}

	public static final class WarpedGarden
	{
		public static final ResourceLocation CHEST_COMMON = chest("warped_garden/common");
	}

	public static final class SoulPrison
	{
		public static final ResourceLocation CHEST_COMMON = chest("soul_prison/common");
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
