package net.felinamods.hg.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Helper class for segregating client-side code
 */
@OnlyIn(Dist.CLIENT)
public final class ClientUtil {
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }
}

