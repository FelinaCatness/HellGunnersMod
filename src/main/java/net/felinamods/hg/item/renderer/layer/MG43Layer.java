package net.felinamods.hg.item.renderer.layer;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.felinamods.hg.item.MG43Item;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class MG43Layer extends GeoRenderLayer<MG43Item> {
	private static final ResourceLocation LAYER = ResourceLocation.parse("hg:textures/item/f_mg43.png");

	public MG43Layer(GeoRenderer<MG43Item> renderer) {
		super(renderer);
	}

@Override
public void render(PoseStack poseStack, MG43Item animatable, BakedGeoModel bakedModel,
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



