package me.bricked.volttech.menu;

import me.bricked.volttech.register.MenuRegistry;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class HeatGeneratorMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public HeatGeneratorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2), new SimpleContainerData(4));
    }

    public HeatGeneratorMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MenuRegistry.HEAT_GENERATOR_MENU.get(), containerId);
        checkContainerSize(container, 2);
        checkContainerDataCount(containerData, 4);
        this.container = container;
        this.containerData = containerData;
        this.container.startOpen(playerInventory.player);
        this.addDataSlots(this.containerData);
        this.addSlot(new Slot(container, 0, 8, 15){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return EnergyUtil.isNonFullEnergyItem(stack);
            }
        });
        this.addSlot(new Slot(container,1, 8, 62) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().is(BlockTags.ICE);
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

    public int getStoredFE() {
        return containerData.get(0);
    }

    public int getGenerationRate() {
        return containerData.get(1);
    }

    public int getIceTicksLeft() {
        return containerData.get(2);
    }

    public int getMaxIceTicks() {
        return containerData.get(3);
    }
}
