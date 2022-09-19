package com.legacy.dungeons_plus.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.entities.SoulFireballEntity;
import com.legacy.dungeons_plus.entities.WarpedAxeEntity;
import com.legacy.structure_gel.api.util.LazyOptional;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPEntityTypes
{
	private static List<Pair<String, LazyOptional<? extends EntityType<?>>>> objs = new ArrayList<>();

	public static final LazyOptional<EntityType<SoulFireballEntity>> SOUL_FIREBALL = register("soul_fireball", () -> EntityType.Builder.<SoulFireballEntity>of(SoulFireballEntity::new, MobCategory.MISC).setCustomClientFactory(SoulFireballEntity::new).fireImmune().setShouldReceiveVelocityUpdates(true).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
	public static final LazyOptional<EntityType<WarpedAxeEntity>> WARPED_AXE = register("warped_axe", () -> EntityType.Builder.<WarpedAxeEntity>of(WarpedAxeEntity::new, MobCategory.MISC).setCustomClientFactory(WarpedAxeEntity::new).setShouldReceiveVelocityUpdates(true).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		objs.forEach(p ->
		{
			EntityType<?> item = p.getSecond().get();
			item.setRegistryName(DungeonsPlus.locate(p.getFirst()));
			registry.register(item);
		});
		objs = null;
	}

	private static <T extends Entity> LazyOptional<EntityType<T>> register(String key, Supplier<EntityType.Builder<T>> obj)
	{
		LazyOptional<EntityType<T>> laz = LazyOptional.of(() -> obj.get().build(DungeonsPlus.locate(key).toString()));
		objs.add(Pair.of(key, laz));
		return laz;
	}
}
