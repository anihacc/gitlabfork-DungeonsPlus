package com.legacy.dungeons_plus.items;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.legacy.dungeons_plus.entities.SoulFireballEntity;
import com.legacy.dungeons_plus.registry.DPDamageSource;
import com.legacy.dungeons_plus.registry.DPSoundEvents;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// Projectile weapon that shoots a soul-themed ghast fireball (no block breaking). Consumes fire charges.
public class SoulCannonItem extends Item implements DPItem, Vanishable, CustomHandRendererSupplier
{
	public static final String SHOOT_KEY = "shoot";
	public static final int MAX_USE_TIME = 20;

	public SoulCannonItem(Properties properties)
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
		int useTime = this.getUseDuration(stack) - timeLeft;

		if (useTime >= MAX_USE_TIME && entity instanceof Player player)
		{
			ItemStack heldStack = player.getItemInHand(player.getUsedItemHand());
			var enchants = EnchantmentHelper.getEnchantments(stack);
			boolean hasFlame = enchants.containsKey(Enchantments.FLAMING_ARROWS);
			boolean isMultishot = enchants.containsKey(Enchantments.MULTISHOT);
			int knockback = enchants.getOrDefault(Enchantments.KNOCKBACK, 0);
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), DPSoundEvents.SOUL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 0.4F, (level.random.nextFloat() - level.random.nextFloat()) * 0.1F + 1.5F);
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 10.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.1F + 1.5F);

			if (!level.isClientSide)
			{
				heldStack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));

				float multiAngle = 15.0F;
				int multishotLevel = enchants.getOrDefault(Enchantments.MULTISHOT, 0);
				float[] angles;

				if (multishotLevel < 1)
				{
					angles = new float[] { 0 };
				}
				else
				{
					angles = new float[multishotLevel * 2 + 1];
					float separation = multiAngle / multishotLevel;
					for (int i = 0; i < angles.length; i++)
						angles[i] = separation * i - multiAngle;
				}

				for (float r : angles)
				{
					// TODO Test multishot
					Vec3 upVec = entity.getUpVector(1.0F);
					Quaternionf quat = (new Quaternionf()).setAngleAxis(r * Mth.DEG_TO_RAD, upVec.x, upVec.y, upVec.z);
					Vec3 viewVec = entity.getViewVector(1.0F);
					Vector3f shootAngle = viewVec.toVector3f().rotate(quat);

					SoulFireballEntity fireball = new SoulFireballEntity(level, player, 0, 0, 0, 2);
					fireball.setKnockback(knockback);
					fireball.setHasFlame(hasFlame);
					fireball.setIsMultishot(isMultishot);
					fireball.shoot(shootAngle.x(), shootAngle.y(), shootAngle.z(), 2.5F, 1.0F);
					fireball.setPos(player.getEyePosition());
					level.addFreshEntity(fireball);
				}
			}
			player.causeFoodExhaustion(hasFlame || isMultishot ? 2.5F : 2.0F);
			if (player.getFoodData().getFoodLevel() <= 6)
				player.hurt(DPDamageSource.CONSUME_SOUL, 2);
			player.getCooldowns().addCooldown(this, 40);
			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant)
	{
		return super.canApplyAtEnchantingTable(stack, enchant) || enchant == Enchantments.FLAMING_ARROWS || enchant == Enchantments.MULTISHOT || enchant == Enchantments.KNOCKBACK;
	}

	@Override
	public int getEnchantmentValue()
	{
		return 1;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairIngredient)
	{
		return repairIngredient.is(Items.GHAST_TEAR) || super.isValidRepairItem(stack, repairIngredient);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag showAdvanced)
	{
		super.appendHoverText(stack, level, tooltip, showAdvanced);
		tooltip.addAll(this.getDescription(stack, SHOOT_KEY));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public com.legacy.dungeons_plus.client.renderers.CustomHandRenderer getHandRenderer()
	{
		return com.legacy.dungeons_plus.client.renderers.CustomHandRenderer.SOUL_BLASTER;
	}
}
