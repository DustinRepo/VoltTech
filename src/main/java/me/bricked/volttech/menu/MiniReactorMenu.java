package me.bricked.volttech.menu;

import me.bricked.volttech.blockentity.MiniReactorBlockEntity;
import me.bricked.volttech.register.MenuRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class MiniReactorMenu extends AbstractContainerMenu {
    private final Container container;
    private final MiniReactorBlockEntity miniReactorBlockEntity;

    public MiniReactorMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(containerId, playerInventory, new SimpleContainer(2), byteBuf.readBlockPos());
    }

    public MiniReactorMenu(int containerId, Inventory playerInventory, Container container, BlockPos pos) {
        super(MenuRegistry.MINI_REACTOR_MENU.get(), containerId);
        checkContainerSize(container, 2);
        this.container = container;
        this.container.startOpen(playerInventory.player);
        this.miniReactorBlockEntity = (MiniReactorBlockEntity) playerInventory.player.level().getBlockEntity(pos);
        this.addSlot(new Slot(container,0, 8, 15) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(TagRegistry.Items.URANIUM_INGOT_TAG);
            }
        });
        this.addSlot(new Slot(container,1, 8, 62));
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

    public MiniReactorBlockEntity getMiniReactorBlockEntity() {
        return miniReactorBlockEntity;
    }
}
