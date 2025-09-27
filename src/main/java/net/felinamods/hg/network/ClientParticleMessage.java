package net.felinamods.hg.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import net.felinamods.hg.HgMod;
import net.felinamods.hg.client.FelsClientHandler;
import net.felinamods.hg.utils.ClientUtil;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ClientParticleMessage(double x, double y, double z, String effect) implements CustomPacketPayload {
    public static final Type<ClientParticleMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "client_particle"));

    // Encode + decode automatically with StreamCodec
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientParticleMessage> STREAM_CODEC =
        StreamCodec.of(
            (buf, msg) -> {
                buf.writeDouble(msg.x);
                buf.writeDouble(msg.y);
                buf.writeDouble(msg.z);
                buf.writeUtf(msg.effect);
            },
            buf -> new ClientParticleMessage(
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readUtf()
            )
        );

    @Override
    public Type<ClientParticleMessage> type() {
        return TYPE;
    }

public static void handleData(final ClientParticleMessage message, final IPayloadContext context) {
    if (context.flow() == PacketFlow.CLIENTBOUND) {
        context.enqueueWork(() -> {
            Player player = ClientUtil.getClientPlayer(); // <-- local client player
            if (player == null) return;

            FelsClientHandler.ParticleClient(
                player,
                message.x(),
                message.y(),
                message.z(),
                message.effect()
            );
        });
    }
}



    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            ClientParticleMessage.TYPE,
            ClientParticleMessage.STREAM_CODEC,
            ClientParticleMessage::handleData
        );
    }
}
