package net.felinamods.felsfirearmswwi.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.felinamods.hg.procedures.BipodCheckProcedure;


@EventBusSubscriber
public class OnBipodGunProcedure {
	private static final String LOCKED_SLOT_TAG = "LockedSlot";

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
        var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);  		

		if (vars.currentGunOnBipod) {
			// ✅ Rotation lock logic
			if (!player.getPersistentData().contains("baseYaw") || !player.getPersistentData().contains("basePitch"))
				return;

			float baseYaw = player.getPersistentData().getFloat("baseYaw");
			float basePitch = player.getPersistentData().getFloat("basePitch");
			int pslot = player.getPersistentData().getInt("selectedSlot");			

			float currentYaw = player.getYRot();
			float currentPitch = player.getXRot();

			// Clamp yaw to ±45° from baseYaw
			float yawOffset = wrapYaw(currentYaw - baseYaw);
			float clampedYaw = baseYaw + clamp(yawOffset, -15f, 15f);

			// Clamp pitch to -10 to +5 around basePitch
			float pitchOffset = currentPitch - basePitch;
			float clampedPitch = basePitch + clamp(pitchOffset, -5f, 5f);

			// Apply rotation
			player.setYRot(clampedYaw);
			player.setYHeadRot(clampedYaw);
			player.setYBodyRot(clampedYaw);
			player.setXRot(clampedPitch);

			if (player.getInventory().selected != pslot) {
				//player.getInventory().selected = pslot;
				BipodCheckProcedure.undeploy(player, vars);
			}			

		} 
	}

	private static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}

	private static float wrapYaw(float angle) {
		angle = angle % 360f;
		if (angle < -180f) angle += 360f;
		if (angle > 180f) angle -= 360f;
		return angle;
	}
}





