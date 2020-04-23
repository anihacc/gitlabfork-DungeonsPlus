package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class DungeonsPlusConfig
{
	public static class Common
	{
		public final ForgeConfigSpec.DoubleValue towerProbability;
		public final ForgeConfigSpec.DoubleValue biggerDungeonProbability;

		public Common(ForgeConfigSpec.Builder builder)
		{
			this.towerProbability = builder.comment("Chance of a tower generating.").defineInRange("tower.probability", 0.5D, 0.0D, 1.0D);
			this.biggerDungeonProbability = builder.comment("Chance of a bigger dungeon generating.").defineInRange("biggerdungeon.probability", 0.8D, 0.0D, 1.0D);

		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static
	{
		Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
