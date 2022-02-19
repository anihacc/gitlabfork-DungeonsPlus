package com.legacy.dungeons_plus.events;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.client.DPBlockLayers;

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
		}
	}
}
