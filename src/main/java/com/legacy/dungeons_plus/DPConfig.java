package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.api.biome_dictionary.BiomeDictionary;
import com.legacy.structure_gel.api.biome_dictionary.BiomeType;
import com.legacy.structure_gel.api.config.StructureConfig;

import net.minecraft.world.level.biome.Biomes;
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
		public final StructureConfig endRuins;
		public final StructureConfig warpedGarden;
		public final StructureConfig soulPrison;

		public final ForgeConfigSpec.IntValue biggerDungeonMonsterBoxChance;

		public final ForgeConfigSpec.IntValue towerWaystoneChance;

		protected Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("Structures");
			// @formatter:off
			this.tower = StructureConfig.builder(builder, "tower")
					.pushPlacement()
						.spacing(25)
						.probability(75)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeType.builder().parents(BiomeDictionary.PLAINS, BiomeDictionary.OAK_FOREST, BiomeDictionary.DARK_FOREST, BiomeDictionary.BIRCH_FOREST, BiomeDictionary.MOUNTAIN).build())
					.popConfigured()
					.build();
			
			this.leviathan = StructureConfig.builder(builder, "leviathan")
					.pushPlacement()
						.spacing(36)
						.probability(100)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeDictionary.DESERT)
					.popConfigured()
					.build();
			
			this.snowyTemple = StructureConfig.builder(builder, "snowy_temple")
					.pushPlacement()
						.spacing(36)
						.probability(100)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeType.builder().parents(BiomeDictionary.SNOWY_PLAINS, BiomeDictionary.SNOWY_SPRUCE_FOREST).build())
					.popConfigured()
					.build();
			
			this.reanimatedRuins = StructureConfig.builder(builder, "reanimated_ruins")
					.pushPlacement()
						.spacing(12)
						.probability(40)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeDictionary.OVERWORLD, BiomeType.builder().biomes(Biomes.MUSHROOM_FIELDS).build())
					.popConfigured()
					.build();
			
			this.endRuins = StructureConfig.builder(builder, "end_ruins")
					.pushPlacement()
						.spacing(24)
						.probability(80)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeDictionary.OUTER_END_ISLAND)
					.popConfigured()
					.build();
			
			this.warpedGarden = StructureConfig.builder(builder, "warped_garden")
					.pushPlacement()
						.spacing(36)
						.probability(100)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeDictionary.OCEAN, BiomeDictionary.FROZEN)
					.popConfigured()
					.build();
			
			this.soulPrison = StructureConfig.builder(builder, "soul_prison")
					.pushPlacement()
						.spacing(25)
						.probability(100)
					.popPlacement()
					.pushConfigured()
						.biomes(BiomeType.builder().biomes(Biomes.SOUL_SAND_VALLEY).build())
					.popConfigured()
					.build();
			
			builder.pop();
			// @formatter:on

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
