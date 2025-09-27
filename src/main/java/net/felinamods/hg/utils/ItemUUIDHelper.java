package net.felinamods.hg.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

import java.util.UUID;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

import java.util.UUID;

public class ItemUUIDHelper {
    private static final String UNIQUE_ID_TAG = "uuid"; // will hold a string like "550e8400-e29b-41d4-a716-446655440000"

    public static UUID getOrCreateUUID(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (!tag.contains(UNIQUE_ID_TAG)) {
            UUID uuid = UUID.randomUUID();
            tag.putString(UNIQUE_ID_TAG, uuid.toString()); // store as string
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            return uuid;
        }

        return UUID.fromString(tag.getString(UNIQUE_ID_TAG));
    }

    public static UUID getUUID(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains(UNIQUE_ID_TAG)) {
                return UUID.fromString(tag.getString(UNIQUE_ID_TAG));
            }
        }
        return null;
    }

    public static String getUUIDString(ItemStack stack) {
        UUID uuid = getOrCreateUUID(stack);
        return uuid.toString(); // directly return the "550e8400-e29b-41d4-a716-446655440000" form
    }
}


