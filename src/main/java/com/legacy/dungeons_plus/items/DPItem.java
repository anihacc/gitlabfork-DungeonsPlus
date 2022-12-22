package com.legacy.dungeons_plus.items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface DPItem
{
	String HOLD_SHIFT_KEY = "item." + DungeonsPlus.MODID + ".info.hold_shift";
	@SuppressWarnings("deprecation")
	BiFunction<Item, String, Component> COMPONENTS = Util.memoize((i, s) ->
	{
		var opKey = BuiltInRegistries.ITEM.getResourceKey(i);
		if (opKey.isPresent())
		{
			var resourceKey = opKey.get();
			ResourceLocation location = resourceKey.location();
			return Component.translatable(Util.makeDescriptionId(resourceKey.registry().getPath().replace('/', '.'), new ResourceLocation(location.getNamespace(), location.getPath() + "." + s))).withStyle(ChatFormatting.GRAY);
		}
		else
		{
			return Component.literal("Unknown item registry name").withStyle(ChatFormatting.GRAY);
		}
	});

	default Item getThis()
	{
		return (Item) this;
	}
	
	default List<Component> getDescription(ItemStack stack, String... descriptionKeys)
	{
		List<Component> tooltip = new ArrayList<>(2);
		if (!net.minecraft.client.gui.screens.Screen.hasShiftDown())
		{
			tooltip.add(Component.translatable(HOLD_SHIFT_KEY).withStyle(ChatFormatting.GRAY));
		}
		else
		{
			for (var k : descriptionKeys)
				tooltip.add(Component.literal("• ").withStyle(ChatFormatting.GRAY).append(COMPONENTS.apply(this.getThis(), k)));
		}
		return tooltip;
	}
}
