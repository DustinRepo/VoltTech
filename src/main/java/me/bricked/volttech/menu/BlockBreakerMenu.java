package me.bricked.volttech.menu;

import me.bricked.volttech.register.MenuRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class BlockBreakerMenu extends AbstractContainerMenu {
    private final Container container;
    private final DataSlot dataSlot;

    public BlockBreakerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(10), DataSlot.standalone());
    }

    public BlockBreakerMenu(int containerId, Inventory playerInventory, Container container, DataSlot containerData) {
        super(MenuRegistry.BLOCK_BREAKER_MENU.get(), containerId);
        checkContainerSize(container, 10);
        this.container = container;
        this.dataSlot = containerData;
        this.container.startOpen(playerInventory.player);
        this.addDataSlot(this.dataSlot);
        this.addSlot(new Slot(container,0, 44, 39) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.has(DataComponents.TOOL);
            };
        });
        int slotId = 1;
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(container, slotId++, 98 + l * 18, 21 + m * 18));
            }
        }
        int invX = 8;
        int invY = 84;
        int hotbarX = 8;
        int hotbarY = 142;
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

    public int getStoredFE() {
        return dataSlot.get();
    }
}
