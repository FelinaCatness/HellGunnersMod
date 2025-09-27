
package net.felinamods.hg.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.felinamods.hg.entity.model.SpawnEntModel;
import net.felinamods.hg.entity.layer.SpawnEntLayer;
import net.felinamods.hg.entity.SpawnEntEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SpawnEntRenderer extends GeoEntityRenderer<SpawnEntEntity> {
	public SpawnEntRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SpawnEntModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new SpawnEntLayer(this));
	}

	@Override
	public RenderType getRenderType(SpawnEntEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	// ✅ Disable frustum culling
	@Override
	public boolean shouldRender(SpawnEntEntity entity, Frustum frustum, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	public void preRender(PoseStack poseStack, SpawnEntEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	@Override
	protected float getDeathMaxRotation(SpawnEntEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
