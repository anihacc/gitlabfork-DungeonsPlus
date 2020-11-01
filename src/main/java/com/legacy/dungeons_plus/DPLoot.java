package com.legacy.dungeons_plus;

import net.minecraft.util.ResourceLocation;

public class DPLoot
{
	// Tower
	public static final ResourceLocation VEX = locate("tower/vex");
	public static final ResourceLocation VEX_MAP = locate("tower/vex_map");

	// Bigger/Buried Dungeon
	public static final ResourceLocation HUSK = locate("bigger_dungeon/husk");
	public static final ResourceLocation HUSK_MAP = locate("bigger_dungeon/husk_map");
	public static final ResourceLocation STRAY = locate("bigger_dungeon/stray");
	public static final ResourceLocation STRAY_MAP = locate("bigger_dungeon/stray_map");

	// Leviathan
	public static final ResourceLocation LEVIATHAN = locate("leviathan/common");

	// Snowy Temple
	public static final ResourceLocation SNOWY_TEMPLE = locate("snowy_temple/common");

	// Warped Garden
	public static final ResourceLocation WARPED_GARDEN = locate("warped_garden/common");

	// Soul Prison
	public static final ResourceLocation SOUL_PRISON = locate("soul_prison/common");

	// Entities
	public static ResourceLocation LEVIATHAN_HUSK = DungeonsPlus.locate("entities/leviathan_husk");
	public static ResourceLocation SNOWY_TEMPLE_STRAY = DungeonsPlus.locate("entities/snowy_temple_stray");

	private static ResourceLocation locate(String key)
	{
		return DungeonsPlus.locate("chests/" + key);
	}
}
