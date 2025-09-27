package net.felinamods.hg.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import net.felinamods.hg.entity.SpawnEntEntity;

public class SpawnEntModel extends GeoModel<SpawnEntEntity> {
	@Override
	public ResourceLocation getAnimationResource(SpawnEntEntity entity) {
		return ResourceLocation.parse("hg:animations/spawn.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(SpawnEntEntity entity) {
		return ResourceLocation.parse("hg:geo/spawn.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(SpawnEntEntity entity) {
		return ResourceLocation.parse("hg:textures/entities/" + entity.getTexture() + ".png");
	}

}
