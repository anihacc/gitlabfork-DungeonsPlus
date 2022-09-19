package com.legacy.dungeons_plus.items;

import com.legacy.dungeons_plus.entities.SoulFireballEntity;
import com.legacy.dungeons_plus.registry.DPSoundEvents;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Projectile weapon that shoots a soul-themed ghast fireball (no block breaking). Consumes fire charges.
public class SoulBlasterItem extends Item implements Vanishable, CustomHandRendererSupplier
{
	public static final int MAX_USE_TIME = 20;

	public SoulBlasterItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		player.startUsingItem(hand);
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft)
	{
		int i = this.getUseDuration(stack) - timeLeft;

		if (i >= MAX_USE_TIME && entity instanceof Player player)
		{
			ItemStack heldStack = player.getItemInHand(player.getUsedItemHand());

			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), DPSoundEvents.SOUL_BLASTER_SHOOT.get(), SoundSource.PLAYERS, 0.4F, (level.random.nextFloat() - level.random.nextFloat()) * 0.1F + 1.5F);
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 10.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.1F + 1.5F);

			if (!level.isClientSide)
			{
				heldStack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
				SoulFireballEntity fireball = new SoulFireballEntity(level, player, 0, 0, 0, 2);
				fireball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
				fireball.setPos(player.getEyePosition());
				level.addFreshEntity(fireball);
			}
			player.getCooldowns().addCooldown(this, 40);
			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public com.legacy.dungeons_plus.client.renderers.CustomHandRenderer getHandRenderer()
	{
		return com.legacy.dungeons_plus.client.renderers.CustomHandRenderer.SOUL_BLASTER;
	}
}
