package com.legacy.dungeons_plus.data;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class DPTags
{
	public static interface Blocks
	{
		// Tags for mod easy loot table mod compat.
		// The loot/common, loot/uncommon, and loot/rare tags are applied to every dungeon, while the more structure specific ones only apply to the structure in its name
		// 
		// Common loot can generate between 1-3 in a chest
		// Uncommon loot can generate between 1-2 in a chest
		// Rare loot will only generate 1

		TagKey<Block> WARPED_AXE_TELEPORTS_TO = create("warped_axe_teleports_to");

		private static TagKey<Block> create(String name)
		{
			return TagKey.create(Registry.BLOCK_REGISTRY, DungeonsPlus.locate(name));
		}
	}

	public static interface Items
	{
		// Tags for mod easy loot table mod compat.
		// The loot/common, loot/uncommon, and loot/rare tags are applied to every dungeon, while the more structure specific ones only apply to the structure in its name
		// 
		// Common loot can generate between 1-3 in a chest
		// Uncommon loot can generate between 1-2 in a chest
		// Rare loot will only generate 1

		TagKey<Item> LOOT_COMMON = create("loot/common");
		TagKey<Item> LOOT_UNCOMMON = create("loot/uncommon");
		TagKey<Item> LOOT_RARE = create("loot/rare");

		TagKey<Item> LOOT_TOWER_COMMON = create("loot/tower/common");
		TagKey<Item> LOOT_TOWER_UNCOMMON = create("loot/tower/uncommon");
		TagKey<Item> LOOT_TOWER_RARE = create("loot/tower/rare");

		TagKey<Item> LOOT_REANIMATED_RUINS_COMMON = create("loot/reanimated_ruins/common");
		TagKey<Item> LOOT_REANIMATED_RUINS_UNCOMMON = create("loot/reanimated_ruins/uncommon");
		TagKey<Item> LOOT_REANIMATED_RUINS_RARE = create("loot/reanimated_ruins/rare");

		TagKey<Item> LOOT_SNOWY_TEMPLE_COMMON = create("loot/snowy_temple/common");
		TagKey<Item> LOOT_SNOWY_TEMPLE_UNCOMMON = create("loot/snowy_temple/uncommon");
		TagKey<Item> LOOT_SNOWY_TEMPLE_RARE = create("loot/snowy_temple/rare");

		TagKey<Item> LOOT_LEVIATHAN_COMMON = create("loot/leviathan/common");
		TagKey<Item> LOOT_LEVIATHAN_UNCOMMON = create("loot/leviathan/uncommon");
		TagKey<Item> LOOT_LEVIATHAN_RARE = create("loot/leviathan/rare");

		TagKey<Item> LOOT_WARPED_GARDEN_COMMON = create("loot/warped_garden/common");
		TagKey<Item> LOOT_WARPED_GARDEN_UNCOMMON = create("loot/warped_garden/uncommon");
		TagKey<Item> LOOT_WARPED_GARDEN_RARE = create("loot/warped_garden/rare");

		TagKey<Item> LOOT_SOUL_PRISON_COMMON = create("loot/soul_prison/common");
		TagKey<Item> LOOT_SOUL_PRISON_UNCOMMON = create("loot/soul_prison/uncommon");
		TagKey<Item> LOOT_SOUL_PRISON_RARE = create("loot/soul_prison/rare");

		private static TagKey<Item> create(String name)
		{
			return TagKey.create(Registry.ITEM_REGISTRY, DungeonsPlus.locate(name));
		}
	}

	public static interface EntityTypes
	{
		TagKey<EntityType<?>> WARPED_AXE_IMMUNE = create("warped_axe_immune");

		private static TagKey<EntityType<?>> create(String name)
		{
			return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, DungeonsPlus.locate(name));
		}
	}

	public static interface Structures
	{
		TagKey<Structure> ON_REANIMATED_RUINS_MAPS = map("reanimated_ruins");
		TagKey<Structure> ON_LEVIATHAN_MAPS = map("leviathan");
		TagKey<Structure> ON_SNOWY_TEMPLE_MAPS = map("snowy_temple");
		TagKey<Structure> ON_WARPED_GARDEN_MAPS = map("warped_garden");

		private static TagKey<Structure> map(String name)
		{
			return create("on_" + name + "_maps");
		}

		private static TagKey<Structure> create(String name)
		{
			return TagKey.create(Registry.STRUCTURE_REGISTRY, DungeonsPlus.locate(name));
		}
	}

	public static interface Biomes
	{
		TagKey<Biome> HAS_TOWER = structure("tower");
		TagKey<Biome> HAS_REANIMATED_RUINS_MOSSY = structure("reanimated_ruins_mossy");
		TagKey<Biome> HAS_REANIMATED_RUINS_MESA = structure("reanimated_ruins_mesa");
		TagKey<Biome> HAS_REANIMATED_RUINS_FROZEN = structure("reanimated_ruins_frozen");
		TagKey<Biome> HAS_LEVIATHAN = structure("leviathan");
		TagKey<Biome> HAS_SNOWY_TEMPLE = structure("snowy_temple");
		TagKey<Biome> HAS_WARPED_GARDEN = structure("warped_garden");
		TagKey<Biome> HAS_SOUL_PRISON = structure("soul_prison");
		TagKey<Biome> HAS_END_RUINS = structure("end_ruins");

		private static TagKey<Biome> structure(String name)
		{
			return create("has_structure/has_" + name);
		}

		private static TagKey<Biome> create(String name)
		{
			return TagKey.create(Registry.BIOME_REGISTRY, DungeonsPlus.locate(name));
		}
	}

	public static interface Enchantments
	{
		TagKey<Enchantment> WARPED_AXE_APPLICABLE = create("warped_axe_applicable");

		private static TagKey<Enchantment> create(String name)
		{
			return TagKey.create(Registry.ENCHANTMENT_REGISTRY, DungeonsPlus.locate(name));
		}
	}
}
