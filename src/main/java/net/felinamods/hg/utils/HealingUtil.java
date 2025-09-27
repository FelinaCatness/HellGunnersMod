package net.felinamods.hg.utils;

import java.util.Iterator;
import java.util.Set;
import net.felinamods.hg.network.HgModVariables;
import net.felinamods.hg.network.HgModVariables.PlayerVariables;
//import net.felinamods.hg.procedures.BayonetEffectsProcedure;
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

public class HealingUtil {
   
   // Entities to be ignored during collision
   private static final Set<String> IGNORED_ENTITIES = Set.of(
        "minecraft:armor_stand",
        "gray_fields:game_core",
        "gray_fields:point_a",
        "gray_fields:point_b",
        "gray_fields:point_c",        
        "gray_fields:flag",
        "gray_fields:flag_2",
        "gray_fields:flag_3",        
        "gray_fields:explosion_entity_a",
        "gray_fields:explosion_entity_b",
        "fels_aircraft_wwi:fokker_eiii"
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

            if(entity instanceof LivingEntity ent)
            healEntity(ent, 10f);

         }
      }
   }

public static void healEntity(LivingEntity entity, float amount) {
    float currentHealth = entity.getHealth();
    float maxHealth = entity.getMaxHealth();
    float newHealth = Math.min(currentHealth + amount, maxHealth);
    entity.setHealth(newHealth);
}


}


