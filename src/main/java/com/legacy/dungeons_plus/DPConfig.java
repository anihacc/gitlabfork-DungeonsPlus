package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsType;
import com.legacy.structure_gel.api.config.ConfigBuilder;
import com.legacy.structure_gel.api.config.ConfigValueWrapper;
import com.legacy.structure_gel.api.config.StructureConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

		public final ConfigValueWrapper<Integer, Double> huskLeviathanBladeChance;
		public final ForgeConfigSpec.BooleanValue husksDropSand;
		public final ConfigValueWrapper<Integer, Double> strayFrostedCowlChance;
		public final ForgeConfigSpec.BooleanValue straysDropIce;
		public final ConfigValueWrapper<Integer, Double> drownedWarpedAxeChance;
		public final ConfigValueWrapper<Integer, Double> drownedCoralChance;
		public final ConfigValueWrapper<Integer, Double> skeletonSoulCannonChance;

		public final ConfigValueWrapper<Integer, Double> towerWaystoneChance;

		protected Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("Structures");
			// @formatter:off
			this.tower = StructureConfig.builder(builder, "tower")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure()
						.biomes(DPTags.Biomes.HAS_TOWER)
					.popStructure()
					.build();
			
			this.leviathan = StructureConfig.builder(builder, "leviathan")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure()
						.biomes(DPTags.Biomes.HAS_LEVIATHAN)
					.popStructure()
					.build();
			
			this.snowyTemple = StructureConfig.builder(builder, "snowy_temple")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure()
						.biomes(DPTags.Biomes.HAS_SNOWY_TEMPLE)
					.popStructure()
					.build();
			
			this.reanimatedRuins = StructureConfig.builder(builder, "reanimated_ruins")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure(ReanimatedRuinsType.MOSSY.toString())
						.biomes(DPTags.Biomes.HAS_REANIMATED_RUINS_MOSSY)
					.popStructure()
					.pushStructure(ReanimatedRuinsType.MESA.toString())
						.biomes(DPTags.Biomes.HAS_REANIMATED_RUINS_MESA)
					.popStructure()
					.pushStructure(ReanimatedRuinsType.FROZEN.toString())
						.biomes(DPTags.Biomes.HAS_REANIMATED_RUINS_FROZEN)
					.popStructure()
					.build();
			
			this.warpedGarden = StructureConfig.builder(builder, "warped_garden")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure()
						.biomes(DPTags.Biomes.HAS_WARPED_GARDEN)
					.popStructure()
					.build();
			
			this.soulPrison = StructureConfig.builder(builder, "soul_prison")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure()
						.biomes(DPTags.Biomes.HAS_SOUL_PRISON)
					.popStructure()
					.build();
			
			this.endRuins = StructureConfig.builder(builder, "end_ruins")
					.pushPlacement()
						.probability(75)
					.popPlacement()
					.pushStructure()
						.biomes(DPTags.Biomes.HAS_END_RUINS)
					.popStructure()
					.build();
			
			builder.pop();
			// @formatter:on

			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

			builder.push("Mobs");
			this.huskLeviathanBladeChance = ConfigValueWrapper.create(ConfigBuilder.makeInt(builder, "husk_leviathan_blade_chance", "Percent chance that a Husk will wield a Leviathan Blade in the Leviathan.", 5, 0, 100), DPConfig::toPercent, bus, DungeonsPlus.MODID);
			this.husksDropSand = ConfigBuilder.makeBoolean(builder, "husks_drop_sand", "Determines if Husks will drop sand when spawned in the Leviathan.", true);

			this.strayFrostedCowlChance = ConfigValueWrapper.create(ConfigBuilder.makeInt(builder, "stray_frosted_cowl_chance", "Percent chance that a Stray will wear a Frosted Cowl in the Snowy Temple.", 5, 0, 100), DPConfig::toPercent, bus, DungeonsPlus.MODID);
			this.straysDropIce = ConfigBuilder.makeBoolean(builder, "strays_drop_ice", "Determines if Strays will drop ice when spawned in the Snowy Temple.", true);

			this.drownedWarpedAxeChance = ConfigValueWrapper.create(ConfigBuilder.makeInt(builder, "drowned_warped_axe_chance", "Percent chance that a Drowned will wear a Warped Axe in the Warped Garden.", 5, 0, 100), DPConfig::toPercent, bus, DungeonsPlus.MODID);
			this.drownedCoralChance = ConfigValueWrapper.create(ConfigBuilder.makeInt(builder, "drowned_coral_chance",  "Percent chance that a Drowned will hold coral in the Warped Garden.", 30, 0, 100), DPConfig::toPercent, bus, DungeonsPlus.MODID);

			this.skeletonSoulCannonChance = ConfigValueWrapper.create(ConfigBuilder.makeInt(builder, "skeleton_soul_cannon_chance", "Percent chance that a Skeleton will wield a Soul Cannon in the Soul Prison.", 5, 0, 100), DPConfig::toPercent, bus, DungeonsPlus.MODID);

			builder.pop();

			builder.push("Mod Compat");
			builder.push("Waystones");
			this.towerWaystoneChance = ConfigValueWrapper.create(ConfigBuilder.makeInt(builder, "tower_waystone_chance", "Percent chance for a waystone from Waystones to generate on top of the tower", 100, 0, 100), DPConfig::toPercent, bus, DungeonsPlus.MODID);
			builder.pop();
			builder.pop();
		}
	}

	private static double toPercent(int i)
	{
		return i / 100.0D;
	}
}
