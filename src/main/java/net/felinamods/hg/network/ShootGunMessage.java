
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

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ShootGunMessage() implements CustomPacketPayload {
    public static final Type<ShootGunMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "shoot_gun"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShootGunMessage> STREAM_CODEC =
        StreamCodec.unit(new ShootGunMessage()); // no data, just the action

    @Override
    public Type<ShootGunMessage> type() {
        return TYPE;
    }

    public static void handleData(final ShootGunMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null) {
                    // server-side logic here
                    if (player.getMainHandItem().getItem() instanceof GunItem gun) {
                    	//System.out.println("[ShootGunMessage] packet has been received");
                        gun.serverShoot(player.level(), player, player.getMainHandItem());
                    }
                }
            });
        }
    }

@SubscribeEvent
public static void registerMessage(FMLCommonSetupEvent event) {
HgMod.addNetworkMessage(
    ShootGunMessage.TYPE,
    ShootGunMessage.STREAM_CODEC,
    ShootGunMessage::handleData
);

}


    
}


