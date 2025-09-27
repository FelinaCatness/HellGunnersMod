
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.felinamods.hg.client.particle.TracerlParticle;
import net.felinamods.hg.client.particle.PlasmapartParticle;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HgModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(HgModParticleTypes.TRACERL.get(), TracerlParticle::provider);
		event.registerSpriteSet(HgModParticleTypes.PLASMAPART.get(), PlasmapartParticle::provider);
	}
}
