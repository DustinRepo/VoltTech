package me.bricked.volttech.menu;

import me.bricked.volttech.item.UpgradeCardItem;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.MenuRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.ItemUtil;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;


public class UpgradeTableMenu extends AbstractContainerMenu {
    private final Container container;

    public UpgradeTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(16));
    }

    public UpgradeTableMenu(int containerId, Inventory playerInventory, Container container) {
        super(MenuRegistry.UPGRADE_TABLE_MENU.get(), containerId);
        checkContainerSize(container, 16);
        this.container = container;
        this.container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 8, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return UpgradeData.isUpgradeableItem(stack.getItem());
            }

            @Override
            public void set(ItemStack stack) {
                if (!stack.isEmpty())
                    loadUpgradeSlots(stack);
                super.set(stack);
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                if (player.level().isClientSide()) {
                    super.onTake(player, stack);
                    return;
                }
                applyUpgrades(stack);
                clearUpgradeSlots();
                super.onTake(player, stack);
            }
        });
        int slotId = 1;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 5; x++) {
                this.addSlot(new Slot(container, slotId, 44 + (x * 18), 17 + (y * 18)) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        ItemStack upgradeStack = container.getItem(0);
                        int stackSize = ItemUtil.getMaxCountOfCard(upgradeStack, stack);
                        if (stackSize == 0)
                            return false;
                        for (int i = 1; i < container.getContainerSize(); i++) {
                            if (i == this.getSlotIndex())
                                continue;
                            if (container.getItem(i).is(stack.getItem()))
                                return false;
                        }
                        return !upgradeStack.isEmpty() && stack.is(TagRegistry.Items.UPGRADE_CARDS);
                    }

                    @Override
                    public int getMaxStackSize(ItemStack stack) {
                        ItemStack upgradeStack = container.getItem(0);
                        int count = ItemUtil.getMaxCountOfCard(upgradeStack, stack);
                        return Math.max(count, 1);
                    }
                });
                slotId++;
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

    private void applyUpgrades(ItemStack itemStack) {
        for (Supplier<DataComponentType<Integer>> upgradeDataComponent : DataComponentRegistry.UPGRADE_COMPONENTS) {
            if (upgradeDataComponent == null)
                continue;
            itemStack.remove(upgradeDataComponent.get());
        }

        for (int i = 1; i < container.getContainerSize(); i++) {
            ItemStack slotStack = container.getItem(i);
            if (slotStack.getItem() instanceof UpgradeCardItem upgradeCardItem) {
                itemStack.set(upgradeCardItem.getDataComponent().get(), slotStack.getCount());
            }
        }
    }

    private void loadUpgradeSlots(ItemStack itemStack) {
        clearUpgradeSlots();
        int placeSlot = 1;
        for (TypedDataComponent<?> component : itemStack.getComponents()) {
            if (!DataComponentRegistry.isUpgradeComponent(component.type()))
                continue;
            DataComponentType<Integer> intComponent = (DataComponentType<Integer>)component.type();
            UpgradeCardItem upgradeCardItem = UpgradeCardItem.from(component.type());
            if (upgradeCardItem != null) {
                int currentCount = itemStack.get(intComponent);
                container.setItem(placeSlot, new ItemStack(upgradeCardItem, Math.min(currentCount, upgradeCardItem.getMaxCount())));
                placeSlot++;
            }
        }
    }

    private void clearUpgradeSlots() {
        for (int i = 1; i < container.getContainerSize(); i++) {
            container.setItem(i, Items.AIR.getDefaultInstance());
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        Slot slot = this.slots.get(invSlot);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack originalStack = slot.getItem();
        ItemStack copy = originalStack.copy();

        if (invSlot == 0) {
            applyUpgrades(originalStack);
            clearUpgradeSlots();

            if (!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            slot.setChanged();
            return copy;
        }

        if (UpgradeData.isUpgradeableItem(originalStack.getItem())) {
            if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
            return copy;
        }
        ItemStack newStack = ItemStack.EMPTY;
        if (slot.hasItem()) {
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
