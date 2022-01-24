package com.legacy.dungeons_plus.items;

import java.util.ArrayList;
import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPBlocks;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPItems
{
	private static List<Item> objs = new ArrayList<>();

	//public static final Item SPAWNER = register("spawner", new Block(BlockBehaviour.Properties.copy(Blocks.SPAWNER)), new Item.Properties().rarity(Rarity.EPIC));

	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		objs.forEach(registry::register);
		objs = null;

		DPBlocks.blockItems.forEach(registry::register);
		DPBlocks.blockItems = null;
	}

	private static <T extends Item> T register(String key, T obj)
	{
		obj.setRegistryName(DungeonsPlus.locate(key));
		objs.add(obj);
		return obj;
	}
}
