package net.felinamods.hg.item.model;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

import net.minecraft.resources.ResourceLocation;

import net.felinamods.hg.item.Gp31Item;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;

import java.util.concurrent.ThreadLocalRandom;

public class Gp31ItemModel extends GeoModel<Gp31Item> {
	@Override
	public ResourceLocation getAnimationResource(Gp31Item animatable) {
		return ResourceLocation.parse("hg:animations/gp31.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(Gp31Item animatable) {
		return ResourceLocation.parse("hg:geo/gp31.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Gp31Item animatable) {
		return ResourceLocation.parse("hg:textures/item/gp31.png");
	}

	@Override
	public void setCustomAnimations(Gp31Item animatable, long instanceId, AnimationState<Gp31Item> animationState) {
		//System.out.println("[DEBUG] setCustomAnimations called for " + animatable);
		super.setCustomAnimations(animatable, instanceId, animationState);
		
		GeoBone flash = getAnimationProcessor().getBone("muzzle");
		
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
		boolean isShooting = tag.getBoolean("flash");
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
		
	}	
	
}
