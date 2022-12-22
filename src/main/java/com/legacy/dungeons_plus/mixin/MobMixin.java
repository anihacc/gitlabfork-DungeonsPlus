package com.legacy.dungeons_plus.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.legacy.dungeons_plus.events.DPCommonEvents;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Mob.class)
public class MobMixin
{
	@Inject(at = @At("RETURN"), method = "finalizeSpawn")
	private void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt, CallbackInfoReturnable<SpawnGroupData> callback)
	{
		DPCommonEvents.ForgeBus.onEntitySpawn((Mob) (Object) this);
	}
}
