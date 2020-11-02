package com.legacy.dungeons_plus;

import net.minecraft.util.ResourceLocation;

public class DPLoot
{
	public static final class Tower
	{
		public static final ResourceLocation CHEST_VEX = chest("tower/vex");
		public static final ResourceLocation CHEST_VEX_MAP = chest("tower/vex_map");
		
		public static final ResourceLocation SKELETON = entity("tower/skeleton");
		public static final ResourceLocation SPIDER = entity("tower/spider");
		public static final ResourceLocation ZOMBIE = entity("tower/zombie");
	}

	public static final class BiggerDungeon
	{
		public static final ResourceLocation CHEST_HUSK = chest("bigger_dungeon/husk");
		public static final ResourceLocation CHEST_HUSK_MAP = chest("bigger_dungeon/husk_map");
		public static final ResourceLocation CHEST_STRAY = chest("bigger_dungeon/stray");
		public static final ResourceLocation CHEST_STRAY_MAP = chest("bigger_dungeon/stray_map");
		
		public static final ResourceLocation SKELETON = entity("bigger_dungeon/skeleton");
		public static final ResourceLocation ZOMBIE = entity("bigger_dungeon/zombie");
	}

	public static final class Leviathan
	{
		public static final ResourceLocation CHEST_COMMON = chest("leviathan/common");
		
		public static final ResourceLocation HUSK = entity("leviathan/husk");
	}

	public static final class SnowyTemple
	{
		public static final ResourceLocation CHEST_COMMON = chest("snowy_temple/common");
		
		public static final ResourceLocation STRAY = entity("snowy_temple/stray");
	}

	public static final class WarpedGarden
	{
		public static final ResourceLocation CHEST_COMMON = chest("warped_garden/common");
	}

	public static final class SoulPrison
	{
		public static final ResourceLocation CHEST_COMMON = chest("soul_prison/common");
	}

	private static ResourceLocation chest(String key)
	{
		return DungeonsPlus.locate("chests/" + key);
	}

	private static ResourceLocation entity(String key)
	{
		return DungeonsPlus.locate("entities/" + key);
	}
}
