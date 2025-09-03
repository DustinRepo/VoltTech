package me.bricked.volttech.menu;

import me.bricked.volttech.recipe.crusher.CrusherRecipe;
import me.bricked.volttech.register.MenuRegistry;
import me.bricked.volttech.register.RecipeRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class CrusherMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public CrusherMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(3));
    }

    public CrusherMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MenuRegistry.CRUSHER_MENU.get(), containerId);
        checkContainerSize(container, 3);
        checkContainerDataCount(containerData, 3);
        this.container = container;
        this.containerData = containerData;
        this.container.startOpen(playerInventory.player);
        this.addDataSlots(this.containerData);
        RecipeManager.CachedCheck<SingleRecipeInput, ? extends CrusherRecipe> quickCheck =
                RecipeManager.createCheck(RecipeRegistry.CRUSHER_RECIPE.get());
        this.addSlot(new Slot(container,0, 54, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!(playerInventory.player.level() instanceof ServerLevel serverLevel))
                    return true;
                return quickCheck.getRecipeFor(new SingleRecipeInput(stack), serverLevel).isPresent();
            };
        });
        this.addSlot(new Slot(container, 1, 107, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new Slot(container, 2, 107, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
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

    public int getCrushingTicks() {
        return containerData.get(1);
    }

    public int getMaxCrushingTicks() {
        return containerData.get(2);
    }
}
