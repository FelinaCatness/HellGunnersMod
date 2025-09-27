package net.felinamods.hg.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleTypes;

public class GrenadeTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.addParticle(ParticleTypes.LARGE_SMOKE, (x + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), (y + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), (z + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), 0, 0, 0);
		world.addParticle(ParticleTypes.LARGE_SMOKE, (x + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), (y + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), (z + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), 0, 0, 0);
		world.addParticle(ParticleTypes.LARGE_SMOKE, (x + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), (y + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), (z + Mth.nextDouble(RandomSource.create(), -0.4, 0.4)), 0, 0, 0);
	}
}
