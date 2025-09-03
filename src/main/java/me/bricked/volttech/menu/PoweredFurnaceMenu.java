package me.bricked.volttech.menu;

import me.bricked.volttech.blockentity.PoweredFurnaceBlockEntity;
import me.bricked.volttech.register.MenuRegistry;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class PoweredFurnaceMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public PoweredFurnaceMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(8), new SimpleContainerData(10));
    }

    public PoweredFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MenuRegistry.POWERED_FURNACE_MENU.get(), containerId);
        checkContainerSize(container, 8);
        checkContainerDataCount(containerData, 10);
        this.container = container;
        this.containerData = containerData;
        this.container.startOpen(playerInventory.player);
        this.addDataSlots(this.containerData);
        RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(container, i, 26 + (i * 36), 15){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    if (!(playerInventory.player.level() instanceof ServerLevel serverLevel))
                        return true;
                    return !ItemUtil.getCookingRecipeStack(stack, serverLevel, quickCheck).isEmpty();
                }
            });
        }
        for (int i = 0; i < 4; i++) {
            this.addSlot(new Slot(container, i + 4, 26 + (i * 36), 62){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public void onTake(Player player, ItemStack stack) {
                    super.onTake(player, stack);
                    if (player instanceof ServerPlayer serverplayer && this.container instanceof PoweredFurnaceBlockEntity poweredFurnaceBlockEntity) {
                        PoweredFurnaceBlockEntity.givePlayerXP(serverplayer, serverplayer.level(), poweredFurnaceBlockEntity);
                    }
                }
            });
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
        return containerData.get(0);
    }

    public int getMaxFE() {
        return containerData.get(1);
    }

    public int getCookTime(int slot) {
        return containerData.get(2 + slot);
    }

    public int getMaxCookTime(int slot) {
        return containerData.get(6 + slot);
    }
}
