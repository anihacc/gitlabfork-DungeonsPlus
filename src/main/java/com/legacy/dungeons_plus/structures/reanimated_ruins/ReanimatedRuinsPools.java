package com.legacy.dungeons_plus.structures.reanimated_ruins;

import java.util.HashMap;
import java.util.Map;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ReanimatedRuinsPools
{
	public static final Holder<StructureTemplatePool> DESERT_ROOT, FROZEN_ROOT, MOSSY_ROOT;

	public static void init()
	{
	}

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "reanimated_ruins/");

		DESERT_ROOT = registry.register("center_desert", registry.builder().names("center/desert_0").maintainWater(false).build());
		FROZEN_ROOT = registry.register("center_frozen", registry.builder().names("center/frozen_0").maintainWater(false).build());
		MOSSY_ROOT = registry.register("center_mossy", registry.builder().names("center/mossy_0").maintainWater(false).build());
		
		JigsawPoolBuilder basicPoolBuilder = registry.builder().maintainWater(false);

		registry.register("terminator", basicPoolBuilder.clone().names("terminator/0", "terminator/1").build());

		Map<String, Integer> rooms = new HashMap<>();
		for (int i = 0; i <= 11; i++)
		{
			rooms.put("room/small_" + i, 2);
		}
		for (int i = 0; i <= 4; i++)
		{
			rooms.put("room/large_3"/* + i*/, 2);
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
