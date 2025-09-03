package me.bricked.volttech.blockentity;

import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.menu.SpatialCrateMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class SpatialCrateBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer {
    public SpatialCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SPATIAL_CRATE_BLOCK_ENTITY.get(), pos, blockState);
    }
    private final NonNullList<ItemStack> items = NonNullList.withSize(190, ItemStack.EMPTY);

    void playSound(BlockState state, SoundEvent sound) {
        this.level.playSound(null, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 0.5f, getBlockPos().getZ() + 0.5f, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(Player player) {
        ImplementedContainer.super.startOpen(player);
        this.playSound(getBlockState(), SoundEvents.BARREL_OPEN);
    }

    @Override
    public void stopOpen(Player player) {
        ImplementedContainer.super.stopOpen(player);
        this.playSound(getBlockState(), SoundEvents.BARREL_CLOSE);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getMaxStackSize() {
        return 64;//Integer.MAX_VALUE;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 64;//Integer.MAX_VALUE;
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new SpatialCrateMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
    }
}
