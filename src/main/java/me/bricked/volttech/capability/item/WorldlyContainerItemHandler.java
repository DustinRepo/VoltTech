package me.bricked.volttech.capability.item;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class WorldlyContainerItemHandler implements IItemHandler {
    private final WorldlyContainer container;
    private final Direction direction;
    private final int[] slots;

    public WorldlyContainerItemHandler(WorldlyContainer container, Direction direction, int[] slots) {
        this.container = container;
        this.direction = direction;
        this.slots = slots;
    }

    @Override
    public int getSlots() {
        return container.getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return container.getItem(i);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !isItemValid(slot, stack) || !isSlotValid(slot) || !container.canPlaceItemThroughFace(slot, stack, direction)) {
            return stack;
        }

        ItemStack existing = container.getItem(slot);
        int slotLimit = getSlotLimit(slot);

        if (existing.isEmpty()) {
            int insertAmount = Math.min(stack.getCount(), slotLimit);
            if (!simulate) {
                ItemStack toInsert = stack.copy();
                toInsert.setCount(insertAmount);
                container.setItem(slot, toInsert);
                container.setChanged();
            }
            ItemStack remaining = stack.copy();
            remaining.shrink(insertAmount);
            return remaining;
        }

        if (ItemStack.isSameItemSameComponents(stack, existing)) {
            int combinedCount = existing.getCount() + stack.getCount();
            int insertAmount = Math.min(slotLimit, combinedCount) - existing.getCount();

            if (insertAmount <= 0) {
                return stack;
            }

            if (!simulate) {
                existing.grow(insertAmount);
                container.setChanged();
            }

            ItemStack remaining = stack.copy();
            remaining.shrink(insertAmount);
            return remaining;
        }

        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0 || !isSlotValid(slot))
            return ItemStack.EMPTY;

        ItemStack existing = container.getItem(slot);
        if (existing.isEmpty() || !container.canTakeItemThroughFace(slot, existing, direction))
            return ItemStack.EMPTY;

        int toExtract = Math.min(existing.getCount(), amount);
        ItemStack result = existing.copy();
        result.setCount(toExtract);

        if (!simulate) {
            existing.shrink(toExtract);
            if (existing.getCount() <= 0) {
                container.setItem(slot, ItemStack.EMPTY);
            }
            container.setChanged();
        }

        return result;
    }

    private boolean isSlotValid(int slot) {
        for (int i : slots) {
            if (i == slot)
                return true;
        }
        return false;
    }

    @Override
    public int getSlotLimit(int slot) {
        return container.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return container.canPlaceItem(slot, stack);
    }
}
