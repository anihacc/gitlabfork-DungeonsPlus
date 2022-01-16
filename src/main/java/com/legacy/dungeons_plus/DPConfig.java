package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.api.config.StructureConfig;

import net.minecraft.world.level.dimension.DimensionType;
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

		public final ForgeConfigSpec.IntValue biggerDungeonMonsterBoxChance;

		public final ForgeConfigSpec.IntValue towerWaystoneChance;

		protected Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("Structures");
			this.tower = new StructureConfig(builder, "tower").probability(75).spacing(25).offset(6).biomes(true, "#structure_gel:plains", "#structure_gel:oak_forest", "#structure_gel:dark_forest", "#structure_gel:birch_forest", "#structure_gel:mountain").validDimensions(DimensionType.OVERWORLD_LOCATION.location().toString());
			this.leviathan = new StructureConfig(builder, "leviathan").probability(100).spacing(36).offset(8).biomes(true, "#structure_gel:desert").validDimensions(DimensionType.OVERWORLD_LOCATION.location().toString());
			this.snowyTemple = new StructureConfig(builder, "snowy_temple").probability(100).spacing(36).offset(8).biomes(true, "#structure_gel:snowy_plains", "#structure_gel:snowy_spruce_forest").validDimensions(DimensionType.OVERWORLD_LOCATION.location().toString());
			this.biggerDungeon = new StructureConfig(builder, "bigger_dungeon").probability(40).spacing(12).offset(5).biomes(true, "#forge:overworld", "!minecraft:mushroom_fields").validDimensions(DimensionType.OVERWORLD_LOCATION.location().toString());
			this.endRuins = new StructureConfig(builder, "end_ruins").probability(80).spacing(24).offset(8).biomes(true, "#structure_gel:outer_end_island").validDimensions(DimensionType.END_LOCATION.location().toString());
			this.warpedGarden = new StructureConfig(builder, "warped_garden").spacing(36).offset(8).biomes(true, "#structure_gel:ocean", "!#structure_gel:frozen").validDimensions(DimensionType.OVERWORLD_LOCATION.location().toString());
			this.soulPrison = new StructureConfig(builder, "soul_prison").spacing(25).offset(6).biomes(true, "minecraft:soul_sand_valley").validDimensions(DimensionType.NETHER_LOCATION.location().toString());
			builder.pop();

			builder.push("Mod Compat");
			builder.push("Quark");
			this.biggerDungeonMonsterBoxChance = builder.comment("Percent chance for monster boxes from Quark to generate in the buried dungeon").defineInRange("bigger_dungeon_monster_box_chance", 35, 0, 100);
			builder.pop();
			builder.push("Waystones");
			this.towerWaystoneChance = builder.comment("Percent chance for a waystone from Waystones to generate on top of the tower").defineInRange("tower_waystone_chance", 100, 0, 100);
			builder.pop();
			builder.pop();
		}
	}
}
