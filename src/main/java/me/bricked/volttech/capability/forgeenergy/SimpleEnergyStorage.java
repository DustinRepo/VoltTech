package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.util.Constraints;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;

public class SimpleEnergyStorage extends EnergyStorage {
    public SimpleEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public SimpleEnergyStorage(Constraints constraints) {
        this(constraints.maxCapacity(), constraints.maxInput(), constraints.maxOutput());
    }

    public int insertEnergy(int maxInsert, boolean simulate) {
        int energyReceived = Mth.clamp(this.capacity - this.energy, 0, maxInsert);
        if (!simulate) {
            this.energy += energyReceived;
        }

        return energyReceived;
    }

    public int removeEnergy(int maxRemove, boolean simulate) {
        int energyExtracted = Math.min(this.energy, maxRemove);
        if (!simulate) {
            this.energy -= energyExtracted;
        }

        return energyExtracted;
    }
}
