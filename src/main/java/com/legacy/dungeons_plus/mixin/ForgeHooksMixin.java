package com.legacy.dungeons_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.gson.Gson;
import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.LootTableLoadEvent;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin
{
	/**
	 * Allows other mods to inject into out loot tables with the
	 * {@link LootTableLoadEvent}.
	 */
	@ModifyVariable(at = @At(value = "LOAD", ordinal = 1), method = "loadLootTable(Lcom/google/gson/Gson;Lnet/minecraft/util/ResourceLocation;Lcom/google/gson/JsonElement;ZLnet/minecraft/loot/LootTableManager;)Lnet/minecraft/loot/LootTable;", remap = false)
	private static boolean modify$custom_1(boolean original, Gson gson, ResourceLocation name)
	{
		return DungeonsPlus.MODID.equals(name.getNamespace()) ? false : original;
	}
}
