package com.legacy.dungeons_plus.events;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.client.DPBlockLayers;
import com.legacy.dungeons_plus.client.renderers.SoulFireballRenderer;
import com.legacy.dungeons_plus.client.renderers.WarpedAxeRenderer;
import com.legacy.dungeons_plus.items.CustomHandRendererSupplier;
import com.legacy.dungeons_plus.items.SoulCannonItem;
import com.legacy.dungeons_plus.registry.DPEntityTypes;
import com.legacy.dungeons_plus.registry.DPItems;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DPClientEvents
{
	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeBus
	{
		@SubscribeEvent
		protected static void renderHand(final RenderHandEvent event)
		{
			ItemStack stack = event.getItemStack();
			if (stack.getItem() instanceof CustomHandRendererSupplier handRenderer)
			{
				Minecraft mc = Minecraft.getInstance();
				InteractionHand hand = event.getHand();
				if (mc.player.isUsingItem() && mc.player.getUsedItemHand() == hand)
				{
					PoseStack poseStack = event.getPoseStack();
					poseStack.pushPose();
					boolean isMainHand = hand == InteractionHand.MAIN_HAND;
					handRenderer.getHandRenderer().renderItem(event, stack, hand);
					mc.getItemInHandRenderer().renderItem(mc.player, stack, isMainHand ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isMainHand, poseStack, event.getMultiBufferSource(), event.getPackedLight());
					poseStack.popPose();
					event.setCanceled(true);
				}
			}
		}

		@SubscribeEvent
		protected static void modifyFov(final FOVModifierEvent event)
		{
			Minecraft mc = Minecraft.getInstance();
			var player = mc.player;
			ItemStack useStack = player.getUseItem();
			if (player.isUsingItem())
			{
				if (useStack.is(DPItems.SOUL_CANNON.get()))
				{
					float f = event.getNewfov();
					float progress = (float) player.getTicksUsingItem() / SoulCannonItem.MAX_USE_TIME;
					if (progress > 1.0F)
						progress = 1.0F;
					else
						progress *= progress;

					f *= 1.0F - progress * 0.15F;
					event.setNewfov(f);
				}
			}
		}
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DungeonsPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBus
	{
		@SubscribeEvent
		protected static void clientInit(final FMLClientSetupEvent event)
		{
			DPBlockLayers.init();
		}

		@SubscribeEvent
		protected static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event)
		{
			event.registerEntityRenderer(DPEntityTypes.SOUL_FIREBALL.get(), SoulFireballRenderer::new);
			event.registerEntityRenderer(DPEntityTypes.WARPED_AXE.get(), WarpedAxeRenderer::new);
		}

		@SubscribeEvent
		protected static void initColors(final ColorHandlerEvent.Item event)
		{
			ItemColors itemcolors = event.getItemColors();
			itemcolors.register((stack, layer) ->
			{
				return layer > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack);
			}, DPItems.FROSTED_COWL.get());
		}
	}
}
