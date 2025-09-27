package net.felinamods.hg.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.Mth;
import java.util.function.Consumer;
import net.felinamods.hg.utils.GunUtils;
import net.felinamods.hg.utils.FireGunUtils;
import net.felinamods.hg.utils.ParticleUtilsClient;
import net.felinamods.hg.utils.ItemUUIDHelper;
import net.felinamods.hg.utils.PlayerAnimUtil;
import net.felinamods.hg.utils.ClientAnimUtils;
import net.felinamods.hg.utils.SoundUtils;
import net.felinamods.hg.utils.ProjectileUtils;
import net.felinamods.hg.client.FelsClientHandler;
import net.felinamods.hg.HgMod;
import net.felinamods.hg.network.HgModVariables;
import net.felinamods.hg.network.ShootGunMessage;
import net.felinamods.hg.network.PlayerShakeMessage;
import net.felinamods.hg.network.ServerSoundMessage;
import net.felinamods.hg.network.GunAmmoMessage;
import net.felinamods.hg.network.ReloadGunMessage;
import net.felinamods.hg.network.RefillGunMessage;
import net.felinamods.hg.network.ClientParticleMessage;
import net.felinamods.hg.network.GunStateMessage;
import net.felinamods.hg.network.BeltTriggerMessage;
import net.felinamods.hg.network.GunFlashMessage;
import net.felinamods.hg.network.LocalSoundMessage;
import net.felinamods.hg.network.SoundStreamMessage;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.UseAnim;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
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
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;

import org.jetbrains.annotations.NotNull;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class GunItem extends Item implements GeoItem {

   protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

//ammo variable   

   protected int maxAmmo() {
      return 8; //8
   }

   protected int internalAmmo() {
      return 64;
   }

   protected boolean useInternalAmmo() {
      return true;
   }

 //gun stats  

   protected double distance() {
      return 90.0D;
   }

   protected float damage() {
      return 6.0F;
   }

   protected boolean isPiercing() {
      return false;
   }

   protected int maxHits() {
      return 1;
   }

   protected double fallOffStart() {
      return 70.0D;
   }

   protected double fallOffEnd() {
      return 80.0D;
   }

   protected float fallOffMultiplier() {
      return 0.4F;
   }

  //animation timing   

   protected double fireTime() {
      return 5.0;
   }

   protected double firelastTime() {
      return 5.0;
   }

   protected double reloadTime() {
      return 70.0;
   }

   protected double reloademptyTime() {
      return 104.0;
   }

   public double drawTime() {
      return 16.0;
   }
   

   //particle settings

   protected double particleX() {
      return 0.15D;
   }

   protected double particleY() {
      return -0.02D;
   }

   protected double particleZ() {
      return 1.1D;
   }

   protected double particleXA() {
      return 0.0D;
   }

   protected double particleYA() {
      return 0.0D;
   }

   protected double particleZA() {
      return 1.1D;
   }

   protected String effect1() {
      return "minecraft:smoke";
   }

   //recoil control

   protected float horizontalRecoil() {
      return (float)(Math.random() * 1.0D - 0.5D);
   }

   protected float verticalRecoil() {
      return 2.6F;
   }

   public float recoilEffect() {
   	  return 4.5F; //5.5
   }

   public int recoilTime() {
   	  return 50;
   }

   //aim zoom value

   public boolean canADS() {
   	return true;
   }

   protected float zoomValue() {
      return 0.9F;
   }  

  // fire mode settings

  public boolean isAutomatic() {
  	return false;
  }

  protected boolean canChangeFireRate() {
  	return false;
  }

  // especial settings

  protected String shootType() {
  	return "bullet";
  }

  public boolean hasBayonet() {
  	return false;
  }

  public boolean hasBipod() {
  	return false;
  } 

  protected boolean hasScope() {
  	return false;
  }   

  public boolean emptyReload() {
  	return false;
  }

  public String reloadMessage() {
  	return "The magazine needs to be empty";
  }

  protected boolean itJams() {
  	return false;
  }

  protected boolean modifiesWeight() {
  	return false;
  }

  protected String weightPreset() {
  	return "Light";
  }

//param needed for third person anim

  public String gunType() {
  	return "Rifle"; //"Rifle", "Pistol"
  }

  // default sounds

   protected abstract SoundEvent getShootSound();

  // Item constructor
   public GunItem(Properties properties) {
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

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    super.initializeClient(consumer);
    consumer.accept(new IClientItemExtensions() {
        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate((float)i * 0.56F, -0.52F, -0.72F);
            return true;
        }
    });
}

// Extra properties

   public boolean isPerspectiveAware() {
      return true;
   }

   public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
      return true;
   }

   @Override
   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      return false; // no entity damage
   }

   @Override
   public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
      return false; // no block breaking
   }    

   @Override
   public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
     return true; // can't hurt entities
   }   

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.NONE;
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return false;
   }

   public float getDestroySpeed(ItemStack stack, BlockState state) {
      return 0.0F;
   }

   public boolean shouldPlayAnimsWhileGamePaused() {
      return true;
   }   

 // --- Animation controllers ---

   @OnlyIn(Dist.CLIENT)
   public void registerControllers(@NotNull ControllerRegistrar var1) {
      AnimationController var2 = (new AnimationController(this, "gun_controller", 0, this::a)).setSoundKeyframeHandler(this::a).setCustomInstructionKeyframeHandler(this::b); 
      var1.add(var2);
   } 

@OnlyIn(Dist.CLIENT)
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
    //state.setControllerSpeed(0.1f);

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

  else if (ClientAnimUtils.currentState.equals("firelast")) {
    if (elapsed < this.firelastTime()) {

    } else {
         ClientAnimUtils.setState("idleempty");         	
        }                
    }    

  else if (ClientAnimUtils.currentState.equals("reload")) {
    if (elapsed < this.reloadTime()) {

    } else {
         ClientAnimUtils.setState("idle");         	
        }                
    }

  else if (ClientAnimUtils.currentState.equals("bolt")) {
      ClientAnimUtils.setState("reload");               	          	               
    }    

  else if (ClientAnimUtils.currentState.equals("reloadempty")) {
    if (elapsed < this.reloademptyTime()) {

    } else {
         ClientAnimUtils.setState("idle");         	
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
   
    else if (anim.equals("reload")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "reload")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "reload"));         
        }
    }

    else if (anim.equals("reloadempty")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "reloadempty")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "reloadempty"));          
        }
    } 

    else if (anim.equals("bolt")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "reload")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "reload"));          
        }
    }     

    else if (anim.equals("draw")) {
        if (!state.isCurrentAnimationStage(AnimPrefix + "draw")) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(AnimPrefix + "draw"));         
        }
    }     
        
    // For idle states
    else if ((anim.equals("idle") && ammo > 0) || (anim.equals("idleempty") && ammo == 0)) {
        if (!state.isCurrentAnimationStage(fullAnim)) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop(fullAnim));
        }
    }



    return PlayState.CONTINUE;
}

   @OnlyIn(Dist.CLIENT)
   protected void b(@NotNull CustomInstructionKeyframeEvent<GunItem> var1) {
      Minecraft var2 = Minecraft.getInstance();
      LocalPlayer var3 = var2.player;
      ClientLevel var4 = var2.level;
      var vars = var3.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 
      if (var3 != null && var4 != null) {
         String var6 = var1.getKeyframeData().getInstructions();

         if (var6.startsWith("play:")) {
          String value = var6.substring(5);
            if (value.endsWith(";")) {
               value = value.substring(0, value.length() - 1);
               }
            PacketDistributor.sendToServer(new ServerSoundMessage(value)); 
            //var2.options.getSoundSourceOptionInstance(SoundSource.MASTER).set(0.5d); lower volume
          }         

    else if (var6.startsWith("local_play:")) {
          String value = var6.substring(11);
            if (value.endsWith(";")) {
               value = value.substring(0, value.length() - 1);
               }
           float pitch = 0.95F + 0.1F * ThreadLocalRandom.current().nextFloat();    
           SoundUtils.playClientSound(ResourceLocation.parse("fels_firearms_wwi:" + value), var3.position(), 0.5f, pitch);
          } 

     if (var6.startsWith("sync_play:")) {
        String value = var6.substring("sync_play:".length());

       if (value.endsWith(";")) {
           value = value.substring(0, value.length() - 1);
      }

       // Format: namespace:soundid,volume,pitch
       String[] parts = value.split(",");
        if (parts.length >= 1) {
          String soundRL = parts[0];
          float range = parts.length > 1 ? Float.parseFloat(parts[1]) : 1.0f;
          float volume  = parts.length > 2 ? Float.parseFloat(parts[2]) : 1.0f;

            PacketDistributor.sendToServer(new SoundStreamMessage(soundRL, range, volume));
       }
     }
         
    else if (var6.startsWith("c_state:")) {
          String value = var6.substring(8);
            if (value.endsWith(";")) {
               value = value.substring(0, value.length() - 1);
               }              
            ClientAnimUtils.setState(value);
            ClientAnimUtils.setStartTick(var1.getAnimatable().getTick(this));
          }           

    else if (var6.startsWith("state:")) { 
          String value = var6.substring(6); // deprecated
            if (value.endsWith(";")) {
              value = value.substring(0, value.length() - 1);
         }

         // now value looks like "aiming,0.5"
        String[] parts = value.split(",");
          if (parts.length == 2) {
          String state = parts[0].trim();
          double number = 0.0;
          try {
              number = Double.parseDouble(parts[1].trim());
          } catch (NumberFormatException e) {
              // fallback in case parsing fails
              number = 0.0;
          }

          // now you have them separately
          //System.out.println("[Animation Controller] State = " + state + ", Number = " + number);

          PacketDistributor.sendToServer(new GunStateMessage(state, number));
             }
         }
                 
    else if (var6.startsWith("add_ammo:")) {
         String value = var6.substring(9);
           if (value.endsWith(";")) {
              value = value.substring(0, value.length() - 1);
         }

    try {
        int ammoValue = Integer.parseInt(value); // convert string → int
        PacketDistributor.sendToServer(new GunAmmoMessage(ammoValue));
    } catch (NumberFormatException e) {
        System.err.println("Invalid ammo value: " + value);
    }
        }
           
    else if (var6.equals("refill_gun;")) {
        PacketDistributor.sendToServer(new RefillGunMessage());             
          }  
     else if (var6.equals("show_belt;")) {
        PacketDistributor.sendToServer(new BeltTriggerMessage(true));             
          }           
     else if (var6.equals("hide_belt;")) {
        PacketDistributor.sendToServer(new BeltTriggerMessage(false));             
          }        
     else if (var6.equals("hide_flash;")) {
        PacketDistributor.sendToServer(new GunFlashMessage(false));             
          }
     else if (var6.equals("show_flash;")) {
        PacketDistributor.sendToServer(new GunFlashMessage(true));             
          }           

         }
     
   } 

   @OnlyIn(Dist.CLIENT)
   protected void a(@NotNull SoundKeyframeEvent<GunItem> var1) {

    String soundId = var1.getKeyframeData().getSound();

    SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("hg:" + soundId));

    if (sound != null) {
        Minecraft mc = Minecraft.getInstance();
        SoundManager soundManager = mc.getSoundManager();

        // Add slight random pitch for variation
        float pitch = 0.95F + 0.1F * ThreadLocalRandom.current().nextFloat();
        float volume = 0.5F;

        soundManager.play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

   }    
 	  
 // Ticking Item

public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {

    if (selected) {
        this.initializeGunData(stack);
        this.gunTick(world, entity, stack);
    }
} 

   protected void gunTick(Level world, Entity entity, ItemStack stack) {
      int ammo = this.getAmmo(stack);
      var vars = entity.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);

      if (vars.zoomValue != zoomValue()){
      	vars.zoomValue = zoomValue();
     	vars.syncPlayerVariables(entity);
      }

   //  if (vars.currentGunAnimation.equals("")){    	
   //  	vars.currentGunAnimation = "draw";     	
   //  	vars.currentGunTime = drawTime();
   //  	vars.syncPlayerVariables(entity);  

   //        if (gunType().equals("Rifle")){
 	//	    PlayerAnimUtil.playAnimation("rifle_idle", true, entity.level(), entity);         	
    //       }
    //      else if (gunType().equals("Pistol")){
 	//	    PlayerAnimUtil.playAnimation("pistol_idle", true, entity.level(), entity);         	
    //       }      	
 
    // }
     
      if (modifiesWeight()){
      	if (weightPreset().equals("Heavy")){
      		applyWeight(entity, 1);
      	}
      }

   }

   // Item Init

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

  //  helpers

public static void applyWeight(Entity entity, int level) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, level, false, false));
		}
	}
  
// NBT getters 

   protected int getAmmo(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getInt("ammo");
   }

   public int getCurrentAmmo(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getInt("ammo");
   }   

   public int getCurrentIntAmmo(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getInt("internalAmmo");
   }

   protected int getInternalAmmo(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getInt("internalAmmo");
   }

   public boolean getFlash(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getBoolean("flash");
   } 

   public boolean getBelt(ItemStack stack) {
      CustomData data = (CustomData)stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag().getBoolean("belt");
   }    

 // NBT setters 

    public void setBelt(ItemStack stack, boolean value) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putBoolean("belt", value);
      });
   }  

    public void setFlash(ItemStack stack, boolean value) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putBoolean("flash", value);
      });
   }

   protected void setAmmo(ItemStack stack, int value) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putDouble("ammo", (double)value);
      });
   } 

   public void addAmmo(ItemStack stack, int value) {
   	  int ammo = getAmmo(stack);
   	  int maxammo = getInternalAmmo(stack);

   	  if (maxammo > 0){
       CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putDouble("ammo", ammo + value);
      });  
       CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putDouble("internalAmmo", maxammo - value);
      });       
   	  }

   }   

   protected void setInternalAmmo(ItemStack stack, int value) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
         tag.putDouble("internalAmmo", (double)value);
      });
   }

 // Item features

   protected static boolean isFirstPersonPlayer(Entity shooter) {
      if (shooter instanceof LocalPlayer) {
         LocalPlayer localPlayer = (LocalPlayer)shooter;
         Minecraft var2 = Minecraft.getInstance();
         return var2.options.getCameraType().isFirstPerson();
      } else {
         return false;
      }
   }

   protected void effectParticle(Entity player) {
      var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);   	
      boolean ads = vars.isAiming;
      if (!ads) {
     if (player instanceof ServerPlayer serverPlayer) {
         PacketDistributor.sendToPlayer(serverPlayer, new ClientParticleMessage(particleZ(), particleX(), particleY(), effect1()));
         }
      }

  else if (ads) {
     if (player instanceof ServerPlayer serverPlayer) {
         PacketDistributor.sendToPlayer(serverPlayer, new ClientParticleMessage(particleZA(), particleXA(), particleYA(), effect1()));
         }
      }

   }

   protected void VRecoil(Entity entity){
    // Get synced PlayerVariables
    var vars = entity.getData(HgModVariables.PLAYER_VARIABLES);
 
    vars.recoilRemainingPitch += verticalRecoil();

    // Sync to client
    vars.syncPlayerVariables(entity);
   }

   public void soundStream(Player player, String value, float range, float volume) {

// Example: send ammo sync packet to players within 32 blocks, excluding shooter
   if (player instanceof ServerPlayer shooter) {
   	
      ServerLevel level = shooter.serverLevel();

      float pitch = 0.95F + 0.1F * ThreadLocalRandom.current().nextFloat();      

      PacketDistributor.sendToPlayersNear(
       level,
       shooter,                 // exclude this player (or null to include all)
       shooter.getX(),
       shooter.getY(),
       shooter.getZ(),
       range,                    // radius in blocks
       new LocalSoundMessage(value,volume, pitch, shooter.getX(), shooter.getY(), shooter.getZ()) // your packet
      );   	

      //System.out.println("sound packet has been send");
   }


   }
   
   protected void Shoot(Level world, Entity shooter, ItemStack stack) {
      if (world instanceof ServerLevel) {
         GunUtils.shootBullet(shooter.level(),
         shooter,
         this.distance(), 
         this.damage(),
         this.isPiercing(),
         this.maxHits(), 
         this.fallOffStart(),
         this.fallOffEnd(), 
         this.fallOffMultiplier()
         );  
      }

   }

   protected void FireShoot(Level world, Entity shooter, ItemStack stack) {
      if (world instanceof ServerLevel) {
         FireGunUtils.shootBullet(shooter.level(),
         shooter,
         this.distance(), 
         this.damage(),
         this.isPiercing(),
         this.maxHits(), 
         this.fallOffStart(),
         this.fallOffEnd(), 
         this.fallOffMultiplier()
         );  
      }

   }   

   public void shakeClient (Entity player){
   	
     if (player instanceof ServerPlayer serverPlayer) {
         PacketDistributor.sendToPlayer(serverPlayer, new PlayerShakeMessage(recoilTime(), recoilEffect()));
         }

      }

   public void serverShoot(Level world, Entity shooter, ItemStack stack) {
      //if (world.isClientSide()) return;   	
      int ammo = this.getAmmo(stack);
     var vars = shooter.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);      
         if (ammo > 0) {

         	if (shootType().equals("plasma")) {

            if (shooter instanceof Player player) ProjectileUtils.spawnProjectile(world, player, 5.0f, 1, (byte)0, 5.0f, 0.0f, "plasma");
        		
         	}
       else if (shootType().equals("grenade")) {

            if (shooter instanceof Player player) ProjectileUtils.spawnProjectile(world, player, 5.0f, 1, (byte)0, 3.5f, 0.0f, "grenade");
        		
         	}   
       else if (shootType().equals("fire")) {

            this.FireShoot(world, shooter, stack); 

         	}            	
         	else {
            this.Shoot(world, shooter, stack);         		
         	}
        	
            this.setAmmo(stack, ammo - 1);
            this.VRecoil(shooter);
            this.setFlash(stack, true);
            this.effectParticle(shooter);  
         } 
      

   }

  @OnlyIn(Dist.CLIENT)
  public void clientShoot(ItemStack stack, Player player){

     Minecraft mc = Minecraft.getInstance();  	
     var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 
    
     if (vars.currentGunAnimation.equals("idle") && vars.currentGunTime == 0) {
        PacketDistributor.sendToServer(new ShootGunMessage());        
     }
  }

   public void serverReload(Level world, Entity shooter, ItemStack stack) {
      //if (world.isClientSide()) return;   	
      int ammo = this.getAmmo(stack);
      int intammo = this.getInternalAmmo(stack);
      var vars = shooter.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 

       if (intammo > 0){

        if (!emptyReload()){
          if (ammo > 0) {
         	vars.currentGunAnimation = "reload";
         	vars.currentGunTime = reloadTime();
            vars.syncPlayerVariables(shooter);         	             
         } else if (ammo == 0) {
         	vars.currentGunAnimation = "reloadempty";
         	vars.currentGunTime = reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         }       	
        }
       	
   else if (emptyReload()){
          if (ammo > 0) {
           if (shooter instanceof Player pl) {
           	pl.displayClientMessage(Component.literal(reloadMessage()), true);        	             
           }
         } else if (ammo == 0) {
         	vars.currentGunAnimation = "reloadempty";
         	vars.currentGunTime = reloademptyTime();
            vars.syncPlayerVariables(shooter);          	            
         }       	
        }

       }

   }

  @OnlyIn(Dist.CLIENT)
  public void clientReload(ItemStack stack, Player player){

     Minecraft mc = Minecraft.getInstance();  	
     var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES); 
    
  }

   public void refillingGun(Entity entity, ItemStack stack) {   	
      int intAmmo = this.getInternalAmmo(stack);
      int ammo = this.getAmmo(stack);
      int ammoNeeded = this.maxAmmo() - ammo;
      //System.out.println("Ammo needed: " + ammoNeeded);
      if (ammoNeeded > 0) {
         if (intAmmo >= ammoNeeded) {
            this.setAmmo(stack, ammo + ammoNeeded);
            this.setInternalAmmo(stack, intAmmo - ammoNeeded);
            //System.out.println("Refilled full: " + (ammo + ammoNeeded) + " | Internal: " + (intAmmo - ammoNeeded));
         } else {
            this.setAmmo(stack, ammo + intAmmo);
            this.setInternalAmmo(stack, 0);
            //System.out.println("Refilled partial: " + (ammo + intAmmo) + " | Internal: 0");
         }

      }
   }
}
 
 

