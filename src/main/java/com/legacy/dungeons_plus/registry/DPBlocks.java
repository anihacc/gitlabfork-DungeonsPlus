package com.legacy.dungeons_plus.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPBlocks
{
	private static List<Block> objs = new ArrayList<>();
	public static List<BlockItem> blockItems = new ArrayList<>();
	
	//public static final DynamicSpawnerBlock DYNAMIC_SPAWNER = register("dynamic_spawner", new DynamicSpawnerBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER)), new Item.Properties().rarity(Rarity.EPIC));
	
	@SubscribeEvent
	protected static void onRegistry(final RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		objs.forEach(registry::register);
		objs = null;
	}
	
	private static <T extends Block> T register(String key, T obj)
	{
		obj.setRegistryName(DungeonsPlus.locate(key));
		objs.add(obj);
		return obj;
	}
	
	/**
	 * Registers the passed block and creates a BlockItem to register later
	 */
	private static <B extends Block> B register(String key, B block, CreativeModeTab creativeTab)
	{
		return register(key, block, new Item.Properties().tab(creativeTab));
	}

	/**
	 * Registers the passed block and creates a BlockItem to register later
	 */
	private static <B extends Block> B register(String key, B block, Item.Properties itemProperties)
	{
		return register(key, block, itemProperties, BlockItem::new);
	}

	/**
	 * Registers the passed block and creates a BlockItem to register later
	 */
	private static <B extends Block> B register(String key, B block, Item.Properties itemProperties, BiFunction<Block, Item.Properties, BlockItem> blockItemFactory)
	{
		register(key, block);
		BlockItem blockItem = blockItemFactory.apply(block, itemProperties);
		blockItem.setRegistryName(block.getRegistryName());
		blockItems.add(blockItem);
		return block;
	}
}
