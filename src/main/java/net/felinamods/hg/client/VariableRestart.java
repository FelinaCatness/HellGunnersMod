package net.felinamods.hg.client;

import net.minecraft.world.entity.Entity;

import net.felinamods.hg.network.HgModVariables;

public class VariableRestart {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(HgModVariables.PLAYER_VARIABLES).recoilRemainingPitch != 0) {
			{
				HgModVariables.PlayerVariables _vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
				_vars.recoilRemainingPitch = 0;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(HgModVariables.PLAYER_VARIABLES).recoilOffsetY != 0) {
			{
				HgModVariables.PlayerVariables _vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
				_vars.recoilOffsetY = 0;
				_vars.syncPlayerVariables(entity);
			}
		}
		{
			HgModVariables.PlayerVariables _vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
			_vars.currentGunAnimation = "";
			_vars.syncPlayerVariables(entity);
		}
		{
			HgModVariables.PlayerVariables _vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
			_vars.currentGunTime = 0;
			_vars.syncPlayerVariables(entity);
		}
		{
			HgModVariables.PlayerVariables _vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
			_vars.isAiming = false;
			_vars.syncPlayerVariables(entity);
		}
		{
			HgModVariables.PlayerVariables _vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
			_vars.currentGunOnBipod = false;
			_vars.syncPlayerVariables(entity);
		}
	}
}

