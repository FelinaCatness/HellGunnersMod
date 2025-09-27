package net.felinamods.hg.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.felinamods.hg.init.HgModParticleTypes;

public class SpawnProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof Level)
			((Level) world).playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("hg:ric")), SoundSource.BLOCKS, 1, 1);
		if (world instanceof ServerLevel)
			((ServerLevel) world).sendParticles((ParticleTypes.SMOKE), x, y, z, 1, 0, 0, 0, 0.1);
		if (world instanceof ServerLevel)
			((ServerLevel) world).sendParticles(((SimpleParticleType) (HgModParticleTypes.TRACERL.get())), x, y, z, 2, 0.1, 0.1, 0.1, 1);
	}
}
