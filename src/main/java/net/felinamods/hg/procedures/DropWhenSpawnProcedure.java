package net.felinamods.hg.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.felinamods.hg.init.HgModItems;
import net.felinamods.hg.entity.DropEntity;
import net.felinamods.hg.HgMod;

public class DropWhenSpawnProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof DropEntity _datEntS ? _datEntS.getEntityData().get(DropEntity.DATA_Slot1) : "").equals("none"))) {
			if ((entity instanceof DropEntity _datEntS ? _datEntS.getEntityData().get(DropEntity.DATA_Slot1) : "").equals("ar23")) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(HgModItems.AR_23L.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (entity instanceof DropEntity _datEntSetS)
					_datEntSetS.getEntityData().set(DropEntity.DATA_Slot1, "none");
				HgMod.queueServerWork(60, () -> {
					if (!entity.level().isClientSide())
						entity.discard();
				});
			} else if ((entity instanceof DropEntity _datEntS ? _datEntS.getEntityData().get(DropEntity.DATA_Slot1) : "").equals("mg43")) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(HgModItems.MG_43.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (entity instanceof DropEntity _datEntSetS)
					_datEntSetS.getEntityData().set(DropEntity.DATA_Slot1, "none");
				HgMod.queueServerWork(60, () -> {
					if (!entity.level().isClientSide())
						entity.discard();
				});
			} else if ((entity instanceof DropEntity _datEntS ? _datEntS.getEntityData().get(DropEntity.DATA_Slot1) : "").equals("plas1")) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(HgModItems.PLAS_1.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (entity instanceof DropEntity _datEntSetS)
					_datEntSetS.getEntityData().set(DropEntity.DATA_Slot1, "none");
				HgMod.queueServerWork(60, () -> {
					if (!entity.level().isClientSide())
						entity.discard();
				});
			} else if ((entity instanceof DropEntity _datEntS ? _datEntS.getEntityData().get(DropEntity.DATA_Slot1) : "").equals("gp31")) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(HgModItems.GP_31.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (entity instanceof DropEntity _datEntSetS)
					_datEntSetS.getEntityData().set(DropEntity.DATA_Slot1, "none");
				HgMod.queueServerWork(60, () -> {
					if (!entity.level().isClientSide())
						entity.discard();
				});
			} else if ((entity instanceof DropEntity _datEntS ? _datEntS.getEntityData().get(DropEntity.DATA_Slot1) : "").equals("ar2")) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(HgModItems.AR_2.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (entity instanceof DropEntity _datEntSetS)
					_datEntSetS.getEntityData().set(DropEntity.DATA_Slot1, "none");
				HgMod.queueServerWork(60, () -> {
					if (!entity.level().isClientSide())
						entity.discard();
				});
			}
		}
	}
}
