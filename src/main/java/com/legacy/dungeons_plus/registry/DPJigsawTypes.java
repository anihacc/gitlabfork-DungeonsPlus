package com.legacy.dungeons_plus.registry;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.structures.end_ruins.EndRuinsStructure;
import com.legacy.dungeons_plus.structures.leviathan.LeviathanStructure;
import com.legacy.dungeons_plus.structures.reanimated_ruins.ReanimatedRuinsStructure;
import com.legacy.dungeons_plus.structures.warped_garden.WarpedGardenStructure;
import com.legacy.structure_gel.api.events.RegisterJigsawCapabilityEvent;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.JigsawType;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DPJigsawTypes
{
	public static final JigsawType<ReanimatedRuinsStructure.Capability> REANIMATED_RUINS = () -> ReanimatedRuinsStructure.Capability.CODEC;
	public static final JigsawType<LeviathanStructure.Capability> LEVIATHAN = () -> LeviathanStructure.Capability.CODEC;
	public static final JigsawType<WarpedGardenStructure.Capability> WARPED_GARDEN = () -> WarpedGardenStructure.Capability.CODEC;
	public static final JigsawType<EndRuinsStructure.Capability> END_RUINS = () -> EndRuinsStructure.Capability.CODEC;

	@SubscribeEvent
	protected static void onRegister(final RegisterJigsawCapabilityEvent event)
	{
		register(event, "reanimated_ruins", REANIMATED_RUINS);
		register(event, "leviathan", LEVIATHAN);
		register(event, "warped_garden", WARPED_GARDEN);
		register(event, "end_ruins", END_RUINS);
	}

	private static void register(RegisterJigsawCapabilityEvent event, String key, JigsawType<?> type)
	{
		event.register(DungeonsPlus.locate(key), type);
	}
}
