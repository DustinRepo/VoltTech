package me.bricked.volttech.menu;

import me.bricked.volttech.register.MenuRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SpatialCrateMenu extends AbstractContainerMenu {
    private final Container container;

    public SpatialCrateMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(190));
    }

    public SpatialCrateMenu(int containerId, Inventory playerInventory, Container container) {
        super(MenuRegistry.SPATIAL_CRATE_MENU.get(), containerId);
        checkContainerSize(container, 190);
        this.container = container;
        this.container.startOpen(playerInventory.player);
        int slotId = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 19; x++) {
                this.addSlot(new Slot(container, slotId, 8 + (x * 18), 12 + (y * 18)) {
                    /*@Override
                    public int getMaxStackSize() {
                        return Integer.MAX_VALUE;
                    }

                    @Override
                    public int getMaxStackSize(ItemStack stack) {
                        return Integer.MAX_VALUE;
                    }*/
                });
                slotId++;
            }
        }
        int invX = 98;
        int invY = 196;
        int hotbarX = 98;
        int hotbarY = 254;
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, invX + l * 18, invY + m * 18));
            }
        }
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, hotbarX + m * 18, hotbarY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public Container getContainer() {
        return container;
    }
}
