package net.felinamods.hg.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import net.felinamods.hg.item.GunItem;
//import net.felinamods.hg.item.ThrowableItem;
import net.felinamods.hg.item.BipodGunItem;
import net.felinamods.hg.init.HgModKeyMappings;
import net.felinamods.hg.network.GunAimMessage;
import net.felinamods.hg.network.ShootGunMessage;
import net.felinamods.hg.utils.ClientAnimUtils;


@EventBusSubscriber(value = Dist.CLIENT)
public class FelsClientHandler {

    private static boolean prevAttackDown = false;
    private static boolean prevReloadDown = false;   
    private static boolean prevAimDown = false;
     
    // Recoil pitch/yaw
    private static float pitchOffset = 0.0F;
    private static float yawOffset = 0.0F;
    private static float pitchVelocity = 0.0F;
    private static float yawVelocity = 0.0F;

    private static final float RECOIL_STIFFNESS = 0.01F;
    private static final float RECOIL_DAMPING = 0.7F;

    // Roll shake
    private static float rollOffset = 0.0F;
    private static float rollVelocity = 0.0F;
    private static final float ROLL_SHAKE_DAMPING = 0.8F;

    private static float shakeIntensity = 0.0F;
    private static int shakeTicks = 0;
    private static float currentRoll = 0.0F;  

    /** Call this to apply recoil from a gunshot */
    public static void addRecoil(float pitchAmount, float yawAmount) {
        pitchVelocity += pitchAmount;
        yawVelocity += yawAmount;
    }

   /** Call this to shake the camera roll with given intensity and duration */
    public static void addCameraShake(float intensity, int durationTicks) {
        shakeIntensity = Math.max(shakeIntensity, intensity);
        shakeTicks = Math.max(shakeTicks, durationTicks);
   }

   /** Updates the oscillating roll shake */
   private static void updateCameraShake(float deltaTicks) {
       if (shakeTicks > 0) {
           shakeTicks--;

        // Use game time + partials for smoothness
        double time = (Minecraft.getInstance().level.getGameTime() + deltaTicks) / 20.0; // seconds
        double frequency = 0.1; // tweak this for faster/slower shake
        double noise = Math.sin(time * frequency * Math.PI * 2.0);

        // Apply intensity to roll
        currentRoll = (float) (noise * shakeIntensity);

        // Framerate-independent decay
        float decayRate = 0.1f; // higher = faster decay
        float decay = (float) Math.exp(-decayRate * deltaTicks);
        shakeIntensity *= decay;

        // Stop tiny leftover shakes
        if (shakeIntensity < 0.01f) {
            shakeIntensity = 0f;
            currentRoll = 0f;
        }

    } else {
        currentRoll = 0f;
        shakeIntensity = 0f;
    }

       // Smooth recoil recovery (pitch + yaw)
        pitchVelocity += -pitchOffset * RECOIL_STIFFNESS;
        yawVelocity += -yawOffset * RECOIL_STIFFNESS;
        pitchVelocity *= RECOIL_DAMPING;
        yawVelocity *= RECOIL_DAMPING;
        pitchOffset += pitchVelocity;
        yawOffset += yawVelocity;
        }



     /** Apply custom pitch/yaw/roll to the camera */
   @SubscribeEvent
   public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
      Entity cameraEntity = event.getCamera().getEntity();
      ClientLevel level = Minecraft.getInstance().level;

       if (level != null && cameraEntity != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
           float deltaTicks = (float) event.getPartialTick();
           updateCameraShake(deltaTicks);

           event.setPitch(event.getPitch() + pitchOffset);
           event.setYaw(event.getYaw() + yawOffset);
           event.setRoll(event.getRoll() + currentRoll);
       }
     }

  public static void ParticleClient(Player player, double forward, double right, double up, String particleId) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel level = (mc.level instanceof ClientLevel cl) ? cl : null;
       if (level == null || player == null) return;

       var id = ResourceLocation.tryParse(particleId);
        if (id == null) {
          System.err.println("[FelsClientHandler] Bad particle id: " + particleId);
          return;
       }

      ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(id);
        if (!(type instanceof net.minecraft.core.particles.SimpleParticleType simple)) {
          System.err.println("[FelsClientHandler] '" + particleId + "' is not a SimpleParticleType (needs extra params).");
          return;
       }

      // build a stable basis around the view dir
      Vec3 fwd = player.getLookAngle().normalize();
      Vec3 worldUp = Math.abs(fwd.y) > 0.98 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
      Vec3 rightVec = fwd.cross(worldUp).normalize();
      Vec3 upVec = rightVec.cross(fwd).normalize();
 
      Vec3 origin = player.getEyePosition()
         .add(fwd.scale(forward))
         .add(rightVec.scale(right))
         .add(upVec.scale(up));

      // force = true so it always renders
       level.addParticle(simple, /*force*/ true, origin.x, origin.y, origin.z, 0, 0, 0);
        //System.out.println("[FelsClientHandler] spawned " + particleId + " at " + origin);
     }

    /** Handles per-tick player logic */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) return;

        var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean attackDown = mc.options.keyAttack.isDown();
        boolean reloadDown = HgModKeyMappings.RELOAD_GUN.isDown();
        boolean aimDown = mc.options.keyUse.isDown();
      
        ItemStack held = player.getMainHandItem();        

        // Detect press (edge: false -> true)
        if (attackDown && !prevAttackDown) {

           if (held.getItem() instanceof GunItem gun) {

             if (!gun.isAutomatic() && !vars.currentGunBayonetAttached && ClientAnimUtils.currentState.equals("idle")) {

              if (gun.getCurrentAmmo(held) > 1) {
                ClientAnimUtils.setState("fire");
               	 ClientAnimUtils.setStartTick(gun.getTick(held));
                 PacketDistributor.sendToServer(new ShootGunMessage());

                 addCameraShake(gun.recoilEffect(), gun.recoilTime());
              }
         else if (gun.getCurrentAmmo(held) == 1) {
                ClientAnimUtils.setState("firelast");
               	 ClientAnimUtils.setStartTick(gun.getTick(held));
                 PacketDistributor.sendToServer(new ShootGunMessage()); 

                 addCameraShake(gun.recoilEffect(), gun.recoilTime());                 
              }             
  

             }
                                       
            } 

        }

        if (reloadDown && !prevReloadDown){
             //System.out.println("[FelsClientHandler] Reload key is pressed");  

          if (held.getItem() instanceof GunItem gun){
          	if(ClientAnimUtils.currentState.equals("idle") && gun.getCurrentIntAmmo(held) > 0 || ClientAnimUtils.currentState.equals("idleempty") && gun.getCurrentIntAmmo(held) > 0) {
          		
              if (!gun.emptyReload()) {

              	if (gun.getCurrentAmmo(held) > 0) {
              	 ClientAnimUtils.setState("reload");
                 ClientAnimUtils.setStartTick(gun.getTick(held));
                  PacketDistributor.sendToServer(new GunAimMessage(false));
              	}
              	else if (gun.getCurrentAmmo(held) == 0) {
              	 ClientAnimUtils.setState("reloadempty");
                 ClientAnimUtils.setStartTick(gun.getTick(held));
                  PacketDistributor.sendToServer(new GunAimMessage(false));
              	}
              	

              }
              else if (gun.emptyReload()) {

              	if (gun.getCurrentAmmo(held) == 0) {
              	 ClientAnimUtils.setState("reload");
                 ClientAnimUtils.setStartTick(gun.getTick(held));
                  PacketDistributor.sendToServer(new GunAimMessage(false));
              	}  

          else if (gun.getCurrentAmmo(held) > 0) {
                   if (player instanceof Player pl) {
                 	pl.displayClientMessage(Component.literal(gun.reloadMessage()), true);        	             
                    }  
              	}              	
              }              
       		
          }
      else if(ClientAnimUtils.currentState.equals("bipod_idle") || ClientAnimUtils.currentState.equals("bipod_idleempty")) {

            ClientAnimUtils.setState("bipod_reload");
            ClientAnimUtils.setStartTick(gun.getTick(held));
          }          

            }              
        }

        if (aimDown && !prevAimDown){

           if (held.getItem() instanceof GunItem gun){

           	  if (!vars.aimingHold && !vars.isAiming && !vars.currentGunOnBipod && gun.canADS()){
           	  	PacketDistributor.sendToServer(new GunAimMessage(true));
           	  	//System.out.println("[FelsClientHandler] Player is Aiming");
           	  }
        else if (!vars.aimingHold && vars.isAiming && !vars.currentGunOnBipod && gun.canADS()){
           	  	PacketDistributor.sendToServer(new GunAimMessage(false));
           	  	//System.out.println("[FelsClientHandler] Player is not Aiming");        	
             }
           }

        	
        }

        // Detect hold (good for automatic)
        if (attackDown && !vars.currentGunIsShooting) {
            // System.out.println("Attack key held");
            // Automatic fire logic
           if (held.getItem() instanceof GunItem gun && !gun.hasBipod()) {

             if (gun.isAutomatic() && ClientAnimUtils.currentState.equals("idle")) {

             	if (gun.getCurrentAmmo(held) > 1) {
               	ClientAnimUtils.setState("fire");
               	ClientAnimUtils.setStartTick(gun.getTick(held));
                PacketDistributor.sendToServer(new ShootGunMessage()); 

                 addCameraShake(gun.recoilEffect(), gun.recoilTime());               		
             	}
             	else if (gun.getCurrentAmmo(held) == 1) {
               	ClientAnimUtils.setState("firelast");
               	ClientAnimUtils.setStartTick(gun.getTick(held));
                PacketDistributor.sendToServer(new ShootGunMessage()); 

                 addCameraShake(gun.recoilEffect(), gun.recoilTime());               		
             	}             	
              
             }            
            }  

        else if (held.getItem() instanceof BipodGunItem bipodgun && bipodgun.hasBipod()) {

             if (bipodgun.isAutomatic()) {

               if (!bipodgun.needsBipodtoShoot() && !vars.currentGunOnBipod) {

                   if (bipodgun.getCurrentAmmo(held) > 1 && ClientAnimUtils.currentState.equals("idle")) {
                   	  ClientAnimUtils.setState("fire");
                      ClientAnimUtils.setStartTick(bipodgun.getTick(held));
                      PacketDistributor.sendToServer(new ShootGunMessage());

                       addCameraShake(bipodgun.recoilEffect(), bipodgun.recoilTime());                      
                   }
                   
                  else if (bipodgun.getCurrentAmmo(held) == 1 && ClientAnimUtils.currentState.equals("idle")) {
                   	  ClientAnimUtils.setState("firelast");
                      ClientAnimUtils.setStartTick(bipodgun.getTick(held));
                      PacketDistributor.sendToServer(new ShootGunMessage());

                       addCameraShake(bipodgun.recoilEffect(), bipodgun.recoilTime());                       

                      
                   }                   
                }

         else if (!bipodgun.needsBipodtoShoot() && vars.currentGunOnBipod) {

                   if (bipodgun.getCurrentAmmo(held) > 1 && ClientAnimUtils.currentState.equals("bipod_idle")) {
                   	  ClientAnimUtils.setState("bipod_fire");
                      ClientAnimUtils.setStartTick(bipodgun.getTick(held));
                      PacketDistributor.sendToServer(new ShootGunMessage());

                       addCameraShake(bipodgun.recoilEffect(), bipodgun.recoilTime());                       
                   }
                   
                  else if (bipodgun.getCurrentAmmo(held) == 1 && ClientAnimUtils.currentState.equals("bipod_idle")) {
                   	  ClientAnimUtils.setState("bipod_firelast");
                      ClientAnimUtils.setStartTick(bipodgun.getTick(held));
                      PacketDistributor.sendToServer(new ShootGunMessage());

                       addCameraShake(bipodgun.recoilEffect(), bipodgun.recoilTime());                       
                   }                   
                }    

         else if (bipodgun.needsBipodtoShoot() && vars.currentGunOnBipod) {

                   if (bipodgun.getCurrentAmmo(held) > 1 && ClientAnimUtils.currentState.equals("bipod_idle")) {
                   	  ClientAnimUtils.setState("bipod_fire");
                      ClientAnimUtils.setStartTick(bipodgun.getTick(held));
                      PacketDistributor.sendToServer(new ShootGunMessage());

                       addCameraShake(bipodgun.recoilEffect(), bipodgun.recoilTime());                       
                   }
                   
                  else if (bipodgun.getCurrentAmmo(held) == 1 && ClientAnimUtils.currentState.equals("bipod_idle")) {
                   	  ClientAnimUtils.setState("bipod_firelast");
                      ClientAnimUtils.setStartTick(bipodgun.getTick(held));
                      PacketDistributor.sendToServer(new ShootGunMessage());

                       addCameraShake(bipodgun.recoilEffect(), bipodgun.recoilTime());                       
                   }                   
                } 

         else if (bipodgun.needsBipodtoShoot() && !vars.currentGunOnBipod) {
         	
                   if (player instanceof Player pl) {
                 	pl.displayClientMessage(Component.literal(bipodgun.bipodWarningMessage()), true);        	             
                    }   
                  
                }                 

             }

             }            
 
        }

        else if (!attackDown && vars.currentGunIsShooting) {

         //PacketDistributor.sendToServer(new GunAutoShootMessage(false)); 
        // System.out.println("[FelsClientHandler] Stopping client shoot method");         
        }

        prevAttackDown = attackDown;
        prevReloadDown = reloadDown;  
        prevAimDown = aimDown;
        
    }
}


