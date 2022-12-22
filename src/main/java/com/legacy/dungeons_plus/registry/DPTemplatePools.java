package com.legacy.dungeons_plus.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.DPProcessors;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class DPTemplatePools
{
	public static final RegistrarHandler<StructureTemplatePool> HANDLER = RegistrarHandler.getOrCreate(Registries.TEMPLATE_POOL, DungeonsPlus.MODID).bootstrap(DPTemplatePools::init);

	public static final ResourceKey<StructureTemplatePool> REANIMATED_RUINS_MOSSY = HANDLER.key("reanimated_ruins/center_mossy");
	public static final ResourceKey<StructureTemplatePool> REANIMATED_RUINS_MESA = HANDLER.key("reanimated_ruins/center_mesa");
	public static final ResourceKey<StructureTemplatePool> REANIMATED_RUINS_FROZEN = HANDLER.key("reanimated_ruins/center_frozen");
	public static final ResourceKey<StructureTemplatePool> LEVIATHAN = HANDLER.key("leviathan/skeleton/spine");
	public static final ResourceKey<StructureTemplatePool> WARPED_GARDEN = HANDLER.key("warped_garden/root");
	public static final ResourceKey<StructureTemplatePool> END_RUINS = HANDLER.key("end_ruins/root");
	
	private static void init(BootstapContext<StructureTemplatePool> context)
	{
		reanimatedRuins(context);
		leviathan(context);
		warpedGarden(context);
		endRuins(context);
	}

	private static void reanimatedRuins(BootstapContext<StructureTemplatePool> context)
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "reanimated_ruins/", context);

		registry.registerBuilder().pools(registry.poolBuilder().names("center/mossy_0").maintainWater(false)).register(REANIMATED_RUINS_MOSSY);
		registry.registerBuilder().pools(registry.poolBuilder().names("center/mesa_0").maintainWater(false)).register(REANIMATED_RUINS_MESA);
		registry.registerBuilder().pools(registry.poolBuilder().names("center/frozen_0").maintainWater(false)).register(REANIMATED_RUINS_FROZEN); // TODO no branching

		JigsawPoolBuilder basicPoolBuilder = registry.poolBuilder().maintainWater(false);

		registry.register("terminator", basicPoolBuilder.clone().names("terminator/0", "terminator/1"));

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
		registry.registerBuilder().fallback("terminator").pools(basicPoolBuilder.clone().names(rooms)).register("room");
	}
	
	private static void leviathan(BootstapContext<StructureTemplatePool> context)
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "leviathan/", context);

		var skeleton = registry.setPrefix(registry.prefix + "skeleton/");
		skeleton.registerBuilder().pools(skeleton.poolBuilder().maintainWater(false).names("spine_front_1", "spine_front_2")).register(LEVIATHAN);
		skeleton.register("spine_back", skeleton.poolBuilder().maintainWater(false).names("spine_back_1", "spine_back_2"));
		skeleton.register("skull", skeleton.poolBuilder().maintainWater(false).names("skull_1", "skull_2"));
		skeleton.register("tail", skeleton.poolBuilder().maintainWater(false).names("tail_1", "tail_2"));

		var temple = registry.setPrefix(registry.prefix + "temple/");
		temple.register("entrance", temple.poolBuilder().maintainWater(false).names("entrance_0"));
		temple.register("main_room", temple.poolBuilder().maintainWater(false).names("main_room_0"));
		temple.register("side_room_left", temple.poolBuilder().maintainWater(false).names(List.of(0, 1, 2, 3, 4).stream().map(i -> "side_room/left/" + i).toList()));
		temple.register("side_room_right", temple.poolBuilder().maintainWater(false).names(List.of(0, 1, 2, 3, 4).stream().map(i -> "side_room/right/" + i).toList()));
		temple.register("pillar", temple.poolBuilder().maintainWater(false).names(Map.of("pillar/broken", 1, "pillar/damaged", 2, "pillar/cracked", 3, "pillar/intact", 5)));
		temple.register("statue", temple.poolBuilder().maintainWater(false).names("statue/creeper"));
		
	}
	
	private static void warpedGarden(BootstapContext<StructureTemplatePool> context)
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "warped_garden/", context);
		var builder = registry.poolBuilder().maintainWater(false);
		
		registry.registerBuilder().pools(builder.clone().names("main_0")).register(WARPED_GARDEN);
		registry.register("cap", builder.clone().names("path_cap"));
		registry.register("path", "cap", builder.clone().names("path_i_0", "path_l_0", "path_t_0", "path_cross_0", "path_cross_1"));
		registry.register("bubble", "cap", builder.clone().names(List.of(0, 1, 2, 3, 4, 5, 6, 7).stream().map(i -> "bubble_" + i).toList()));
	}
	
	private static void endRuins(BootstapContext<StructureTemplatePool> context)
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "end_ruins/", context);

		/**
		 * This piece is the start for the entire structure. By using a root piece, I'm
		 * able to control where the center of the structure is.
		 */
		registry.registerBuilder().pools(registry.poolBuilder().names("root")).register(END_RUINS);

		registry.register("pylon_plate_spacer", registry.poolBuilder().names("pylon_plate_spacer"));
		/**
		 * I'm using the terrain matching placement behavior to ensure the structures
		 * that generate off of this plate are at ground level.
		 */
		registry.registerBuilder().pools(registry.poolBuilder().names("pylon_plate")).projection(StructureTemplatePool.Projection.TERRAIN_MATCHING).register("pylon_plate");

		/**
		 * By using the .weight(int) method, I can set the weights of all structures in
		 * the pool to the same value. Combined with the JigsawPoolBuilder.collect
		 * method, I can set the weights of all normal towers to 2 and all broken towers
		 * to 1 (default).
		 */
		registry.register("pylon", JigsawPoolBuilder.collect(registry.poolBuilder().names("pylon/tall", "pylon/medium", "pylon/small"), registry.poolBuilder().weight(2).names("pylon/tall_broken", "pylon/medium_broken", "pylon/small_broken")));

		registry.register("pylon/debris", registry.poolBuilder().names("pylon/debris_1", "pylon/debris_2", "pylon/debris_3", "pylon/debris_4"));

		/**
		 * Since all of the next registry entries will start with "end_ruins/tower/" I'm
		 * changing the prefix. Using setPrefix creates a clone of the registry, so I'm
		 * creating a new registry for this prefix. In practice, this allows me to use
		 * the old registry and it's prefix along with this one.
		 */
		JigsawRegistryHelper towerRegistry = registry.setPrefix("end_ruins/tower/");

		/**
		 * By using the .clone() method, I can use the same settings for each pool
		 * builder. All tower pools will use the RandomBlockSwapProcessor to replace end
		 * stone bricks with end stone.
		 */
		JigsawPoolBuilder towerPieces = towerRegistry.poolBuilder().processors(DPProcessors.END_RUINS_TOWER.getKey());
		towerRegistry.register("base", towerPieces.clone().names("base_1", "base_2"));
		towerRegistry.register("mid", towerPieces.clone().names("mid_1", "mid_2"));
		towerRegistry.register("top", towerPieces.clone().names("top_1", "top_2"));

		towerRegistry.register("block_pile", towerRegistry.poolBuilder().names(ImmutableMap.of("block_pile_1", 2, "block_pile_2", 2, "block_pile_3", 2, "block_pile_4", 1)));

	}
}
