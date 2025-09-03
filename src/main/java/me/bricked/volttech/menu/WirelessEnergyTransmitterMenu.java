package me.bricked.volttech.menu;

import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.MenuRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class WirelessEnergyTransmitterMenu extends AbstractContainerMenu {
    private final Container container;

    public WirelessEnergyTransmitterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(9));
    }

    public WirelessEnergyTransmitterMenu(int containerId, Inventory playerInventory, Container container) {
        super(MenuRegistry.WIRELESS_ENERGY_TRANSMITTER_MENU.get(), containerId);
        checkContainerSize(container, 9);
        this.container = container;
        this.container.startOpen(playerInventory.player);
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(container, i, 8 + (i * 18), 12){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() == ItemRegistry.LOCATION_CARD_ITEM.get();
                }
            });
        }
        int invX = 8;
        int invY = 34;
        int hotbarX = 8;
        int hotbarY = 92;
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
}
