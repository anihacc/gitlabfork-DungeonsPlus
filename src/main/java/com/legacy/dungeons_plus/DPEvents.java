package com.legacy.dungeons_plus;

import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;
import com.legacy.dungeons_plus.DungeonsPlus.Structures;
import com.legacy.structure_gel.api.entity.EntityAccessHelper;
import com.legacy.structure_gel.api.events.AddStructureToBiomeEvent;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DPEvents
{
	private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> TOWER_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.Tower.ZOMBIE, EntityType.SPIDER, DPLoot.Tower.SPIDER, EntityType.SKELETON, DPLoot.Tower.SKELETON));
	private static final NonNullLazy<Map<EntityType<?>, ResourceLocation>> BURIED_ENTITY_LOOT = NonNullLazy.of(() -> ImmutableMap.of(EntityType.ZOMBIE, DPLoot.BiggerDungeon.ZOMBIE, EntityType.SKELETON, DPLoot.BiggerDungeon.SKELETON));

	@SubscribeEvent
	protected static void onEntitySpawn(final EntityJoinWorldEvent event)
	{
		Level level = event.getWorld();
		if (!level.isClientSide)
		{
			Entity entity = event.getEntity();

			TOWER_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DungeonsPlus.Structures.TOWER, e -> EntityAccessHelper.setDeathLootTable((Mob) e, loot)));
			BURIED_ENTITY_LOOT.get().forEach((type, loot) -> ifInStructure(entity, type, DungeonsPlus.Structures.BIGGER_DUNGEON, e -> EntityAccessHelper.setDeathLootTable((Mob) e, loot)));
			ifInStructure(entity, EntityType.HUSK, DungeonsPlus.Structures.LEVIATHAN, e -> EntityAccessHelper.setDeathLootTable((Mob) e, DPLoot.Leviathan.HUSK));
			ifInStructure(entity, EntityType.STRAY, DungeonsPlus.Structures.SNOWY_TEMPLE, e -> EntityAccessHelper.setDeathLootTable((Mob) e, DPLoot.SnowyTemple.STRAY));
			ifInStructure(entity, EntityType.ENDERMAN, DungeonsPlus.Structures.END_RUINS, e -> ((EnderMan) e).targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((EnderMan) e, Player.class, true, false)));
			ifInStructure(entity, EntityType.GHAST, DungeonsPlus.Structures.SOUL_PRISON, e -> ((Ghast) e).targetSelector.addGoal(1, new NearestAttackableTargetGoal<Player>((Ghast) e, Player.class, true, false)));
		}
	}

	private static void ifInStructure(Entity entity, EntityType<?> entityTest, StructureRegistrar<?, ?> structure, Consumer<Entity> consumer)
	{
		if (entity.getType().equals(entityTest) && entity.level instanceof ServerLevel && ((ServerLevel) entity.level).structureFeatureManager().getStructureWithPieceAt(entity.blockPosition(), structure.getStructure()).isValid())
			consumer.accept(entity);
	}

	@SubscribeEvent
	protected static void addStructureToBiome(final AddStructureToBiomeEvent event)
	{
		event.register(Structures.TOWER);
		event.register(Structures.BIGGER_DUNGEON);
		event.register(Structures.LEVIATHAN);
		event.register(Structures.SNOWY_TEMPLE);
		event.register(Structures.WARPED_GARDEN);

		event.register(Structures.END_RUINS);
		event.register(Structures.SOUL_PRISON);
	}
}
