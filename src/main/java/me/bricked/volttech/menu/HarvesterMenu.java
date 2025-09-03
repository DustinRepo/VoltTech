package me.bricked.volttech.menu;

import me.bricked.volttech.blockentity.HarvesterBlockEntity;
import me.bricked.volttech.network.c2s.HarvesterOffsetsPayload;
import me.bricked.volttech.register.BlockRegistry;
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
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class HarvesterMenu extends AbstractContainerMenu {
    private final Container container;
    private final HarvesterBlockEntity harvesterBlockEntity;

    public HarvesterMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new SimpleContainer(9), buf.readBlockPos());
    }

    public HarvesterMenu(int containerId, Inventory playerInventory, Container container, BlockPos blockPos) {
        super(MenuRegistry.HARVESTER_MENU.get(), containerId);
        checkContainerSize(container, 9);
        this.container = container;
        this.container.startOpen(playerInventory.player);
        this.harvesterBlockEntity = (HarvesterBlockEntity) playerInventory.player.level().getBlockEntity(blockPos);
        int slotId = 0;
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(container, slotId++, 102 + l * 18, 116 + m * 18));
            }
        }
        int invX = 48;
        int invY = 174;
        int hotbarX = 48;
        int hotbarY = 232;
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

    public void sendPacket() {
        ClientPacketDistributor.sendToServer(new HarvesterOffsetsPayload(
                harvesterBlockEntity.getBlockPos(),
                new BlockPos(
                    harvesterBlockEntity.getxOffset(),
                    harvesterBlockEntity.getyOffset(),
                    harvesterBlockEntity.getzOffset()
                ),
                new BlockPos(
                    harvesterBlockEntity.getxSize(),
                    harvesterBlockEntity.getySize(),
                    harvesterBlockEntity.getzSize()
                ),
                harvesterBlockEntity.isRenderBox()
        ));
    }

    public int getStoredFE() {
        return harvesterBlockEntity.getEnergyStorage(BlockRegistry.HARVESTER.get().defaultBlockState(), null).getEnergyStored();
    }

    public int getXOffset() {
        return harvesterBlockEntity.getxOffset();
    }

    public int getYOffset() {
        return harvesterBlockEntity.getyOffset();
    }

    public int getZOffset() {
        return harvesterBlockEntity.getzOffset();
    }

    public int getXSize() {
        return harvesterBlockEntity.getxSize();
    }

    public int getYSize() {
        return harvesterBlockEntity.getySize();
    }

    public int getZSize() {
        return harvesterBlockEntity.getzSize();
    }

    public boolean shouldRenderBox() {
        return harvesterBlockEntity.isRenderBox();
    }

    public void setXOffset(int value) {
        harvesterBlockEntity.setxOffset(value);
        sendPacket();
    }

    public void setYOffset(int value) {
        harvesterBlockEntity.setyOffset(value);
        sendPacket();
    }

    public void setZOffset(int value) {
        harvesterBlockEntity.setzOffset(value);
        sendPacket();
    }

    public void setXSize(int value) {
        harvesterBlockEntity.setxSize(value);
        sendPacket();
    }

    public void setYSize(int value) {
        harvesterBlockEntity.setySize(value);
        sendPacket();
    }

    public void setZSize(int value) {
        harvesterBlockEntity.setzSize(value);
        sendPacket();
    }

    public void setShouldRenderBox(boolean value) {
        harvesterBlockEntity.setRenderBox(value);
        sendPacket();
    }
}
