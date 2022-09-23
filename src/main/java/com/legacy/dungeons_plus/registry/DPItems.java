package com.legacy.dungeons_plus.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.items.FrostedCowlItem;
import com.legacy.dungeons_plus.items.LeviathanBladeItem;
import com.legacy.dungeons_plus.items.SoulCannonItem;
import com.legacy.dungeons_plus.items.WarpedAxeItem;
import com.legacy.structure_gel.api.util.LazyOptional;
import com.mojang.datafixers.util.Pair;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPItems
{
	private static List<Pair<String, LazyOptional<? extends Item>>> objs = new ArrayList<>();

	public static final LazyOptional<Item> FROSTED_COWL = register("frosted_cowl", () -> new FrostedCowlItem(DPArmors.STRAY, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final LazyOptional<Item> LEVIATHAN_BLADE = register("leviathan_blade", () -> new LeviathanBladeItem(DPTiers.LEVIATHAN, 3, -2.6F, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final LazyOptional<Item> WARPED_AXE = register("warped_axe", () -> new WarpedAxeItem(DPTiers.WARPED_GOLD, 7, -3.1F, new Item.Properties().durability(312).tab(CreativeModeTab.TAB_COMBAT)));
	public static final LazyOptional<Item> SOUL_CANNON = register("soul_cannon", () -> new SoulCannonItem(new Item.Properties().durability(250).tab(CreativeModeTab.TAB_COMBAT)));

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		objs.forEach(p ->
		{
			Item item = p.getSecond().get();
			item.setRegistryName(DungeonsPlus.locate(p.getFirst()));
			registry.register(item);
		});
		objs = null;
	}

	private static <T extends Item> LazyOptional<T> register(String key, Supplier<T> obj)
	{
		LazyOptional<T> laz = LazyOptional.of(obj);
		objs.add(Pair.of(key, laz));
		return laz;
	}

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
