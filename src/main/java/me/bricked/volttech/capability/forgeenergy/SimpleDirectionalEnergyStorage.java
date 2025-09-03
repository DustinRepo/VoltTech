package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.util.Constraints;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class SimpleDirectionalEnergyStorage {
    private final SimpleEnergyStorage[] energyStorages = new SimpleEnergyStorage[7];
    private int storedEnergy = 0;
    private final Constraints constraints;

    public SimpleDirectionalEnergyStorage(Constraints logisticalConstraints) {
        for (int i = 0; i < 7; i++) {
            Direction direction;
            if (i == 6)
                direction = null;
            else
                direction = Direction.values()[i];
            this.energyStorages[i] = new DirectionalStorage(direction, logisticalConstraints.maxCapacity(), logisticalConstraints.maxInput(), logisticalConstraints.maxOutput());
        }
        this.constraints = logisticalConstraints;
    }

    public IEnergyStorage getEnergyStorage(Direction direction) {
        if (direction == null)
            return energyStorages[6];
        return energyStorages[direction.ordinal()];
    }

    public abstract int getMaxInsert(Direction direction);
    public abstract int getMaxOutput(Direction direction);

    public void removeEnergy(int amount) {
        int removeAmount = Math.min(amount, this.storedEnergy);
        this.storedEnergy -= removeAmount;
    }

    public void addEnergy(int amount) {
        int addAmount = Math.min(amount, this.constraints.maxCapacity() - this.storedEnergy);
        this.storedEnergy += addAmount;
    }

    public void serialize(ValueOutput output) {
        output.putInt("energy", this.getEnergyStored());
    }

    public void deserialize(ValueInput input) {
        this.setStoredEnergy(input.getIntOr("energy", 0));
    }

    public void setStoredEnergy(int storedEnergy) {
        this.storedEnergy = storedEnergy;
    }

    public int getEnergyStored() {
        return storedEnergy;
    }

    public int getMaxEnergyStored() {
        return this.constraints.maxCapacity();
    }

    public Constraints getLogisticalConstraints() {
        return constraints;
    }

    public class DirectionalStorage extends SimpleEnergyStorage {
        private final Direction direction;
        public DirectionalStorage(Direction direction, int capacity, int maxInput, int maxOutput) {
            super(capacity, maxInput, maxOutput);
            this.direction = direction;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            int maxInput = Math.min(toReceive, SimpleDirectionalEnergyStorage.this.getMaxInsert(direction));
            if (!canReceive() || maxInput <= 0) {
                return 0;
            }

            int energyReceived = Mth.clamp(this.capacity - SimpleDirectionalEnergyStorage.this.storedEnergy, 0, Math.min(this.maxReceive, maxInput));
            if (!simulate)
                SimpleDirectionalEnergyStorage.this.storedEnergy += energyReceived;
            return energyReceived;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int maxOutput = Math.min(toExtract, SimpleDirectionalEnergyStorage.this.getMaxOutput(direction));
            if (!canExtract() || toExtract <= 0) {
                return 0;
            }

            int energyExtracted = Math.min(SimpleDirectionalEnergyStorage.this.storedEnergy, Math.min(this.maxExtract, maxOutput));
            if (!simulate)
                SimpleDirectionalEnergyStorage.this.storedEnergy -= energyExtracted;
            return energyExtracted;
        }

        @Override
        public int getEnergyStored() {
            return SimpleDirectionalEnergyStorage.this.storedEnergy;
        }

        @Override
        public boolean canExtract() {
            return SimpleDirectionalEnergyStorage.this.getMaxOutput(direction) > 0;
        }

        @Override
        public boolean canReceive() {
            return SimpleDirectionalEnergyStorage.this.getMaxInsert(direction) > 0;
        }
    }
}
