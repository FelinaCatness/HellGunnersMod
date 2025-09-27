
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.felinamods.hg.HgMod;

public class HgModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, HgMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TRACERL = REGISTRY.register("tracerl", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PLASMAPART = REGISTRY.register("plasmapart", () -> new SimpleParticleType(true));
}
