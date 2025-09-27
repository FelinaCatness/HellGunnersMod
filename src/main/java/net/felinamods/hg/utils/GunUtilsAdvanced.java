package net.felinamods.hg.utils;

import java.util.*;
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
import net.minecraft.world.phys.*;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

public class GunUtilsAdvanced {
    private static final Set<String> IGNORED_ENTITIES = Set.of(
        "minecraft:armor_stand",
        "grey_fields:game_core",
        "grey_fields:point_a",
        "grey_fields:point_b",
        "grey_fields:point_c",        
        "grey_fields:flag",
        "grey_fields:flag_2",
        "grey_fields:flag_3",        
        "grey_fields:explosion_entity_a",
        "grey_fields:explosion_entity_b"
    );
    private static final Set<String> HARD_ENTITIES = Set.of(
        "gray_fields:tsar_tank",
        "gray_fields:metal_barrier",
        "minecraft:iron_golem"
    );

    public static void shootBullet(
        Level world,
        Entity shooter,
        double maxDistance,
        float baseDamage,
        boolean piercing,
        int pierceLimit,
        double falloffStart,
        double falloffEnd,
        float minMultiplier,
        double offsetForward,
        double offsetRight,
        double offsetUp
    ) {
        if (shooter == null || world.isClientSide) return;

        Vec3 eyePos = new Vec3(shooter.getX(), shooter.getEyeY(), shooter.getZ());
        Vec3 lookVec = shooter.getLookAngle();
        Vec3 upVec = new Vec3(0, 1, 0);
        Vec3 rightVec = lookVec.cross(upVec).normalize();
        upVec = rightVec.cross(lookVec).normalize(); // Adjust upVec to be orthogonal

        // Apply offsets
        Vec3 offset = lookVec.scale(offsetForward)
            .add(rightVec.scale(offsetRight))
            .add(upVec.scale(offsetUp));
        Vec3 startPos = eyePos.add(offset);
        Vec3 endPos = startPos.add(lookVec.scale(maxDistance));

        ClipContext context = new ClipContext(startPos, endPos, Block.COLLIDER, Fluid.NONE, shooter);
        BlockHitResult blockHit = world.clip(context);
        Vec3 finalHitPos = blockHit.getLocation();

        AABB hitbox = new AABB(startPos, finalHitPos).inflate(0.5D);
        List<Entity> entities = world.getEntities(shooter, hitbox, e -> e != shooter && e.isAlive());
        entities.sort(Comparator.comparingDouble(e -> e.distanceToSqr(startPos)));

        Scoreboard scoreboard = world.getScoreboard();
        Team shooterTeam = scoreboard.getPlayersTeam(shooter.getScoreboardName());

        int pierceCount = 0;
        boolean bulletStopped = false;

        for (Entity entity : entities) {
            AABB entityBox = entity.getBoundingBox().inflate(0.5D);
            Optional<Vec3> intersection = entityBox.clip(startPos, endPos);

            if (intersection.isEmpty()) continue;

            finalHitPos = intersection.get();
            String registryName = Optional.ofNullable(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()))
                                          .map(ResourceLocation::toString)
                                          .orElse("unknown");

            if (IGNORED_ENTITIES.contains(registryName)) continue;

            if (HARD_ENTITIES.contains(registryName)) {
                bulletStopped = true;
                break;
            }

            Team targetTeam = scoreboard.getPlayersTeam(entity.getScoreboardName());
            if (shooterTeam != null && shooterTeam == targetTeam) continue;

            double hitHeight = finalHitPos.y - entity.getY();
            double headThreshold = entity.getBbHeight() * 0.85;
            boolean isHeadshot = hitHeight >= headThreshold;

            float damage = baseDamage;
            double distance = startPos.distanceTo(finalHitPos);

            if (distance > falloffStart) {
                double t = Math.min((distance - falloffStart) / (falloffEnd - falloffStart), 1.0);
                float multiplier = (float)(1.0 - t * (1.0 - minMultiplier));
                damage *= multiplier;
            }

            if (isHeadshot) damage *= 2.0F;

            entity.invulnerableTime = 0;
            entity.hurt(world.damageSources().generic(), damage);

            pierceCount++;
            PlayerVariables vars = shooter.getData(HgModVariables.PLAYER_VARIABLES);
            vars.hitmarker = true;
            vars.syncPlayerVariables(shooter);

            if (!piercing || pierceCount >= pierceLimit) break;
        }

        if (bulletStopped || blockHit.getType() == HitResult.Type.BLOCK || pierceCount == 0) {
            SpawnProcedure.execute(world, finalHitPos.x, finalHitPos.y, finalHitPos.z);
        }
    }
}
