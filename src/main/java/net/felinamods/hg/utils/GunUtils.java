package net.felinamods.hg.utils;

import net.felinamods.hg.network.HgModVariables;
import net.felinamods.hg.network.HgModVariables.PlayerVariables;
import net.felinamods.hg.procedures.SpawnProcedure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraft.world.entity.Mob;

//import net.felinamods.grayfields.network.GfModVariables;

import java.util.*;
import net.minecraft.world.entity.monster.Enemy;

public class GunUtils {

    private static final Set<String> IGNORED_ENTITIES = Set.of(
        "minecraft:armor_stand",
        "hg:drop",
        "gf:spawn_ent",
        "gf:point_b",
        "gf:point_c",        
        "gf:flag",
        "gf:flag_2",
        "gf:flag_3",        
        "gf:explosion_entity_a",
        "gf:explosion_entity_b"
    );

    private static final Set<String> HARD_ENTITIES = Set.of(
        "gf:tsar_tank",
        "gf:metal_barrier",
        "minecraft:iron_golem"
    );

    public static void shootBullet(Level world, Entity shooter, double maxDistance, float baseDamage,
                                   boolean piercing, int pierceLimit, double falloffStart,
                                   double falloffEnd, float minMultiplier) {
        if (shooter == null || world.isClientSide) return;

        Vec3 startPos = new Vec3(shooter.getX(), shooter.getEyeY(), shooter.getZ());
        Vec3 lookVector = shooter.getLookAngle();
        Vec3 endPos = startPos.add(lookVector.scale(maxDistance));

        ClipContext context = new ClipContext(startPos, endPos, Block.COLLIDER, Fluid.NONE, shooter);
        BlockHitResult blockHit = world.clip(context);
        Vec3 finalHitPos = blockHit.getLocation();

        // Expand hitbox slightly for entity detection
        AABB hitbox = new AABB(startPos, finalHitPos).inflate(0.5D);
        List<Entity> entities = world.getEntities(shooter, hitbox, entity -> entity != shooter && entity.isAlive());

        // Sort by distance
        entities.sort(Comparator.comparingDouble(e -> e.distanceToSqr(startPos)));

        Scoreboard scoreboard = world.getScoreboard();
        Team shooterTeam = scoreboard.getPlayersTeam(shooter.getScoreboardName());

        int pierceCount = 0;
        boolean bulletStopped = false;

        for (Entity entity : entities) {
            // Check if the bullet intersects with the entity
            AABB entityBox = entity.getBoundingBox().inflate(0.5D);
            Optional<Vec3> intersection = entityBox.clip(startPos, endPos);
            if (intersection.isEmpty()) continue;

            finalHitPos = intersection.get();

            ResourceLocation entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
            String entityId = entityKey != null ? entityKey.toString() : "unknown";

            // Skip ignored entities
            if (IGNORED_ENTITIES.contains(entityId)) continue;

            // Stop on hard entities
            if (HARD_ENTITIES.contains(entityId)) {
                bulletStopped = true;
                break;
            }

            // Check for team damage
            Team targetTeam = scoreboard.getPlayersTeam(entity.getScoreboardName());
            boolean isEnemy = shooterTeam == null || targetTeam == null || shooterTeam != targetTeam;
            if (!isEnemy) continue;

            // Calculate damage falloff
            double distance = startPos.distanceTo(finalHitPos);
            float damage = baseDamage;
            if (distance > falloffStart) {
                double t = Math.min((distance - falloffStart) / (falloffEnd - falloffStart), 1.0D);
                float multiplier = (float)(1.0D - t * (1.0D - minMultiplier));
                damage *= multiplier;
            }

            // Headshot check
            double hitHeight = finalHitPos.y - entity.getY();
            boolean isHeadshot = hitHeight >= entity.getBbHeight() * 0.85D;
            if (isHeadshot) {
                damage *= 2.0F;
            }

            // Apply damage
            entity.invulnerableTime = 0;
            entity.hurt(world.damageSources().generic(), damage);

            // Apply hitmarker
 	        var vars = shooter.getData(HgModVariables.PLAYER_VARIABLES);
            vars.hitmarker = true;
            vars.syncPlayerVariables(shooter);
			
		    //GfModVariables.PlayerVariables _vars = entity.getData(GfModVariables.PLAYER_VARIABLES);
		   // _vars.playerKillerName = shooter.getDisplayName().getString();
		   // _vars.syncPlayerVariables(entity);

		   // GfModVariables.PlayerVariables _vars2 = entity.getData(GfModVariables.PLAYER_VARIABLES);
		   // _vars2.playerKillerUUID = shooter.getStringUUID();
		  //  _vars2.syncPlayerVariables(entity);

		    if (entity instanceof Mob || entity instanceof Enemy){
		    	entity.getPersistentData().putString("killer", shooter.getStringUUID());
                //System.out.println("The entity: " + entity.getDisplayName().getString() + " has been killed" );
		    }

		    //System.out.println("Target Name: " + entity.getDisplayName().getString() + " Killer UUID: " + shooter.getStringUUID());
		                
            pierceCount++;
            if (!piercing || pierceCount >= pierceLimit) {
                break;
            }
        }

        // Spawn impact particle if bullet hit a block, stopped, or hit nothing
        if (bulletStopped || blockHit.getType() == Type.BLOCK || pierceCount == 0) {
            SpawnProcedure.execute(world, finalHitPos.x, finalHitPos.y, finalHitPos.z);
        }
    }
}

