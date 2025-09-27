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
public record GunAmmoMessage(int ammo) implements CustomPacketPayload {
    public static final Type<GunAmmoMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "gun_ammo"));

    // codec for encoding/decoding a single int
    public static final StreamCodec<RegistryFriendlyByteBuf, GunAmmoMessage> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT,       // how to encode/decode an int
            GunAmmoMessage::ammo,        // getter for the int
            GunAmmoMessage::new          // constructor
        );

    @Override
    public Type<GunAmmoMessage> type() {
        return TYPE;
    }

    public static void handleData(final GunAmmoMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && player.getMainHandItem().getItem() instanceof GunItem gun) {
                    //System.out.println("[GunAmmoMessage] Packet received with ammo: " + message.ammo());
                    gun.addAmmo(player.getMainHandItem(), message.ammo()); 
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        HgMod.addNetworkMessage(
            GunAmmoMessage.TYPE,
            GunAmmoMessage.STREAM_CODEC,
            GunAmmoMessage::handleData
        );
    }
}
