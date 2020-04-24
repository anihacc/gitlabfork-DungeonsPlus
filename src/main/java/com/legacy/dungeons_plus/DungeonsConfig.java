package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class DungeonsConfig
{
	public static class Common
	{
		public final ForgeConfigSpec.DoubleValue towerProbability;
		public final ForgeConfigSpec.IntValue towerSpacing;
		public final ForgeConfigSpec.IntValue towerOffset;
		public final ForgeConfigSpec.DoubleValue biggerDungeonProbability;
		public final ForgeConfigSpec.IntValue biggerDungeonSpacing;
		public final ForgeConfigSpec.IntValue biggerDungeonOffset;

		public Common(ForgeConfigSpec.Builder builder)
		{
			this.towerProbability = builder.comment("Chance of a tower generating").defineInRange("tower.probability", 0.75D, 0.0D, 1.0D);
			this.towerSpacing = builder.comment("Spacing between towers").defineInRange("tower.spacing", 25, 1, Integer.MAX_VALUE);
			this.towerOffset = builder.comment("Offset that towers can have").defineInRange("tower.offset", 6, 0, Integer.MAX_VALUE);

			this.biggerDungeonProbability = builder.comment("Chance of a bigger dungeon generating").defineInRange("biggerdungeon.probability", 0.10D, 0.0D, 1.0D);
			this.biggerDungeonSpacing = builder.comment("Spacing between bigger dungeons").defineInRange("biggerdungeon.spacing", 4, 1, Integer.MAX_VALUE);
			this.biggerDungeonOffset = builder.comment("Offset that bigger dungeons can have").defineInRange("biggerdungeon.offset", 1, 0, Integer.MAX_VALUE);

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
