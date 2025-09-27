package net.felinamods.hg.utils;

import java.util.Iterator;
import java.util.Set;
import net.felinamods.hg.network.HgModVariables;
import net.felinamods.hg.network.HgModVariables.PlayerVariables;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.AABB;

public class CollisionDamageUtility {
   
   // Entities to be ignored during collision
   private static final Set<String> IGNORED_ENTITIES = Set.of(
        "minecraft:armor_stand",
        "gf:game_core",
        "gf:point_a",
        "gf:point_b",
        "gf:point_c",        
        "gf:flag",
        "gf:flag_2",
        "gf:flag_3",        
        "gf:explosion_entity_a",
        "gf:explosion_entity_b"
   );

   // Check if an entity is in the ignored list
   private static boolean isIgnored(Entity entity) {
      ResourceLocation entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
      String entityRegistryName = entityKey != null ? entityKey.toString() : "unknown";
      return IGNORED_ENTITIES.contains(entityRegistryName);
   }

   // Handle entity collisions and apply damage
   public static void onEntityCollide(Entity caller, float damageAmount) {
      // Define the collision box around the caller
      AABB collisionBox = caller.getBoundingBox().inflate(1.0D, 1.0D, 1.0D);

      // Iterate through all entities in the collision box, excluding the caller
      Iterator<Entity> entitiesInBox = caller.getCommandSenderWorld().getEntitiesOfClass(
         Entity.class, collisionBox, (e) -> e != caller
      ).iterator();

      // Process each entity in the collision box
      while (entitiesInBox.hasNext()) {
         Entity entity = entitiesInBox.next();

         // If the entity is a LivingEntity and not ignored, apply damage
         if (entity instanceof LivingEntity && !isIgnored(entity)) {
            LivingEntity livingEntity = (LivingEntity) entity;

         // Skip friendly fire: same team
         if (caller.getTeam() != null && caller.getTeam().isAlliedTo(entity.getTeam())) {
            continue;
         }             

            // Reset the invulnerability time of the entity
            livingEntity.invulnerableTime = 0;

            // Apply damage to the entity
            DamageSource damageSource = caller.getCommandSenderWorld().damageSources().generic();
            livingEntity.hurt(damageSource, damageAmount);


            // Remove the speed effect from the caller (if it's a LivingEntity)
            if (caller instanceof LivingEntity) {
               LivingEntity _livEnt = (LivingEntity) caller;
               _livEnt.removeEffect(MobEffects.MOVEMENT_SPEED);
            }

            // Reset charge duration in the item's data if applicable
            if (caller instanceof LivingEntity) {
               LivingEntity _livEnt = (LivingEntity) caller;
               ItemStack mainHandItem = _livEnt.getMainHandItem();

              // if (mainHandItem.getItem() instanceof BoltRifleItem gun) {
              //    gun.setPhase(mainHandItem, "idle");
              //    gun.setAnimationTimer(mainHandItem, 0);
              // }
            }
         }
      }
   }
}

