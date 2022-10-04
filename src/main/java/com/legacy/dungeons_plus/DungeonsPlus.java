package com.legacy.dungeons_plus;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.reflect.Reflection;
import com.legacy.dungeons_plus.registry.DPEntityTypes;
import com.legacy.dungeons_plus.registry.DPItems;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPSoundEvents;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.core.util.LoggerWrapper;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String MODID = "dungeons_plus";
	public static final LoggerWrapper LOGGER = new LoggerWrapper(MODID);

	public static boolean isWaystonesLoaded = false;

	public DungeonsPlus()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DPConfig.COMMON_SPEC);
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		DPLoot.LOOT_POOLS_TYPES.register(modBus);
		
		Reflection.initialize(DPStructures.class);
		RegistrarHandler.registerHandlers(MODID, modBus, DPEntityTypes.HANDLER, DPItems.HANDLER, DPSoundEvents.HANDLER);
	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}
	
	public static ResourceLocation[] locateAll(String... keys)
	{
		return Arrays.stream(keys).map(DungeonsPlus::locate).toArray(ResourceLocation[]::new);
	}
	
	public static ResourceLocation[] locateAllPrefix(String prefix, String... keys)
	{
		return Arrays.stream(keys).map(s -> DungeonsPlus.locate(prefix + s)).toArray(ResourceLocation[]::new);
	}

	public static Logger makeLogger(Class<?> containerClass)
	{
		return LogManager.getLogger("ModdingLegacy/" + MODID + "/" + containerClass.getSimpleName());
	}
}
