package me.bricked.volttech.blockentity;

import com.google.common.collect.Maps;
import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.menu.MiniReactorMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class MiniReactorBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer, IEnergyBlockEntity {
    public MiniReactorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.MINI_REACTOR_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        energyStorage = new SimpleEnergyStorage(constraints);
        waterTank = new FluidTank(Config.MINI_REACTOR_WATER_CAPACITY.get(), fluidStack -> fluidStack.is(Tags.Fluids.WATER));
        maxConsumeTicks = Config.MINI_REACTOR_URANIUM_TICK_DURATION.get();
    }
    private final Constraints constraints;
    private final int maxConsumeTicks;
    private final FluidTank waterTank;
    private final SimpleEnergyStorage energyStorage;
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final Map<Direction, BlockCapabilityCache<IEnergyStorage, @Nullable Direction>> capabilityCaches = Maps.newHashMap();
    private ResourceLocation consumedItem = ResourceLocation.withDefaultNamespace("air");

    private int consumeTicksLeft;
    private int lastConsumeTicksLeft;
    private boolean isRunning;
    private int lastWaterAmount;
    private boolean hadUranium;

    public static void tick(Level level, BlockPos pos, BlockState state, MiniReactorBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        SimpleEnergyStorage selfEnergyStorage = blockEntity.energyStorage;
        if (selfEnergyStorage == null)
            return;
        ItemStack uraniumStack = blockEntity.getItem(0);
        ItemStack bucketStack = blockEntity.getItem(1);
        boolean hasUranium = uraniumStack != ItemStack.EMPTY;
        boolean reactorRunning = blockEntity.consumeTicksLeft > 0;
        int energyLeft = selfEnergyStorage.getMaxEnergyStored() - selfEnergyStorage.getEnergyStored();
        if (reactorRunning) {
            if (energyLeft > 0)
                selfEnergyStorage.insertEnergy(Math.min(energyLeft, Config.MINI_REACTOR_URANIUM_FE_PER_TICK.get()), false);
            blockEntity.consumeTicksLeft--;
        }

        if (!bucketStack.isEmpty()) {
            Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(bucketStack);
            capabilityOptional.ifPresent(itemStackTank -> {
                FluidActionResult result = FluidUtil.tryEmptyContainer(bucketStack, blockEntity.waterTank, blockEntity.waterTank.getCapacity(), null, true);
                if (result.isSuccess())
                    blockEntity.setItem(1, result.getResult());
            });
        }

        if (blockEntity.consumeTicksLeft <= 0) {
            if (blockEntity.waterTank.getFluidAmount() >= Config.MINI_REACTOR_WATER_USAGE.get() && uraniumStack.is(TagRegistry.Items.URANIUM_INGOT_TAG)) {
                if (energyLeft > 0) {
                    blockEntity.consumedItem = BuiltInRegistries.ITEM.getKey(uraniumStack.getItem());
                    uraniumStack.setCount(uraniumStack.getCount() - 1);
                    blockEntity.waterTank.drain(Config.MINI_REACTOR_WATER_USAGE.get(), IFluidHandler.FluidAction.EXECUTE);
                    blockEntity.consumeTicksLeft = Config.MINI_REACTOR_URANIUM_TICK_DURATION.get();
                }
            }
        }
        if (blockEntity.isRunning != blockEntity.consumeTicksLeft > 0 ||
                blockEntity.lastWaterAmount != blockEntity.waterTank.getFluidAmount() ||
                blockEntity.hadUranium != hasUranium ||
                blockEntity.consumeTicksLeft != blockEntity.lastConsumeTicksLeft) {
            blockEntity.markUpdated();
        }
        blockEntity.isRunning = blockEntity.consumeTicksLeft > 0;
        blockEntity.lastWaterAmount = blockEntity.waterTank.getFluidAmount();
        blockEntity.hadUranium = hasUranium;
        blockEntity.lastConsumeTicksLeft = blockEntity.consumeTicksLeft;

        int toMove = Math.min(blockEntity.constraints.maxOutput(), blockEntity.energyStorage.getEnergyStored());
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            blockEntity.capabilityCaches.computeIfAbsent(dir, d ->
                    BlockCapabilityCache.create(
                            Capabilities.EnergyStorage.BLOCK,
                            (ServerLevel) level,
                            neighborPos,
                            d.getOpposite()
                    )
            );
            if (toMove <= 0)
                continue;
            BlockCapabilityCache<IEnergyStorage, @Nullable Direction> cache = blockEntity.capabilityCaches.get(dir);
            IEnergyStorage targetStorage = cache.getCapability();
            if (targetStorage != null && targetStorage.canReceive()) {
                toMove = EnergyUtil.moveEnergy(toMove, selfEnergyStorage, targetStorage);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        waterTank.serialize(output);
        energyStorage.serialize(output);
        output.putInt("consumeTicksLeft", consumeTicksLeft);
        output.putString("consumedItem", consumedItem.toString());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        waterTank.deserialize(input);
        energyStorage.deserialize(input);
        this.consumeTicksLeft = input.getIntOr("consumeTicksLeft", 0);
        this.consumedItem = ResourceLocation.parse(input.getStringOr("consumedItem", "minecraft:air"));
    }

    private void markUpdated() {
        if (!getLevel().isClientSide())
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (!this.waterTank.getFluid().isEmpty()) {
            tag.store("Fluid", FluidStack.CODEC, this.waterTank.getFluid());
        }
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("consumeTicksLeft", consumeTicksLeft);
        tag.putString("consumedItem", consumedItem.toString());
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
        loadAdditional(valueInput);
    }

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
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MiniReactorMenu(containerId, inventory, this, getBlockPos());
    }

    public FluidTank getWaterTank() {
        return waterTank;
    }

    public int getConsumeTicksLeft() {
        return consumeTicksLeft;
    }

    public int getMaxConsumeTicks() {
        return maxConsumeTicks;
    }

    public ResourceLocation getConsumedItem() {
        return consumedItem;
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage;
    }
}
