package com.legacy.dungeons_plus.registry;

import java.util.ArrayList;
import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.block_entities.DynamicSpawnerBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPBlockEntities
{
	private static List<BlockEntityType<?>> objs = new ArrayList<>();

	public static final BlockEntityType<DynamicSpawnerBlockEntity> DYNAMIC_SPAWNER = register("dynamic_spawner", BlockEntityType.Builder.of(DynamicSpawnerBlockEntity::new, DPBlocks.DYNAMIC_SPAWNER).build(null));

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<BlockEntityType<?>> event)
	{		
		IForgeRegistry<BlockEntityType<?>> registry = event.getRegistry();
		objs.forEach(registry::register);
		objs = null;
	}

	private static <T extends BlockEntity> BlockEntityType<T> register(String key, BlockEntityType<T> obj)
	{
		obj.setRegistryName(DungeonsPlus.locate(key));
		objs.add(obj);
		return obj;
	}
}
