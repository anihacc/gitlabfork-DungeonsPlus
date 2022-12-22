package com.legacy.dungeons_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.legacy.dungeons_plus.items.FrostedCowlItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(Entity.class)
public class EntityMixin
{
	@Inject(at = @At("HEAD"), method = "canFreeze", cancellable = true)
	private void canFreezeHook(CallbackInfoReturnable<Boolean> callback)
	{
		if ((Object) this instanceof LivingEntity living && FrostedCowlItem.isWearing(living))
		{
			callback.setReturnValue(false);
		}
	}
}
