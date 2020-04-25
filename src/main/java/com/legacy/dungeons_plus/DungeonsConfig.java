package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.ConfigTemplates;

import net.minecraftforge.common.ForgeConfigSpec;

public class DungeonsConfig
{
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static
	{
		Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class Common
	{
		public final ConfigTemplates.BiomeStructureConfig tower;
		public final ConfigTemplates.BiomeStructureConfig leviathan;
		public final ConfigTemplates.StructureConfig biggerDungeon;

		public Common(ForgeConfigSpec.Builder builder)
		{
			this.tower = new ConfigTemplates.BiomeStructureConfig(builder, "tower", 0.75D, 25, 6, "plains, forest, dark_forest, birch_forest, mountains");
			this.leviathan = new ConfigTemplates.BiomeStructureConfig(builder, "leviathan", 1.0D, 36, 8, "desert");
			this.biggerDungeon = new ConfigTemplates.StructureConfig(builder, "bigger_dungeon", 0.1D, 5, 2);

		}
	}
}
