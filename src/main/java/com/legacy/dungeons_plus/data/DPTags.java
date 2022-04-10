package com.legacy.dungeons_plus.data;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class DPTags
{
	public static interface StructureTags
	{
		TagKey<ConfiguredStructureFeature<?, ?>> ON_REANIMATED_RUINS_MAPS = create("on_reanimated_ruins_maps");
		TagKey<ConfiguredStructureFeature<?, ?>> ON_LEVIATHAN_MAPS = create("on_leviathan_maps");
		TagKey<ConfiguredStructureFeature<?, ?>> ON_SNOWY_TEMPLE_MAPS = create("on_snowy_temple_maps");
		TagKey<ConfiguredStructureFeature<?, ?>> ON_WARPED_GARDEN_MAPS = create("on_warped_garden_maps");
		
		private static TagKey<ConfiguredStructureFeature<?, ?>> create(String name)
		{
			return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, DungeonsPlus.locate(name));
		}
	}
}
