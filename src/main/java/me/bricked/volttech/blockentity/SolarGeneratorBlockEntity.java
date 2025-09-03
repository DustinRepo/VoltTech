package me.bricked.volttech.blockentity;

import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleDirectionalEnergyStorage;
import me.bricked.volttech.menu.SolarGeneratorMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class SolarGeneratorBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer, IEnergyBlockEntity {
    public SolarGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SOLAR_GENERATOR_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        energyStorage = new SimpleDirectionalEnergyStorage(constraints) {
            @Override
            public int getMaxInsert(Direction direction) {
                if (direction == null)
                    return getLogisticalConstraints().maxInput();
                return 0;
            }

            @Override
            public int getMaxOutput(Direction direction) {
                if (direction == Direction.DOWN)
                    return getLogisticalConstraints().maxOutput();
                return 0;
            }
        };
    }
    private final Constraints constraints;
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final SimpleDirectionalEnergyStorage energyStorage;
    private boolean isDay;
    private boolean hasSkylight;
    private int generationRate;
    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int i) {
            int bits = 0;
            if (isDay)
                bits |= 1;
            if (hasSkylight)
                bits |= 1 << 1;
            return switch (i) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> generationRate;
                case 2 -> bits;
                default -> 0;
            };
        }

        @Override
        public void set(int i, int i1) {

        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public static void tick(Level level, BlockPos pos, BlockState state, SolarGeneratorBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        long time = level.getDayTime() % 24000;
        int maxGenerationRate = blockEntity.constraints.usageOrGeneration();
        blockEntity.isDay = time >= 0 && (time <= 13000 || time >= 23000);
        blockEntity.hasSkylight = level.canSeeSky(BlockPos.containing(pos.getX(), pos.getY() + 1, pos.getZ()));
        blockEntity.generationRate = maxGenerationRate;

        int lowestGeneration = 1;
        if (time >= 12000 && time <= 13000) {
            long timeLeft = 13000 - time;
            int variance = maxGenerationRate - lowestGeneration;
            blockEntity.generationRate = (int) (lowestGeneration + (variance * (timeLeft / 1000.f)));
        } else
        if (time >= 23000) {
            long timeLeft = 24000 - time;
            int variance = maxGenerationRate - lowestGeneration;
            blockEntity.generationRate = (int) (maxGenerationRate - (variance * (timeLeft / 1000.f)));
        }

        if (level.isRaining())
            blockEntity.generationRate = (int)(blockEntity.generationRate * Config.SOLAR_GENERATOR_RAIN_MULTIPLIER.get());

        if (blockEntity.isDay && blockEntity.hasSkylight) {
            if (blockEntity.energyStorage.getEnergyStored() < blockEntity.energyStorage.getMaxEnergyStored()) {
                int amountLeft = blockEntity.energyStorage.getMaxEnergyStored() - blockEntity.energyStorage.getEnergyStored();
                int amount = blockEntity.generationRate;
                if (amount > amountLeft)
                    amount = amountLeft;
                blockEntity.energyStorage.addEnergy(amount);
            }
        } else {
            blockEntity.generationRate = 0;
        }
        ItemStack chargeStack = blockEntity.getItem(0);
        IEnergyStorage chargeStackEnergyStorage = chargeStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (chargeStackEnergyStorage != null) {
            if (chargeStackEnergyStorage.canReceive()) {
                EnergyUtil.moveEnergy(blockEntity.constraints.maxOutput(), chargeStackEnergyStorage, blockEntity.energyStorage.getEnergyStorage(null));
            }
        }

        IEnergyStorage belowEnergyStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.below(), Direction.UP);
        if (belowEnergyStorage != null) {
            EnergyUtil.moveEnergy(blockEntity.constraints.maxOutput(), blockEntity.energyStorage.getEnergyStorage(Direction.DOWN), belowEnergyStorage);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        this.energyStorage.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        this.energyStorage.deserialize(input);
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
        return new SolarGeneratorMenu(containerId, inventory, this, containerData);
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage.getEnergyStorage(direction);
    }
}
