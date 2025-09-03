package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.util.Constraints;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;

public abstract class PassthroughDirectionalEnergyStorage {
    private final SimpleEnergyStorage[] energyStorages = new SimpleEnergyStorage[7];
    private final Constraints constraints;
    private ArrayList<IEnergyStorage> targets;
    public int cableMaxMove;
    private final Runnable refreshTargetsFunction;

    public PassthroughDirectionalEnergyStorage(Constraints constraints, Runnable refreshTargetsFunction) {
        for (int i = 0; i < 7; i++) {
            Direction direction;
            if (i == 6)
                direction = null;
            else
                direction = Direction.values()[i];
            this.energyStorages[i] = new DirectionalStorage(this, direction, constraints.maxCapacity(), constraints.maxInput(), constraints.maxOutput());
        }
        this.constraints = constraints;
        this.refreshTargetsFunction = refreshTargetsFunction;
        this.cableMaxMove = constraints.maxInput();
    }

    public IEnergyStorage getEnergyStorage(Direction direction) {
        if (direction == null)
            return energyStorages[6];
        return energyStorages[direction.ordinal()];
    }

    public int getMaxInsert(Direction direction) {
        return cableMaxMove;
    }

    public int getMaxEnergyStored() {
        return this.constraints.maxCapacity();
    }

    public Constraints getLogisticalConstraints() {
        return constraints;
    }

    public void setTargets(ArrayList<IEnergyStorage> targets) {
        this.targets = targets;
    }

    public ArrayList<IEnergyStorage> getTargets() {
        return targets;
    }

    public void addTarget(IEnergyStorage iEnergyStorage) {
        this.targets.add(iEnergyStorage);
    }

    public void clearTargets() {
        this.targets.clear();
    }

    public class DirectionalStorage extends SimpleEnergyStorage {
        private final Direction direction;
        private final PassthroughDirectionalEnergyStorage passthrough;
        public DirectionalStorage(PassthroughDirectionalEnergyStorage passthrough, Direction direction, int capacity, int maxInput, int maxOutput) {
            super(capacity, maxInput, maxOutput);
            this.direction = direction;
            this.passthrough = passthrough;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            toReceive = Math.min(toReceive, PassthroughDirectionalEnergyStorage.this.getMaxInsert(direction));
            if (targets == null) {
                if (refreshTargetsFunction != null)
                    refreshTargetsFunction.run();
                return 0;
            }
            int moved = 0;
            for (IEnergyStorage target : targets) {
                if (target == null || target == this)
                    continue;
                if (toReceive <= 0)
                    break;
                int inserted = target.receiveEnergy(toReceive, simulate);
                if (inserted > 0) {
                    toReceive -= inserted;
                    moved += inserted;
                }
            }
            return moved;
        }

        @Override
        public int getEnergyStored() {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return PassthroughDirectionalEnergyStorage.this.getMaxInsert(direction) > 0;
        }

        public PassthroughDirectionalEnergyStorage getPassthrough() {
            return passthrough;
        }
    }
}
