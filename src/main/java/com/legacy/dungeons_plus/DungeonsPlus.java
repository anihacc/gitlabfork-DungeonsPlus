package com.legacy.dungeons_plus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

// TODO Fix mods.toml structure gel dependency
@Mod(DungeonsPlus.MODID)
public class DungeonsPlus
{
	public static final String MODID = "dungeons_plus";
	public static final Logger LOGGER = LogManager.getLogger("ModdingLegacy/" + MODID);

	public static boolean isWaystonesLoaded = false;
	public static boolean isQuarkLoaded = false;

	public DungeonsPlus()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DPConfig.COMMON_SPEC);
	}

	public static ResourceLocation locate(String key)
	{
		return new ResourceLocation(MODID, key);
	}

	public static Logger makeLogger(Class<?> containerClass)
	{
		return LogManager.getLogger("ModdingLegacy/" + MODID + "/" + containerClass.getSimpleName());
	}
}
