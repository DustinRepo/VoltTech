package me.bricked.volttech.capability.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class SingleTankFluidHandler implements IFluidHandler {
    private final FluidTank fluidTank;
    private final int maxFill;
    private final int maxDrain;
    public SingleTankFluidHandler(FluidTank tank, int maxFill, int maxDrain) {
        this.fluidTank = tank;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
    }
    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return fluidTank.getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return fluidTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack fluidStack) {
        return fluidTank.isFluidValid(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        if (!fluidTank.isFluidValid(fluidStack))
            return 0;
        int toInsert = Math.min(fluidStack.getAmount(), maxFill);
        if (toInsert > 0) {
            FluidStack adjustedStack = fluidStack.copy();
            adjustedStack.setAmount(toInsert);
            return fluidTank.fill(adjustedStack, fluidAction);
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        if (!fluidTank.getFluid().is(fluidStack.getFluid()))
            return FluidStack.EMPTY;
        int toDrain = Math.min(fluidStack.getAmount(), maxDrain);
        if (toDrain > 0) {
            FluidStack adjustedStack = fluidStack.copy();
            adjustedStack.setAmount(toDrain);
            return fluidTank.drain(adjustedStack, fluidAction);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int drainAmount, FluidAction fluidAction) {
        int toDrain = Math.min(drainAmount, maxDrain);
        toDrain = Math.min(toDrain, fluidTank.getFluidAmount());
        if (toDrain > 0) {
            FluidStack adjustedStack = new FluidStack(fluidTank.getFluid().getFluid(), toDrain);
            return fluidTank.drain(adjustedStack, fluidAction);
        }
        return FluidStack.EMPTY;
    }
}
