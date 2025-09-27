package net.felinamods.hg.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.felinamods.hg.init.HgModItems;

public class AR23HoldProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == HgModItems.AR_23L.get()) {
			return true;
		}
		return false;
	}
}
