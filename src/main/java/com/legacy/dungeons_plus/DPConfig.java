package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsType;
import com.legacy.structure_gel.api.config.StructureConfig;

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
		public final StructureConfig reanimatedRuins;
		public final StructureConfig warpedGarden;
		public final StructureConfig soulPrison;
		public final StructureConfig endRuins;

		public final ForgeConfigSpec.IntValue huskLeviathanBladeChance;
		public final ForgeConfigSpec.BooleanValue husksDropSand;
		public final ForgeConfigSpec.IntValue strayFrostedCowlChance;
		public final ForgeConfigSpec.BooleanValue straysDropIce;
		public final ForgeConfigSpec.IntValue drownedWarpedAxeChance;
		public final ForgeConfigSpec.IntValue drownedCoralChance;
		public final ForgeConfigSpec.IntValue skeletonSoulCannonChance;

		public final ForgeConfigSpec.IntValue towerWaystoneChance;

		protected Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("Structures");
			// @formatter:off
			this.tower = StructureConfig.builder(builder, "tower")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(DPTags.BiomeTags.HAS_TOWER)
					.popConfigured()
					.build();
			
			this.leviathan = StructureConfig.builder(builder, "leviathan")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(DPTags.BiomeTags.HAS_LEVIATHAN)
					.popConfigured()
					.build();
			
			this.snowyTemple = StructureConfig.builder(builder, "snowy_temple")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(DPTags.BiomeTags.HAS_SNOWY_TEMPLE)
					.popConfigured()
					.build();
			
			this.reanimatedRuins = StructureConfig.builder(builder, "reanimated_ruins")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured(ReanimatedRuinsType.MOSSY.toString())
						.biomes(DPTags.BiomeTags.HAS_REANIMATED_RUINS_MOSSY)
					.popConfigured()
					.pushConfigured(ReanimatedRuinsType.MESA.toString())
						.biomes(DPTags.BiomeTags.HAS_REANIMATED_RUINS_MESA)
					.popConfigured()
					.pushConfigured(ReanimatedRuinsType.FROZEN.toString())
						.biomes(DPTags.BiomeTags.HAS_REANIMATED_RUINS_FROZEN)
					.popConfigured()
					.build();
			
			this.warpedGarden = StructureConfig.builder(builder, "warped_garden")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(DPTags.BiomeTags.HAS_WARPED_GARDEN)
					.popConfigured()
					.build();
			
			this.soulPrison = StructureConfig.builder(builder, "soul_prison")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(DPTags.BiomeTags.HAS_SOUL_PRISON)
					.popConfigured()
					.build();
			
			this.endRuins = StructureConfig.builder(builder, "end_ruins")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(DPTags.BiomeTags.HAS_END_RUINS)
					.popConfigured()
					.build();
			
			builder.pop();
			// @formatter:on

			builder.push("Mobs");
			this.huskLeviathanBladeChance = builder.comment("Percent chance that a Husk will wield a Leviathan Blade in the Leviathan.").defineInRange("husk_leviathan_blade_chance", 5, 0, 100);
			this.husksDropSand = builder.comment("Determines if Husks will drop sand when spawned in the Leviathan.").define("husks_drop_sand", true);

			this.strayFrostedCowlChance = builder.comment("Percent chance that a Stray will wear a Frosted Cowl in the Snowy Temple.").defineInRange("stray_frosted_cowl_chance", 5, 0, 100);
			this.straysDropIce = builder.comment("Determines if Strays will drop ice when spawned in the Snowy Temple.").define("strays_drop_ice", true);

			this.drownedWarpedAxeChance = builder.comment("Percent chance that a Drowned will wear a Warped Axe in the Warped Garden.").defineInRange("drowned_warped_axe_chance", 5, 0, 100);
			this.drownedCoralChance = builder.comment("Percent chance that a Drowned will hold coral in the Warped Garden.").defineInRange("drowned_coral_chance", 30, 0, 100);
			
			this.skeletonSoulCannonChance = builder.comment("Percent chance that a Skeleton will wield a Soul Cannon in the Soul Prison.").defineInRange("skeleton_soul_cannon_chance", 5, 0, 100);
			
			builder.pop();
			
			builder.push("Mod Compat");
			builder.push("Waystones");
			this.towerWaystoneChance = builder.comment("Percent chance for a waystone from Waystones to generate on top of the tower").defineInRange("tower_waystone_chance", 100, 0, 100);
			builder.pop();
			builder.pop();
		}
	}
}
