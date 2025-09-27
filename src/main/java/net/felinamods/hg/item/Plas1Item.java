
package net.felinamods.hg.item;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import net.felinamods.hg.item.renderer.Plas1ItemRenderer;

public class Plas1Item extends GunItem {
	public String animationprocedure = "empty";//don't remove

	public Plas1Item() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected BlockEntityWithoutLevelRenderer createRenderer() {
		return new Plas1ItemRenderer();
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public boolean isPerspectiveAware() {
		return true;
	}

	//Item variables
	@Override
	protected SoundEvent getShootSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("fels_firearms_wwi:gewehr_shoot"));
	}

	//ammo variable   
	@Override
	protected int maxAmmo() {
		return 20; //5
	}

	@Override
	protected int internalAmmo() {
		return 100;
	}

	@Override
	protected boolean useInternalAmmo() {
		return true;
	}

	//gun stats  
	@Override
	protected double distance() {
		return 128.0D;
	}

	@Override
	protected float damage() {
		return 9.0F;
	}

	@Override
	protected boolean isPiercing() {
		return false;
	}

	@Override
	protected int maxHits() {
		return 1;
	}

	@Override
	protected double fallOffStart() {
		return 100.0D;
	}

	@Override
	protected double fallOffEnd() {
		return 120.0D;
	}

	@Override
	protected float fallOffMultiplier() {
		return 0.4F;
	}

	//animation timing   
	@Override
	protected double fireTime() {
		return 4.8; //3.2
	}

	@Override
	protected double firelastTime() {
		return 4.8; //3.2
	}

	@Override
	protected double reloadTime() {
		return 88.0;
	}

	@Override
	protected double reloademptyTime() {
		return 88.0;
	}

	@Override
	public double drawTime() {
		return 16.0;
	}

	//particle settings
	@Override
	protected double particleX() {
		return 0.04D;
	}

	@Override
	protected double particleY() {
		return -0.08D;
	}

	@Override
	protected double particleZ() {
		return 1.0D;
	}

	@Override
	protected double particleXA() {
		return 0.0D;
	}

	@Override
	protected double particleYA() {
		return -0.08D;
	}

	@Override
	protected double particleZA() {
		return 1.0D;
	}

	@Override
	protected String effect1() {
		return "minecraft:smoke";
	}

	//recoil control
	@Override
	protected float horizontalRecoil() {
		return (float) (Math.random() * 1.0D - 0.5D);
	}

	@Override
	protected float verticalRecoil() {
		return 4.8F;
	}

	@Override
	public float recoilEffect() {
		return 4.8F; //5.5
	}

	@Override
	public int recoilTime() {
		return 50;
	}

	//aim zoom value
	@Override
	protected float zoomValue() {
		return 0.6F;
	}

	// fire mode settings
	@Override
	public boolean isAutomatic() {
		return false;
	}

	@Override
	protected boolean canChangeFireRate() {
		return false;
	}

	@Override
	public String gunType() {
		return "Rifle"; //"Rifle", "Pistol"
	}

	@Override
	protected String shootType() {
		return "plasma";
	}
}
