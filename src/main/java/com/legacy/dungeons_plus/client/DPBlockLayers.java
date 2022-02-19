package com.legacy.dungeons_plus.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class DPBlockLayers
{
	public static void init()
	{

	}

	private static void cutout(Block block)
	{
		ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
	}
}
