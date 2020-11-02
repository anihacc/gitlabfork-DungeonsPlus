package com.legacy.dungeons_plus;

import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.legacy.structure_gel.access_helpers.EntityAccessHelper;
import com.legacy.structure_gel.registrars.StructureRegistrar;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DPEvents
{
	private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> TOWER_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.Tower.ZOMBIE, EntityType.SPIDER, DPLoot.Tower.SPIDER, EntityType.SKELETON, DPLoot.Tower.SKELETON));
	private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> BURIED_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.BiggerDungeon.ZOMBIE, EntityType.SKELETON, DPLoot.BiggerDungeon.SKELETON));

	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isRemote)
		{
			Entity entity = event.getEntity();
			
			TOWER_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DungeonsPlus.Structures.TOWER, e -> EntityAccessHelper.setDeathLootTable((MobEntity) entity, loot)));
			BURIED_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DungeonsPlus.Structures.BIGGER_DUNGEON, e -> EntityAccessHelper.setDeathLootTable((MobEntity) entity, loot)));
			ifInStructure(entity, EntityType.HUSK, DungeonsPlus.Structures.LEVIATHAN, e -> EntityAccessHelper.setDeathLootTable((MobEntity) entity, DPLoot.Leviathan.HUSK));
			ifInStructure(entity, EntityType.STRAY, DungeonsPlus.Structures.SNOWY_TEMPLE, e -> EntityAccessHelper.setDeathLootTable((MobEntity) entity, DPLoot.SnowyTemple.STRAY));
			ifInStructure(entity, EntityType.ENDERMAN, DungeonsPlus.Structures.END_RUINS, e -> ((EndermanEntity) entity).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>((EndermanEntity) entity, PlayerEntity.class, true, false)));
		}
	}

	private static void ifInStructure(Entity entity, EntityType<?> entityTest, StructureRegistrar<?, ?> structure, Consumer<Entity> consumer)
	{
		if (entity.getType().equals(entityTest) && (((ServerWorld) entity.world).func_241112_a_()).getStructureStart(entity.getPosition(), false, structure.getStructure()).isValid())
			consumer.accept(entity);
	}
}
