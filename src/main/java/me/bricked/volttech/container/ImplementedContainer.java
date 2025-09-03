package me.bricked.volttech.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ImplementedContainer extends Container {
    NonNullList<ItemStack> getItems();
    int getMaxStackSize();

    @Override
    default int getContainerSize() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    default ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    default void setChanged() {

    }

    @Override
    default ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        this.setChanged();
        return stack;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = ContainerHelper.takeItem(this.getItems(), slot);
        this.setChanged();
        return stack;
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        //stack.limitSize(this.getMaxStackSize(stack));
        this.getItems().set(slot, stack);
        this.setChanged();
    }

    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    @Override
    default void clearContent() {
        getItems().clear();
        this.setChanged();
    }
}