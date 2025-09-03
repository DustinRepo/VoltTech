package me.bricked.volttech.capability.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;

public class PassthroughItemHandler implements IItemHandler {
    private ArrayList<IItemHandler> targets;
    public int maxThroughput;
    private final Runnable refreshTargetsFunction;

    public PassthroughItemHandler(int maxThroughput, Runnable refreshTargetsFunction) {
        this.maxThroughput = maxThroughput;
        this.refreshTargetsFunction = refreshTargetsFunction;
        this.targets = new ArrayList<>();
    }

    public void setTargets(ArrayList<IItemHandler> targets) {
        this.targets = targets;
    }

    public ArrayList<IItemHandler> getTargets() {
        return targets;
    }

    public void addTarget(IItemHandler itemHandler) {
        this.targets.add(itemHandler);
    }

    public void clearTargets() {
        this.targets.clear();
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || targets == null || targets.isEmpty()) {
            if (refreshTargetsFunction != null)
                refreshTargetsFunction.run();
            return stack;
        }

        ItemStack toInsert = stack.copy();
        int maxInsert = Math.min(toInsert.getCount(), maxThroughput);

        for (IItemHandler target : targets) {
            ItemStack attempt = toInsert.copy();
            attempt.setCount(maxInsert);

            for (int i = 0; i < target.getSlots(); i++) {
                if (!target.isItemValid(i, attempt)) continue;

                ItemStack result = target.insertItem(i, attempt, simulate);

                if (result.isEmpty()) {
                    toInsert.shrink(maxInsert);
                    return toInsert.getCount() > 0 ? toInsert : ItemStack.EMPTY;
                } else if (result.getCount() < maxInsert) {
                    int inserted = maxInsert - result.getCount();
                    toInsert.shrink(inserted);
                    return toInsert;
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (targets == null || targets.isEmpty()) {
            if (refreshTargetsFunction != null)
                refreshTargetsFunction.run();
            return ItemStack.EMPTY;
        }

        int maxExtract = Math.min(amount, maxThroughput);

        for (IItemHandler target : targets) {
            for (int i = 0; i < target.getSlots(); i++) {
                ItemStack stackInSlot = target.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    ItemStack extracted = target.extractItem(i, maxExtract, simulate);
                    if (!extracted.isEmpty()) return extracted;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}
