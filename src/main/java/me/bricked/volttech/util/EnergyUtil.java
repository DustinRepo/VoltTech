package me.bricked.volttech.util;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.capability.forgeenergy.ItemStackEnergyStorage;
import me.bricked.volttech.capability.forgeenergy.UpgradeableItemStackEnergyStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyUtil {
    public static int getStoredEnergy(ItemStack itemStack) {
        IEnergyStorage storage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage != null)
            return storage.getEnergyStored();
        return 0;
    }

    public static int useStoredEnergy(ItemStack itemStack, int amount) {
        IEnergyStorage storage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage != null) {
            if (storage instanceof ItemStackEnergyStorage itemStackEnergyStorage) {
                int move = itemStackEnergyStorage.removeEnergy(amount, true);
                return itemStackEnergyStorage.removeEnergy(move, false);
            } else if (storage instanceof UpgradeableItemStackEnergyStorage itemStackEnergyStorage) {
                int move = itemStackEnergyStorage.removeEnergy(amount, true);
                return itemStackEnergyStorage.removeEnergy(move, false);
            }
            int move = storage.extractEnergy(amount, true);
            return storage.extractEnergy(move, false);
        }
        return 0;
    }

    public static int moveEnergy(int amount, IEnergyStorage from, IEnergyStorage to) {
        amount = from.extractEnergy(amount, true);
        int toMove = to.receiveEnergy(amount, true);
        if (toMove > 0) {
            from.extractEnergy(toMove, false);
            to.receiveEnergy(toMove, false);
        }
        return toMove;
    }

    public static boolean isNonFullEnergyItem(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage == null)
            return false;
        return energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored();
    }
}
