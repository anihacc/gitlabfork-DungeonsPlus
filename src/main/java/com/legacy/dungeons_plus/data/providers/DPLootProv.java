package com.legacy.dungeons_plus.data.providers;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.registry.DPItems;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.mojang.datafixers.util.Pair;

import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

public class DPLootProv extends LootTableProvider
{
	public DPLootProv(DataGenerator dataGen)
	{
		super(dataGen);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
	{
		return ImmutableList.of(Pair.of(DPBlockLoot::new, LootContextParamSets.BLOCK), Pair.of(DPEntityLoot::new, LootContextParamSets.ENTITY), Pair.of(DPChestLoot::new, LootContextParamSets.CHEST));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context)
	{
		// Not validating anything because entity loot tables will fail since the vanilla one technically doesn't exist
		//map.forEach((location, table) -> LootTables.validate(context, location, table));
	}

	@Override
	public String getName()
	{
		return "Dungeons Plus Loot Tables";
	}

	private class DPChestLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>, LootPoolUtil
	{
		@Override
		public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			this.tower(consumer);
			this.reanimatedRuins(consumer);
			this.leviathan(consumer);
			this.snowyTemple(consumer);
			this.warpedGarden(consumer);

			this.soulPrison(consumer);
			this.endRuins(consumer);
		}

		private void tower(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.Tower.CHEST_COMMON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.BONE, 1, 3).setWeight(1),
							basicEntry(Items.GUNPOWDER, 1, 2).setWeight(1),
							basicEntry(Items.ROTTEN_FLESH, 2, 4).setWeight(1),
							basicEntry(Items.STRING, 1, 3).setWeight(1),
							basicEntry(Items.FEATHER, 1, 2).setWeight(1),
							basicEntry(Items.FLINT, 1, 2).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 6)),
					poolOf(ImmutableList.of(
							basicEntry(Items.IRON_NUGGET, 1, 3).setWeight(3),
							basicEntry(Items.REDSTONE, 1, 3).setWeight(2),
							basicEntry(Items.LAPIS_LAZULI, 2, 4).setWeight(1),
							basicEntry(Items.BOOK, 1, 3).setWeight(2),
							basicEntry(Items.GOLD_INGOT, 1, 2).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 3)),
					poolOf(ImmutableList.of(
							basicEntry(Items.INFESTED_STONE_BRICKS, 1, 3).setWeight(3),
							basicEntry(Items.BOOK, 1, 2).setWeight(2),
							basicEntry(Items.ENDER_PEARL, 1, 2).setWeight(1),
							basicEntry(Items.SADDLE).setWeight(2),
							basicEntry(Items.NAME_TAG).setWeight(1),
							basicEntry(Items.BOOK).setWeight(1).apply(enchant())
						)).setRolls(UniformGenerator.between(0, 2)))));
			
			consumer.accept(DPLoot.Tower.CHEST_BARREL, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.SUGAR, 1, 3).setWeight(2),
							basicEntry(Items.BUCKET).setWeight(1),
							basicEntry(Items.POTION).setWeight(1).apply(SetPotionFunction.setPotion(Potions.WATER)),
							basicEntry(Items.WHEAT, 1, 4).setWeight(2),
							basicEntry(Items.BREAD, 1, 2).setWeight(2),
							basicEntry(Items.STICK, 2, 3).setWeight(2),
							basicEntry(Items.WOODEN_SWORD).setWeight(1),
							basicEntry(Items.IRON_NUGGET, 2, 5).setWeight(1),
							basicEntry(Items.COBBLESTONE, 2, 4).setWeight(3),
							basicEntry(Items.BOWL).setWeight(1),
							basicEntry(Items.RED_MUSHROOM, 1, 2).setWeight(1),
							basicEntry(Items.BROWN_MUSHROOM, 2, 3).setWeight(2)
						)).setRolls(UniformGenerator.between(3, 7)))));
			
			consumer.accept(DPLoot.Tower.CHEST_VEX, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.FEATHER, 1, 2).setWeight(1),
							basicEntry(Items.IRON_NUGGET, 4, 9).setWeight(1),
							basicEntry(Items.IRON_INGOT, 1, 5).setWeight(1),
							basicEntry(Items.DARK_OAK_SAPLING, 1, 3).setWeight(1),
							basicEntry(Items.LEATHER, 1, 3).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 5)),
					poolOf(ImmutableList.of(
							basicEntry(Items.LEATHER_HORSE_ARMOR).setWeight(2),
							basicEntry(Items.GOLD_INGOT, 2, 4).setWeight(5),
							basicEntry(Items.PHANTOM_MEMBRANE, 1, 3).setWeight(4),
							basicEntry(Items.IRON_AXE).setWeight(1),
							basicEntry(Items.IRON_AXE).setWeight(1),
							basicEntry(Items.IRON_SHOVEL).setWeight(1),
							basicEntry(Items.IRON_HOE).setWeight(1),
							basicEntry(Items.IRON_PICKAXE).setWeight(1),
							basicEntry(Items.IRON_SWORD).setWeight(1),
							basicEntry(Items.SPYGLASS).setWeight(2),
							basicEntry(Items.SOUL_SAND, 2, 3).setWeight(4)
						)).setRolls(UniformGenerator.between(2, 4)))));

			consumer.accept(DPLoot.Tower.CHEST_VEX_MAP, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.MAP).setWeight(1).apply(map(DPTags.StructureTags.ON_REANIMATED_RUINS_MAPS)).apply(mapName(DPStructures.REANIMATED_RUINS))
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.Tower.CHEST_VEX)
						)))));
			//@formatter:on
		}

		private void reanimatedRuins(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_COMMON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.BONE, 1, 3).setWeight(1),
							basicEntry(Items.GUNPOWDER, 1, 2).setWeight(1),
							basicEntry(Items.ROTTEN_FLESH, 2, 4).setWeight(1),
							basicEntry(Items.STRING, 1, 3).setWeight(1),
							basicEntry(Items.FEATHER, 1, 2).setWeight(1),
							basicEntry(Items.STONE_BRICKS, 3, 5).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 6)),
					poolOf(ImmutableList.of(
							basicEntry(Items.IRON_NUGGET, 1, 3).setWeight(3),
							basicEntry(Items.REDSTONE, 1, 3).setWeight(2),
							basicEntry(Items.HONEYCOMB, 1, 2).setWeight(1),
							basicEntry(Items.BOOK, 1, 3).setWeight(2),
							basicEntry(Items.GOLD_NUGGET, 2, 4).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 3)),
					poolOf(ImmutableList.of(
							basicEntry(Items.BREAD, 1, 3).setWeight(4),
							basicEntry(Items.ENDER_PEARL, 0, 1).setWeight(2),
							basicEntry(Items.SADDLE).setWeight(2),
							basicEntry(Items.NAME_TAG).setWeight(1),
							basicEntry(Items.BOOK).setWeight(1).apply(enchant())
						)).setRolls(UniformGenerator.between(0, 2)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_DESERT, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.SAND, 1, 2).setWeight(1),
							basicEntry(Items.BONE, 1, 2).setWeight(1),
							basicEntry(Items.ROTTEN_FLESH, 1, 5).setWeight(1),
							basicEntry(Items.DEAD_BUSH).setWeight(1),
							basicEntry(Items.GOLD_NUGGET, 2, 5).setWeight(1),
							basicEntry(Items.IRON_INGOT, 1, 3).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 5)),
					poolOf(ImmutableList.of(
							basicEntry(Items.RED_SAND, 2, 3).setWeight(2),
							basicEntry(Items.GLASS_BOTTLE).setWeight(1),
							basicEntry(Items.GOLD_INGOT, 2, 4).setWeight(4),
							basicEntry(Items.REDSTONE, 1, 3).setWeight(4),
							basicEntry(Items.COAL, 1, 3).setWeight(4),
							basicEntry(Items.IRON_SHOVEL).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.GOLDEN_APPLE).setWeight(1),
							basicEntry(Items.BUCKET).setWeight(2),
							basicEntry(Items.BOOK).setWeight(5).apply(enchant()),
							basicEntry(Items.GOLD_NUGGET).setWeight(5)
						)).setRolls(UniformGenerator.between(1, 3)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_DESERT_MAP, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.MAP).setWeight(1).apply(map(DPTags.StructureTags.ON_LEVIATHAN_MAPS)).apply(mapName(DPStructures.LEVIATHAN))
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.ReanimatedRuins.CHEST_DESERT)
						)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_FROZEN, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.ARROW, 1, 5).setWeight(3),
							basicEntry(Items.BONE, 2, 5).setWeight(3),
							basicEntry(Items.SNOWBALL, 1, 3).setWeight(2),
							basicEntry(Items.FLINT, 1, 2).setWeight(1),
							basicEntry(Items.FEATHER, 1, 2).setWeight(1),
							basicEntry(Items.STRING, 1, 2).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 5)),
					poolOf(ImmutableList.of(
							basicEntry(Items.ICE, 2, 3).setWeight(2),
							basicEntry(Items.TIPPED_ARROW, 2, 4).setWeight(4).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)),
							basicEntry(Items.IRON_INGOT, 2, 4).setWeight(4),
							basicEntry(Items.REDSTONE, 1, 3).setWeight(4),
							basicEntry(Items.COAL, 1, 3).setWeight(4),
							basicEntry(Items.BOW).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.BOW).setWeight(2).apply(enchant()),
							basicEntry(Items.CHAINMAIL_CHESTPLATE).setWeight(2),
							basicEntry(Items.BOOK).setWeight(5).apply(enchant()),
							basicEntry(Items.MUSIC_DISC_WAIT).setWeight(1)
						)).setRolls(UniformGenerator.between(1, 3)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_FROZEN_MAP, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.MAP).setWeight(1).apply(map(DPTags.StructureTags.ON_SNOWY_TEMPLE_MAPS)).apply(mapName(DPStructures.SNOWY_TEMPLE))
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.ReanimatedRuins.CHEST_FROZEN)
						)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_MOSSY, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.ROTTEN_FLESH, 1, 5).setWeight(3),
							basicEntry(Items.GLOW_BERRIES, 2, 5).setWeight(3),
							basicEntry(Items.SMALL_DRIPLEAF, 1, 3).setWeight(2),
							basicEntry(Items.RAW_COPPER, 1, 2).setWeight(1),
							basicEntry(Items.CLAY_BALL, 1, 2).setWeight(1),
							basicEntry(Items.STRING, 1, 2).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 5)),
					poolOf(ImmutableList.of(
							basicEntry(Items.MOSS_BLOCK, 2, 3).setWeight(2),
							basicEntry(Items.SLIME_BALL, 2, 4).setWeight(4),
							basicEntry(Items.GOLD_NUGGET, 1, 4).setWeight(3),
							basicEntry(Items.RAW_COPPER, 1, 2).setWeight(3),
							basicEntry(Items.COAL, 1, 3).setWeight(4),
							basicEntry(Items.MINECART).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.RAW_COPPER_BLOCK, 0, 2).setWeight(5),
							basicEntry(Items.WATER_BUCKET).setWeight(1),
							basicEntry(Items.BOOK).setWeight(2).apply(enchant()),
							basicEntry(Items.IRON_PICKAXE).setWeight(1).apply(enchant(Enchantments.BLOCK_EFFICIENCY, Enchantments.UNBREAKING, Enchantments.SILK_TOUCH, Enchantments.VANISHING_CURSE)).apply(setDamage(10, 60))
						)).setRolls(UniformGenerator.between(1, 3)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.CHEST_MOSSY_MAP, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.MAP).setWeight(1).apply(map(DPTags.StructureTags.ON_WARPED_GARDEN_MAPS)).apply(mapName(DPStructures.WARPED_GARDEN))
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.ReanimatedRuins.CHEST_MOSSY)
						)))));
			
			//@formatter:on
		}

		private void leviathan(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.Leviathan.CHEST_COMMON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.BONE, 1, 2).setWeight(10),
							basicEntry(Items.RABBIT_HIDE, 1, 2).setWeight(10),
							basicEntry(Items.ROTTEN_FLESH, 1, 5).setWeight(10),
							basicEntry(Items.GOLD_NUGGET).setWeight(10),
							basicEntry(Items.SAND, 1, 2).setWeight(10),
							basicEntry(Items.DEAD_BUSH).setWeight(1)
						)).setRolls(UniformGenerator.between(4, 7)),
					poolOf(ImmutableList.of(
							basicEntry(Items.QUARTZ, 1, 5).setWeight(1),
							basicEntry(Items.LAPIS_LAZULI, 1, 5).setWeight(2),
							basicEntry(Items.GOLD_INGOT, 2, 4).setWeight(5),
							basicEntry(Items.EMERALD, 1, 3).setWeight(5),
							basicEntry(Items.GOLDEN_SHOVEL).setWeight(3).apply(enchant())
						)).setRolls(UniformGenerator.between(3, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.GOLDEN_APPLE).setWeight(1),
							basicEntry(Items.EXPERIENCE_BOTTLE, 1, 2).setWeight(3),
							basicEntry(Items.GOLDEN_HORSE_ARMOR).setWeight(1),
							basicEntry(Items.NAME_TAG).setWeight(1),
							basicEntry(Items.BOOK).setWeight(3).apply(enchant())
						)).setRolls(UniformGenerator.between(2, 3)))));
			
			consumer.accept(DPLoot.Leviathan.CHEST_RARE, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(DPItems.LEVIATHAN_BLADE.get()).setWeight(1)
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.Leviathan.CHEST_COMMON)
						)))));
			//@formatter:on
		}

		private void snowyTemple(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.SnowyTemple.CHEST_COMMON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.ICE, 1, 2).setWeight(1),
							basicEntry(Items.SNOWBALL, 1, 2).setWeight(1),
							basicEntry(Items.ARROW, 1, 4).setWeight(1),
							basicEntry(Items.BONE, 1, 4).setWeight(1),
							basicEntry(Items.FLINT, 1, 2).setWeight(1),
							basicEntry(Items.FEATHER, 1, 2).setWeight(1)
						)).setRolls(UniformGenerator.between(4, 7)),
					poolOf(ImmutableList.of(
							basicEntry(Items.PACKED_ICE, 1, 5).setWeight(1),
							basicEntry(Items.TIPPED_ARROW, 2, 4).setWeight(2).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)),
							basicEntry(Items.IRON_INGOT, 1, 3).setWeight(5),
							basicEntry(Items.LAPIS_LAZULI, 1, 3).setWeight(5),
							basicEntry(Items.BOW).setWeight(1).apply(enchant()),
							basicEntry(Items.POWDER_SNOW_BUCKET, 1, 2).setWeight(1)
						)).setRolls(UniformGenerator.between(3, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.EXPERIENCE_BOTTLE, 1, 2).setWeight(3),
							basicEntry(Items.CHAINMAIL_CHESTPLATE).setWeight(1),
							basicEntry(Items.CHAINMAIL_LEGGINGS).setWeight(1),
							basicEntry(Items.BOOK).setWeight(3).apply(enchant()),
							basicEntry(Items.LEATHER_BOOTS).setWeight(1).apply(enchant(Enchantments.FROST_WALKER, Enchantments.UNBREAKING, Enchantments.VANISHING_CURSE))
						)).setRolls(UniformGenerator.between(2, 3)))));

			consumer.accept(DPLoot.SnowyTemple.CHEST_RARE, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(DPItems.FROSTED_COWL.get()).setWeight(1)
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.SnowyTemple.CHEST_COMMON)
						)))));
			//@formatter:on
		}

		private void warpedGarden(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.WarpedGarden.CHEST_COMMON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.ROTTEN_FLESH, 1, 3).setWeight(1),
							basicEntry(Items.WARPED_ROOTS, 1, 2).setWeight(1),
							basicEntry(Items.WARPED_FUNGUS, 1, 2).setWeight(1),
							basicEntry(Items.GOLD_NUGGET, 1, 3).setWeight(1),
							basicEntry(Items.QUARTZ, 1, 3).setWeight(1)
						)).setRolls(UniformGenerator.between(4, 7)),
					poolOf(ImmutableList.of(
							basicEntry(Items.NETHERRACK, 1, 2).setWeight(1),
							basicEntry(Items.POTION, 1, 3).setWeight(1).apply(SetPotionFunction.setPotion(Potions.WATER)),
							basicEntry(Items.SCUTE, 1, 3).setWeight(1),
							basicEntry(Items.WATER_BUCKET).setWeight(1),
							basicEntry(Items.GOLD_INGOT, 1, 3).setWeight(3)
						)).setRolls(UniformGenerator.between(3, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.NAUTILUS_SHELL).setWeight(3),
							basicEntry(Items.NETHER_GOLD_ORE, 5, 11).setWeight(10),
							basicEntry(Items.PUFFERFISH).setWeight(7),
							basicEntry(Items.BOOK).setWeight(5).apply(enchant()),
							basicEntry(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 3)))));
			
			consumer.accept(DPLoot.WarpedGarden.CHEST_RARE, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(DPItems.WARPED_AXE.get()).setWeight(1)
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.WarpedGarden.CHEST_COMMON)
						)))));
			//@formatter:on
		}

		private void soulPrison(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.SoulPrison.CHEST_COMMON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.BONE, 1, 2).setWeight(1),
							basicEntry(Items.ARROW, 1, 2).setWeight(1),
							basicEntry(Items.FIRE_CHARGE, 1, 2).setWeight(1),
							basicEntry(Items.GOLD_INGOT, 1, 3).setWeight(2),
							basicEntry(Items.GUNPOWDER, 1, 2).setWeight(2)
						)).setRolls(UniformGenerator.between(4, 7)),
					poolOf(ImmutableList.of(
							basicEntry(Items.GOLDEN_PICKAXE).setWeight(1),
							basicEntry(Items.SOUL_LANTERN, 1, 2).setWeight(1).apply(SetPotionFunction.setPotion(Potions.WATER)),
							basicEntry(Items.SOUL_SOIL, 1, 3).setWeight(2),
							basicEntry(Items.GLOWSTONE, 1, 2).setWeight(2),
							basicEntry(Items.BONE_BLOCK, 1, 2).setWeight(3),
							basicEntry(Items.GHAST_TEAR, 1, 2).setWeight(3)
						)).setRolls(UniformGenerator.between(3, 4)),
					poolOf(ImmutableList.of(
							basicEntry(Items.DIAMOND, 1, 2).setWeight(3),
							basicEntry(Items.MUSIC_DISC_11).setWeight(4),
							basicEntry(Items.BOOK).setWeight(4).apply(enchant()),
							basicEntry(Items.GOLDEN_CARROT, 2, 4).setWeight(5),
							LootTableReference.lootTableReference(DPLoot.SoulPrison.CHEST_GOLDEN_ARMOR)
						)).setRolls(UniformGenerator.between(2, 3)))));
			
			consumer.accept(DPLoot.SoulPrison.CHEST_GOLDEN_ARMOR, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.GOLDEN_HELMET).setWeight(2).apply(enchant(Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.UNBREAKING)),
							basicEntry(Items.GOLDEN_CHESTPLATE).setWeight(2).apply(enchant(Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.UNBREAKING)),
							basicEntry(Items.GOLDEN_HELMET).setWeight(1).apply(enchant()),
							basicEntry(Items.GOLDEN_CHESTPLATE).setWeight(1).apply(enchant())
						)).setRolls(UniformGenerator.between(1, 2)))));
			
			consumer.accept(DPLoot.SoulPrison.CHEST_RARE, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(DPItems.SOUL_CANNON.get()).setWeight(1)
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(DPLoot.SoulPrison.CHEST_COMMON)
						)))));
			//@formatter:on
		}

		private void endRuins(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{

		}
	}

	private class DPEntityLoot extends EntityLoot implements LootPoolUtil
	{
		@Override
		public void accept(BiConsumer<ResourceLocation, Builder> consumer)
		{
			//@formatter:off
			consumer.accept(DPLoot.Tower.ENTITY_ZOMBIE, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.ZOMBIE.getDefaultLootTable())
						)).when(LootItemRandomChanceCondition.randomChance(0.5F)))));
			
			consumer.accept(DPLoot.Tower.ENTITY_SKELETON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.SKELETON.getDefaultLootTable())
						)).when(LootItemRandomChanceCondition.randomChance(0.5F)))));
			
			consumer.accept(DPLoot.Tower.ENTITY_SPIDER, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.SPIDER.getDefaultLootTable())
						)).when(LootItemRandomChanceCondition.randomChance(0.5F)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.ENTITY_ZOMBIE, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.ZOMBIE.getDefaultLootTable())
						)).when(LootItemRandomChanceCondition.randomChance(0.25F)))));
			
			consumer.accept(DPLoot.ReanimatedRuins.ENTITY_SKELETON, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.SKELETON.getDefaultLootTable())
						)).when(LootItemRandomChanceCondition.randomChance(0.25F)))));
			
			consumer.accept(DPLoot.Leviathan.ENTITY_HUSK, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.SAND, 0, 2).setWeight(1).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.HUSK.getDefaultLootTable())
						)))));
			
			consumer.accept(DPLoot.SnowyTemple.ENTITY_STRAY, tableOf(ImmutableList.of(
					poolOf(ImmutableList.of(
							basicEntry(Items.ICE, 0, 2).setWeight(128).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1))),
							basicEntry(Items.PACKED_ICE, 0, 2).setWeight(1).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
						)),
					poolOf(ImmutableList.of(
							LootTableReference.lootTableReference(EntityType.STRAY.getDefaultLootTable())
						)))));
			
			//@formatter:on
		}

		/*private LootPool.Builder lootingPool(ItemLike item, int min, int max, int minLooting, int maxLooting)
		{
			return basicPool(item, min, max).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(minLooting, maxLooting)));
		}*/

		@Override
		protected Iterable<EntityType<?>> getKnownEntities()
		{
			return ForgeRegistries.ENTITIES.getValues().stream().filter(e -> e.getRegistryName().getNamespace().contains(DungeonsPlus.MODID))::iterator;
		}
	}

	private class DPBlockLoot extends BlockLoot implements LootPoolUtil
	{
		@Override
		protected void addTables()
		{
			blocks().forEach(block ->
			{
				/*if (block == DPBlocks.DYNAMIC_SPAWNER)
					this.add(block, noDrop());
				else*/
				this.dropSelf(block);
			});
		}

		@Override
		protected Iterable<Block> getKnownBlocks()
		{
			return blocks()::iterator;
		}

		private Stream<Block> blocks()
		{
			return ForgeRegistries.BLOCKS.getValues().stream().filter(b -> b.getRegistryName().getNamespace().equals(DungeonsPlus.MODID) && !b.getLootTable().equals(BuiltInLootTables.EMPTY));
		}
	}

	/**
	 * Interface with basic loot table generators
	 * 
	 * @author David
	 *
	 */
	public interface LootPoolUtil
	{
		/**
		 * Creates a table from the given loot pools.
		 * 
		 * @param pools
		 * @return
		 */
		default LootTable.Builder tableOf(List<LootPool.Builder> pools)
		{
			LootTable.Builder table = LootTable.lootTable();
			pools.forEach(pool -> table.withPool(pool));
			return table;
		}

		/**
		 * Creates a table from the given loot pool.
		 * 
		 * @param pool
		 * @return
		 */
		default LootTable.Builder tableOf(LootPool.Builder pool)
		{
			return LootTable.lootTable().withPool(pool);
		}

		/**
		 * Creates a loot pool with the given item. Gives an amount between the min and
		 * max.
		 * 
		 * @param item
		 * @param min
		 * @param max
		 * @return
		 */
		default LootPool.Builder basicPool(ItemLike item, int min, int max)
		{
			return LootPool.lootPool().add(basicEntry(item, min, max));
		}

		/**
		 * Creates a loot pool with the given item. Will only give one item.
		 * 
		 * @param item
		 * @return
		 */
		default LootPool.Builder basicPool(ItemLike item)
		{
			return LootPool.lootPool().add(basicEntry(item));
		}

		/**
		 * Creates a loot pool that will give a random item from the list.
		 * 
		 * @param items
		 * @return
		 */
		default LootPool.Builder randItemPool(List<ItemLike> items)
		{
			return poolOf(items.stream().map((i) -> basicEntry(i)).collect(Collectors.toList()));
		}

		/**
		 * Creates a loot pool with multiple entries. One of these entries will be
		 * picked at random each time the pool rolls.
		 * 
		 * @param lootEntries
		 * @return
		 */
		default LootPool.Builder poolOf(List<LootPoolEntryContainer.Builder<?>> lootEntries)
		{
			LootPool.Builder pool = LootPool.lootPool();
			lootEntries.forEach(entry -> pool.add(entry));
			return pool;
		}

		/**
		 * Creates a loot entry for the given item. Gives an amount between the min and
		 * max.
		 * 
		 * @param item
		 * @param min
		 * @param max
		 * @return
		 */
		default LootItem.Builder<?> basicEntry(ItemLike item, int min, int max)
		{
			return basicEntry(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
		}

		/**
		 * Creates a loot entry for the given item. Will only give one item.
		 * 
		 * @param item
		 * @return
		 */
		default LootItem.Builder<?> basicEntry(ItemLike item)
		{
			return LootItem.lootTableItem(item);
		}

		/**
		 * Sets the damage of the item (percentage)
		 * 
		 * @param min
		 *            0 - 100
		 * @param max
		 *            0 - 100
		 * @return
		 */
		default LootItemConditionalFunction.Builder<?> setDamage(int min, int max)
		{
			return SetItemDamageFunction.setDamage(UniformGenerator.between(min / 100F, max / 100F));
		}

		/**
		 * Cooks the item if the predicate passes
		 * 
		 * @param predicate
		 * @return
		 */
		default LootItemConditionalFunction.Builder<?> smeltItem(EntityPredicate.Builder predicate)
		{
			return SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, predicate));
		}

		/**
		 * Enchants the item randomly between the levels provided
		 * 
		 * @param minLevel
		 * @param maxLevel
		 * @return
		 */
		default LootItemConditionalFunction.Builder<?> enchant(int minLevel, int maxLevel)
		{
			return EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(minLevel, maxLevel));
		}

		/**
		 * Enchants the item randomly with the enchantments passed
		 * 
		 * @param enchantments
		 * @return
		 */
		default LootItemConditionalFunction.Builder<?> enchant(Enchantment... enchantments)
		{
			EnchantRandomlyFunction.Builder builder = EnchantRandomlyFunction.randomEnchantment();
			for (Enchantment enchantment : enchantments)
				builder.withEnchantment(enchantment);
			return builder;
		}

		default LootItemConditionalFunction.Builder<?> map(TagKey<ConfiguredStructureFeature<?, ?>> structure)
		{
			return ExplorationMapFunction.makeExplorationMap().setDestination(structure).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte) 1).setSkipKnownStructures(false);
		}

		default LootItemConditionalFunction.Builder<?> mapName(StructureRegistrar<?, ?> structure)
		{
			return SetNameFunction.setName(new TranslatableComponent(DPLangProvider.mapName(structure)));
		}

		/**
		 * Sets the nbt of the item
		 * 
		 * @param nbt
		 * @return
		 */
		@SuppressWarnings("deprecation")
		default LootItemConditionalFunction.Builder<?> setNbt(Consumer<CompoundTag> nbt)
		{
			return SetNbtFunction.setTag(Util.make(new CompoundTag(), nbt));
		}
	}
}
