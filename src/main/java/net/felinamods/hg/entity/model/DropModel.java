package net.felinamods.hg.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.felinamods.hg.entity.DropEntity;

public class DropModel extends GeoModel<DropEntity> {
	@Override
	public ResourceLocation getAnimationResource(DropEntity entity) {
		return ResourceLocation.parse("hg:animations/drop.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DropEntity entity) {
		return ResourceLocation.parse("hg:geo/drop.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DropEntity entity) {
		return ResourceLocation.parse("hg:textures/entities/" + entity.getTexture() + ".png");
	}

}
