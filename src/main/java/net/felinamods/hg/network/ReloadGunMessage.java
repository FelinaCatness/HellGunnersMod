
package net.felinamods.hg.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.felinamods.hg.HgMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ReloadGunMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<ReloadGunMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "key_reload_gun"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ReloadGunMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ReloadGunMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new ReloadGunMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<ReloadGunMessage> type() {
		return TYPE;
	}

	public static void handleData(final ReloadGunMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		HgMod.addNetworkMessage(ReloadGunMessage.TYPE, ReloadGunMessage.STREAM_CODEC, ReloadGunMessage::handleData);
	}
}
