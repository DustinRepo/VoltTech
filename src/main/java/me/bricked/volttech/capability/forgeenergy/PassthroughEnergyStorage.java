package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.util.Constraints;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;

public class PassthroughEnergyStorage extends EnergyStorage {
    private ArrayList<IEnergyStorage> targets;
    public int cableMaxMove;
    private final Runnable refreshTargetsFunction;

    public PassthroughEnergyStorage(Constraints energySetting, Runnable refreshTargetsFunction) {
        super(energySetting.maxCapacity(), energySetting.maxInput(), energySetting.maxOutput());
        this.refreshTargetsFunction = refreshTargetsFunction;
        this.cableMaxMove = energySetting.maxInput();
    }

    @Override
    public int receiveEnergy(int maxIn, boolean simulate) {
        maxIn = Math.min(maxIn, cableMaxMove);
        if (targets == null) {
            if (refreshTargetsFunction != null)
                refreshTargetsFunction.run();
            return 0;
        }
        int moved = 0;
        for (IEnergyStorage target : targets) {
            if (target == null || target == this)
                continue;
            if (maxIn <= 0)
                break;
            int inserted = target.receiveEnergy(maxIn, simulate);
            if (inserted > 0) {
                maxIn -= inserted;
                moved += inserted;
            }
        }
        return moved;
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

    public int getMaxReceieve() {
        return maxReceive;
    }
}
