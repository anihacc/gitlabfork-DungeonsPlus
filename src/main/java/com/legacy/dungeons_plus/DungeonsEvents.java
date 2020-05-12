package com.legacy.dungeons_plus;

import com.legacy.dungeons_plus.DungeonsPlus.Features;
import com.legacy.structure_gel.entities.EntityAccessHelper;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DungeonsEvents
{
	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinWorldEvent event)
	{
		if (event.getEntity().getType().equals(EntityType.HUSK) && Features.LEVIATHAN.getFirst().isPositionInStructure(event.getWorld(), event.getEntity().getPosition()))
			EntityAccessHelper.setDeathLootTable((MobEntity) event.getEntity(), DungeonsPlusLoot.LEVIATHAN_HUSK);

		if (event.getEntity().getType().equals(EntityType.STRAY) && Features.SNOWY_TEMPLE.getFirst().isPositionInStructure(event.getWorld(), event.getEntity().getPosition()))
			EntityAccessHelper.setDeathLootTable((MobEntity) event.getEntity(), DungeonsPlusLoot.SNOWY_TEMPLE_STRAY);

		if (event.getEntity().getType().equals(EntityType.ENDERMAN) && Features.END_RUINS.getFirst().isPositionInsideStructure(event.getWorld(), event.getEntity().getPosition()))
			((EndermanEntity) event.getEntity()).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>((EndermanEntity) event.getEntity(), PlayerEntity.class, true, false));

	}
}