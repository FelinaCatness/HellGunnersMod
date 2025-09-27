
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.felinamods.hg.HgMod;

public class HgModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HgMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HELL_TAB = REGISTRY.register("hell_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.hg.hell_tab")).icon(() -> new ItemStack(HgModItems.AR_23L.get())).displayItems((parameters, tabData) -> {
				tabData.accept(HgModItems.AR_23L.get());
				tabData.accept(HgModItems.MG_43.get());
				tabData.accept(HgModItems.PLAS_1.get());
				tabData.accept(HgModItems.GP_31.get());
				tabData.accept(HgModItems.AR_2.get());
				tabData.accept(HgModItems.SUPPLY_1.get());
				tabData.accept(HgModItems.SUPPLY_2.get());
				tabData.accept(HgModItems.SUPPLY_3.get());
				tabData.accept(HgModItems.SUPPLY_4.get());
				tabData.accept(HgModItems.SUPPLY_5.get());
			})

					.build());
}
