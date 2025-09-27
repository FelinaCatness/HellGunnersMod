package net.felinamods.hg.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import net.felinamods.hg.HgMod;

import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class HgModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HgMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		HgMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
			PlayerVariables clone = new PlayerVariables();
			clone.aimingHold = original.aimingHold;
			if (!event.isWasDeath()) {
				clone.zoomValue = original.zoomValue;
				clone.TempItem = original.TempItem;
				clone.recoilRemainingPitch = original.recoilRemainingPitch;
				clone.isAiming = original.isAiming;
				clone.hitmarker = original.hitmarker;
				clone.currentGunUUID = original.currentGunUUID;
				clone.currentGunTime = original.currentGunTime;
				clone.currentGunAnimation = original.currentGunAnimation;
				clone.currentGunBayonetAttached = original.currentGunBayonetAttached;
				clone.recoilOffsetY = original.recoilOffsetY;
				clone.recoilZ = original.recoilZ;
				clone.recoilZint = original.recoilZint;
				clone.currentGunOnBipod = original.currentGunOnBipod;
				clone.currentGunIsShooting = original.currentGunIsShooting;
			}
			event.getEntity().setData(PLAYER_VARIABLES, clone);
		}
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		public double zoomValue = 0;
		public ItemStack TempItem = ItemStack.EMPTY;
		public double recoilRemainingPitch = 0;
		public boolean isAiming = false;
		public boolean hitmarker = false;
		public String currentGunUUID = "";
		public double currentGunTime = 0;
		public String currentGunAnimation = "idle";
		public boolean aimingHold = false;
		public boolean currentGunBayonetAttached = false;
		public double recoilOffsetY = 0;
		public double recoilZ = 0;
		public double recoilZint = 0;
		public boolean currentGunOnBipod = false;
		public boolean currentGunIsShooting = false;

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putDouble("zoomValue", zoomValue);
			nbt.put("TempItem", TempItem.saveOptional(lookupProvider));
			nbt.putDouble("recoilRemainingPitch", recoilRemainingPitch);
			nbt.putBoolean("isAiming", isAiming);
			nbt.putBoolean("hitmarker", hitmarker);
			nbt.putString("currentGunUUID", currentGunUUID);
			nbt.putDouble("currentGunTime", currentGunTime);
			nbt.putString("currentGunAnimation", currentGunAnimation);
			nbt.putBoolean("aimingHold", aimingHold);
			nbt.putBoolean("currentGunBayonetAttached", currentGunBayonetAttached);
			nbt.putDouble("recoilOffsetY", recoilOffsetY);
			nbt.putDouble("recoilZ", recoilZ);
			nbt.putDouble("recoilZint", recoilZint);
			nbt.putBoolean("currentGunOnBipod", currentGunOnBipod);
			nbt.putBoolean("currentGunIsShooting", currentGunIsShooting);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			zoomValue = nbt.getDouble("zoomValue");
			TempItem = ItemStack.parseOptional(lookupProvider, nbt.getCompound("TempItem"));
			recoilRemainingPitch = nbt.getDouble("recoilRemainingPitch");
			isAiming = nbt.getBoolean("isAiming");
			hitmarker = nbt.getBoolean("hitmarker");
			currentGunUUID = nbt.getString("currentGunUUID");
			currentGunTime = nbt.getDouble("currentGunTime");
			currentGunAnimation = nbt.getString("currentGunAnimation");
			aimingHold = nbt.getBoolean("aimingHold");
			currentGunBayonetAttached = nbt.getBoolean("currentGunBayonetAttached");
			recoilOffsetY = nbt.getDouble("recoilOffsetY");
			recoilZ = nbt.getDouble("recoilZ");
			recoilZint = nbt.getDouble("recoilZint");
			currentGunOnBipod = nbt.getBoolean("currentGunOnBipod");
			currentGunIsShooting = nbt.getBoolean("currentGunIsShooting");
		}

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				PacketDistributor.sendToPlayer(serverPlayer, new PlayerVariablesSyncMessage(this));
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HgMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec
				.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
					PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables());
					message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
					return message;
				});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> context.player().getData(PLAYER_VARIABLES).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()))).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}
