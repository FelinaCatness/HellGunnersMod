package net.felinamods.hg.item.model;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

import net.minecraft.resources.ResourceLocation;

import net.felinamods.hg.item.MG43Item;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;

import java.util.concurrent.ThreadLocalRandom;

public class MG43ItemModel extends GeoModel<MG43Item> {
	@Override
	public ResourceLocation getAnimationResource(MG43Item animatable) {
		return ResourceLocation.parse("hg:animations/mg43.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MG43Item animatable) {
		return ResourceLocation.parse("hg:geo/mg43.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MG43Item animatable) {
		return ResourceLocation.parse("hg:textures/item/mg43.png");
	}

	@Override
	public void setCustomAnimations(MG43Item animatable, long instanceId, AnimationState<MG43Item> animationState) {
		//System.out.println("[DEBUG] setCustomAnimations called for " + animatable);
		super.setCustomAnimations(animatable, instanceId, animationState);
		
		GeoBone flash = getAnimationProcessor().getBone("muzzle");
		GeoBone b1 = getAnimationProcessor().getBone("bulletg");
		GeoBone b2 = getAnimationProcessor().getBone("bulletg2");
		GeoBone b3 = getAnimationProcessor().getBone("bulletg3");
		GeoBone b4 = getAnimationProcessor().getBone("bulletg4");
		GeoBone b5 = getAnimationProcessor().getBone("bulletg5");
		GeoBone b6 = getAnimationProcessor().getBone("bulletg6");		
		
		if (flash == null) {
			// System.out.println("[DEBUG] 'muzzle' bone not found!");
			return;
		}
		ItemStack stack = animationState.getData(DataTickets.ITEMSTACK);
		if (stack == null) {
			// System.out.println("[DEBUG] ItemStack is null!");
			return;
		}
		CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		CompoundTag tag = data.copyTag();
		//  System.out.println("[DEBUG] CustomData tag on client: " + tag);
		if (!tag.contains("flash")) {
			//    System.out.println("[DEBUG] 'muzzle' tag not found in CustomData!");
			return;
		}
		boolean isShooting = tag.getBoolean("flash");;
		int ammo = tag.getInt("ammo");
		// System.out.println("[DEBUG] 'muzzle' tag value: " + isShooting);
		if (isShooting == true) {
			//flash.setHidden(true);
			flash.setScaleX(1f);
			flash.setScaleY(1f);
			flash.setScaleZ(1f);
			flash.setRotZ((float) Math.toRadians(ThreadLocalRandom.current().nextFloat() * 360f));
		} else { //flash.setHidden(false);
			flash.setScaleX(0f);
			flash.setScaleY(0f);
			flash.setScaleZ(0f);
			flash.setRotZ((float) Math.toRadians(ThreadLocalRandom.current().nextFloat() * 360f));
		}

		if (ammo <= 0){
			b1.setScaleX(0f);
			b1.setScaleY(0f);
			b1.setScaleZ(0f);
		}
		else if (ammo >= 1){
			b1.setScaleX(1f);
			b1.setScaleY(1f);
			b1.setScaleZ(1f);
		}

		if (ammo <= 1){
			b2.setScaleX(0f);
			b2.setScaleY(0f);
			b2.setScaleZ(0f);
		}
		else if (ammo >= 2){
			b2.setScaleX(1f);
			b2.setScaleY(1f);
			b2.setScaleZ(1f);
		}	

		if (ammo <= 2){
			b3.setScaleX(0f);
			b3.setScaleY(0f);
			b3.setScaleZ(0f);
		}
		else if (ammo >= 3){
			b3.setScaleX(1f);
			b3.setScaleY(1f);
			b3.setScaleZ(1f);
		}

		if (ammo <= 3){
			b4.setScaleX(0f);
			b4.setScaleY(0f);
			b4.setScaleZ(0f);
		}
		else if (ammo >= 3){
			b4.setScaleX(1f);
			b4.setScaleY(1f);
			b4.setScaleZ(1f);
		}		

		if (ammo <= 4){
			b5.setScaleX(0f);
			b5.setScaleY(0f);
			b5.setScaleZ(0f);
		}
		else if (ammo >= 5){
			b5.setScaleX(1f);
			b5.setScaleY(1f);
			b5.setScaleZ(1f);
		}

		if (ammo <= 5){
			b6.setScaleX(0f);
			b6.setScaleY(0f);
			b6.setScaleZ(0f);
		}
		else if (ammo >= 6){
			b6.setScaleX(1f);
			b6.setScaleY(1f);
			b6.setScaleZ(1f);
		}	
		
	}
	
}
