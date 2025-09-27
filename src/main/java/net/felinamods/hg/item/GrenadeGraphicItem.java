
package net.felinamods.hg.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class GrenadeGraphicItem extends Item {
	public GrenadeGraphicItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
