package me.bricked.volttech.util;

import me.bricked.volttech.Config;
import me.bricked.volttech.item.charge.IChargeableItem;
import me.bricked.volttech.item.UpgradeCardItem;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemUtil {

    public static boolean transferItemStack(IItemHandler from, IItemHandler to, int slot) {
        ItemStack stackInSlot = from.getStackInSlot(slot);
        if (stackInSlot.isEmpty()) return false;

        ItemStack stackToTransfer = stackInSlot.copy();

        for (int i = 0; i < to.getSlots(); i++) {
            if (!to.isItemValid(i, stackToTransfer)) continue;

            ItemStack remaining = to.insertItem(i, stackToTransfer, true); // simulate insert

            int transferableAmount = stackToTransfer.getCount() - remaining.getCount();
            if (transferableAmount <= 0) continue;

            // Actually extract that much from the source
            ItemStack extracted = from.extractItem(slot, transferableAmount, false);
            if (extracted.isEmpty()) continue;

            // And actually insert it into the destination
            ItemStack leftover = to.insertItem(i, extracted, false);
            if (!leftover.isEmpty()) {
                // Try to re-insert leftovers (edge case)
                from.insertItem(slot, leftover, false);
            }

            return true; // Transfer only one stack
        }
        return false;
    }

    public static ItemStack getCookingRecipeStack(ItemStack input, ServerLevel level, RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck) {
        RecipeHolder<? extends AbstractCookingRecipe> recipeHolder = quickCheck.getRecipeFor(new SingleRecipeInput(input), level).orElse(null);
        if (recipeHolder == null)
            return ItemStack.EMPTY;
        return recipeHolder.value().assemble(new SingleRecipeInput(input), level.registryAccess());

    }

    public static boolean canFitIntoItemStack(ItemStack base, ItemStack input) {
        int maxSize = base.getMaxStackSize();
        int size = input.getCount();
        int baseSize = base.getCount();
        return baseSize + size <= maxSize;
    }

    public static void setJetpackEnabled(ItemStack itemStack, boolean enabled) {
        itemStack.set(DataComponentRegistry.ACTIVATE_JETPACK, enabled);
    }

    public static boolean isJetpackEnabled(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponentRegistry.ACTIVATE_JETPACK, false);
    }

    public static boolean canBeJetpack(ItemStack itemStack) {
        if (UpgradeData.isUpgradeableItem(itemStack.getItem()))
            return getUpgradeCount(itemStack, ItemRegistry.JETPACK_UPGRADE_CARD.get()) != 0;
        return itemStack.is(TagRegistry.Items.JETPACKS);
    }

    public static boolean hasJetpackCharge(ItemStack itemStack) {
        IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null)
            return energyStorage.getEnergyStored() > 0;
        // If it doesn't store energy just assume it's free
        return true;
    }

    public static boolean isArmor(ItemStack itemStack) {
        return itemStack.has(DataComponents.EQUIPPABLE);
    }

    public static boolean isDiggerItem(ItemStack itemStack) {
        return itemStack.is(ItemTags.PICKAXES) || itemStack.is(ItemTags.AXES) || itemStack.is(ItemTags.SHOVELS);
    }

    public static boolean isCombatItem(ItemStack itemStack) {
        return isMeleeItem(itemStack) || itemStack.is(Tags.Items.TOOLS_BOW);
    }

    public static boolean isMeleeItem(ItemStack itemStack) {
        return itemStack.is(ItemTags.SWORDS) || itemStack.is(Tags.Items.TOOLS_MACE) || itemStack.is(Tags.Items.TOOLS_SPEAR);
    }

    public static int getUpgradedEnergyCapacity(ItemStack itemStack) {
        int base = 0;
        int mult = 0;
        UpgradeData data = UpgradeData.get(itemStack.getItem());
        if (data != null) {
            base = data.baseEnergy().maxCapacity();
            mult = data.energyCardData().energyAdd();
        }
        return base + (getUpgradeCount(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get()) * mult);
    }

    public static int getUpgradedEnergyMaxInput(ItemStack itemStack) {
        int base = 0;
        int mult = 0;
        UpgradeData data = UpgradeData.get(itemStack.getItem());
        if (data != null) {
            base = data.baseEnergy().maxInput();
            mult = data.energyCardData().energyInputAdd();
        }
        return base + (getUpgradeCount(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get()) * mult);
    }

    public static int getEnergyUsage(ItemStack itemStack) {
        int base = 100;
        int use = 0;
        UpgradeData upgradeData = UpgradeData.get(itemStack.getItem());
        if (upgradeData != null) {
            base = upgradeData.baseEnergy().usageOrGeneration();
            use = upgradeData.energyCardData().useRemoval();
        }
        else if (itemStack.getItem() instanceof IChargeableItem chargeableItem)
            base = chargeableItem.getLogisticalConstraints().usageOrGeneration();
        return base - (getUpgradeCount(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get()) * use);
    }

    public static boolean hasUpgradeApplied(ItemStack itemStack, UpgradeCardItem cardItem) {
        return getUpgradeCount(itemStack, cardItem) > 0;
    }

    public static int getUpgradeCount(ItemStack upgradeItem, UpgradeCardItem cardItem) {
        return upgradeItem.getOrDefault(cardItem.getDataComponent().get(), 0);
    }

    public static int getMaxCountOfCard(ItemStack upgradeStack, ItemStack cardStack) {
        if (cardStack.getItem() instanceof UpgradeCardItem upgradeCardItem) {
            UpgradeData upgradeData = UpgradeData.get(upgradeStack.getItem());
            if (upgradeData != null) {
                if (upgradeData.isUpgradeBlocked(upgradeCardItem))
                    return 0;
                if (cardStack.is(ItemRegistry.ENERGY_UPGRADE_CARD.get()) && upgradeCardItem.canApply(upgradeStack))
                    return upgradeData.energyCardData().maxCards();
            }
            if (upgradeCardItem.canApply(upgradeStack))
                return upgradeCardItem.getMaxCount();
        }
        return 0;
    }
}
