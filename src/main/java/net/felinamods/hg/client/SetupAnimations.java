package net.felinamods.hg.client;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

// Java standard imports
import java.util.Optional;
import java.util.function.Function;

// Minecraft imports
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

import net.felinamods.hg.HgMod;
import net.felinamods.hg.item.GunItem;

import javax.annotation.Nullable;

// Player Animator API imports
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;

@EventBusSubscriber(modid = "hg", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SetupAnimations {

@SubscribeEvent
public static void onClientSetup(FMLClientSetupEvent event) {
    PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register((player, animationStack) -> {
ModifierLayer<IAnimation> layer = new ModifierLayer<>();

layer.addModifier(new AdjustmentModifier(partName -> {
    if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer)) {
        return Optional.empty();
    }

    ItemStack held = localPlayer.getMainHandItem();
    String type = "";

    if (held.getItem() instanceof GunItem gun){
      if(gun.gunType().equals("Rifle")) {
      type = "Rifle";      
      }
 else if(gun.gunType().equals("Pistol")) {
      type = "Pistol";
      }      
    }
    else {
      type = "None";    	
    }

    // Only use pitch
    float pitchRad = -(float) Math.toRadians(localPlayer.getXRot());

    float rotationX = 0, rotationY = 0, rotationZ = 0;

    if (type.equals("Rifle") && partName.equals("rightArm") || type.equals("Rifle") && partName.equals("leftArm")) {
        rotationX = -pitchRad; // rotate arms up/down
        // rotationY stays 0
    }
else if (type.equals("Pistol") && partName.equals("rightArm")) {
        rotationX = -pitchRad; // rotate arms up/down
        // rotationY stays 0
    }
    else {
        return Optional.empty();
    }

    return Optional.of(new AdjustmentModifier.PartModifier(
            new Vec3f(rotationX, rotationY, rotationZ),
            Vec3f.ZERO,
            Vec3f.ZERO
    ));
}), 0); // priority



        animationStack.addAnimLayer(69, layer);
        PlayerAnimationAccess.getPlayerAssociatedData(player)
            .set(ResourceLocation.fromNamespaceAndPath("hg", "player_animation"), layer);
    });
}


	@EventBusSubscriber(modid = "hg", bus = EventBusSubscriber.Bus.MOD)
	public static record HgModAnimationMessage(String animation, int target, boolean override) implements CustomPacketPayload {

		public static final Type<HgModAnimationMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "setup_animations"));
		public static final StreamCodec<RegistryFriendlyByteBuf, HgModAnimationMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, HgModAnimationMessage message) -> {
			buffer.writeUtf(message.animation);
			buffer.writeInt(message.target);
			buffer.writeBoolean(message.override);
		}, (RegistryFriendlyByteBuf buffer) -> new HgModAnimationMessage(buffer.readUtf(), buffer.readInt(), buffer.readBoolean()));
		@Override
		public Type<HgModAnimationMessage> type() {
			return TYPE;
		}

		public static void handleData(final HgModAnimationMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND) {
				context.enqueueWork(() -> {
					Level level = context.player().level();
					if (level.getEntity(message.target) != null) {
						Player player = (Player) level.getEntity(message.target);
						setAnimationClientside(player, message.animation, message.override);
					}
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}

		@SubscribeEvent
		public static void registerMessage(FMLCommonSetupEvent event) {
			HgMod.addNetworkMessage(HgModAnimationMessage.TYPE, HgModAnimationMessage.STREAM_CODEC, HgModAnimationMessage::handleData);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void setAnimationClientside(Player player, String anim, boolean override) {
		if (player instanceof net.minecraft.client.player.AbstractClientPlayer player_) {
			var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player_).get(ResourceLocation.fromNamespaceAndPath("hg", "player_animation"));
			if (animation != null && override ? true : !animation.isActive()) {
				animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(20, (modelName, type, value) -> value), PlayerAnimationRegistry.getAnimation(ResourceLocation.fromNamespaceAndPath("hg", anim)).playAnimation()
						.setFirstPersonMode(FirstPersonMode.DISABLED).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftItem(false)));
			}
		}
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
	}
}
