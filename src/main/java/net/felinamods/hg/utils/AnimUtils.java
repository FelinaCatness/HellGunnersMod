package net.felinamods.hg.utils;

import software.bernie.geckolib.cache.object.GeoBone;

import net.minecraft.client.model.geom.ModelPart;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class AnimUtils {
	// Use when syncing model part with a GeckoLib bone (like animated parts)
	public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn) {
		if (model == null || bone == null)
			return;
		setupModelFromBone(model, bone);
		model.render(stack, buffer, packedLightIn, packedOverlayIn);
	}

	public static void setupModelFromBone(ModelPart model, GeoBone bone) {
		if (model == null || bone == null)
			return;
		model.setPos(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
		model.xRot = 0.0f;
		model.yRot = 0.0f;
		model.zRot = 0.0f;
	}

	// Use this when you *don't* have a bone (like vanilla arms or sleeves)
	public static void renderPart(ModelPart model, PoseStack stack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn) {
		if (model == null)
			return;
		model.render(stack, buffer, packedLightIn, packedOverlayIn);
	}
}
