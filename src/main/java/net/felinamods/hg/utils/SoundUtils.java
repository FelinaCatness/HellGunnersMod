package net.felinamods.hg.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SoundUtils {
   public static void playClientSound(ResourceLocation soundRL, Vec3 position, float volume, float pitch) {
      SoundEvent sound = (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(soundRL);
      if (sound != null && Minecraft.getInstance().level != null) {
         Minecraft.getInstance().level.playLocalSound(position.x, position.y, position.z, sound, SoundSource.PLAYERS, volume, pitch, false);
      }

   }

   public static void playServerSound(Level level, ResourceLocation soundRL, Vec3 position, float volume, float pitch) {
      if (level instanceof ServerLevel) {
         ServerLevel serverLevel = (ServerLevel)level;
         SoundEvent sound = (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(soundRL);
         if (sound != null) {
            serverLevel.playSound((Player)null, position.x, position.y, position.z, sound, SoundSource.PLAYERS, volume, pitch);
         }

      }
   }

public static void playServerShootSound(Level level, SoundEvent sound, Vec3 position, float volume, float pitch) {
    if (level instanceof ServerLevel serverLevel) {
        if (sound != null) {
            serverLevel.playSound(
                null, // null = broadcast to all nearby players
                position.x, position.y, position.z,
                sound,
                SoundSource.PLAYERS, // you can pick other sources if needed (BLOCKS, HOSTILE, etc.)
                volume,
                pitch
            );
        }
    }
}

   
}
