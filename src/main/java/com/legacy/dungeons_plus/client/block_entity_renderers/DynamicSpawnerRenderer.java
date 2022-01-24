package com.legacy.dungeons_plus.client.block_entity_renderers;

import com.legacy.dungeons_plus.block_entities.DynamicSpawnerBlockEntity;
import com.legacy.dungeons_plus.data.managers.DPSpawners;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class DynamicSpawnerRenderer implements BlockEntityRenderer<DynamicSpawnerBlockEntity>
{
	public DynamicSpawnerRenderer(BlockEntityRendererProvider.Context context)
	{
	}

	@Override
	public void render(DynamicSpawnerBlockEntity blockEntity, float partialTicks, PoseStack posStack, MultiBufferSource buff, int packedLight, int overlay)
	{
		posStack.pushPose();
		posStack.translate(0.5D, 0.0D, 0.5D);
		DPSpawners.Spawner spawner = blockEntity.getSpawner();
		Entity entity = spawner.getOrCreateDisplayEntity(blockEntity.getLevel());
		if (entity != null)
		{
			float scale = 0.53125F;
			float largestLength = Math.max(entity.getBbWidth(), entity.getBbHeight());
			if (largestLength > 1.0D)
			{
				scale /= largestLength;
			}

			posStack.translate(0.0D, 0.4D, 0.0D);
			posStack.mulPose(Vector3f.YP.rotationDegrees((float) Mth.lerp(partialTicks, spawner.getoSpin(), spawner.getSpin()) * 10.0F));
			posStack.translate(0.0D, -0.2D, 0.0D);
			posStack.mulPose(Vector3f.XP.rotationDegrees(-30.0F));
			posStack.scale(scale, scale, scale);
			Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, posStack, buff, packedLight);
		}

		posStack.popPose();
	}
}