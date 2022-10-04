package com.legacy.dungeons_plus.registry;

import java.util.function.Supplier;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.entities.SoulFireballEntity;
import com.legacy.dungeons_plus.entities.WarpedAxeEntity;
import com.legacy.structure_gel.api.registry.registrar.Registrar;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class DPEntityTypes
{
	public static final RegistrarHandler<EntityType<?>> HANDLER = RegistrarHandler.getOrCreate(Registry.ENTITY_TYPE_REGISTRY, DungeonsPlus.MODID);

	public static final Registrar.Static<EntityType<SoulFireballEntity>> SOUL_FIREBALL = entityType("soul_fireball", () -> EntityType.Builder.<SoulFireballEntity>of(SoulFireballEntity::new, MobCategory.MISC).setCustomClientFactory(SoulFireballEntity::new).fireImmune().setShouldReceiveVelocityUpdates(true).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
	public static final Registrar.Static<EntityType<WarpedAxeEntity>> WARPED_AXE = entityType("warped_axe", () -> EntityType.Builder.<WarpedAxeEntity>of(WarpedAxeEntity::new, MobCategory.MISC).setCustomClientFactory(WarpedAxeEntity::new).setShouldReceiveVelocityUpdates(true).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
	
	private static <T extends Entity> Registrar.Static<EntityType<T>> entityType(String id, Supplier<EntityType.Builder<T>> builder)
	{
		return HANDLER.createStatic(id, () -> builder.get().build(DungeonsPlus.locate(id).toString()));
	}
}
