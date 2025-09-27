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
import net.felinamods.hg.utils.SoundUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ServerSoundMessage(String value) implements CustomPacketPayload {
    public static final Type<ServerSoundMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "server_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerSoundMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ServerSoundMessage::value,
            ServerSoundMessage::new
        );

    @Override
    public Type<ServerSoundMessage> type() {
        return TYPE;
    }

public static void handleData(final ServerSoundMessage message, final IPayloadContext context) {
    if (context.flow() == PacketFlow.SERVERBOUND) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                SoundUtils.playServerSound(
                    player.level(),
                    ResourceLocation.fromNamespaceAndPath(HgMod.MODID, message.value()), // fixed
                    player.position(),
                    0.5f,
                    1.0f
                );
            }
        });
    }
}


    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            ServerSoundMessage.TYPE,
            ServerSoundMessage.STREAM_CODEC,
            ServerSoundMessage::handleData
        );
    }
}

