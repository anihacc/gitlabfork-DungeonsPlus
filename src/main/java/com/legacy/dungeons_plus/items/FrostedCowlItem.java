package com.legacy.dungeons_plus.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

// Helmet item that prevents slowness and protects against freezing
public class FrostedCowlItem extends ArmorItem implements DyeableLeatherItem
{
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
}
