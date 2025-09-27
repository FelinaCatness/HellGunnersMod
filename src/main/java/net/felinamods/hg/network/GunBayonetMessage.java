
package net.felinamods.hg.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.felinamods.hg.HgMod;
import net.felinamods.hg.item.GunItem;
//import net.felinamods.hg.item.BayonetRifleItem;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record GunBayonetMessage() implements CustomPacketPayload {
    public static final Type<GunBayonetMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "gun_bayonet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GunBayonetMessage> STREAM_CODEC =
        StreamCodec.unit(new GunBayonetMessage()); // no data, just the action

    @Override
    public Type<GunBayonetMessage> type() {
        return TYPE;
    }

    public static void handleData(final GunBayonetMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null) {
                    // server-side logic here
                    //if (player.getMainHandItem().getItem() instanceof BayonetRifleItem gun) {
                    	//System.out.println("[ShootGunMessage] packet has been received");
                    //    gun.serverBayonet(player.level(), player, player.getMainHandItem());
                   // }
                }
            });
        }
    }

@SubscribeEvent
public static void registerMessage(FMLCommonSetupEvent event) {
HgMod.addNetworkMessage(
    GunBayonetMessage.TYPE,
    GunBayonetMessage.STREAM_CODEC,
    GunBayonetMessage::handleData
);

}


    
}



