package com.legacy.dungeons_plus.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.legacy.dungeons_plus.entities.WarpedAxeEntity;
import com.legacy.dungeons_plus.registry.DPSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Lazy;

// Throwable axe that teleports you to the entity it hits
public class WarpedAxeItem extends AxeItem
{
	public static final int THROW_THRESHOLD_TIME = 10;
	public static final float BASE_DAMAGE = 8.0F;
	public static final float ATTACK_SPEED = -3.2F;
	public static final float SHOOT_POWER = 2.5F;
	private static final Lazy<Multimap<Attribute, AttributeModifier>> ATTRIBUTES = Lazy.of(() ->
	{
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "attack_damage", BASE_DAMAGE, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "attack_speed", ATTACK_SPEED, AttributeModifier.Operation.ADDITION));
		return builder.build();
	});

	public WarpedAxeItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties)
	{
		super(tier, attackDamage, attackSpeed, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
		return slot == EquipmentSlot.MAINHAND ? ATTRIBUTES.get() : ImmutableMultimap.of();
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
	{
		return !player.isCreative();
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int useTime)
	{
		if (user instanceof Player player)
		{
			int totalUseTime = this.getUseDuration(stack) - useTime;
			if (totalUseTime >= THROW_THRESHOLD_TIME && !level.isClientSide)
			{
				stack.hurtAndBreak(1, player, u -> u.broadcastBreakEvent(user.getUsedItemHand()));

				WarpedAxeEntity axe = new WarpedAxeEntity(level, player, stack);
				axe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, SHOOT_POWER, 1.0F);
				;
				boolean isCreative = player.isCreative();
				if (isCreative)
					axe.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
				level.addFreshEntity(axe);
				level.playSound((Player) null, axe, DPSoundEvents.WARPED_AXE_THROW.get(), SoundSource.PLAYERS, 0.8F, 0.9F);
				if (!isCreative)
					player.getInventory().removeItem(stack);

				player.swing(player.getUsedItemHand(), true);
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack handItem = player.getItemInHand(hand);
		if (handItem.getDamageValue() >= handItem.getMaxDamage() - 1)
			return InteractionResultHolder.fail(handItem);

		player.startUsingItem(hand);
		return InteractionResultHolder.consume(handItem);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user)
	{
		// This is a weapon, so taking 2 damage is dumb
		stack.hurtAndBreak(1, user, u -> u.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.LOYALTY || enchantment instanceof DamageEnchantment;
	}
}
