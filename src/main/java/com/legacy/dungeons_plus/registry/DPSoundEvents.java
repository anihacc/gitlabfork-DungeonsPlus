package com.legacy.dungeons_plus.registry;

import java.util.ArrayList;
import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.util.LazyOptional;
import com.mojang.datafixers.util.Pair;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPSoundEvents
{
	private static List<Pair<String, LazyOptional<? extends SoundEvent>>> objs = new ArrayList<>();

	public static final LazyOptional<SoundEvent> SOUL_BLASTER_SHOOT = register("item.soul_blaster.shoot");
	public static final LazyOptional<SoundEvent> WARPED_AXE_THROW = register("item.warped_axe.throw");
	public static final LazyOptional<SoundEvent> WARPED_AXE_LAND = register("item.warped_axe.land");
	public static final LazyOptional<SoundEvent> WARPED_AXE_HIT = register("item.warped_axe.hit");
	public static final LazyOptional<SoundEvent> WARPED_AXE_RETURN = register("item.warped_axe.return");
	public static final LazyOptional<SoundEvent> WARPED_AXE_TELEPORT = register("item.warped_axe.teleport");
	
	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<SoundEvent> event)
	{
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		objs.forEach(p ->
		{
			SoundEvent item = p.getSecond().get();
			item.setRegistryName(DungeonsPlus.locate(p.getFirst()));
			registry.register(item);
		});
		objs = null;
	}

	private static LazyOptional<SoundEvent> register(String key)
	{
		LazyOptional<SoundEvent> laz = LazyOptional.of(() -> new SoundEvent(DungeonsPlus.locate(key)));
		objs.add(Pair.of(key, laz));
		return laz;
	}
}
