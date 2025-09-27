package net.felinamods.hg.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.felinamods.hg.HgMod;
import net.felinamods.hg.item.GunItem;
//import net.felinamods.gh.item.BayonetRifleItem;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record GunFlashMessage(boolean aiming) implements CustomPacketPayload {
    public static final Type<GunFlashMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "gun_flash"));

    // codec for encoding/decoding a boolean
    public static final StreamCodec<RegistryFriendlyByteBuf, GunFlashMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.BOOL,          // how to encode/decode a boolean
            GunFlashMessage::aiming,      // getter for the boolean
            GunFlashMessage::new          // constructor
        );

    @Override
    public Type<GunFlashMessage> type() {
        return TYPE;
    }

    public static void handleData(final GunFlashMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && player.getMainHandItem().getItem() instanceof GunItem gun) {
                 gun.setFlash(player.getMainHandItem(), message.aiming());
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            GunFlashMessage.TYPE,
            GunFlashMessage.STREAM_CODEC,
            GunFlashMessage::handleData
        );
    }
}

