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
import net.felinamods.hg.item.BipodGunItem;
//import net.felinamods.hg.item.BayonetRifleItem;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record BeltTriggerMessage(boolean aiming) implements CustomPacketPayload {
    public static final Type<BeltTriggerMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "belt_trigger"));

    // codec for encoding/decoding a boolean
    public static final StreamCodec<RegistryFriendlyByteBuf, BeltTriggerMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.BOOL,          // how to encode/decode a boolean
            BeltTriggerMessage::aiming,      // getter for the boolean
            BeltTriggerMessage::new          // constructor
        );

    @Override
    public Type<BeltTriggerMessage> type() {
        return TYPE;
    }

    public static void handleData(final BeltTriggerMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && player.getMainHandItem().getItem() instanceof BipodGunItem gun) {
                 gun.setBelt(player.getMainHandItem(), message.aiming());
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            BeltTriggerMessage.TYPE,
            BeltTriggerMessage.STREAM_CODEC,
            BeltTriggerMessage::handleData
        );
    }
}


