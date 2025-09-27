package net.felinamods.hg.item.renderer;

import software.bernie.geckolib.util.RenderUtil;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;

import net.felinamods.hg.utils.AnimUtils;
import net.felinamods.hg.item.model.Gp31ItemModel;
import net.felinamods.hg.item.Gp31Item;
import net.felinamods.hg.item.renderer.layer.Gp31Layer;

import java.util.Set;
import java.util.HashSet;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class Gp31ItemRenderer extends GeoItemRenderer<Gp31Item> {

   private float adsProgress = 0.0F;

	public Gp31ItemRenderer() {
		super(new Gp31ItemModel());
	    this.addRenderLayer(new Gp31Layer(this));			
	}

	@Override
	public RenderType getRenderType(Gp31Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
	protected boolean renderArms = false;
	protected MultiBufferSource currentBuffer;
	protected RenderType renderType;
	public ItemDisplayContext transformType;
	protected Gp31Item animatable;
	private final Set<String> hiddenBones = new HashSet<>();
	private final Set<String> suppressedBones = new HashSet<>();

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int p_239207_6_) {
		this.transformType = transformType;
		super.renderByItem(stack, transformType, matrixStack, bufferIn, combinedLightIn, p_239207_6_);
	}

	@Override
public void actuallyRender(PoseStack poseStack, Gp31Item animatable,
             BakedGeoModel model, RenderType type, MultiBufferSource renderTypeBuffer,
             VertexConsumer vertexBuilder, boolean isReRender, float partialTicks,
             int packedLightIn, int packedOverlayIn, int color) {
		this.currentBuffer = renderTypeBuffer;
		this.renderType = type;
		this.animatable = animatable;

       LocalPlayer player = Minecraft.getInstance().player;//here
        boolean isAiming = false;

    // Force-enable bobbing when this item renders
    if (!Minecraft.getInstance().options.bobView().get()) {
        Minecraft.getInstance().options.bobView().set(true);
    }         

        if (player != null && this.transformType.firstPerson()) {
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof Gp31Item) {
                var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);
                isAiming = vars.isAiming;
            }

// target = 1 when aiming, 0 when not
float target = isAiming ? 1.0f : 0.0f;

// real frame delta time (in seconds)
float deltaTime = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);

// higher speed = faster snap
float speed = 0.2f;

// exponential smoothing
float alpha = 1.0f - (float)Math.exp(-speed * deltaTime);
adsProgress += (target - adsProgress) * alpha;

// clamp if close enough
if (Math.abs(target - adsProgress) < 0.001f) {
    adsProgress = target;
}
               

            // 🎯 Align scope bone
            if (isAiming) {
                GeoBone scopeBone = this.getGeoModel().getBone("scope").orElse(null);
                if (scopeBone != null) {
                    float scopeX = scopeBone.getPivotX();
                    float scopeY = scopeBone.getPivotY();
                    float scopeZ = scopeBone.getPivotZ();

                CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);//debug
                double ax = data.copyTag().getDouble("ax");
                double ay = data.copyTag().getDouble("ay");
                double az = data.copyTag().getDouble("az");

                    //System.out.println("Scope pivot: X=" + scopeX + ", Y=" + scopeY + ", Z=" + scopeZ);

               double tweakX = 0.007;
               double tweakY = 1.278;
               double tweakZ = 0.0;
                    
                    poseStack.translate(
                        -scopeX / 16f + tweakX,//tweakX,
                        -scopeY / 16f + tweakY,//tweakY,
                        -scopeZ / 16f + tweakZ//tweakZ
                    );
                }
            }

            // 💃 Animate ADS transform
            float interpX = lerp(adsProgress, 0.0f, -0.05f);
            float interpY = lerp(adsProgress, 0.0f, 0.05F);
            float interpZ = lerp(adsProgress, 0.0f, 0.0f);
            poseStack.translate(interpX, interpY, interpZ);

            // 🔄 Animate ADS rotation
            float rotX = lerp(adsProgress, 0.0f, 0.5f); // up/down -8.0f
            float rotY = lerp(adsProgress, 0.0f, 0.0f);   // twist
            float rotZ = lerp(adsProgress, 0.0f, 0.0f);   // tilt

            poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
        }
		
      super.actuallyRender(poseStack, animatable, model, type, renderTypeBuffer, vertexBuilder, isReRender, partialTicks, packedLightIn, packedOverlayIn, color);
		if (this.renderArms) {
			this.renderArms = false;
		}
	}

   private float lerp(float t, float a, float b) {
      return a + (b - a) * t;
   }	

	@Override
	public void renderRecursively(PoseStack stack, Gp31Item animatable, GeoBone bone, RenderType type, MultiBufferSource buffer, VertexConsumer bufferIn, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, int color) {
		Minecraft mc = Minecraft.getInstance();
		String name = bone.getName();
		boolean renderingArms = false;
		if (name.equals("leftarm") || name.equals("rightarm")) {
			bone.setHidden(true);
			renderingArms = true;
		} else {
			bone.setHidden(this.hiddenBones.contains(name));
		}
		if (this.transformType.firstPerson() && renderingArms) {
			AbstractClientPlayer player = mc.player;
			PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
			PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
			stack.pushPose();
			RenderUtil.translateMatrixToBone(stack, bone);
			RenderUtil.translateToPivotPoint(stack, bone);
			RenderUtil.rotateMatrixAroundBone(stack, bone);
			RenderUtil.scaleMatrixForBone(stack, bone);
			RenderUtil.translateAwayFromPivotPoint(stack, bone);
			ResourceLocation loc = player.getSkin().texture();
			if (name.equals("leftarm")) {
				stack.translate(-1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				if (!player.isInvisible()) {
					AnimUtils.renderPartOverBone(model.leftArm, bone, stack, this.currentBuffer.getBuffer(RenderType.entitySolid(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
					AnimUtils.renderPartOverBone(model.leftSleeve, bone, stack, this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
				}
			} else if (name.equals("rightarm")) {
				stack.translate(1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				if (!player.isInvisible()) {
					AnimUtils.renderPartOverBone(model.rightArm, bone, stack, this.currentBuffer.getBuffer(RenderType.entitySolid(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
					AnimUtils.renderPartOverBone(model.rightSleeve, bone, stack, this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
				}
			}
			stack.popPose();
		}
		super.renderRecursively(stack, animatable, bone, type, buffer, bufferIn, isReRender, partialTick, packedLightIn, packedOverlayIn, color);
	}

	@Override
	public ResourceLocation getTextureLocation(Gp31Item instance) {
		return super.getTextureLocation(instance);
	}
}
