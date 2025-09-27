
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.felinamods.hg.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import net.minecraft.world.item.Item;

import net.felinamods.hg.item.Supply5Item;
import net.felinamods.hg.item.Supply4Item;
import net.felinamods.hg.item.Supply3Item;
import net.felinamods.hg.item.Supply2Item;
import net.felinamods.hg.item.Supply1Item;
import net.felinamods.hg.item.PlasmaItem;
import net.felinamods.hg.item.Plas1Item;
import net.felinamods.hg.item.MG43Item;
import net.felinamods.hg.item.GrenadeGraphicItem;
import net.felinamods.hg.item.Gp31Item;
import net.felinamods.hg.item.AR2Item;
import net.felinamods.hg.item.AR23lItem;
import net.felinamods.hg.HgMod;

public class HgModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(HgMod.MODID);
	public static final DeferredItem<Item> AR_23L = REGISTRY.register("ar_23l", AR23lItem::new);
	public static final DeferredItem<Item> MG_43 = REGISTRY.register("mg_43", MG43Item::new);
	public static final DeferredItem<Item> PLAS_1 = REGISTRY.register("plas_1", Plas1Item::new);
	public static final DeferredItem<Item> PLASMA = REGISTRY.register("plasma", PlasmaItem::new);
	public static final DeferredItem<Item> GP_31 = REGISTRY.register("gp_31", Gp31Item::new);
	public static final DeferredItem<Item> GRENADE_GRAPHIC = REGISTRY.register("grenade_graphic", GrenadeGraphicItem::new);
	public static final DeferredItem<Item> AR_2 = REGISTRY.register("ar_2", AR2Item::new);
	public static final DeferredItem<Item> SUPPLY_1 = REGISTRY.register("supply_1", Supply1Item::new);
	public static final DeferredItem<Item> SUPPLY_2 = REGISTRY.register("supply_2", Supply2Item::new);
	public static final DeferredItem<Item> SUPPLY_3 = REGISTRY.register("supply_3", Supply3Item::new);
	public static final DeferredItem<Item> SUPPLY_4 = REGISTRY.register("supply_4", Supply4Item::new);
	public static final DeferredItem<Item> SUPPLY_5 = REGISTRY.register("supply_5", Supply5Item::new);
	// Start of user code block custom items
	// End of user code block custom items
}
