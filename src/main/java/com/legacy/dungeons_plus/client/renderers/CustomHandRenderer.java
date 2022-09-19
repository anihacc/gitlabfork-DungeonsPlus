package com.legacy.dungeons_plus.client.renderers;

import com.legacy.dungeons_plus.items.SoulBlasterItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;

@OnlyIn(Dist.CLIENT)
public interface CustomHandRenderer
{
	public static final CustomHandRenderer SOUL_BLASTER = CustomHandRenderer::soulBlaster;

	// The PoseStack is pushed and popped outside of this method
	void renderItem(RenderHandEvent event, ItemStack stack, InteractionHand hand);

	private static void soulBlaster(RenderHandEvent event, ItemStack stack, InteractionHand hand)
	{
		boolean isMainHand = hand == InteractionHand.MAIN_HAND;
		Minecraft mc = Minecraft.getInstance();
		HumanoidArm humanoidArm = isMainHand ? mc.player.getMainArm() : mc.player.getMainArm().getOpposite();
		PoseStack poseStack = event.getPoseStack();
		float swingProgress = event.getSwingProgress();
		float equipProgress = event.getEquipProgress();
		float x = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
		float y = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * ((float) Math.PI * 2F));
		float z = -0.2F * Mth.sin(swingProgress * (float) Math.PI);


		float useTime = (float) stack.getUseDuration() - ((float) mc.player.getUseItemRemainingTicks() - event.getPartialTicks() + 1.0F);
		float maxTime = SoulBlasterItem.MAX_USE_TIME;
		float useProgress = Math.min(useTime, maxTime) / maxTime;
		float bob = Mth.sin(useTime * (useProgress + 1)) * 0.015F * useProgress;

		int handOffset = isMainHand ? 1 : -1;
		poseStack.translate(handOffset * x + (bob / 2.0F), y + bob + useProgress / 5.0, z + (useProgress / 10.0));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(-10.935F * useProgress));

		applyItemArmTransform(poseStack, humanoidArm, equipProgress);
		applyItemArmAttackTransform(poseStack, humanoidArm, swingProgress);
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
		float f = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
		poseStack.mulPose(Vector3f.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
		float f1 = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
		poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) i * f1 * -20.0F));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(f1 * -80.0F));
		poseStack.mulPose(Vector3f.YP.rotationDegrees((float) i * -45.0F));
	}
}
