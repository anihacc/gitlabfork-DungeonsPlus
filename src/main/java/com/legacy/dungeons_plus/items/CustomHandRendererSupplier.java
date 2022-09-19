package com.legacy.dungeons_plus.items;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface CustomHandRendererSupplier
{
	@OnlyIn(Dist.CLIENT)
	com.legacy.dungeons_plus.client.renderers.CustomHandRenderer getHandRenderer();
}
