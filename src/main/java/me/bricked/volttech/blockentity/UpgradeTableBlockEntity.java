package me.bricked.volttech.blockentity;

import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.menu.UpgradeTableMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class UpgradeTableBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer {
    public UpgradeTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.UPGRADE_TABLE_BLOCK_ENTITY.get(), pos, blockState);
    }
    private final NonNullList<ItemStack> items = NonNullList.withSize(16, ItemStack.EMPTY);
    private ResourceLocation displayItem = ResourceLocation.withDefaultNamespace("air");
    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new UpgradeTableMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        output.putString("displayItem", displayItem.toString());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        displayItem = ResourceLocation.parse(input.getStringOr("displayItem", "minecraft:air"));
    }

    @Override
    public void setChanged() {
        displayItem = BuiltInRegistries.ITEM.getKey(getItem(0).getItem());
        markUpdated();
        super.setChanged();
    }

    private void markUpdated() {
        if (!getLevel().isClientSide())
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("displayItem", displayItem.toString());
        return tag;
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        loadAdditional(input);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput input) {
        loadAdditional(input);
    }

    public ResourceLocation getDisplayItem() {
        return displayItem;
    }
}
