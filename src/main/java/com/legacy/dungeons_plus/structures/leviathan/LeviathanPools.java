package com.legacy.dungeons_plus.structures.leviathan;

import java.util.List;
import java.util.Map;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class LeviathanPools
{
	public static final Holder<StructureTemplatePool> ROOT;

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "leviathan/");

		var skeleton = registry.setPrefix(registry.prefix + "skeleton/");
		ROOT = skeleton.register("spine", skeleton.builder().maintainWater(false).names("spine_front_1", "spine_front_2").build());
		skeleton.register("spine_back", skeleton.builder().maintainWater(false).names("spine_back_1", "spine_back_2").build());
		skeleton.register("skull", skeleton.builder().maintainWater(false).names("skull_1", "skull_2").build());
		skeleton.register("tail", skeleton.builder().maintainWater(false).names("tail_1", "tail_2").build());

		var temple = registry.setPrefix(registry.prefix + "temple/");
		temple.register("entrance", temple.builder().maintainWater(false).names("entrance_0").build());
		temple.register("main_room", temple.builder().maintainWater(false).names("main_room_0").build());
		temple.register("side_room_left", temple.builder().maintainWater(false).names(List.of(0, 1, 2, 3, 4).stream().map(i -> "side_room/left/" + i).toList()).build());
		temple.register("side_room_right", temple.builder().maintainWater(false).names(List.of(0, 1, 2, 3, 4).stream().map(i -> "side_room/right/" + i).toList()).build());
		temple.register("pillar", temple.builder().maintainWater(false).names(Map.of("pillar/broken", 1, "pillar/damaged", 2, "pillar/cracked", 3, "pillar/intact", 5)).build());
		temple.register("statue", temple.builder().maintainWater(false).names("statue/creeper").build());
		
	}
}