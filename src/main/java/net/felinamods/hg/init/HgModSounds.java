
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.felinamods.hg.HgMod;

public class HgModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, HgMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> RIC = REGISTRY.register("ric", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "ric")));
	public static final DeferredHolder<SoundEvent, SoundEvent> AR23_BOLT = REGISTRY.register("ar23_bolt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "ar23_bolt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> AR23_CLIPIN = REGISTRY.register("ar23_clipin", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "ar23_clipin")));
	public static final DeferredHolder<SoundEvent, SoundEvent> AR23_CLIPOUT = REGISTRY.register("ar23_clipout", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "ar23_clipout")));
	public static final DeferredHolder<SoundEvent, SoundEvent> AR23_SHOOT = REGISTRY.register("ar23_shoot", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "ar23_shoot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> GUN_CLOTH = REGISTRY.register("gun_cloth", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "gun_cloth")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MG43_SHOOT = REGISTRY.register("mg43_shoot", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "mg43_shoot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MG43_FOLEY = REGISTRY.register("mg43_foley", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "mg43_foley")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MG43_BOLT = REGISTRY.register("mg43_bolt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "mg43_bolt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PLAS1_RELOAD = REGISTRY.register("plas1_reload", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "plas1_reload")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PLAS1_SHOOT = REGISTRY.register("plas1_shoot", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "plas1_shoot")));
	public static final DeferredHolder<SoundEvent, SoundEvent> FLARE_SHOOT02 = REGISTRY.register("flare_shoot02", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "flare_shoot02")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MGL_LOAD = REGISTRY.register("mgl_load", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("hg", "mgl_load")));
}
