package com.legacy.dungeons_plus.structures;

import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.registry.registrar.Registrar;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class DPProcessors
{
	private static final RegistrarHandler<StructureProcessorList> HANDLER = RegistrarHandler.getOrCreate(Registries.PROCESSOR_LIST, DungeonsPlus.MODID);

	public static final Registrar.Pointer<StructureProcessorList> END_RUINS_TOWER = HANDLER.createPointer("end_ruins_tower", () -> listOf(new RandomBlockSwapProcessor(Blocks.END_STONE_BRICKS, 0.1F, Blocks.END_STONE)));

	private static StructureProcessorList listOf(StructureProcessor... processors)
	{
		return new StructureProcessorList(List.of(processors));
	}
}
