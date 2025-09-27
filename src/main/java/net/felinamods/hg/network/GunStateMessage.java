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
public record GunStateMessage(String state, double value) implements CustomPacketPayload {
    public static final Type<GunStateMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "gun_state"));

    // codec for encoding/decoding a String and a double
    public static final StreamCodec<RegistryFriendlyByteBuf, GunStateMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,    // codec for String
            GunStateMessage::state,       // getter for String
            ByteBufCodecs.DOUBLE,         // codec for double
            GunStateMessage::value,       // getter for double
            GunStateMessage::new          // constructor
        );

    @Override
    public Type<GunStateMessage> type() {
        return TYPE;
    }

    public static void handleData(final GunStateMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && player.getMainHandItem().getItem() instanceof GunItem gun) {
                    var vars = player.getData(net.felinamods.hg.network.HgModVariables.PLAYER_VARIABLES);

                    // Example: apply the string + double to your variables
                    vars.currentGunAnimation = message.state();
                    vars.currentGunTime = message.value();

                    vars.syncPlayerVariables(player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            GunStateMessage.TYPE,
            GunStateMessage.STREAM_CODEC,
            GunStateMessage::handleData
        );
    }
}
