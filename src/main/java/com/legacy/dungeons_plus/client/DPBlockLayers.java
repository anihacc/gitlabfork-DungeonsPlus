package com.legacy.dungeons_plus.client;

import com.legacy.dungeons_plus.registry.DPBlocks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class DPBlockLayers
{
	public static void init()
	{
		cutout(DPBlocks.DYNAMIC_SPAWNER);
	}
	
	private static void cutout(Block block)
	{
		ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
	}
}
