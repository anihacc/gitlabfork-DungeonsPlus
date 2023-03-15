package com.legacy.dungeons_plus;

import com.legacy.dungeons_plus.DungeonsPlus.Features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DungeonsEvents
{
	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();
		EntityType<?> entityType = entity.getType();
		BlockPos pos = entity.getPosition();
		if (!event.getWorld().isRemote)
		{
			StructureManager strucManager = ((ServerWorld) event.getWorld()).func_241112_a_();

			if (entityType.equals(EntityType.HUSK) && strucManager.func_235010_a_(pos, false, DungeonsPlus.Structures.LEVIATHAN.getFirst()).isValid())
				EntityAccessHelper.setDeathLootTable((MobEntity) event.getEntity(), DungeonsPlusLoot.LEVIATHAN_HUSK);

			if (entityType.equals(EntityType.STRAY) && strucManager.func_235010_a_(pos, false, DungeonsPlus.Structures.SNOWY_TEMPLE.getFirst()).isValid())
				EntityAccessHelper.setDeathLootTable((MobEntity) event.getEntity(), DungeonsPlusLoot.SNOWY_TEMPLE_STRAY);

			if (entityType.equals(EntityType.ENDERMAN) && strucManager.func_235010_a_(pos, false, DungeonsPlus.Structures.END_RUINS.getFirst()).isValid())
				((EndermanEntity) entity).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>((EndermanEntity) entity, PlayerEntity.class, true, false));
		}
	}
}