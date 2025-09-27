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
import net.minecraft.world.phys.Vec3;

import net.felinamods.hg.HgMod;
import net.felinamods.hg.utils.ClientUtil;
import net.felinamods.hg.utils.SoundUtils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record LocalSoundMessage(String effect, float volume, float pitch,
                                double x, double y, double z) implements CustomPacketPayload {

    public static final Type<LocalSoundMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "local_sound"));

    // Encode + decode with all fields
    public static final StreamCodec<RegistryFriendlyByteBuf, LocalSoundMessage> STREAM_CODEC =
        StreamCodec.of(
            (buf, msg) -> {
                buf.writeUtf(msg.effect());
                buf.writeFloat(msg.volume());
                buf.writeFloat(msg.pitch());
                buf.writeDouble(msg.x());
                buf.writeDouble(msg.y());
                buf.writeDouble(msg.z());
            },
            buf -> new LocalSoundMessage(
                buf.readUtf(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
            )
        );

    @Override
    public Type<LocalSoundMessage> type() {
        return TYPE;
    }

    public static void handleData(final LocalSoundMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.CLIENTBOUND) {
            context.enqueueWork(() -> {
                // effect should be full "modid:soundname"
                ResourceLocation soundRL = ResourceLocation.tryParse(message.effect());
                if (soundRL == null) return;

                Vec3 pos = new Vec3(message.x(), message.y(), message.z());

                SoundUtils.playClientSound(
                    soundRL,
                    pos,
                    message.volume(),
                    message.pitch()
                );
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            LocalSoundMessage.TYPE,
            LocalSoundMessage.STREAM_CODEC,
            LocalSoundMessage::handleData
        );
    }
}
