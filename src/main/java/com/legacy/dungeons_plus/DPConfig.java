package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class DPConfig
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
		public final StructureConfig tower;
		public final StructureConfig leviathan;
		public final StructureConfig snowyTemple;
		public final StructureConfig biggerDungeon;
		public final StructureConfig endRuins;

		public Common(ForgeConfigSpec.Builder builder)
		{
			this.tower = new StructureConfig(builder, "tower").probability(0.75D).spacing(25).offset(6).biomes(true, "#dungeons_plus:tower_biomes");
			this.leviathan = new StructureConfig(builder, "leviathan").probability(1.0D).spacing(36).offset(8).biomes(true, "#dungeons_plus:leviathan_biomes");
			this.snowyTemple = new StructureConfig(builder, "snowy_temple").probability(1.0D).spacing(36).offset(8).biomes(true, "#dungeons_plus:snowy_temple_biomes");
			this.biggerDungeon = new StructureConfig(builder, "bigger_dungeon").probability(0.4D).spacing(12).offset(5).biomes(true, "#dungeons_plus:bigger_dungeon_biomes, !mushroom_fields, !mushroom_field_shore");
			this.endRuins = new StructureConfig(builder, "end_ruins").probability(0.8D).spacing(24).offset(8).biomes(true, "#dungeons_plus:end_ruins_biomes");
		}
	}
}
