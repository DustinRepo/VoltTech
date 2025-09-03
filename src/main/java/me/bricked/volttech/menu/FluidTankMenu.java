package me.bricked.volttech.menu;

import me.bricked.volttech.blockentity.FluidTankBlockEntity;
import me.bricked.volttech.register.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.Optional;

public class FluidTankMenu extends AbstractContainerMenu {
    private final Container container;
    private final FluidTankBlockEntity fluidTankBlockEntity;

    public FluidTankMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(containerId, playerInventory, new SimpleContainer(2), byteBuf.readBlockPos());
    }

    public FluidTankMenu(int containerId, Inventory playerInventory, Container container, BlockPos blockPos) {
        super(MenuRegistry.FLUID_TANK_MENU.get(), containerId);
        checkContainerSize(container, 2);
        this.container = container;
        this.fluidTankBlockEntity = (FluidTankBlockEntity) playerInventory.player.level().getBlockEntity(blockPos);
        this.container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 8, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(stack);
                return capabilityOptional.isPresent();
            }
        });
        this.addSlot(new Slot(container, 1, 152, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(stack);
                return capabilityOptional.isPresent();
            }
        });
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

    public FluidTankBlockEntity getFluidTankBlockEntity() {
        return fluidTankBlockEntity;
    }
}
