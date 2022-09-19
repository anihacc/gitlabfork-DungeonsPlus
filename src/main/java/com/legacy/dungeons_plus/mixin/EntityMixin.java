package com.legacy.dungeons_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.legacy.dungeons_plus.registry.DPItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

@Mixin(Entity.class)
public class EntityMixin
{
	@Inject(at = @At("HEAD"), method = "canFreeze", cancellable = true)
	private void canFreezeHook(CallbackInfoReturnable<Boolean> callback)
	{
		if (DPItems.FROSTED_COWL.isPresent() && (Object) this instanceof LivingEntity living)
		{
			for (ItemStack stack : living.getArmorSlots())
			{
				if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == DPItems.DPArmors.STRAY)
				{
					callback.setReturnValue(false);
					break;
				}
			}
		}
	}
}
