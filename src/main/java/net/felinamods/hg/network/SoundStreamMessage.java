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
public record SoundStreamMessage(String value, float range, float volume) implements CustomPacketPayload {
    public static final Type<SoundStreamMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "sound_stream"));

    // Encode both String and float
    public static final StreamCodec<RegistryFriendlyByteBuf, SoundStreamMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SoundStreamMessage::value,   // string
            ByteBufCodecs.FLOAT,       SoundStreamMessage::range,   // float
            ByteBufCodecs.FLOAT,       SoundStreamMessage::volume,   // float            
            SoundStreamMessage::new                                   // constructor
        );

    @Override
    public Type<SoundStreamMessage> type() {
        return TYPE;
    }

    public static void handleData(final SoundStreamMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && player.getMainHandItem().getItem() instanceof GunItem gun) {
                    // Call your gun's sound method
                    gun.soundStream(player, message.value(), message.range(), message.volume());
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            SoundStreamMessage.TYPE,
            SoundStreamMessage.STREAM_CODEC,
            SoundStreamMessage::handleData
        );
    }
}


