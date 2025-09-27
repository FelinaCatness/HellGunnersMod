package net.felinamods.hg.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleUtilsClient {
   public static void ParticleClient(Entity player, double forward, double right, double up, String particleId1) {
      if (player != null) {
         Level level = player.level();
         if (level != null && level.isClientSide) {
            Vec3 view = player.getLookAngle().normalize();
            Vec3 upVec = new Vec3(0.0D, 1.0D, 0.0D);
            Vec3 rightVec = view.cross(upVec).normalize();
            Vec3 origin = player.getEyePosition().add(view.scale(forward)).add(rightVec.scale(right)).add(upVec.scale(up));
            spawnParticle(level, origin, particleId1);
         }
      }
   }

   private static void spawnParticle(Level level, Vec3 pos, String particleId) {
      if (level.isClientSide) {
         ParticleType<?> particleType = (ParticleType)BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particleId));
         if (particleType == null) {
            System.err.println("[ParticleUtils2] Unknown particle type: " + particleId);
         } else {
            try {
               ParticleOptions particle = (ParticleOptions)particleType;
               level.addParticle(particle, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            } catch (ClassCastException var5) {
               System.err.println("[ParticleUtils2] Particle " + particleId + " cannot be cast to ParticleOptions!");
            }

         }
      }
   }
}
