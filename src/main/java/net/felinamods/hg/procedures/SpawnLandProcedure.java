package net.felinamods.hg.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.felinamods.hg.init.HgModEntities;
import net.felinamods.hg.entity.SpawnEntEntity;
import net.felinamods.hg.entity.DropEntity;
import net.felinamods.hg.HgMod;

public class SpawnLandProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.onGround() && !(entity instanceof SpawnEntEntity _datEntL1 && _datEntL1.getEntityData().get(SpawnEntEntity.DATA_Landed))) {
			if (entity instanceof SpawnEntEntity) {
				((SpawnEntEntity) entity).setAnimation("animation.model.idle3");
			}
			entity.setDeltaMovement(new Vec3(0, 0, 0));
			if (!((entity instanceof SpawnEntEntity _datEntS ? _datEntS.getEntityData().get(SpawnEntEntity.DATA_Slot1) : "").equals("none"))) {
				HgMod.queueServerWork(60, () -> {
					if (world instanceof ServerLevel _serverLevel) {
						Entity entityinstance = HgModEntities.DROP.get().create(_serverLevel, null, BlockPos.containing(x, y + 350, z), MobSpawnType.MOB_SUMMONED, false, false);
						if (entityinstance != null) {
							entityinstance.setYRot(world.getRandom().nextFloat() * 360.0F);
							if (entityinstance instanceof DropEntity _datEntSetS)
								_datEntSetS.getEntityData().set(DropEntity.DATA_Slot1, (entity instanceof SpawnEntEntity _datEntS ? _datEntS.getEntityData().get(SpawnEntEntity.DATA_Slot1) : ""));
							if (entityinstance instanceof DropEntity _datEntSetS)
								_datEntSetS.getEntityData().set(DropEntity.DATA_Slot2, (entity instanceof SpawnEntEntity _datEntS ? _datEntS.getEntityData().get(SpawnEntEntity.DATA_Slot2) : ""));
							_serverLevel.addFreshEntity(entityinstance);
						}
					}
				});
			}
			if (entity instanceof SpawnEntEntity _datEntSetL)
				_datEntSetL.getEntityData().set(SpawnEntEntity.DATA_Landed, true);
		}
	}
}
