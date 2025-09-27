package net.felinamods.hg.client;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import net.felinamods.hg.network.HgModVariables;
import net.felinamods.hg.item.GunItem;
//import net.felinamods.hg.item.BayonetRifleItem;
import net.felinamods.hg.utils.ItemUUIDHelper;
import net.felinamods.hg.utils.PlayerAnimUtil;
import net.felinamods.hg.utils.ClientUtil;
import net.felinamods.hg.utils.ClientAnimUtils;

import javax.annotation.Nullable;
import net.minecraft.client.player.LocalPlayer;

@EventBusSubscriber
public class EmptyGunVariable {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event, event.getEntity());
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (!(entity instanceof Player pl)) return;

        var vars = pl.getData(HgModVariables.PLAYER_VARIABLES);
        ItemStack held = pl.getMainHandItem();

        // 🔹 Case 1: Empty hand → reset everything
        if (held.isEmpty()) {
            vars.currentGunTime = 0;
            vars.currentGunAnimation = "";
            vars.isAiming = false;
            vars.currentGunBayonetAttached = false;
            vars.currentGunUUID = "";
            vars.syncPlayerVariables(pl);

          if (pl.level().isClientSide){
            Player clientPl = ClientUtil.getClientPlayer();
          	
            if (clientPl == null) {
            	return;
            }
            else if (clientPl != null) {
            	ClientAnimUtils.currentState = "";
            }
          }
   
            PlayerAnimUtil.playAnimation("cancel_anim", true, pl.level(), pl);
            return;
        }

        // 🔹 Case 2: Player is holding a gun (any subclass)
        if (held.getItem() instanceof GunItem gun) {
            String uuid = ItemUUIDHelper.getUUIDString(held);

            // Only refresh state if player switched guns
            if (!uuid.equals(vars.currentGunUUID)) {
                vars.currentGunUUID = uuid;
                vars.currentGunTime = gun.drawTime();
                vars.currentGunAnimation = "draw";
                vars.isAiming = false;

          if (pl.level().isClientSide){
            Player clientPl = ClientUtil.getClientPlayer();
            
            if (clientPl == null) {
            	return;
            }
            else if (clientPl != null) {
            	ClientAnimUtils.currentState = "draw";
            	ClientAnimUtils.setStartTick(gun.getTick(held));
            }                
          }

                // Bayonet check (only applies to bayonet rifles)
             //   if (gun instanceof BayonetRifleItem rifle) {
             //       vars.currentGunBayonetAttached = rifle.getBayonet(held);
             //   } else {
             //       vars.currentGunBayonetAttached = false;
             //   }

                vars.syncPlayerVariables(pl);

                // Play idle animation based on gun type
                switch (gun.gunType()) {
                    case "Rifle" ->
                        PlayerAnimUtil.playAnimation("rifle_idle", true, pl.level(), pl);
                    case "Pistol" ->
                        PlayerAnimUtil.playAnimation("pistol_idle", true, pl.level(), pl);
                    default ->
                        PlayerAnimUtil.playAnimation("cancel_anim", true, pl.level(), pl);
                }
            }

         else {

          if (pl.level().isClientSide){
            Player clientPl = ClientUtil.getClientPlayer();
            
            if (clientPl == null) {
            	return;
            }
            else if (clientPl != null && ClientAnimUtils.currentState.equals("")) {
            	ClientAnimUtils.currentState = "draw";
            	ClientAnimUtils.setStartTick(gun.getTick(held));
            }                
          }
            	
            }
        }
        else {
            PlayerAnimUtil.playAnimation("cancel_anim", true, pl.level(), pl);        	
        }
    }
}
