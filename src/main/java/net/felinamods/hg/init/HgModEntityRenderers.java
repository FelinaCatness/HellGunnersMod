
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.felinamods.hg.client.renderer.SpawnEntRenderer;
import net.felinamods.hg.client.renderer.DropRenderer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HgModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(HgModEntities.PLASMA_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(HgModEntities.GRENADE_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(HgModEntities.DROP.get(), DropRenderer::new);
		event.registerEntityRenderer(HgModEntities.SPAWN_ENT.get(), SpawnEntRenderer::new);
	}
}
