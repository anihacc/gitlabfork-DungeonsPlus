package com.legacy.dungeons_plus.items;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.util.Lazy;

// Weapon that produces a weakness effect on the target, and grants a knockback resistance buff
public class LeviathanBladeItem extends SwordItem
{
	private static final Lazy<Multimap<Attribute, AttributeModifier>> ATTRIBUTES = Lazy.of(() -> ImmutableMultimap.of(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("d1ff2158-37ac-11ed-a261-0242ac120002"), "Knockback Resistance", 0.10, AttributeModifier.Operation.MULTIPLY_TOTAL)));

	public LeviathanBladeItem(Tier tier, int baseDamage, float baseAttackSpeed, Properties properties)
	{
		super(tier, baseDamage, baseAttackSpeed, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
		var base = super.getAttributeModifiers(slot, stack);
		return slot == EquipmentSlot.MAINHAND ? ImmutableMultimap.<Attribute, AttributeModifier>builder().putAll(ATTRIBUTES.get()).putAll(base).build() : base;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0), attacker);
		return super.hurtEnemy(stack, target, attacker);
	}
}
