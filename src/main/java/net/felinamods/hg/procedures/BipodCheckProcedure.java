package net.felinamods.hg.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;

//import net.felinamods.hg.init.HgModItems;
import net.felinamods.hg.utils.SoundUtils;
import net.felinamods.hg.item.BipodGunItem;
import net.felinamods.hg.utils.ClientAnimUtils;

public class BipodCheckProcedure {

    /** Max distance the raycast will check */
    private static final double RAY_DISTANCE = 2.0;

    public static void execute(Entity entity) {
        if (entity == null) return;

        if (entity instanceof Player ent){
        	ItemStack held = ent.getMainHandItem();
        
        // Only works if player is holding the MG_15_NA
        if (held.getItem() instanceof BipodGunItem gun) {
 

        Level level = entity.level();
        Vec3 eyePos = entity.getEyePosition();
        Vec3 lookVec = entity.getLookAngle();
        Vec3 end = eyePos.add(lookVec.scale(RAY_DISTANCE));

        var vars = entity.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);

        // Raycast
        BlockHitResult hitResult = level.clip(new net.minecraft.world.level.ClipContext(
                eyePos,
                end,
                net.minecraft.world.level.ClipContext.Block.OUTLINE,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                entity
        ));

        // If no block was hit → undeploy if active, then stop
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            if (vars.currentGunOnBipod) {
                undeploy(entity, vars);
            }
            return;
        }

        // Block hit position
        BlockPos blockPos = hitResult.getBlockPos();
        Direction lookDir = entity.getDirection(); // player facing (horizontal)

        // Target position = behind the block, from player perspective
        BlockPos targetPos = blockPos.relative(lookDir.getOpposite());

        // Clearance check
        BlockPos feetPos = targetPos;
        BlockPos headPos = targetPos.above();

        boolean clearFeet = level.getBlockState(feetPos).isAir();
        boolean clearHead = level.getBlockState(headPos).isAir();

        boolean clearSides = true;
        if (lookDir.getAxis().isHorizontal()) {
            Direction left = lookDir.getCounterClockWise();
            Direction right = lookDir.getClockWise();

            clearSides = level.getBlockState(feetPos.relative(left)).isAir()
                      && level.getBlockState(feetPos.relative(right)).isAir();
        }

        if (clearFeet && clearHead && clearSides) {
            if (!vars.currentGunOnBipod) {
                // Deploy
                vars.currentGunOnBipod = true;
                vars.isAiming = true;
                vars.syncPlayerVariables(entity);

                if (entity.level().isClientSide) {
              	 ClientAnimUtils.setState("bipod_deploy");
                 ClientAnimUtils.setStartTick(gun.getTick(held));                	
                }

                SoundUtils.playServerSound(entity.level(), ResourceLocation.parse("fels_firearms_wwi:bipod_deploy"), entity.position(), 1.0f, 1.0f);

                if (entity instanceof Player player) {
                    player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                    player.getAttributes().getInstance(Attributes.JUMP_STRENGTH).setBaseValue(0.0D);
                    player.getPersistentData().putFloat("baseYaw", player.getYRot());
                    player.getPersistentData().putFloat("basePitch", 0.0f);
                    player.getPersistentData().putInt("selectedSlot", player.getInventory().selected);
                }

                // Teleport centered behind block
                double targetX = targetPos.getX() + 0.5;
                double targetY = targetPos.getY();
                double targetZ = targetPos.getZ() + 0.5;

                entity.teleportTo(targetX, targetY, targetZ);

                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.teleport(
                            targetX, targetY, targetZ,
                            entity.getYRot(), entity.getXRot()
                    );
                }
            }
        } else {
            // Not clear → undeploy if needed
            if (vars.currentGunOnBipod) {
                undeploy(entity, vars);
            }
        }
    }
   }
  }

    /** Shared undeploy logic */
    public static void undeploy(Entity entity, net.felinamods.hg.network.HgModVariables.PlayerVariables vars) {
        vars.currentGunOnBipod = false;
        vars.isAiming = false;

        if (entity instanceof Player ent){
         	ItemStack held2 = ent.getMainHandItem();  
          if (held2.getItem() instanceof BipodGunItem gun2){

              if (ent.level().isClientSide) {
              	 ClientAnimUtils.setState("bipod_remove");
                 ClientAnimUtils.setStartTick(gun2.getTick(held2));                	
                }
          }
        }
        
        vars.syncPlayerVariables(entity);

        SoundUtils.playServerSound(entity.level(), ResourceLocation.parse("fels_firearms_wwi:bipod_remove"), entity.position(), 1.0f, 1.0f);       

        if (entity instanceof Player player) {
            player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.10D);
            player.getAttributes().getInstance(Attributes.JUMP_STRENGTH).setBaseValue(0.41D);
            player.getPersistentData().remove("baseYaw");
            player.getPersistentData().remove("basePitch");
            player.getPersistentData().remove("selectedSlot");
        }
    }
}

