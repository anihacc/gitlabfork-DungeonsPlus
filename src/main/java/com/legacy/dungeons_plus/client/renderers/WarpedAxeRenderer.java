package com.legacy.dungeons_plus.client.renderers;

import com.legacy.dungeons_plus.entities.WarpedAxeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WarpedAxeRenderer extends EntityRenderer<WarpedAxeEntity>
{
	private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
	private final ItemRenderer itemRenderer;

	public WarpedAxeRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(WarpedAxeEntity entity, float p_116086_, float partialTicks, PoseStack poseStack, MultiBufferSource buffSource, int packedLight)
	{
		if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < MIN_CAMERA_DISTANCE_SQUARED))
		{
			poseStack.pushPose();
			float spinRot = Mth.lerp(partialTicks, entity.oldSpinRot, entity.spinRot);
			poseStack.translate(0, 0.126, 0);

			if (entity.isInBlock())
			{
				double blockY = entity.blockPosition().getY() + 0.5;
				double y = entity.position().y;
				if (y > blockY + 0.45)
				{
					spinRot = -20;
				}
				else if (y < blockY - 0.45)
				{
					spinRot = 120;
				}
				else
				{
					spinRot = 60;
				}
			}
			poseStack.mulPose(Vector3f.YP.rotationDegrees(90 - entity.getRenderRotation()));
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(spinRot));
			poseStack.mulPose(Vector3f.XN.rotationDegrees((float) Math.sin(spinRot) * 7));

			poseStack.translate(0, -0.35, 0);
			float s = 2.0F;
			poseStack.scale(s, s, s);
			this.itemRenderer.renderStatic(entity.getAxe(), ItemTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffSource, entity.getId());
			poseStack.popPose();
			super.render(entity, p_116086_, partialTicks, poseStack, buffSource, packedLight);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(WarpedAxeEntity entity)
	{
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
