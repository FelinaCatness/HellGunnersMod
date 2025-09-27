
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

import net.felinamods.hg.world.inventory.DropGUIMenu;
import net.felinamods.hg.HgMod;

public class HgModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, HgMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<DropGUIMenu>> DROP_GUI = REGISTRY.register("drop_gui", () -> IMenuTypeExtension.create(DropGUIMenu::new));
}
