package net.felinamods.hg.utils;

import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class GunInteractionRegistry {
   public static final Set<String> SUPPRESSING_ITEMS = Set.of(
"awawa:wawa"
   	);

   public static boolean isSuppressingItem(Item item) {
      ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
      return id != null && SUPPRESSING_ITEMS.contains(id.toString());
   }

//Remove this class later
   
}
