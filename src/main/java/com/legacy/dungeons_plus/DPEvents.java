package com.legacy.dungeons_plus;

import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.legacy.dungeons_plus.DungeonsPlus.Structures;
import com.legacy.structure_gel.access_helpers.BiomeAccessHelper;
import com.legacy.structure_gel.access_helpers.EntityAccessHelper;
import com.legacy.structure_gel.registrars.StructureRegistrar2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DPEvents
{
	private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> TOWER_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.Tower.ZOMBIE, EntityType.SPIDER, DPLoot.Tower.SPIDER, EntityType.SKELETON, DPLoot.Tower.SKELETON));
	private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> BURIED_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.BiggerDungeon.ZOMBIE, EntityType.SKELETON, DPLoot.BiggerDungeon.SKELETON));

	@SubscribeEvent
	protected static void onEntitySpawn(final EntityJoinWorldEvent event)
	{
		World world = event.getWorld();
		if (!world.isClientSide)
		{
			Entity entity = event.getEntity();

			TOWER_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DungeonsPlus.Structures.TOWER, e -> EntityAccessHelper.setDeathLootTable((MobEntity) e, loot)));
			BURIED_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DungeonsPlus.Structures.BIGGER_DUNGEON, e -> EntityAccessHelper.setDeathLootTable((MobEntity) e, loot)));
			ifInStructure(entity, EntityType.HUSK, DungeonsPlus.Structures.LEVIATHAN, e -> EntityAccessHelper.setDeathLootTable((MobEntity) e, DPLoot.Leviathan.HUSK));
			ifInStructure(entity, EntityType.STRAY, DungeonsPlus.Structures.SNOWY_TEMPLE, e -> EntityAccessHelper.setDeathLootTable((MobEntity) e, DPLoot.SnowyTemple.STRAY));
			ifInStructure(entity, EntityType.ENDERMAN, DungeonsPlus.Structures.END_RUINS, e -> ((EndermanEntity) e).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>((EndermanEntity) e, PlayerEntity.class, true, false)));
			ifInStructure(entity, EntityType.GHAST, DungeonsPlus.Structures.SOUL_PRISON, e -> ((GhastEntity) e).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>((GhastEntity) e, PlayerEntity.class, true, false)));
		}
	}

	private static void ifInStructure(Entity entity, EntityType<?> entityTest, StructureRegistrar2<?, ?> structure, Consumer<Entity> consumer)
	{
		if (entity.getType().equals(entityTest) && (((ServerWorld) entity.level).structureFeatureManager()).getStructureAt(entity.blockPosition(), false, structure.getStructure()).isValid())
			consumer.accept(entity);
	}

	@SubscribeEvent
	protected static void biomeLoad(final BiomeLoadingEvent event)
	{
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.TOWER.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.BIGGER_DUNGEON.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.LEVIATHAN.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.SNOWY_TEMPLE.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.END_RUINS.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.WARPED_GARDEN.getStructureFeature());
		BiomeAccessHelper.addStructureIfAllowed(event, Structures.SOUL_PRISON.getStructureFeature());
	}
}
