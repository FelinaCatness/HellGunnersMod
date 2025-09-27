
package net.felinamods.hg.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;

import net.felinamods.hg.procedures.Plas1HoldProcedure;
import net.felinamods.hg.procedures.MG43HoldProcedure;
import net.felinamods.hg.procedures.IsHoldingGunProcedure;
import net.felinamods.hg.procedures.GP31HoldProcedure;
import net.felinamods.hg.procedures.DebugProcedure;
import net.felinamods.hg.procedures.AmmoGetterProcedure;
import net.felinamods.hg.procedures.AR23HoldProcedure;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@EventBusSubscriber({Dist.CLIENT})
public class GUNHUDOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		if (IsHoldingGunProcedure.execute(entity)) {
			if (AR23HoldProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("hg:textures/screens/ar23_icon.png"), w - 61, h - 29, 0, 0, 32, 16, 32, 16);
			}
			if (MG43HoldProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("hg:textures/screens/mg43_icon.png"), w - 60, h - 29, 0, 0, 42, 16, 42, 16);
			}
			if (Plas1HoldProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("hg:textures/screens/plas1_icon.png"), w - 60, h - 29, 0, 0, 32, 16, 32, 16);
			}
			if (GP31HoldProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("hg:textures/screens/gp31_icon.png"), w - 65, h - 29, 0, 0, 32, 16, 32, 16);
			}
			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					DebugProcedure.execute(entity), 4, -26, -1, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					AmmoGetterProcedure.execute(entity), w - 61, h - 12, -1, false);
		}
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
