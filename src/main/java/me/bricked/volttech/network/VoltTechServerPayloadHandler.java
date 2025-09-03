package me.bricked.volttech.network;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.blockentity.HarvesterBlockEntity;
import me.bricked.volttech.network.c2s.*;
import me.bricked.volttech.network.s2c.UpgradeablesConfigPayload;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.task.ConstraintConfigTask;
import me.bricked.volttech.task.UpgradeablesConfigTask;
import me.bricked.volttech.util.ItemUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class VoltTechServerPayloadHandler {

    public static void handleConstraintsAckPayload(final ConstraintConfigAckPayload payload, final IPayloadContext context) {
        VoltTech.getLogger().info("[Server] Client acknowledged constraints payload");
        context.finishCurrentTask(ConstraintConfigTask.TYPE);
    }

    public static void handleUpgradeablesAckPayload(final UpgradeablesConfigAckPayload payload, final IPayloadContext context) {
        VoltTech.getLogger().info("[Server] Client acknowledged upgradeables payload");
        context.finishCurrentTask(UpgradeablesConfigTask.TYPE);
    }

    public static void handleHarvesterSettings(final HarvesterOffsetsPayload payload, final IPayloadContext context) {
        BlockEntity blockEntity = context.player().level().getBlockEntity(payload.blockPos());
        if (blockEntity instanceof HarvesterBlockEntity harvesterBlockEntity) {
            harvesterBlockEntity.setxOffset(payload.offset().getX());
            harvesterBlockEntity.setyOffset(payload.offset().getY());
            harvesterBlockEntity.setzOffset(payload.offset().getZ());
            harvesterBlockEntity.setxSize(payload.size().getX());
            harvesterBlockEntity.setySize(payload.size().getY());
            harvesterBlockEntity.setzSize(payload.size().getZ());
            harvesterBlockEntity.setRenderBox(payload.shouldRenderBox());
        }
    }

    public static void handleJetpackToggle(final JetpackTogglePayload payload, final IPayloadContext context) {
        if (payload.slot() >= EquipmentSlot.values().length || payload.slot() < 0) {
            context.connection().disconnect(StringifyUtil.translate("disconnect.invalid_jetpack_slot"));
            return;
        }
        ItemStack itemStack = context.player().getItemBySlot(EquipmentSlot.values()[payload.slot()]);
        if (!itemStack.isEmpty() && ItemUtil.canBeJetpack(itemStack) && ItemUtil.hasJetpackCharge(itemStack)) {
            boolean isAlreadyEnabled = ItemUtil.isJetpackEnabled(itemStack);
            ItemUtil.setJetpackEnabled(itemStack, !isAlreadyEnabled);
            context.player().displayClientMessage(isAlreadyEnabled ? StringifyUtil.translate("message.jetpack_disabled").withColor(ChatFormatting.GOLD.getColor()) : StringifyUtil.translate("message.jetpack_enabled").withColor(ChatFormatting.GREEN.getColor()), false);
        }
    }

    public static void handlePortalGunSeed(final SetPortalGunSeedPayload payload, final IPayloadContext context) {
        Player player = context.player();
        if (payload.slot() > player.getInventory().getContainerSize() || payload.slot() < 0) {
            context.connection().disconnect(StringifyUtil.translate("disconnect.invalid_portal_gun_slot"));
            return;
        }
        ItemStack itemStack = player.getInventory().getItem(payload.slot());
        if (!itemStack.is(ItemRegistry.PORTAL_GUN)) {
            context.connection().disconnect(StringifyUtil.translate("disconnect.invalid_portal_gun_slot"));
            return;
        }
        if (payload.seed().length() > 32 || payload.seed().isEmpty()) {
            context.connection().disconnect(StringifyUtil.translate("disconnect.invalid_portal_gun_seed"));
            return;
        }
        SetPortalGunSeedPayload.PortalLocation location = payload.location();
        if (location.usePosition()) {
            WorldBorder worldBorder = context.player().level().getWorldBorder();
            Vec3 pos = new Vec3(
                    Math.clamp(location.pos().x, worldBorder.getMinX() + 2, worldBorder.getMaxX() - 2),
                    location.pos().y,
                    Math.clamp(location.pos().z, worldBorder.getMinZ() + 2, worldBorder.getMaxZ() - 2));
            itemStack.set(DataComponentRegistry.PORTAL_LOCATION, pos);
        }
        else
            itemStack.remove(DataComponentRegistry.PORTAL_LOCATION);
        itemStack.set(DataComponentRegistry.PORTAL_HOME, payload.homeDim());
        itemStack.set(DataComponentRegistry.PORTAL_SEED, payload.seed());
    }
}
