package me.bricked.volttech.blockentity;

import com.google.common.collect.Maps;
import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.menu.HeatGeneratorMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class HeatGeneratorBlockEntity extends BlockEntity implements IEnergyBlockEntity, MenuProvider, ImplementedContainer {
    public HeatGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.HEAT_GENERATOR_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleEnergyStorage(constraints);
        this.containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> generationRate;
                    case 2 -> iceUseTicks;
                    case 3 -> Config.HEAT_GENERATOR_ICE_TICKS.get();
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
    private final Constraints constraints;
    private final SimpleEnergyStorage energyStorage;
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final ContainerData containerData;
    private final Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> cacheMap = Maps.newHashMap();
    private int generationRate;
    private int iceUseTicks;

    public static void tick(Level level, BlockPos pos, BlockState state, HeatGeneratorBlockEntity blockEntity) {
        if (level == null || level.isClientSide())
            return;
        if (blockEntity.iceUseTicks > 0)
            blockEntity.iceUseTicks--;
        if (blockEntity.energyStorage.getEnergyStored() < blockEntity.energyStorage.getMaxEnergyStored()) {
            blockEntity.generationRate = blockEntity.constraints.usageOrGeneration();

            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            FluidState fluidState = belowState.getFluidState();
            if (!fluidState.isEmpty()) {
                // Water temp = 300. Consider 300 to be 100% generation rate
                int baseTemp = Fluids.WATER.getFluidType().getTemperature();
                int temp = fluidState.getFluidType().getTemperature();
                float rate = (float) temp / baseTemp;
                blockEntity.generationRate *= rate;
            } else {
                blockEntity.generationRate = 0;
            }

            if (blockEntity.generationRate > 0) {
                if (blockEntity.iceUseTicks <= 0) {
                    ItemStack iceStack = blockEntity.getItem(1);
                    if (iceStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().is(BlockTags.ICE)) {
                        iceStack.setCount(iceStack.getCount() - 1);
                        blockEntity.iceUseTicks = Config.HEAT_GENERATOR_ICE_TICKS.get();
                    }
                }
                if (level.dimensionType().ultraWarm())
                    blockEntity.generationRate *= Config.HEAT_GENERATOR_WARM_DIM_MULTIPLIER.get();
                if (blockEntity.iceUseTicks > 0)
                    blockEntity.generationRate *= Config.HEAT_GENERATOR_ICE_MULTIPLIER.get();

                int amountLeft = blockEntity.energyStorage.getMaxEnergyStored() - blockEntity.energyStorage.getEnergyStored();
                int amount = Math.min(blockEntity.generationRate, amountLeft);
                blockEntity.energyStorage.insertEnergy(amount, false);
            }
        }

        ItemStack chargeStack = blockEntity.getItem(0);
        IEnergyStorage chargeStackEnergyStorage = chargeStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (chargeStackEnergyStorage != null) {
            if (chargeStackEnergyStorage.canReceive())
                EnergyUtil.moveEnergy(blockEntity.constraints.maxOutput(), blockEntity.energyStorage, chargeStackEnergyStorage);
        }

        int maxOutput = blockEntity.constraints.maxOutput();
        for (Direction direction : Direction.values()) {
            if (maxOutput <= 0)
                break;
            BlockPos neighborPos = blockEntity.getBlockPos().relative(direction);
            blockEntity.cacheMap.computeIfAbsent(direction, direction1 -> BlockCapabilityCache.create(
                    Capabilities.EnergyStorage.BLOCK,
                    (ServerLevel) level,
                    neighborPos,
                    direction.getOpposite()
            ));

            BlockCapabilityCache<IEnergyStorage, Direction> cache = blockEntity.cacheMap.get(direction);
            IEnergyStorage capability = cache.getCapability();
            if (capability != null) {
                maxOutput -= EnergyUtil.moveEnergy(maxOutput, blockEntity.getEnergyStorage(state, direction), capability);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        energyStorage.serialize(output);
        output.putInt("iceUseTicks", iceUseTicks);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        energyStorage.deserialize(input);
        this.iceUseTicks = input.getIntOr("iceUseTicks", 0);
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage;
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
        return new HeatGeneratorMenu(containerId, playerInventory, this, containerData);
    }
}
