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
import com.legacy.dungeons_plus.data.DPLoot;
import com.legacy.dungeons_plus.registry.DPBlocks;
import com.mojang.datafixers.util.Pair;

import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

public class DPLootProv extends LootTableProvider
{
	// Chests

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
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker)
	{
		map.forEach((location, table) -> LootTables.validate(validationtracker, location, table));
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
			this.warpedGarden(consumer);
		}

		private void tower(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			
		}
		
		// TODO bigger dungeon replacement
		private void biggerDungeon(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			
		}
		
		private void leviathan(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			
		}
		
		private void snowyTemple(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			
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
							basicEntry(Items.BOOK).setWeight(5).apply(EnchantRandomlyFunction.randomEnchantment()),
							basicEntry(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1)
						)).setRolls(UniformGenerator.between(2, 3)))));
			
			//@formatter:on
		}
		
		private void soulPrison(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			
		}
		
		private void endRuins(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
		{
			
		}
	}

	private class DPEntityLoot extends EntityLoot implements LootPoolUtil
	{
		@Override
		protected void addTables()
		{
			// TODO entity loot
		}

		private LootPool.Builder lootingPool(ItemLike item, int min, int max, int minLooting, int maxLooting)
		{
			return basicPool(item, min, max).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(minLooting, maxLooting)));
		}

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
				if (block == DPBlocks.DYNAMIC_SPAWNER)
					this.add(block, noDrop());
				else
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
			EnchantRandomlyFunction.Builder func = new EnchantRandomlyFunction.Builder();
			for (Enchantment enchantment : enchantments)
				func.withEnchantment(enchantment);
			return func;
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
