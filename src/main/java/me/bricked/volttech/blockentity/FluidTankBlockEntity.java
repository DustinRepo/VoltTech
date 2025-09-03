package me.bricked.volttech.blockentity;

import me.bricked.volttech.block.FluidTankBlock;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.fluid.InfiniteFluidTank;
import me.bricked.volttech.menu.FluidTankMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidTankBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer {
    public FluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        if (type == BlockEntityRegistry.CREATIVE_FLUID_TANK_BLOCK_ENTITY.get())
            tank = new InfiniteFluidTank();
        else
            tank = new FluidTank(Constraints.get(blockState.getBlock()).maxCapacity(), fluidStack -> !fluidStack.is(TagRegistry.Fluids.FLUID_TANK_BANNED));
    }

    public NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final FluidTank tank;
    private Fluid lastFluid;
    private int lastTankAmount;

    public static void tick(Level level, BlockPos pos, BlockState state, FluidTankBlockEntity blockEntity) {
        if (level.isClientSide()) {
            Fluid fluid = blockEntity.getTank().getFluid().getFluid();
            if (blockEntity.lastFluid != fluid)
                level.getLightEngine().checkBlock(pos);
            blockEntity.lastFluid = fluid;
            return;
        }
        ItemStack drainStack = blockEntity.getItem(0);
        ItemStack fillStack = blockEntity.getItem(1);
        FluidTank tank = blockEntity.getTank();
        FluidTankBlock fluidTankBlock = (FluidTankBlock) state.getBlock();
        boolean isCreativeTank = fluidTankBlock.getTankTier() == FluidTankBlock.TankTier.CREATIVE;

        if (isCreativeTank && !tank.isEmpty() && tank.getFluidAmount() < Integer.MAX_VALUE)
            tank.setFluid(new FluidStack(tank.getFluid().getFluid(), Integer.MAX_VALUE));

        if (!drainStack.isEmpty() && tank.getFluidAmount() < tank.getCapacity()) {
            Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(drainStack);
            capabilityOptional.ifPresent(itemStackTank -> {
                FluidActionResult result = FluidUtil.tryEmptyContainer(drainStack, tank, tank.getCapacity(), null, true);
                if (result.isSuccess())
                    blockEntity.setItem(0, result.getResult());
            });
        }

        if (!fillStack.isEmpty() && !tank.isEmpty()) {
            Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(fillStack);
            capabilityOptional.ifPresent(itemStackTank -> {
                FluidActionResult result = FluidUtil.tryFillContainer(fillStack, tank, tank.getCapacity(), null, true);
                if (result.isSuccess())
                    blockEntity.setItem(1, result.getResult());
            });
        }
        if (blockEntity.lastTankAmount != tank.getFluidAmount())
            blockEntity.markUpdated();
        blockEntity.lastTankAmount = tank.getFluidAmount();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FluidTankMenu(i, inventory, this, getBlockPos());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        tank.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        tank.deserialize(input);
    }

    private void markUpdated() {
        if (!getLevel().isClientSide())
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (!this.tank.getFluid().isEmpty()) {
            tag.store("Fluid", FluidStack.CODEC, this.getTank().getFluid());
        }
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
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        loadAdditional(valueInput);
    }

    public FluidTank getTank() {
        return tank;
    }
}
