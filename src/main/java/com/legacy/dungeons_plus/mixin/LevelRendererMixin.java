package com.legacy.dungeons_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.legacy.dungeons_plus.items.FrostedCowlItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin
{
	@ModifyVariable(at = @At(value = "STORE"), index = 41, method = "renderSnowAndRain")
	private float modifySnowAlpha(float original)
	{
		// Set the alpha channel of snow to be 50% of what it would normally be
		if (FrostedCowlItem.isWearing(Minecraft.getInstance().player))
			return original * 0.5F;
		return original;
	}
}
