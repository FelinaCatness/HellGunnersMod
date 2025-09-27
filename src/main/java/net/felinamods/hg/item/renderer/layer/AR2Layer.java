package net.felinamods.hg.item.renderer.layer;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.felinamods.hg.item.AR2Item;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class AR2Layer extends GeoRenderLayer<AR2Item> {
	private static final ResourceLocation LAYER = ResourceLocation.parse("hg:textures/item/f_ar2.png");

	public AR2Layer(GeoRenderer<AR2Item> renderer) {
		super(renderer);
	}

@Override
public void render(PoseStack poseStack, AR2Item animatable, BakedGeoModel bakedModel,
                   RenderType baseRenderType, MultiBufferSource bufferSource, VertexConsumer baseBuffer,
                   float partialTick, int packedLight, int packedOverlay) {

    RenderType glowRenderType = RenderType.eyes(LAYER);
    VertexConsumer glowBuffer = bufferSource.getBuffer(glowRenderType);

    int glowColor = 0xFFFFFFFF; // White color with full alpha (ARGB)

    for (var bone : bakedModel.topLevelBones()) {
        getRenderer().renderRecursively(
            poseStack,
            animatable,
            bone,
            glowRenderType,
            bufferSource,
            glowBuffer,
            false,
            partialTick,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            glowColor
        );
    }
}


}


