package net.felinamods.hg.utils;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;

import net.felinamods.hg.init.HgModEntities;
import net.felinamods.hg.entity.PlasmaProjectileEntity;
import net.felinamods.hg.entity.GrenadeProjectileEntity;

public class ProjectileUtils {

    /**
     * Spawns a Plasma projectile with custom settings.
     *
     * @param world    The world to spawn in (must be a ServerLevel).
     * @param shooter  The entity that shoots the projectile.
     * @param damage   Base damage of the projectile.
     * @param knockback Knockback strength applied on hit.
     * @param piercing Piercing level (0 = none).
     * @param velocity Projectile velocity.
     * @param inaccuracy Spread (0 = perfect aim).
     */
    public static void spawnProjectile(Level world, Entity shooter,
                                             float damage, int knockback, byte piercing,
                                             float velocity, float inaccuracy, String type) {
        if (!(world instanceof ServerLevel serverLevel) || shooter == null) return;

        

     if (type.equals("plasma")) {
        Projectile projectile = createPlasmaArrow(serverLevel, shooter, damage, knockback, piercing);

        projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ()); // spawn from eyes
        projectile.shoot(shooter.getLookAngle().x, shooter.getLookAngle().y, shooter.getLookAngle().z, velocity, inaccuracy);

        serverLevel.addFreshEntity(projectile);        
     }

else if (type.equals("grenade")) {
        Projectile projectile = createGrenadeArrow(serverLevel, shooter, damage, knockback, piercing);

        projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ()); // spawn from eyes
        projectile.shoot(shooter.getLookAngle().x, shooter.getLookAngle().y, shooter.getLookAngle().z, velocity, inaccuracy);

        serverLevel.addFreshEntity(projectile);        
     }     

    }

    private static Projectile createPlasmaArrow(Level level, Entity shooter,
                                                float damage, int knockback, byte piercing) {
        AbstractArrow arrow = new PlasmaProjectileEntity(HgModEntities.PLASMA_PROJECTILE.get(), level) {
            @Override
            public byte getPierceLevel() {
                return piercing;
            }

            @Override
            protected void doKnockback(LivingEntity livingEntity, DamageSource damageSource) {
                if (knockback > 0) {
                    double d1 = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback * 0.6 * d1);
                    if (vec3.lengthSqr() > 0.0) {
                        livingEntity.push(vec3.x, 0.1, vec3.z);
                    }
                }
            }
        };

        arrow.setOwner(shooter);
        arrow.setBaseDamage(damage);
        arrow.setSilent(true);
        return arrow;
    }

    private static Projectile createGrenadeArrow(Level level, Entity shooter,
                                                float damage, int knockback, byte piercing) {
        AbstractArrow arrow = new GrenadeProjectileEntity(HgModEntities.GRENADE_PROJECTILE.get(), level) {
            @Override
            public byte getPierceLevel() {
                return piercing;
            }

            @Override
            protected void doKnockback(LivingEntity livingEntity, DamageSource damageSource) {
                if (knockback > 0) {
                    double d1 = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback * 0.6 * d1);
                    if (vec3.lengthSqr() > 0.0) {
                        livingEntity.push(vec3.x, 0.1, vec3.z);
                    }
                }
            }
        };

        arrow.setOwner(shooter);
        arrow.setBaseDamage(damage);
        arrow.setSilent(true);
        return arrow;
    }
    
}
