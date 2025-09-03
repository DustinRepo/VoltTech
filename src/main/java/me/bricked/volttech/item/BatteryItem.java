package me.bricked.volttech.item;

import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.EnergyUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BatteryItem extends ChargeableItem {
    public BatteryItem(Properties properties) {
        super(properties);
    }
    public BatteryItem(Properties properties, boolean isInfinite) {
        super(properties, isInfinite);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (level.isClientSide())
                return InteractionResult.SUCCESS_SERVER;
            boolean isSharing = itemStack.getOrDefault(DataComponentRegistry.SHARE_ENERGY, false);
            itemStack.set(DataComponentRegistry.SHARE_ENERGY, !isSharing);
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.SHARE_ENERGY, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        if (stack.getOrDefault(DataComponentRegistry.SHARE_ENERGY, false))
            tooltipAdder.accept(StringifyUtil.translate("tooltip.sharing_energy"));
        else
            tooltipAdder.accept(StringifyUtil.translate("tooltip.not_sharing_energy"));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (level.isClientSide())
            return;
        if (!(entity instanceof Player player))
            return;
        IEnergyStorage selfStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (selfStorage == null)
            return;
        if (stack.getOrDefault(DataComponentRegistry.SHARE_ENERGY, false)) {
            int sharedAmount = 0;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (sharedAmount >= getLogisticalConstraints().maxOutput())
                    break;
                ItemStack otherStack = player.getInventory().getItem(i);
                if (otherStack.isEmpty() || otherStack.is(TagRegistry.Items.BATTERIES))
                    continue;
                IEnergyStorage otherStorage = otherStack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (otherStorage != null && otherStorage.canReceive()) {
                    sharedAmount += EnergyUtil.moveEnergy(getLogisticalConstraints().maxOutput(), selfStorage, otherStorage);
                }
            }
        }
    }
}
