package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class UpgradeableItemStackEnergyStorage implements IEnergyStorage, ICapabilityProvider<ItemStack, Void, IEnergyStorage> {
    private final ItemStack itemStack;
    private final DataComponentType<Integer> dataComponent;

    public UpgradeableItemStackEnergyStorage(ItemStack itemStack, DataComponentType<Integer> feDataComponent) {
        this.itemStack = itemStack;
        this.dataComponent = feDataComponent;
    }

    private int getEnergy() {
        return itemStack.getOrDefault(dataComponent, 0);
    }

    private void setEnergy(int value) {
        itemStack.set(dataComponent, Math.min(value, getMaxEnergyStored()));
    }

    @Override
    public @Nullable IEnergyStorage getCapability(ItemStack stack, Void context) {
        return this;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        maxReceive = Math.min(maxReceive, ItemUtil.getUpgradedEnergyMaxInput(itemStack));
        int energy = getEnergy();
        int received = Math.min(getMaxEnergyStored() - energy, maxReceive);
        if (!simulate) {
            setEnergy(energy + received);
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        maxExtract = Math.min(maxExtract, ItemUtil.getEnergyUsage(itemStack));
        int energy = getEnergy();
        int extracted = Math.min(energy, maxExtract);
        if (!simulate) {
            setEnergy(energy - extracted);
        }
        return extracted;
    }

    public int removeEnergy(int maxExtract, boolean simulate) {
        int energy = getEnergy();
        int extracted = Math.min(energy, maxExtract);
        if (!simulate) {
            setEnergy(energy - extracted);
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return ItemUtil.getUpgradedEnergyCapacity(itemStack);
    }

    @Override
    public boolean canExtract() {
        return ItemUtil.hasUpgradeApplied(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get());
    }

    @Override
    public boolean canReceive() {
        return ItemUtil.hasUpgradeApplied(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get()) && ItemUtil.getUpgradedEnergyMaxInput(itemStack) > 0;
    }
}
