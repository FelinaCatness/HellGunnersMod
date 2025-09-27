package net.felinamods.hg.procedures;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import net.felinamods.hg.init.HgModItems;
import net.felinamods.hg.init.HgModEntities;
import net.felinamods.hg.entity.SpawnEntEntity;

public class SupplyThrowProcedure {
    /**
     * Call this from your item’s `use` method instead of listening to events.
     */
    public static InteractionResultHolder<ItemStack> onRightClick(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide && world instanceof ServerLevel serverLevel) {
            if (stack.getItem() == HgModItems.SUPPLY_1.get()) {
                throwSupply(serverLevel, player, "ar23", "value2");
                consumeItem(player, hand);
            } else if (stack.getItem() == HgModItems.SUPPLY_2.get()) {
                throwSupply(serverLevel, player, "mg43", "value2");
                consumeItem(player, hand);
            } else if (stack.getItem() == HgModItems.SUPPLY_3.get()) {
                throwSupply(serverLevel, player, "plas1", "value2");
                consumeItem(player, hand);
            } else if (stack.getItem() == HgModItems.SUPPLY_4.get()) {
                throwSupply(serverLevel, player, "gp31", "value2");
                consumeItem(player, hand);
            } else if (stack.getItem() == HgModItems.SUPPLY_5.get()) {
                throwSupply(serverLevel, player, "ar2", "value2");
                consumeItem(player, hand);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    private static void throwSupply(ServerLevel serverLevel, Entity thrower, String slot1, String slot2) {
        Entity entityinstance = HgModEntities.SPAWN_ENT.get().create(serverLevel, null, BlockPos.ZERO, MobSpawnType.MOB_SUMMONED, false, false);
        if (entityinstance == null) return;

        // Spawn at thrower's eye position
        double eyeX = thrower.getX();
        double eyeY = thrower.getEyeY() - 0.1;
        double eyeZ = thrower.getZ();
        entityinstance.moveTo(eyeX, eyeY, eyeZ, thrower.getYRot(), thrower.getXRot());

        // Launch forward
        Vec3 look = thrower.getLookAngle();
        double speed = 2.7D; // tweak throw strength
        entityinstance.setDeltaMovement(look.scale(speed));

        // Random spin
        entityinstance.setYRot(serverLevel.getRandom().nextFloat() * 360.0F);

        if (entityinstance instanceof SpawnEntEntity supply) {
            supply.getEntityData().set(SpawnEntEntity.DATA_Slot1, slot1);
            supply.getEntityData().set(SpawnEntEntity.DATA_Slot2, slot2);
        }

        serverLevel.addFreshEntity(entityinstance);
    }

    private static void consumeItem(LivingEntity living, InteractionHand hand) {
        ItemStack held = living.getItemInHand(hand);
        if (!held.isEmpty()) {
            held.shrink(1);
            if (held.isEmpty()) {
                living.setItemInHand(hand, ItemStack.EMPTY);
            }
        }
    }
}
