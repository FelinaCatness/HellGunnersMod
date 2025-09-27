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
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.felinamods.hg.HgMod;
import net.felinamods.hg.client.FelsClientHandler;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record PlayerShakeMessage(int someInt, float someFloat) implements CustomPacketPayload {
    public static final Type<PlayerShakeMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "player_shake"));

    // Encode + decode automatically with StreamCodec
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerShakeMessage> STREAM_CODEC =
        StreamCodec.of(
            (buf, msg) -> {
                buf.writeInt(msg.someInt);
                buf.writeFloat(msg.someFloat);
            },
            buf -> new PlayerShakeMessage(buf.readInt(), buf.readFloat())
        );

    @Override
    public Type<PlayerShakeMessage> type() {
        return TYPE;
    }

public static void handleData(final PlayerShakeMessage message, final IPayloadContext context) {
    if (context.flow() == PacketFlow.CLIENTBOUND) { // <-- changed
        context.enqueueWork(() -> {
            // we are already on the client now
            FelsClientHandler.addCameraShake(message.someFloat(), message.someInt());
        });
    }
}


    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            PlayerShakeMessage.TYPE,
            PlayerShakeMessage.STREAM_CODEC,
            PlayerShakeMessage::handleData
        );
    }
}




