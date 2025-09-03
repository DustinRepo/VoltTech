package me.bricked.volttech.blockentity;

import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.menu.HarvesterMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HarvesterBlockEntity extends BlockEntity implements IEnergyBlockEntity, MenuProvider, ImplementedContainer {
    public HarvesterBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.HARVESTER_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleEnergyStorage(constraints);
    }
    private final Constraints constraints;
    private int xOffset, yOffset, zOffset;
    private int xSize = 1, ySize = 1, zSize = 1;
    private final ArrayList<BlockPos> blocksToCheck = new ArrayList<>();
    private boolean renderBox;
    private final SimpleEnergyStorage energyStorage;
    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    private int lastEnergyAmount;
    private int ticks;

    public static void tick(Level level, BlockPos pos, BlockState state, HarvesterBlockEntity blockEntity) {
        blockEntity.enforceLimits();
        if (level == null || level.isClientSide())
            return;
        if (blockEntity.energyStorage.getEnergyStored() < blockEntity.constraints.usageOrGeneration())
            return;
        if (blockEntity.ticks > 0) {
            blockEntity.ticks--;
            return;
        }
        AABB box = new AABB(0, 0, 0, blockEntity.getxSize(), blockEntity.getySize(), blockEntity.getzSize());
        box = box.move(pos.relative(state.getValue(HorizontalDirectionalBlock.FACING)));
        box = box.move(blockEntity.getxOffset(), blockEntity.getyOffset(), blockEntity.getzOffset());

        if (blockEntity.blocksToCheck.isEmpty()) {
            for (int x = (int) box.minX; x < box.maxX; x++) {
                for (int y = (int) box.minY; y < box.maxY; y++) {
                    for (int z = (int) box.minZ; z < box.maxZ; z++) {
                        blockEntity.blocksToCheck.add(new BlockPos(x, y, z));
                    }
                }
            }
            return;
        }

        BlockPos blockPos = blockEntity.blocksToCheck.getFirst();
        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof CropBlock cropBlock && cropBlock.isMaxAge(blockState)) {
            List<ItemStack> drops = blockState.getDrops(new LootParams.Builder((ServerLevel) level).
                    withParameter(LootContextParams.TOOL, ItemStack.EMPTY).
                    withParameter(LootContextParams.BLOCK_STATE, blockState).
                    withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos)));
            ItemStack seedStack = null;
            for (ItemStack drop : drops) {
                if (drop.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CropBlock) {
                    seedStack = drop;
                    break;
                }
            }
            if (seedStack != null) {
                Block newBlock = ((BlockItem)seedStack.getItem()).getBlock();
                BlockState newBlockState = newBlock.defaultBlockState();
                level.setBlockAndUpdate(blockPos, newBlockState);
                seedStack.setCount(seedStack.getCount() - 1);
                if (seedStack.isEmpty())
                    drops.remove(seedStack);
                blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration(), false);
                IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, blockEntity, null);
                drops.forEach(itemStack -> {
                    if (handler != null) {
                        ItemStack remaining = itemStack;
                        for (int slot = 0; slot < handler.getSlots(); slot++) {
                            remaining = handler.insertItem(slot, remaining, false);
                            if (remaining.isEmpty())
                                break;
                        }
                        if (!remaining.isEmpty())
                            Block.popResource(level, pos, remaining);
                    }
                });
            }
        }
        blockEntity.blocksToCheck.remove(blockPos);
        blockEntity.ticks = Config.HARVESTER_TICK_DELAY.get();

        if (blockEntity.lastEnergyAmount != blockEntity.energyStorage.getEnergyStored())
            blockEntity.markUpdated();
        blockEntity.lastEnergyAmount = blockEntity.energyStorage.getEnergyStored();
    }

    private void enforceLimits() {
        this.xSize = Math.clamp(this.xSize, 1, 9);
        this.ySize = Math.clamp(this.ySize, 1, 5);
        this.zSize = Math.clamp(this.zSize, 1, 9);
        this.xOffset = Math.clamp(this.xOffset, -9, 9);
        this.yOffset = Math.clamp(this.yOffset, -3, 3);
        this.zOffset = Math.clamp(this.zOffset, -9, 9);
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
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new HarvesterMenu(i, inventory, this, this.getBlockPos());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        this.energyStorage.serialize(output);
        output.putIntArray("offsets", new int[]{xOffset, yOffset, zOffset});
        output.putIntArray("size", new int[]{xSize, ySize, zSize});
        output.putBoolean("renderBox", renderBox);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        input.getIntArray("offsets").ifPresent(ints -> {
            this.xOffset = ints[0];
            this.yOffset = ints[1];
            this.zOffset = ints[2];
        });
        input.getIntArray("size").ifPresent(ints -> {
            this.xSize = ints[0];
            this.ySize = ints[1];
            this.zSize = ints[2];
        });
        this.renderBox = input.getBooleanOr("renderBox", false);
        this.energyStorage.deserialize(input);
    }

    private void markUpdated() {
        if (!getLevel().isClientSide())
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putIntArray("offsets", new int[]{xOffset, yOffset, zOffset});
        tag.putIntArray("size", new int[]{xSize, ySize, zSize});
        tag.putBoolean("renderBox", renderBox);
        return tag;
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        loadAdditional(input);
        super.handleUpdateTag(input);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        loadAdditional(valueInput);
        super.onDataPacket(net, valueInput);
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public int getzOffset() {
        return zOffset;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getzSize() {
        return zSize;
    }

    public boolean isRenderBox() {
        return renderBox;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setzOffset(int zOffset) {
        this.zOffset = zOffset;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public void setzSize(int zSize) {
        this.zSize = zSize;
    }

    public void setRenderBox(boolean renderBox) {
        this.renderBox = renderBox;
    }
}
