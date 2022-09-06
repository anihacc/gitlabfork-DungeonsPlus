package com.legacy.dungeons_plus.structures.end_ruins;

import com.google.common.collect.ImmutableMap;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.DPProcessors;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class EndRuinsPools
{
	public static final Holder<StructureTemplatePool> ROOT;

	static
	{
		JigsawRegistryHelper registry = new JigsawRegistryHelper(DungeonsPlus.MODID, "end_ruins/");

		/**
		 * This piece is the start for the entire structure. By using a root piece, I'm
		 * able to control where the center of the structure is.
		 */
		ROOT = registry.register("root", registry.builder().names("root").build());

		registry.register("pylon_plate_spacer", registry.builder().names("pylon_plate_spacer").build());
		/**
		 * I'm using the terrain matching placement behavior to ensure the structures
		 * that generate off of this plate are at ground level.
		 */
		registry.register("pylon_plate", registry.builder().names("pylon_plate").build(), StructureTemplatePool.Projection.TERRAIN_MATCHING);

		/**
		 * By using the .weight(int) method, I can set the weights of all structures in
		 * the pool to the same value. Combined with the JigsawPoolBuilder.collect
		 * method, I can set the weights of all normal towers to 2 and all broken towers
		 * to 1 (default).
		 */
		registry.register("pylon", JigsawPoolBuilder.collect(registry.builder().names("pylon/tall", "pylon/medium", "pylon/small"), registry.builder().weight(2).names("pylon/tall_broken", "pylon/medium_broken", "pylon/small_broken")));

		registry.register("pylon/debris", registry.builder().names("pylon/debris_1", "pylon/debris_2", "pylon/debris_3", "pylon/debris_4").build());

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
		JigsawPoolBuilder towerPieces = towerRegistry.builder().processors(DPProcessors.END_RUINS_TOWER);
		towerRegistry.register("base", towerPieces.clone().names("base_1", "base_2").build());
		towerRegistry.register("mid", towerPieces.clone().names("mid_1", "mid_2").build());
		towerRegistry.register("top", towerPieces.clone().names("top_1", "top_2").build());

		towerRegistry.register("block_pile", towerRegistry.builder().names(ImmutableMap.of("block_pile_1", 2, "block_pile_2", 2, "block_pile_3", 2, "block_pile_4", 1)).build());

	}
}
