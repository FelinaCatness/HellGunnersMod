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

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record GunAimMessage(boolean aiming) implements CustomPacketPayload {
    public static final Type<GunAimMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "gun_aim"));

    // codec for encoding/decoding a boolean
    public static final StreamCodec<RegistryFriendlyByteBuf, GunAimMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.BOOL,          // how to encode/decode a boolean
            GunAimMessage::aiming,      // getter for the boolean
            GunAimMessage::new          // constructor
        );

    @Override
    public Type<GunAimMessage> type() {
        return TYPE;
    }

    public static void handleData(final GunAimMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && player.getMainHandItem().getItem() instanceof GunItem gun) {
                var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);

                vars.isAiming = message.aiming();
                vars.syncPlayerVariables(player);                
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            GunAimMessage.TYPE,
            GunAimMessage.STREAM_CODEC,
            GunAimMessage::handleData
        );
    }
}
