package net.felinamods.hg.client;

import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@EventBusSubscriber(value = Dist.CLIENT)
public class ZoomManager {
    private static ViewportEvent.ComputeFov provider = null;
    private static float zoomProgress = -1f; // -1 = not initialized

    public static void setFOV(double fov) {
        provider.setFOV(fov);
    }

    @SubscribeEvent
    public static void computeFOV(ViewportEvent.ComputeFov event) {
        provider = event;
        ClientLevel level = Minecraft.getInstance().level;
        Entity entity = provider.getCamera().getEntity();
        if (level != null && entity != null) {
            float deltaTicks = (float) event.getPartialTick(); // cast double → float
            execute(entity, deltaTicks);
        }
    }

    public static void execute(Entity entity, float deltaTicks) {
        if (!(entity instanceof Player living)) return;

        var vars = living.getData(
            net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES
        );
        boolean isAiming = vars.isAiming;
        float zoomValue = vars.zoomValue != 0.0f ? (float) vars.zoomValue : 1.0f;

        float baseFOV = (float) provider.getFOV();
        float targetFOV = isAiming ? baseFOV * zoomValue : baseFOV;

        if (zoomProgress < 0) zoomProgress = baseFOV; // init first use

        // Frame-rate independent smoothing
        float speed = 0.02f; // higher = faster snap
        float alpha = 1.0f - (float)Math.exp(-speed * deltaTicks);
        zoomProgress += (targetFOV - zoomProgress) * alpha;

        // ✅ Clamp to avoid floating-point drift
        if (Math.abs(targetFOV - zoomProgress) < 0.001f) {
            zoomProgress = targetFOV;
        }

        setFOV(zoomProgress);
    }
}

