package com.legacy.dungeons_plus.registry;

import javax.annotation.Nullable;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.ItemStack;

public interface DPDamageSource
{
	DamageSource CONSUME_SOUL = new DamageSource(name("consume_soul")).bypassArmor().bypassMagic();

	public static DamageSource warpedAxe(Entity damageCauser, @Nullable Entity thrower, @Nullable ItemStack axe)
	{
		return new ThrownItemDamageSource(name("warped_axe"), damageCauser, thrower, axe).setProjectile();
	}

	public static DamageSource fireballExplosion(Fireball fireball, @Nullable Entity shooter)
	{
		return shooter == null ? (new IndirectEntityDamageSource("explosion.player", fireball, fireball)).setExplosion().setProjectile() : (new IndirectEntityDamageSource("explosion", fireball, shooter)).setExplosion().setProjectile();
	}

	private static String name(String key)
	{
		return DungeonsPlus.MODID + "." + key;
	}

	public static class ThrownItemDamageSource extends IndirectEntityDamageSource
	{
		@Nullable
		private final ItemStack thrownItem;

		public ThrownItemDamageSource(String key, Entity directEntity, @Nullable Entity owner, @Nullable ItemStack thrownItem)
		{
			super(key, directEntity, owner);
			this.thrownItem = thrownItem;
		}

		@Override
		public Component getLocalizedDeathMessage(LivingEntity killedEntity)
		{
			Component attackerName = this.getEntity() == null ? this.entity.getDisplayName() : this.getEntity().getDisplayName();
			ItemStack stack = this.thrownItem == null ? ItemStack.EMPTY : this.thrownItem;
			String msg = "death.attack." + this.msgId;
			String msgWithItem = msg + ".item";
			return !stack.isEmpty() && stack.hasCustomHoverName() ? new TranslatableComponent(msgWithItem, killedEntity.getDisplayName(), attackerName, stack.getDisplayName()) : new TranslatableComponent(msg, killedEntity.getDisplayName(), attackerName);
		}
	}
}
