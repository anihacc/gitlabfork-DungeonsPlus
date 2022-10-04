package com.legacy.dungeons_plus.data;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class DPTags
{
	public static interface StructureTags
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

	public static interface BiomeTags
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
}
