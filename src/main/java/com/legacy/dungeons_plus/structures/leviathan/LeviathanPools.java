package com.legacy.dungeons_plus.structures.leviathan;

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
		ROOT = registry.register("spine", registry.builder().names("spine_front_1", "spine_front_2").build());
		registry.register("spine_back", registry.builder().names("spine_back_1", "spine_back_2").build());
		registry.register("skull", registry.builder().names("skull_1", "skull_2").build());
		registry.register("tail", registry.builder().names("tail_1", "tail_2").build());
		registry.register("room", registry.builder().names("room").maintainWater(false).build());
	}
}