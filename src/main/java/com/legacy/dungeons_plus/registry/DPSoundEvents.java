package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.registry.registrar.Registrar;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class DPSoundEvents
{
	public static final RegistrarHandler<SoundEvent> HANDLER = RegistrarHandler.getOrCreate(Registries.SOUND_EVENT, DungeonsPlus.MODID);

	public static final Registrar.Static<SoundEvent> SOUL_CANNON_SHOOT = register("item.soul_cannon.shoot");
	public static final Registrar.Static<SoundEvent> WARPED_AXE_THROW = register("item.warped_axe.throw");
	public static final Registrar.Static<SoundEvent> WARPED_AXE_LAND = register("item.warped_axe.land");
	public static final Registrar.Static<SoundEvent> WARPED_AXE_HIT = register("item.warped_axe.hit");
	public static final Registrar.Static<SoundEvent> WARPED_AXE_RETURN = register("item.warped_axe.return");
	public static final Registrar.Static<SoundEvent> WARPED_AXE_TELEPORT = register("item.warped_axe.teleport");
	
	private static Registrar.Static<SoundEvent> register(String key)
	{
		return HANDLER.createStatic(key, () -> SoundEvent.createVariableRangeEvent(DungeonsPlus.locate(key)));
	}
}
