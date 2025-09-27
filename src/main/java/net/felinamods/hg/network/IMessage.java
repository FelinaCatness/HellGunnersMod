
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

import net.felinamods.hg.procedures.MoveFowardProcedure;
import net.felinamods.hg.HgMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record IMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<IMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "key_i"));
	public static final StreamCodec<RegistryFriendlyByteBuf, IMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, IMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new IMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<IMessage> type() {
		return TYPE;
	}

	public static void handleData(final IMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.pressedms);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 1) {

			MoveFowardProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		HgMod.addNetworkMessage(IMessage.TYPE, IMessage.STREAM_CODEC, IMessage::handleData);
	}
}
