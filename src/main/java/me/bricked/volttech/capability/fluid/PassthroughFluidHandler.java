package me.bricked.volttech.capability.fluid;

import me.bricked.volttech.util.Constraints;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;

public class PassthroughFluidHandler implements IFluidHandler {
    private ArrayList<IFluidHandler> targets;
    public int maxThroughput;
    private final Runnable refreshTargetsFunction;

    public PassthroughFluidHandler(Constraints fluidSetting, Runnable refreshTargetsFunction) {
        this.refreshTargetsFunction = refreshTargetsFunction;
        this.maxThroughput = fluidSetting.maxInput();
    }

    public void setTargets(ArrayList<IFluidHandler> targets) {
        this.targets = targets;
    }

    public ArrayList<IFluidHandler> getTargets() {
        return targets;
    }

    public void addTarget(IFluidHandler iFluidHandler) {
        this.targets.add(iFluidHandler);
    }

    public void clearTargets() {
        this.targets.clear();
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack useStack = fluidStack.copy();
        if (targets == null) {
            if (refreshTargetsFunction != null)
                refreshTargetsFunction.run();
            return 0;
        }
        int moved = 0;
        for (IFluidHandler target : targets) {
            int toInsert = Math.min(useStack.getAmount(), maxThroughput);
            if (toInsert > 0) {
                FluidStack adjustedStack = useStack.copy();
                adjustedStack.setAmount(toInsert);
                boolean isFluidValid = false;
                for (int i = 0; i < target.getTanks(); i++)
                    if (target.isFluidValid(i, adjustedStack)) {
                        isFluidValid = true;
                        break;
                    }
                if (!isFluidValid)
                    continue;
                int filled = target.fill(adjustedStack, fluidAction);
                moved += filled;
                useStack.setAmount(useStack.getAmount() - moved);
            }
        }
        return moved;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int i) {
        return maxThroughput;
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return true;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return FluidStack.EMPTY;
    }
}
