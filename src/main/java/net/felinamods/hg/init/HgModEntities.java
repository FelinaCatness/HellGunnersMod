
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;

import net.felinamods.hg.entity.SpawnEntEntity;
import net.felinamods.hg.entity.PlasmaProjectileEntity;
import net.felinamods.hg.entity.GrenadeProjectileEntity;
import net.felinamods.hg.entity.DropEntity;
import net.felinamods.hg.HgMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class HgModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, HgMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<PlasmaProjectileEntity>> PLASMA_PROJECTILE = register("plasma_projectile",
			EntityType.Builder.<PlasmaProjectileEntity>of(PlasmaProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<GrenadeProjectileEntity>> GRENADE_PROJECTILE = register("grenade_projectile",
			EntityType.Builder.<GrenadeProjectileEntity>of(GrenadeProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<DropEntity>> DROP = register("drop",
			EntityType.Builder.<DropEntity>of(DropEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<SpawnEntEntity>> SPAWN_ENT = register("spawn_ent",
			EntityType.Builder.<SpawnEntEntity>of(SpawnEntEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		DropEntity.init(event);
		SpawnEntEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(DROP.get(), DropEntity.createAttributes().build());
		event.put(SPAWN_ENT.get(), SpawnEntEntity.createAttributes().build());
	}
}
