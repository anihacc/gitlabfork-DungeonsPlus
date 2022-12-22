package com.legacy.dungeons_plus.data.advancement;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class ThrownItemHitBlockTrigger extends SimpleCriterionTrigger<ThrownItemHitBlockTrigger.TriggerInstance>
{
	static final ResourceLocation ID = DungeonsPlus.locate("thrown_item_hit_block");
	private static final String ITEM_KEY = "item", BLOCK_KEY = "block", STATE_KEY = "state";

	public static final ThrownItemHitBlockTrigger TRIGGER = new ThrownItemHitBlockTrigger();

	public ResourceLocation getId()
	{
		return ID;
	}

	public ThrownItemHitBlockTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext context)
	{
		ItemPredicate itemPredicate = null;
		if (json.has(ITEM_KEY))
		{
			itemPredicate = ItemPredicate.fromJson(json.get(ITEM_KEY));
		}
		Block block = deserializeBlock(json);
		StatePropertiesPredicate statePredicate = StatePropertiesPredicate.fromJson(json.get(STATE_KEY));
		if (block != null)
		{
			statePredicate.checkState(block.getStateDefinition(), s ->
			{
				throw new JsonSyntaxException("Block " + block + " has no property " + s);
			});
		}

		return new ThrownItemHitBlockTrigger.TriggerInstance(entityPredicate, itemPredicate, block, statePredicate);
	}

	@Nullable
	private static Block deserializeBlock(JsonObject json)
	{
		if (json.has(BLOCK_KEY))
		{
			ResourceLocation blockID = new ResourceLocation(GsonHelper.getAsString(json, BLOCK_KEY));
			return BuiltInRegistries.BLOCK.getOptional(blockID).orElseThrow(() ->
			{
				return new JsonSyntaxException("Unknown block type '" + blockID + "'");
			});
		}
		else
		{
			return null;
		}
	}

	public void trigger(ServerPlayer player, ItemStack stack, BlockState state)
	{
		this.trigger(player, trigger ->
		{
			return trigger.matches(stack, state);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance
	{
		@Nullable
		private final ItemPredicate item;
		@Nullable
		private final Block block;
		private final StatePropertiesPredicate state;

		public TriggerInstance(EntityPredicate.Composite entityPredicate, @Nullable ItemPredicate itemPredicate, @Nullable Block block, StatePropertiesPredicate statePredicate)
		{
			super(ThrownItemHitBlockTrigger.ID, entityPredicate);
			this.item = itemPredicate;
			this.block = block;
			this.state = statePredicate;
		}

		public static ThrownItemHitBlockTrigger.TriggerInstance of(@Nullable ItemPredicate itemPredicate, @Nullable Block block)
		{
			return new ThrownItemHitBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, itemPredicate, block, StatePropertiesPredicate.ANY);
		}

		public static ThrownItemHitBlockTrigger.TriggerInstance hitsBlock(@Nullable Block block)
		{
			return of(ItemPredicate.ANY, block);
		}

		public static ThrownItemHitBlockTrigger.TriggerInstance withItem(@Nullable ItemPredicate itemPredicate)
		{
			return of(itemPredicate, null);
		}

		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			if (this.item != null)
			{
				json.add(ITEM_KEY, this.item.serializeToJson());
			}
			if (this.block != null)
			{
				json.addProperty(BLOCK_KEY, BuiltInRegistries.BLOCK.getKey(this.block).toString());
			}

			json.add(STATE_KEY, this.state.serializeToJson());
			return json;
		}

		public boolean matches(ItemStack stack, BlockState state)
		{
			boolean itemMatches = this.item == null ? true : this.item.matches(stack);
			boolean blockMatches = this.block == null ? true : state.is(block);
			boolean stateMatches = this.state == null ? true : this.state.matches(state);
			return itemMatches && blockMatches && stateMatches;
		}
	}
}