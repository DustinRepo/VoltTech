package me.bricked.volttech.blockentity;

import com.google.common.collect.Maps;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.menu.CombustionGeneratorMenu;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CombustionGeneratorBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer, IEnergyBlockEntity{
    private final Constraints constraints;
    public CombustionGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.COMBUSTION_GENERATOR_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.maxBurnTicks = (int) (4000 / constraints.usageOrGeneration());
        this.energyStorage = new SimpleEnergyStorage(constraints);
        this.containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> burnTicksLeft;
                    case 2 -> maxBurnTicks;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }
    public int maxBurnTicks; // 4000 FE = 1 coal
    private int burnTicksLeft = 0;
    private final Map<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> cacheMap = Maps.newHashMap();
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final SimpleEnergyStorage energyStorage;
    private final ContainerData containerData;

    public static void tick(Level level, BlockPos pos, BlockState state, CombustionGeneratorBlockEntity blockEntity) {
        if (level == null || level.isClientSide())
            return;
        if (blockEntity.burnTicksLeft > 0) {
            // Generate energy
            if (blockEntity.energyStorage.getEnergyStored() < blockEntity.energyStorage.getMaxEnergyStored()) {
                int amountLeft = blockEntity.energyStorage.getMaxEnergyStored() - blockEntity.energyStorage.getEnergyStored();
                int amount = Math.min(blockEntity.constraints.usageOrGeneration(), amountLeft);
                blockEntity.energyStorage.insertEnergy(amount, false);
            }
            blockEntity.burnTicksLeft--;
        } else {
            ItemStack fuelStack = blockEntity.getItem(0);
            boolean hasFuel = false;
            int fuelTicks = fuelStack.getBurnTime(RecipeType.SMELTING, level.fuelValues());

            if (level.fuelValues().isFuel(fuelStack) && fuelTicks > 0) {
                int totalEnergy = (int)(fuelTicks * 2.5f);
                blockEntity.maxBurnTicks = totalEnergy / blockEntity.constraints.usageOrGeneration();
                hasFuel = true;
            }

            if (hasFuel && blockEntity.energyStorage.getEnergyStored() < blockEntity.energyStorage.getMaxEnergyStored()) {
                blockEntity.burnTicksLeft = blockEntity.maxBurnTicks;
                Item fuelItem = fuelStack.getItem();
                ItemStack copy = fuelStack.copy();
                fuelStack.shrink(1);
                if (fuelStack.isEmpty()) {
                    ItemStack replaceStack = fuelItem.getCraftingRemainder(copy);
                    blockEntity.setItem(0, replaceStack);
                }
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), Block.UPDATE_ALL);
            } else if (state.getValue(BlockStateProperties.LIT)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), Block.UPDATE_ALL);
            }
        }

        ItemStack chargeStack = blockEntity.getItem(1);
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
        energyStorage.serialize(output);
        ContainerHelper.saveAllItems(output, items);
        output.putInt("burnTicksLeft", burnTicksLeft);
        output.putInt("maxBurnTicks", maxBurnTicks);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.deserialize(input);
        ContainerHelper.loadAllItems(input, items);
        this.burnTicksLeft = input.getIntOr("burnTicksLeft", 0);
        this.maxBurnTicks = input.getIntOr("maxBurnTicks", 0);
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
        return new CombustionGeneratorMenu(containerId, playerInventory, this, containerData);
    }
}
