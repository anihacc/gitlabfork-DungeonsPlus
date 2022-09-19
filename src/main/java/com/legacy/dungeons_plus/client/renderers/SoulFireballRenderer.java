package com.legacy.dungeons_plus.client.renderers;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.entities.SoulFireballEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulFireballRenderer extends EntityRenderer<SoulFireballEntity>
{
	private static final ResourceLocation TEXTURE_LOCATION = DungeonsPlus.locate("textures/entity/soul_fireball.png");
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

	public SoulFireballRenderer(EntityRendererProvider.Context context)
	{
		super(context);
	}

	@Override
	protected int getBlockLightLevel(SoulFireballEntity entity, BlockPos pos)
	{
		return 12;
	}

	@Override
	public void render(SoulFireballEntity entity, float p_114081_, float partialTick, PoseStack poseStack, MultiBufferSource buffSource, int packedLighting)
	{
		poseStack.pushPose();
		float scale = 1.0F;
		poseStack.scale(scale, scale, scale);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		PoseStack.Pose lastPose = poseStack.last();
		Matrix4f matrix4f = lastPose.pose();
		Matrix3f matrix3f = lastPose.normal();
		VertexConsumer vertCon = buffSource.getBuffer(RENDER_TYPE);
		vertex(vertCon, matrix4f, matrix3f, packedLighting, 0, 0, 0, 1);
		vertex(vertCon, matrix4f, matrix3f, packedLighting, 1, 0, 1, 1);
		vertex(vertCon, matrix4f, matrix3f, packedLighting, 1, 1, 1, 0);
		vertex(vertCon, matrix4f, matrix3f, packedLighting, 0, 1, 0, 0);
		poseStack.popPose();
		super.render(entity, p_114081_, partialTick, poseStack, buffSource, packedLighting);
	}

	private static void vertex(VertexConsumer p_114090_, Matrix4f matrix4, Matrix3f matrix3, int packedLight, int x, int y, int u, int v)
	{
		p_114090_.vertex(matrix4, x - 0.5F, y - 0.25F, 0.0F).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix3, 0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(SoulFireballEntity entity)
	{
		return TEXTURE_LOCATION;
	}
}
