package com.legacy.dungeons_plus.items;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

// Helmet item that prevents slowness and protects against freezing
public class FrostedCowlItem extends ArmorItem implements DyeableLeatherItem, DPItem
{
	public static final String FREEZE_KEY = "freeze_immune", SLOWNESS_KEY = "slowness_immune";
	
	public FrostedCowlItem(ArmorMaterial material, EquipmentSlot slot, Properties properties)
	{
		super(material, slot, properties);
	}

	@Override
	public int getColor(ItemStack stack)
	{
		CompoundTag compoundnbt = stack.getTagElement("display");
		return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 0x576C6D; // From the stray texture
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag showAdvanced)
	{
		super.appendHoverText(stack, level, tooltip, showAdvanced);
		tooltip.addAll(this.getDescription(stack, FREEZE_KEY, SLOWNESS_KEY));
	}
}
