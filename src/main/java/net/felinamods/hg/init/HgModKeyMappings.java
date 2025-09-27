
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import org.lwjgl.glfw.GLFW;

import org.checkerframework.checker.units.qual.K;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.felinamods.hg.network.UpMessage;
import net.felinamods.hg.network.RightMessage;
import net.felinamods.hg.network.LeftMessage;
import net.felinamods.hg.network.KMessage;
import net.felinamods.hg.network.IMessage;
import net.felinamods.hg.network.DownMessage;
import net.felinamods.hg.network.BipodMessage;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class HgModKeyMappings {
	public static final KeyMapping RELOAD_GUN = new KeyMapping("key.hg.reload_gun", GLFW.GLFW_KEY_R, "key.categories.gameplay");
	public static final KeyMapping BIPOD = new KeyMapping("key.hg.bipod", GLFW.GLFW_KEY_B, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				BIPOD_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - BIPOD_LASTPRESS);
				PacketDistributor.sendToServer(new BipodMessage(1, dt));
				BipodMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping RIGHT = new KeyMapping("key.hg.right", GLFW.GLFW_KEY_RIGHT, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				RIGHT_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - RIGHT_LASTPRESS);
				PacketDistributor.sendToServer(new RightMessage(1, dt));
				RightMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping LEFT = new KeyMapping("key.hg.left", GLFW.GLFW_KEY_LEFT, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				LEFT_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - LEFT_LASTPRESS);
				PacketDistributor.sendToServer(new LeftMessage(1, dt));
				LeftMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping UP = new KeyMapping("key.hg.up", GLFW.GLFW_KEY_UP, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				UP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - UP_LASTPRESS);
				PacketDistributor.sendToServer(new UpMessage(1, dt));
				UpMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping DOWN = new KeyMapping("key.hg.down", GLFW.GLFW_KEY_DOWN, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				DOWN_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - DOWN_LASTPRESS);
				PacketDistributor.sendToServer(new DownMessage(1, dt));
				DownMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping I = new KeyMapping("key.hg.i", GLFW.GLFW_KEY_I, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				I_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - I_LASTPRESS);
				PacketDistributor.sendToServer(new IMessage(1, dt));
				IMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping K = new KeyMapping("key.hg.k", GLFW.GLFW_KEY_K, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				K_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - K_LASTPRESS);
				PacketDistributor.sendToServer(new KMessage(1, dt));
				KMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long BIPOD_LASTPRESS = 0;
	private static long RIGHT_LASTPRESS = 0;
	private static long LEFT_LASTPRESS = 0;
	private static long UP_LASTPRESS = 0;
	private static long DOWN_LASTPRESS = 0;
	private static long I_LASTPRESS = 0;
	private static long K_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(RELOAD_GUN);
		event.register(BIPOD);
		event.register(RIGHT);
		event.register(LEFT);
		event.register(UP);
		event.register(DOWN);
		event.register(I);
		event.register(K);
	}

	@EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				BIPOD.consumeClick();
				RIGHT.consumeClick();
				LEFT.consumeClick();
				UP.consumeClick();
				DOWN.consumeClick();
				I.consumeClick();
				K.consumeClick();
			}
		}
	}
}
