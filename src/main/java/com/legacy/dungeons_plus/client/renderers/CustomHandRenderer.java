package com.legacy.dungeons_plus.client.renderers;

import com.legacy.dungeons_plus.items.SoulCannonItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;

@OnlyIn(Dist.CLIENT)
public interface CustomHandRenderer
{
	public static final CustomHandRenderer SOUL_BLASTER = CustomHandRenderer::soulBlaster;

	// The PoseStack is pushed and popped outside of this method. Return true if should cancel vanilla rendering.
	boolean renderItem(RenderHandEvent event, ItemStack stack, InteractionHand hand);

	private static boolean soulBlaster(RenderHandEvent event, ItemStack stack, InteractionHand hand)
	{
		boolean isMainHand = hand == InteractionHand.MAIN_HAND;
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if (player.isUsingItem() && player.getUsedItemHand() == hand)
		{
			HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
			PoseStack poseStack = event.getPoseStack();
			float swingProgress = event.getSwingProgress();
			float equipProgress = event.getEquipProgress();
			int handOffset = isMainHand ? 1 : -1;
			float x = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * Mth.PI);
			float y = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * (Mth.PI * 2F));
			float z = -0.2F * Mth.sin(swingProgress * Mth.PI);

			float useTime = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - event.getPartialTick() + 1.0F);
			float maxTime = SoulCannonItem.MAX_USE_TIME;
			float useProgress = Math.min(useTime, maxTime) / maxTime;
			float bob = Mth.sin(useTime * (useProgress + 1)) * 0.015F * useProgress;
			
			double xBob = handOffset * bob / 2.0F;
			double yBob = bob + (useProgress / 5.0);
			double zBob = useProgress / 10.0;
			poseStack.pushPose();
			poseStack.translate(x + xBob, y + yBob, z + zBob);
			float windUpRot = 10.935F * handOffset;
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(windUpRot * useProgress));
			poseStack.mulPose(Vector3f.XP.rotationDegrees(-handOffset * windUpRot * useProgress));
			applyItemArmTransform(poseStack, arm, equipProgress);
			applyItemArmAttackTransform(poseStack, arm, swingProgress);
			mc.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, stack, isMainHand ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isMainHand, poseStack, event.getMultiBufferSource(), event.getPackedLight());
			poseStack.popPose();
			return true;
		}
		return false;
	}

	private static void renderUnusedItem(PoseStack poseStack, HumanoidArm arm, float equipProgress, float swingProgress)
	{
		float x = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * Mth.PI);
		float y = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * (Mth.PI * 2F));
		float z = -0.2F * Mth.sin(swingProgress * Mth.PI);
		int offset = arm == HumanoidArm.RIGHT ? 1 : -1;
		poseStack.translate(offset * x, y, z);
		applyItemArmTransform(poseStack, arm, equipProgress);
		applyItemArmAttackTransform(poseStack, arm, swingProgress);
	}

	// From ItemInHandRenderer
	private static void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProgress)
	{
		int i = arm == HumanoidArm.RIGHT ? 1 : -1;
		poseStack.translate((double) ((float) i * 0.56F), (double) (-0.52F + equipProgress * -0.6F), (double) -0.72F);
	}

	// From ItemInHandRenderer
	private static void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float swingProgress)
	{
		int i = arm == HumanoidArm.RIGHT ? 1 : -1;
		float f = Mth.sin(swingProgress * swingProgress * Mth.PI);
		poseStack.mulPose(Vector3f.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
		float f1 = Mth.sin(Mth.sqrt(swingProgress) * Mth.PI);
		poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) i * f1 * -20.0F));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(f1 * -80.0F));
		poseStack.mulPose(Vector3f.YP.rotationDegrees((float) i * -45.0F));
	}

	/// From ItemInHandRenderer
	private static void renderPlayerArm(PoseStack poseStack, MultiBufferSource buffSource, int packedLight, float swingProgress, float equipProgress, HumanoidArm arm)
	{
		poseStack.pushPose();
		Minecraft mc = Minecraft.getInstance();
		boolean isMain = arm != HumanoidArm.LEFT;
		float mainOffset = isMain ? 1.0F : -1.0F;
		float equip = Mth.sqrt(equipProgress);
		float f2 = -0.3F * Mth.sin(equip * Mth.PI);
		float f3 = 0.4F * Mth.sin(equip * (Mth.PI * 2F));
		float f4 = -0.4F * Mth.sin(equipProgress * Mth.PI);
		poseStack.translate(mainOffset * (f2 + 0.64000005F), f3 + -0.6F + swingProgress * -0.6F, f4 + -0.71999997F);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(mainOffset * 45.0F));
		float f5 = Mth.sin(equipProgress * equipProgress * Mth.PI);
		float f6 = Mth.sin(equip * Mth.PI);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(mainOffset * f6 * 70.0F));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(mainOffset * f5 * -20.0F));
		AbstractClientPlayer abstractclientplayer = mc.player;
		RenderSystem.setShaderTexture(0, abstractclientplayer.getSkinTextureLocation());
		poseStack.translate((double) (mainOffset * -1.0F), (double) 3.6F, 3.5D);
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(mainOffset * 120.0F));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(200.0F));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(mainOffset * -135.0F));
		poseStack.translate((double) (mainOffset * 5.6F), 0.0D, 0.0D);
		PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(abstractclientplayer);
		if (isMain)
		{
			playerRenderer.renderRightHand(poseStack, buffSource, packedLight, abstractclientplayer);
		}
		else
		{
			playerRenderer.renderLeftHand(poseStack, buffSource, packedLight, abstractclientplayer);
		}
		poseStack.popPose();
	}
}
