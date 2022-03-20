package com.legacy.dungeons_plus.structures;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.registry.RegistryHelper;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class DPProcessors
{
	public static final Holder<StructureProcessorList> COBBLE_TO_MOSSY = register("cobble_to_mossy", new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.MOSSY_COBBLESTONE));
	public static final Holder<StructureProcessorList> COBBLE_TO_ICE = register("cobble_to_ice", new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.PACKED_ICE));
	public static final Holder<StructureProcessorList> COBBLE_TO_TERRACOTTA = register("cobble_to_terracotta", new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.TERRACOTTA));
	public static final Holder<StructureProcessorList> ICE_TO_BLUE = register("ice_to_blue", new RandomBlockSwapProcessor(Blocks.PACKED_ICE, 0.07F, Blocks.BLUE_ICE));
	public static final Holder<StructureProcessorList> END_RUINS_TOWER = register("end_ruins_tower", new RandomBlockSwapProcessor(Blocks.END_STONE_BRICKS, 0.1F, Blocks.END_STONE));

	private static Holder<StructureProcessorList> register(String key, StructureProcessor processor)
	{
		return RegistryHelper.registerProcessor(DungeonsPlus.locate(key), processor);
	}

	private static Holder<StructureProcessorList> register(String key, StructureProcessorList processorList)
	{
		return RegistryHelper.registerProcessor(DungeonsPlus.locate(key), processorList);
	}
}
