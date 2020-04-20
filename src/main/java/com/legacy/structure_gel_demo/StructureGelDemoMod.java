package com.legacy.structure_gel_demo;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod(StructureGelDemoMod.MODID)
public class StructureGelDemoMod
{
	public static final String NAME = "Structure Gel Demo";
	public static final String MODID = "structure_gel_demo";
	public static final String VERSION = "1.0.0";

	public StructureGelDemoMod()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
	}

	private void commonInit(final FMLCommonSetupEvent event)
	{

	}

	private void clientInit(final FMLClientSetupEvent event)
	{

	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}

	public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, String name, T object)
	{
		object.setRegistryName(StructureGelDemoMod.locate(name));
		registry.register(object);
		return object;
	}

	@Mod.EventBusSubscriber(modid = StructureGelDemoMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Features
	{

		@SubscribeEvent
		public static void onRegistry(final RegistryEvent.Register<Feature<?>> event)
		{

		}


	}
}
