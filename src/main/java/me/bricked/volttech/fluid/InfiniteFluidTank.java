package me.bricked.volttech.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class InfiniteFluidTank extends FluidTank {
    public InfiniteFluidTank() {
        super(Integer.MAX_VALUE);
    }

    public InfiniteFluidTank(Predicate<FluidStack> validator) {
        super(Integer.MAX_VALUE, validator);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        resource.setAmount(Integer.MAX_VALUE);
        return super.fill(resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return super.drain(maxDrain, FluidAction.SIMULATE); // Always simulate draining instead of execute
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return super.drain(resource, FluidAction.SIMULATE);
    }

    @Override
    public int getFluidAmount() {
        if (this.getFluid() != FluidStack.EMPTY) {
            this.getFluid().setAmount(Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }
}
