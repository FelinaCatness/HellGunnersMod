package net.felinamods.hg.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.felinamods.hg.item.PlasmaItem;

public class PlasmaItemModel extends GeoModel<PlasmaItem> {
	@Override
	public ResourceLocation getAnimationResource(PlasmaItem animatable) {
		return ResourceLocation.parse("hg:animations/pro1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(PlasmaItem animatable) {
		return ResourceLocation.parse("hg:geo/pro1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(PlasmaItem animatable) {
		return ResourceLocation.parse("hg:textures/item/proi.png");
	}
}
