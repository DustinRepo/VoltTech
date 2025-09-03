package me.bricked.volttech.blockentity;

import me.bricked.volttech.block.WirelessEnergyTransmitterBlock;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.PassthroughEnergyStorage;
import me.bricked.volttech.item.LocationCardItem;
import me.bricked.volttech.menu.WirelessEnergyTransmitterMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;

public class WirelessTransmitterBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer, IEnergyBlockEntity {
    public WirelessTransmitterBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.WIRELESS_ENERGY_TRANSMITTER_BLOCK_ENTITY.get(), pos, blockState);
        energyStorage = new PassthroughEnergyStorage(Constraints.get(blockState.getBlock()), () -> {});
    }

    private final PassthroughEnergyStorage energyStorage;
    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    public static void tick(Level level, BlockPos pos, BlockState state, WirelessTransmitterBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        if (blockEntity.energyStorage.getTargets() == null)
            blockEntity.energyStorage.setTargets(new ArrayList<>());
        boolean active = state.getValue(WirelessEnergyTransmitterBlock.IS_ACTIVE);
        if (blockEntity.getItems().isEmpty()) {
            if (active)
                level.setBlockAndUpdate(pos, state.setValue(WirelessEnergyTransmitterBlock.IS_ACTIVE, false));
            blockEntity.energyStorage.clearTargets();
            return;
        }

        boolean setActive = false;
        ArrayList<IEnergyStorage> targets = new ArrayList<>();
        for (ItemStack itemStack : blockEntity.getItems()) {
            if (itemStack.isEmpty() || itemStack.getItem() != ItemRegistry.LOCATION_CARD_ITEM.get())
                continue;
            ResourceLocation transmitDimension = LocationCardItem.getStoredDimension(itemStack);
            Direction transmitDirection = LocationCardItem.getStoredDirection(itemStack);
            Vector3f vec = LocationCardItem.getStoredPos(itemStack);
            BlockPos transmitPos = new BlockPos((int) vec.x, (int) vec.y, (int) vec.z);
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, transmitDimension);
            Level transmitLevel = ((ServerLevel) level).getServer().getLevel(dim);
            if (transmitLevel == null)
                continue;
            IEnergyStorage targetStorage = transmitLevel.getCapability(Capabilities.EnergyStorage.BLOCK, transmitPos, transmitDirection);
            if (targetStorage != null && targetStorage.canReceive()) {
                targets.add(targetStorage);
                setActive = true;
            }
        }
        blockEntity.energyStorage.setTargets(targets);
        if (setActive != active)
            level.setBlockAndUpdate(pos, state.setValue(WirelessEnergyTransmitterBlock.IS_ACTIVE, setActive));
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

    public PassthroughEnergyStorage getEnergyStorage() {
        return energyStorage;
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
        return new WirelessEnergyTransmitterMenu(i, inventory, this);
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage;
    }
}
