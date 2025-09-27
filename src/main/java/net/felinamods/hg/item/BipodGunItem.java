package net.felinamods.hg.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.util.Mth;
import java.util.function.Consumer;
import net.felinamods.hg.utils.GunUtils;
import net.felinamods.hg.utils.MeleeUtils;
import net.felinamods.hg.utils.ParticleUtilsClient;
import net.felinamods.hg.utils.ItemUUIDHelper;
import net.felinamods.hg.utils.ClientAnimUtils;
import net.felinamods.hg.client.FelsClientHandler;
import net.felinamods.hg.network.HgModVariables;
import net.felinamods.hg.HgMod;
import net.felinamods.hg.network.ShootGunMessage;
import net.felinamods.hg.network.PlayerShakeMessage;
import net.felinamods.hg.network.ServerSoundMessage;
import net.felinamods.hg.network.GunAmmoMessage;
import net.felinamods.hg.network.ReloadGunMessage;
import net.felinamods.hg.network.RefillGunMessage;
import net.felinamods.hg.network.ClientParticleMessage;
import net.felinamods.hg.network.GunStateMessage;
import net.felinamods.hg.network.GunBayonetMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent;

import org.jetbrains.annotations.NotNull;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class BipodGunItem extends GunItem implements GeoItem {

  //Item variables
     protected double b_fireTime() {
      return 8.8;
   }   

     protected double b_firelastTime() {
      return 9.5;
   }  

     protected double b_reloadTime() {
      return 9.5;
   }  

     protected double b_reloademptyTime() {
      return 9.5;
   }

     public double b_deployTime() {
      return 3.0;
   } 

     public double b_removeTime() {
      return 3.0;
   }    

     public boolean needsBipodtoShoot() {
     	return false;
     }

     protected boolean needsBipodtoReload() {
     	return false;
     } 

     public String bipodWarningMessage() {
     	return "Place the gun on a hard surface";
     }

  // Item constructor
   public BipodGunItem(Properties properties) {
      super(properties.stacksTo(1));
      SingletonGeoAnimatable.registerSyncedAnimatable(this);
   }   

    // --- Setup renderer ---
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (renderer == null) {
                    renderer = createRenderer();
                }
                return renderer;
            }
        });
    }

   protected abstract BlockEntityWithoutLevelRenderer createRenderer();

 // --- Animation controllers ---

   @OnlyIn(Dist.CLIENT)
   @Override
   public void registerControllers(@NotNull ControllerRegistrar var1) {
      AnimationController var2 = (new AnimationController(this, "gun_controller", 0, this::a)).setSoundKeyframeHandler(this::a).setCustomInstructionKeyframeHandler(this::b); 
      var1.add(var2);
   } 

@OnlyIn(Dist.CLIENT)
@Override
protected PlayState a(@NotNull AnimationState<GunItem> state) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
        return PlayState.STOP;
    }

    ItemStack stack = (ItemStack) state.getData(DataTickets.ITEMSTACK);
    int ammo = this.getAmmo(stack);

    ItemDisplayContext ctx = (ItemDisplayContext) state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
    if (ctx == null || !ctx.firstPerson()) {
        return PlayState.STOP;
    }

    var vars = mc.player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);
    String anim = ClientAnimUtils.currentState; // states: idle, reload, fire
    double elapsed = this.getTick(stack) - ClientAnimUtils.animStartTick;

    if (anim == null || anim.isEmpty()) {
        return PlayState.STOP;
    }

    String fullAnim = "animation.model." + anim;
    String AnimPrefix = "animation.model.";

 if (ClientAnimUtils.currentState.equals("fire")) {
    if (elapsed < this.fireTime()) {

    } else {
        if (ammo > 0) {
         ClientAnimUtils.setState("idle");       	
        }
        else if (ammo == 0) {
         ClientAnimUtils.setState("idleempty");        	
        }        
        
    }
   }

 else if (ClientAnimUtils.currentState.equals("bipod_fire")) {
    if (elapsed < this.fireTime()) {

    } else {
        if (ammo > 0) {
         ClientAnimUtils.setState("bipod_idle");       	
        }
        else if (ammo == 0) {
         ClientAnimUtils.setState("bipod_idleempty");        	
        }        
        
    }
   }

  else if (ClientAnimUtils.currentState.equals("firelast")) {
    if (elapsed < this.firelastTime()) {

    } else {
         ClientAnimUtils.setState("idleempty");         	
        }                
    } 

  else if (ClientAnimUtils.currentState.equals("bipod_firelast")) {
    if (elapsed < this.firelastTime()) {

    } else {
         ClientAnimUtils.setState("bipod_idleempty");         	
        }                
    }    

  else if (ClientAnimUtils.currentState.equals("reload")) {
    if (elapsed < this.reloadTime()) {

    } else {
         ClientAnimUtils.setState("idle");         	
        }                
    }

  else if (ClientAnimUtils.currentState.equals("bipod_reload")) {
    if (elapsed < this.b_reloadTime()) {

    } else {
         ClientAnimUtils.setState("bipod_idle");         	
        }                
    }    

  else if (ClientAnimUtils.currentState.equals("reloadempty")) {
    if (elapsed < this.reloademptyTime()) {

    } else {
         ClientAnimUtils.setState("idle");         	
        }                
    }  

  else if (ClientAnimUtils.currentState.equals("bipod_reloadempty")) {
    if (elapsed < this.b_reloademptyTime()) {

    } else {
         ClientAnimUtils.setState("bipod_idle");         	
        }                
    }

  else if (ClientAnimUtils.currentState.equals("bipod_remove")) {
    if (elapsed < this.b_removeTime()) {

    } else {
         ClientAnimUtils.setState("idle");         	
        }                
    }    

  else if (ClientAnimUtils.currentState.equals("bipod_deploy")) {
    if (elapsed < this.b_deployTime()) {

    } else {
        if (ammo > 0) {
         ClientAnimUtils.setState("bipod_idle");       	
        }
        else if (ammo == 0) {
         ClientAnimUtils.setState("bipod_idleempty");        	
        }          	
        }                
    } 

  else if (ClientAnimUtils.currentState.equals("draw")) {
    if (elapsed < this.drawTime()) {

    } else {
        if (ammo > 0) {
         ClientAnimUtils.setState("idle");       	
        }
        else if (ammo == 0) {
         ClientAnimUtils.setState("idleempty");        	
        }          	
        }                
    }    
     

    // For non-idle (fire, firelast, reload, etc.) register all animations here
     if (anim.equals("fire") && ammo > 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "fire")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "fire"));         
        }
    }

    else if (anim.equals("firelast") && ammo > 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "firelast")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "firelast"));         
        }
    }  

    else if (anim.equals("bipod_fire") && ammo > 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "bipod_fire")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "bipod_fire"));         
        }
    }   

    else if (anim.equals("bipod_firelast") && ammo > 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "bipod_firelast")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "bipod_firelast"));         
        }
    }     
   
    else if (anim.equals("reload") && ammo > 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "reload")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "reload"));         
        }
    }

    else if (anim.equals("reload") && ammo == 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "reloadempty")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "reloadempty"));          
        }
    } 

    else if (anim.equals("bipod_reload") && ammo > 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "bipod_reload")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "bipod_reload"));         
        }
    } 

    else if (anim.equals("bipod_reload") && ammo == 0) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "bipod_reloadempty")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "bipod_reloadempty"));          
        }
    }     

    else if (anim.equals("draw")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "draw")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "draw"));         
        }
    }  

    else if (anim.equals("bipod_deploy")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "bipod_deploy")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "bipod_deploy"));         
        }
    } 

    else if (anim.equals("bipod_remove")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "bipod_remove")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "bipod_remove"));         
        }
    }     
        
    // For idle states
    else if ((anim.equals("idle") && ammo > 0 && !vars.currentGunOnBipod) || (anim.equals("idleempty") && ammo == 0 && !vars.currentGunOnBipod)) {
        if (!state.isCurrentAnimationStage(fullAnim)) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop(fullAnim));
        }
    }  

    else if ((anim.equals("bipod_idle") && ammo > 0 && vars.currentGunOnBipod) || (anim.equals("bipod_idleempty") && ammo == 0 && vars.currentGunOnBipod)) {
        if (!state.isCurrentAnimationStage(fullAnim)) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop(fullAnim));
        }
    }     

    return PlayState.CONTINUE;
} 

 // Ticking Item

@Override
public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {

    if (selected) {
        this.initializeGunData(stack);
        this.gunTick(world, entity, stack);
    }
} 

@Override
   protected void gunTick(Level world, Entity entity, ItemStack stack) {
      int ammo = this.getAmmo(stack);
      int intammo = this.getInternalAmmo(stack);
      var vars = entity.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);  

      if (vars.zoomValue != zoomValue()){
      	vars.zoomValue = zoomValue();
     	vars.syncPlayerVariables(entity);
      }
     
      if (modifiesWeight()){
      	if (weightPreset().equals("Heavy")){
      		applyWeight(entity, 1);
      	}
      }      

   }

@Override
protected void initializeGunData(ItemStack stack) {
    CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    CompoundTag tag = customData.copyTag();

    setIfMissing(tag, "ammo", () -> this.setAmmo(stack, this.maxAmmo()));
    setIfMissing(tag, "internalAmmo", () -> this.setInternalAmmo(stack, this.internalAmmo()));
    setIfMissing(tag, "flash", () -> this.setFlash(stack,false));  
    setIfMissing(tag, "belt", () -> this.setBelt(stack, true));     
    setIfMissing(tag, "uuid", () -> ItemUUIDHelper.getOrCreateUUID(stack));    
}

private void setIfMissing(CompoundTag tag, String key, Runnable setter) {
    if (!tag.contains(key)) {
        setter.run();
    }
}   

// NBT getters

   public boolean getBelt(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getBoolean("belt");
   } 

   public int getCurrentAmmo(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getInt("ammo");
   }   

  // NBT setters   

    public void setBelt(ItemStack stack, boolean value) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putBoolean("belt", value);
      });
   }  

//

@Override
   public void serverShoot(Level world, Entity shooter, ItemStack stack) {
      //if (world.isClientSide()) return;   	
      int ammo = this.getAmmo(stack);
      var vars = shooter.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 

         if (ammo > 0 && !vars.currentGunOnBipod) {

            this.setAmmo(stack, ammo - 1);
            this.Shoot(world, shooter, stack);
            this.VRecoil(shooter);
            this.setFlash(stack, true);
            this.effectParticle(shooter);  
         }
         else if (ammo > 0 && vars.currentGunOnBipod) {

            this.setAmmo(stack, ammo - 1);
            this.Shoot(world, shooter, stack);
            this.setFlash(stack, true);
            this.effectParticle(shooter);  
         }
         
   }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void clientShoot(ItemStack stack, Player player){

     Minecraft mc = Minecraft.getInstance();  	
     var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 
    
     if (vars.currentGunAnimation.equals("idle") && vars.currentGunTime == 0 || vars.currentGunAnimation.equals("bipod_idle") && vars.currentGunTime == 0) {
        PacketDistributor.sendToServer(new ShootGunMessage());        
     }
  }

@Override
   public void serverReload(Level world, Entity shooter, ItemStack stack) {
      //if (world.isClientSide()) return;   	
      int ammo = this.getAmmo(stack);
      int intammo = this.getInternalAmmo(stack);
      var vars = shooter.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 

      if (intammo > 0){

       if (!emptyReload() && !needsBipodtoReload()){
          if (ammo > 0 && !vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "reload";
         	vars.currentGunTime = reloadTime();
            vars.syncPlayerVariables(shooter);         	             
           }
           else if (ammo == 0 && !vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "reloadempty";
         	vars.currentGunTime = reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         } 
           else if (ammo > 1 && vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "bipod_reload";
         	vars.currentGunTime = b_reloadTime();
            vars.syncPlayerVariables(shooter);          	            
         } 
           else if (ammo == 0 && vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "bipod_reloadempty";
         	vars.currentGunTime = b_reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         }         
       }

 else if (!emptyReload() && needsBipodtoReload()){
          if (ammo > 0 && vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "bipod_reload";
         	vars.currentGunTime = b_reloadTime();
            vars.syncPlayerVariables(shooter);         	             
           }
           else if (ammo == 0 && vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "bipod_reloadempty";
         	vars.currentGunTime = b_reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         } 
         else {
           if (shooter instanceof Player pl) {
           	pl.displayClientMessage(Component.literal(reloadMessage()), true);        	             
           }          	
         }
       }       
       	
 else if (emptyReload() && !needsBipodtoReload()){
          if (ammo > 0) {
           if (shooter instanceof Player pl) {
           	pl.displayClientMessage(Component.literal(reloadMessage()), true);        	             
           }         	             
           }
           else if (ammo == 0 && !vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "reloadempty";
         	vars.currentGunTime = reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         } 
           else if (ammo == 0 && vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "bipod_reloadempty";
         	vars.currentGunTime = b_reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         }          
       }         

 else if (emptyReload() && needsBipodtoReload()){
          if (ammo > 0 && vars.currentGunOnBipod) {
           if (shooter instanceof Player pl) {
           	pl.displayClientMessage(Component.literal(reloadMessage()), true);        	             
           }         	             
           }
           else if (ammo == 0 && vars.currentGunOnBipod) {
         	vars.currentGunAnimation = "bipod_reloadempty";
         	vars.currentGunTime = b_reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         } 
         else {
           if (shooter instanceof Player pl) {
           	pl.displayClientMessage(Component.literal(bipodWarningMessage()), true);        	             
           }          	
         }
       }  

      }      
    }
   


  @OnlyIn(Dist.CLIENT)
  @Override
  public void clientReload(ItemStack stack, Player player){

     Minecraft mc = Minecraft.getInstance();  	
     var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 
    

  }  

}

