package com.legacy.dungeons_plus.pools;

import com.legacy.dungeons_plus.DPProcessors;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class SnowyTemplePools
{
	public static final StructureTemplatePool ROOT;

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "snowy_temple/");

		ROOT = registry.register("temple/outside", registry.builder().names("temple/outside").processors(DPProcessors.ICE_TO_BLUE).build());
		registry.register("temple/inside", registry.builder().names("temple/inside_1", "temple/inside_2").processors(DPProcessors.ICE_TO_BLUE).build());
		registry.register("road", registry.builder().names("road_1", "road_2").build(), StructureTemplatePool.Projection.TERRAIN_MATCHING);
	}
}
