package com.legacy.dungeons_plus;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.api.config.ConfigBuilder;
import com.legacy.structure_gel.api.config.ConfigValueWrapper;

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
		public final ConfigValueWrapper<Integer, Double> huskLeviathanBladeChance;
		public final ForgeConfigSpec.BooleanValue husksDropSand;
		public final ConfigValueWrapper<Integer, Double> strayFrostedCowlChance;
		public final ForgeConfigSpec.BooleanValue straysDropIce;
		public final ConfigValueWrapper<Integer, Double> drownedWarpedAxeChance;
		public final ConfigValueWrapper<Integer, Double> drownedCoralChance;
		public final ConfigValueWrapper<Integer, Double> skeletonSoulCannonChance;

		public final ForgeConfigSpec.BooleanValue loyaltyReturnsFromVoid;
		public final ForgeConfigSpec.BooleanValue soulCannonProducesFire;

		public final ConfigValueWrapper<Integer, Double> towerWaystoneChance;

		protected Common(ForgeConfigSpec.Builder builder)
		{
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
			
			builder.push("Items");
			this.loyaltyReturnsFromVoid = ConfigBuilder.makeBoolean(builder, "loyalty_returns_from_void", "When true, throwable items with loyalty will return if they enter the void.", true);
			this.soulCannonProducesFire = ConfigBuilder.makeBoolean(builder, "soul_cannon_produces_fire", "When true, the fireball from a Soul Cannon enchanted with flame will place fire.", true);
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
