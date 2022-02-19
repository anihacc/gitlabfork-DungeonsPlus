package com.legacy.dungeons_plus.loot;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.legacy.dungeons_plus.registry.DPLoot;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class OptionalLootEntry extends LootPoolSingletonContainer
{
	@Nullable
	private final LootPoolEntryContainer optionalEntry;
	private final LootPoolEntryContainer defaultEntry;

	OptionalLootEntry(@Nullable LootPoolEntryContainer optionalEntry, LootPoolEntryContainer defaultEntry, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions)
	{
		super(weight, quality, conditions, functions);
		this.optionalEntry = optionalEntry;
		this.defaultEntry = defaultEntry;
	}

	@Override
	public LootPoolEntryType getType()
	{
		return DPLoot.OPTIONAL_ENTRY;
	}

	@Override
	public void createItemStack(Consumer<ItemStack> stackAdder, LootContext context)
	{
	}

	@Override
	public boolean expand(LootContext context, Consumer<LootPoolEntry> lootPoolConsumer)
	{
		if (this.optionalEntry != null)
			return this.optionalEntry.expand(context, lootPoolConsumer);
		else
			return this.defaultEntry.expand(context, lootPoolConsumer);
	}

	public static LootPoolSingletonContainer.Builder<?> of(LootPoolEntryContainer.Builder<?> optionalEntry, LootPoolEntryContainer.Builder<?> defaultEntry)
	{
		return simpleBuilder((weight, quality, conditions, functions) ->
		{
			return new OptionalLootEntry(optionalEntry.build(), defaultEntry.build(), weight, quality, conditions, functions);
		});
	}

	public static class Serializer extends LootPoolSingletonContainer.Serializer<OptionalLootEntry>
	{
		@Override
		public void serializeCustom(JsonObject json, OptionalLootEntry instance, JsonSerializationContext context)
		{
			super.serializeCustom(json, instance, context);
			if (instance.optionalEntry != null)
				json.add("optional_entry", context.serialize(instance.optionalEntry));
			json.add("default_entry", context.serialize(instance.defaultEntry));
		}

		@Override
		protected OptionalLootEntry deserialize(JsonObject json, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions)
		{
			LootPoolEntryContainer optionalEntry;
			try
			{
				optionalEntry = GsonHelper.getAsObject(json, "optional_entry", context, LootPoolEntryContainer.class);
			}
			catch (Exception e)
			{
				optionalEntry = null;
			}
			LootPoolEntryContainer defaultEntry = GsonHelper.getAsObject(json, "default_entry", context, LootPoolEntryContainer.class);

			return new OptionalLootEntry(optionalEntry, defaultEntry, weight, quality, conditions, functions);
		}
	}
}
