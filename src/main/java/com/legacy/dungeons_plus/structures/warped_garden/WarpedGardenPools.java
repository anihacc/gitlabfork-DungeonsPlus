package com.legacy.dungeons_plus.structures.warped_garden;

import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class WarpedGardenPools
{
	public static final Holder<StructureTemplatePool> ROOT;

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "warped_garden/");
		var builder = registry.builder().maintainWater(false);
		
		ROOT = registry.register("root", builder.clone().names("main_0").build());
		registry.register("cap", builder.clone().names("path_cap").build());
		registry.register("path", "cap", builder.clone().names("path_i_0", "path_l_0", "path_t_0", "path_cross_0", "path_cross_1").build());
		registry.register("bubble", "cap", builder.clone().names(List.of(0, 1, 2, 3, 4, 5, 6, 7).stream().map(i -> "bubble_" + i).toList()).build());
	}
}
