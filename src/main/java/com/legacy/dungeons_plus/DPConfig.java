package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class DPConfig
{
	public static final Common COMMON;
	protected static final ForgeConfigSpec COMMON_SPEC;
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
		public final StructureConfig warpedGarden;
		public final StructureConfig soulPrison;

		protected Common(ForgeConfigSpec.Builder builder)
		{
			this.tower = new StructureConfig(builder, "tower").probability(0.75D).spacing(25).offset(6).biomes(true, "#structure_gel:plains, #structure_gel:oak_forest, #structure_gel:dark_forest, #structure_gel:birch_forest, #structure_gel:mountain");
			this.leviathan = new StructureConfig(builder, "leviathan").probability(1.0D).spacing(36).offset(8).biomes(true, "#structure_gel:desert");
			this.snowyTemple = new StructureConfig(builder, "snowy_temple").probability(1.0D).spacing(36).offset(8).biomes(true, "#structure_gel:snowy_plains, #structure_gel:snowy_spruce_forest");
			this.biggerDungeon = new StructureConfig(builder, "bigger_dungeon").probability(0.4D).spacing(12).offset(5).biomes(true, "#structure_gel:overworld, !mushroom_fields, !mushroom_field_shore");
			this.endRuins = new StructureConfig(builder, "end_ruins").probability(0.8D).spacing(24).offset(8).biomes(true, "#structure_gel:outer_end_island");
			this.warpedGarden = new StructureConfig(builder, "warped_garden").spacing(36).offset(8).biomes(true, "#structure_gel:ocean, !#structure_gel:frozen");
			this.soulPrison = new StructureConfig(builder, "soul_prison").spacing(25).offset(6).biomes(true, "minecraft:soul_sand_valley");

		}
	}
}
