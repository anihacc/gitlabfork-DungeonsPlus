package com.legacy.dungeons_plus.events;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.client.DPBlockLayers;
import com.legacy.dungeons_plus.client.block_entity_renderers.DynamicSpawnerRenderer;
import com.legacy.dungeons_plus.registry.DPBlockEntities;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DPClientEvents
{
	/*@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBus
	{
	
	}*/

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBus
	{
		@SubscribeEvent
		protected static void clientInit(final FMLClientSetupEvent event)
		{
			DPBlockLayers.init();
			initBlockEntityRenderers();
		}

		protected static void initBlockEntityRenderers()
		{
			BlockEntityRenderers.register(DPBlockEntities.DYNAMIC_SPAWNER, DynamicSpawnerRenderer::new);
		}
	}
}
