package com.legacy.dungeons_plus.registry;

import java.util.function.Supplier;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.items.FrostedCowlItem;
import com.legacy.dungeons_plus.items.LeviathanBladeItem;
import com.legacy.dungeons_plus.items.SoulCannonItem;
import com.legacy.dungeons_plus.items.WarpedAxeItem;
import com.legacy.structure_gel.api.registry.registrar.Registrar;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.util.Lazy;

public class DPItems
{
	public static final RegistrarHandler<Item> HANDLER = RegistrarHandler.getOrCreate(Registries.ITEM, DungeonsPlus.MODID);

	public static final Registrar.Static<Item> FROSTED_COWL = HANDLER.createStatic("frosted_cowl", () -> new FrostedCowlItem(DPArmors.STRAY, EquipmentSlot.HEAD, new Item.Properties()));
	public static final Registrar.Static<Item> LEVIATHAN_BLADE = HANDLER.createStatic("leviathan_blade", () -> new LeviathanBladeItem(DPTiers.LEVIATHAN, 3, -2.6F, new Item.Properties()));
	public static final Registrar.Static<Item> WARPED_AXE = HANDLER.createStatic("warped_axe", () -> new WarpedAxeItem(DPTiers.WARPED_GOLD, 7, -3.1F, new Item.Properties().durability(312)));
	public static final Registrar.Static<Item> SOUL_CANNON = HANDLER.createStatic("soul_cannon", () -> new SoulCannonItem(new Item.Properties().durability(250)));

	public static interface DPTiers
	{
		Tier LEVIATHAN = new ForgeTier(2, 1000, 7.0F, 2.5F, 12, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.BONE_BLOCK));
		Tier WARPED_GOLD = new ForgeTier(1, 275, 10.0F, 1.0F, 18, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.GOLD_INGOT));
	}

	public static interface DPArmors
	{
		ArmorMaterial STRAY = new DPArmorMaterial(DungeonsPlus.locate("stray").toString(), 10, new int[] { 1, 2, 4, 2 }, 12, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ItemTags.WOOL));
	}

	private static final class DPArmorMaterial implements ArmorMaterial
	{
		private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
		private final String name;
		private final int durabilityMultiplier;
		private final int[] slotProtections;
		private final int enchantmentValue;
		private final SoundEvent sound;
		private final float toughness;
		private final float knockbackResistance;
		private final Lazy<Ingredient> repairIngredient;

		private DPArmorMaterial(String name, int durabilityModifier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient)
		{
			this.name = name;
			this.durabilityMultiplier = durabilityModifier;
			this.slotProtections = slotProtections;
			this.enchantmentValue = enchantmentValue;
			this.sound = sound;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairIngredient = Lazy.of(repairIngredient);
		}

		@Override
		public int getDurabilityForSlot(EquipmentSlot slot)
		{
			return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
		}

		@Override
		public int getDefenseForSlot(EquipmentSlot slot)
		{
			return this.slotProtections[slot.getIndex()];
		}

		@Override
		public int getEnchantmentValue()
		{
			return this.enchantmentValue;
		}

		@Override
		public SoundEvent getEquipSound()
		{
			return this.sound;
		}

		@Override
		public Ingredient getRepairIngredient()
		{
			return this.repairIngredient.get();
		}

		@Override
		public String getName()
		{
			return this.name;
		}

		@Override
		public float getToughness()
		{
			return this.toughness;
		}

		@Override
		public float getKnockbackResistance()
		{
			return this.knockbackResistance;
		}
	}
}
