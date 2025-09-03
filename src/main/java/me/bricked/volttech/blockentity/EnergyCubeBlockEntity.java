package me.bricked.volttech.blockentity;

import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleDirectionalEnergyStorage;
import me.bricked.volttech.menu.EnergyCubeMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class EnergyCubeBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer, IEnergyBlockEntity {
    private final Constraints constraints;
    private BlockCapabilityCache<IEnergyStorage, @Nullable Direction> cache;
    private final SimpleDirectionalEnergyStorage energyStorage;
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final ContainerData containerData;
    public EnergyCubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleDirectionalEnergyStorage(constraints) {
            @Override
            public int getMaxInsert(Direction direction) {
                Direction facing = EnergyCubeBlockEntity.this.getBlockState().getValue(DirectionalBlock.FACING);
                if (facing == direction)
                    return 0;
                return constraints.maxInput();
            }

            @Override
            public int getMaxOutput(Direction direction) {
                Direction facing = EnergyCubeBlockEntity.this.getBlockState().getValue(DirectionalBlock.FACING);
                if (direction != null && facing != direction)
                    return 0;
                return constraints.maxInput();
            }
        };
        this.containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> constraints.maxInput();
                    case 3 -> constraints.maxOutput();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyCubeBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        boolean isCreative = blockEntity.getType() == BlockEntityRegistry.CREATIVE_ENERGY_CUBE_BLOCK_ENTITY.get();
        if (isCreative)
            blockEntity.energyStorage.setStoredEnergy(Integer.MAX_VALUE);

        ItemStack chargeStack = blockEntity.getItem(0);
        ItemStack drainStack = blockEntity.getItem(1);
        IEnergyStorage chargeStackEnergyStorage = chargeStack.getCapability(Capabilities.EnergyStorage.ITEM);
        IEnergyStorage drainStackEnergyStorage = drainStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (chargeStackEnergyStorage != null) {
            if (chargeStackEnergyStorage.canReceive()) {
                EnergyUtil.moveEnergy(blockEntity.constraints.maxOutput(), blockEntity.energyStorage.getEnergyStorage(null), chargeStackEnergyStorage);
            }
        }
        if (drainStackEnergyStorage != null) {
            if (drainStackEnergyStorage.canReceive()) {
                EnergyUtil.moveEnergy(blockEntity.constraints.maxInput(), drainStackEnergyStorage, blockEntity.energyStorage.getEnergyStorage(null));
            }
        }

        Direction facing = state.getValue(DirectionalBlock.FACING);
        BlockPos neighborPos = pos.relative(facing);

        if (blockEntity.cache == null) {
            blockEntity.cache = BlockCapabilityCache.create(
                    Capabilities.EnergyStorage.BLOCK,
                    (ServerLevel) level,
                    neighborPos,
                    facing.getOpposite()
            );
        }

        IEnergyStorage storage = blockEntity.cache.getCapability();
        if (storage != null) {
            EnergyUtil.moveEnergy(blockEntity.constraints.maxOutput(), blockEntity.getEnergyStorage(state, facing), storage);
            if (isCreative)
                blockEntity.energyStorage.setStoredEnergy(Integer.MAX_VALUE);
        }
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage.getEnergyStorage(direction);
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
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new EnergyCubeMenu(containerId, playerInventory, this, containerData);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        energyStorage.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        energyStorage.deserialize(input);
    }
}
