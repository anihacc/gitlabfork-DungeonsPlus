package com.legacy.dungeons_plus.loot;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.legacy.dungeons_plus.registry.DPLoot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

public class NamedLootItem extends LootPoolSingletonContainer
{
	private final ResourceLocation itemName;
	@Nullable
	private Item item = null;

	NamedLootItem(ResourceLocation itemName, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions)
	{
		super(weight, quality, conditions, functions);
		this.itemName = itemName;
	}

	@Override
	public LootPoolEntryType getType()
	{
		return DPLoot.NAMED_ITEM.get();
	}

	@Override
	public void createItemStack(Consumer<ItemStack> stackAdder, LootContext context)
	{
		stackAdder.accept(new ItemStack(this.getItem()));
	}

	public Item getItem()
	{
		if (this.item == null)
			this.item = ForgeRegistries.ITEMS.containsKey(this.itemName) ? ForgeRegistries.ITEMS.getValue(this.itemName) : Items.AIR;
		return this.item;
	}

	public static LootPoolSingletonContainer.Builder<?> lootTableItem(String namespace, String path)
	{
		return lootTableItem(new ResourceLocation(namespace, path));
	}

	public static LootPoolSingletonContainer.Builder<?> lootTableItem(ResourceLocation namedItem)
	{
		return simpleBuilder((weight, quality, conditions, functions) ->
		{
			return new NamedLootItem(namedItem, weight, quality, conditions, functions);
		});
	}

	public static class Serializer extends LootPoolSingletonContainer.Serializer<NamedLootItem>
	{
		@Override
		public void serializeCustom(JsonObject json, NamedLootItem instance, JsonSerializationContext context)
		{
			super.serializeCustom(json, instance, context);
			json.addProperty("name", instance.itemName.toString());
		}

		@Override
		protected NamedLootItem deserialize(JsonObject json, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions)
		{
			ResourceLocation namedItem = new ResourceLocation(GsonHelper.getAsString(json, "name"));
			if (!ForgeRegistries.ITEMS.containsKey(namedItem))
				throw new IllegalArgumentException("Can't deserialize unknown item " + namedItem);
			return new NamedLootItem(namedItem, weight, quality, conditions, functions);
		}
	}
}