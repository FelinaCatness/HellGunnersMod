
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
public record RefillGunMessage() implements CustomPacketPayload {
    public static final Type<RefillGunMessage> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "refill_gun"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RefillGunMessage> STREAM_CODEC =
        StreamCodec.unit(new RefillGunMessage()); // no data, just the action

    @Override
    public Type<RefillGunMessage> type() {
        return TYPE;
    }

    public static void handleData(final RefillGunMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null) {
                    // server-side logic here
                    if (player.getMainHandItem().getItem() instanceof GunItem gun) {
                    	//System.out.println("[RefillGunMessage] packet has been received");
                        gun.refillingGun(player, player.getMainHandItem());
                    }
                }
            });
        }
    }

@SubscribeEvent
public static void registerMessage(FMLCommonSetupEvent event) {
HgMod.addNetworkMessage(
    RefillGunMessage.TYPE,
    RefillGunMessage.STREAM_CODEC,
    RefillGunMessage::handleData
);

}


    
}
