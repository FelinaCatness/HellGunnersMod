package net.felinamods.hg.client;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;

import net.felinamods.hg.network.HgModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RecoilTick {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

private static void execute(@Nullable Event event, Entity entity) {
	if (entity == null)
		return;

	var vars = entity.getData(HgModVariables.PLAYER_VARIABLES);

	if (vars.recoilRemainingPitch > 0.01f) {
		// Dynamic step with acceleration effect
		float accelerationFactor = 0.35f;
		float minStep = 0.5f;
		float maxStep = 3.5f;

		float dynamicStep = (float) vars.recoilRemainingPitch * accelerationFactor;
		float step = Mth.clamp(dynamicStep, minStep, maxStep);

		// Final apply amount this tick
		float apply = Math.min(step, (float) vars.recoilRemainingPitch);

		// Apply pitch recoil (looking up is negative)
		entity.setXRot(Mth.clamp(entity.getXRot() - apply, -50f, 50f));

		// Track and sync
		vars.recoilRemainingPitch -= apply;
		vars.recoilOffsetY = entity.getXRot();
		vars.syncPlayerVariables(entity);
	}
}


}

