package com.legacy.dungeons_plus.structures.pools;

import java.util.HashMap;
import java.util.Map;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.DPProcessors;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ReanimatedRuinsPools
{
	public static final Holder<StructureTemplatePool> ROOT;

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "reanimated_ruins/");

		ROOT = registry.register("center", registry.builder().names("center/husk_0").maintainWater(false).build());

		JigsawPoolBuilder basicPoolBuilder = registry.builder().maintainWater(false).processors(DPProcessors.COBBLE_TO_MOSSY);

		registry.register("terminator", basicPoolBuilder.clone().names("terminator/0", "terminator/1").build());

		Map<String, Integer> rooms = new HashMap<>();
		for (int i = 0; i <= 11; i++)
		{
			rooms.put("room/small_" + i, 2);
		}
		for (int i = 0; i <= 4; i++)
		{
			rooms.put("room/large_" + i, 2);
		}
		for (int i = 0; i <= 12; i++)
		{
			rooms.put("hallway/" + i, 3);
		}
		for (int i = 0; i <= 2; i++)
		{
			rooms.put("stairs/" + i, 3);
		}
		registry.register("room", "terminator", basicPoolBuilder.clone().names(rooms).build());
	}
}
