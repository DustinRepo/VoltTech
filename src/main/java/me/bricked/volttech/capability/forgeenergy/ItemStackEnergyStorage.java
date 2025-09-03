package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class ItemStackEnergyStorage implements IEnergyStorage, ICapabilityProvider<ItemStack, Void, IEnergyStorage> {
    private final ItemStack itemStack;
    private final DataComponentType<Integer> dataComponent;
    private final Constraints energySetting;
    private final boolean isInfinite;

    public ItemStackEnergyStorage(ItemStack itemStack, DataComponentType<Integer> feDataComponent, Constraints energySetting) {
        this.itemStack = itemStack;
        this.dataComponent = feDataComponent;
        this.energySetting = energySetting;
        this.isInfinite = itemStack.getItem() instanceof ChargeableItem chargeableItem && chargeableItem.isInfiniteEnergy();
    }

    private int getEnergy() {
        if (isInfinite)
            return getMaxEnergyStored();
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
        maxReceive = Math.min(maxReceive, energySetting.maxInput());
        int energy = getEnergy();
        int received = Math.min(getMaxEnergyStored() - energy, maxReceive);
        if (!simulate) {
            setEnergy(energy + received);
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        maxExtract = Math.min(maxExtract, energySetting.maxOutput());
        int energy = getEnergy();
        int extracted = Math.min(energy, maxExtract);
        if (!simulate && !isInfinite) {
            setEnergy(energy - extracted);
        }
        return extracted;
    }

    public int removeEnergy(int maxExtract, boolean simulate) {
        int energy = getEnergy();
        int extracted = Math.min(energy, maxExtract);
        if (!simulate && !isInfinite) {
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
        return energySetting.maxCapacity();
    }

    @Override
    public boolean canExtract() {
        return energySetting.maxOutput() > 0;
    }

    @Override
    public boolean canReceive() {
        return energySetting.maxInput() > 0;
    }
}
