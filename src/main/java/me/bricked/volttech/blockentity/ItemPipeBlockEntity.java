package me.bricked.volttech.blockentity;

import me.bricked.volttech.capability.item.PassthroughItemHandler;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ItemPipeBlockEntity extends BlockEntity {
    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.ITEM_PIPE_BLOCK_ENTITY.get(), pos, blockState);
        this.passthroughItemHandler = new PassthroughItemHandler(64, this::forceRefreshTargets);
    }
    private final PassthroughItemHandler passthroughItemHandler;
    private final Set<BlockPos> visited = new HashSet<>();
    private final Set<BlockPos> pipesInChain = new HashSet<>();
    private boolean needsRediscovery = true;
    private final ArrayList<Direction> blockedDirections = new ArrayList<>();
    private final ArrayList<Direction> extractDirections = new ArrayList<>();
    private String coverBlockID = "minecraft:air";

    public static void tick(Level level, BlockPos pos, BlockState state, ItemPipeBlockEntity blockEntity) {
        if (level == null || level.isClientSide())
            return;
        if (blockEntity.needsRediscovery || blockEntity.passthroughItemHandler.getTargets() == null) {
            blockEntity.discoverEndpoints(level);
        }

        for (Direction direction : Direction.values()) {
            if (blockEntity.isDirectionExtracting(direction)) {
                IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction), direction.getOpposite());
                if (itemHandler != null) {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack itemStack = itemHandler.getStackInSlot(i);
                        if (!itemStack.isEmpty()) {
                            if (ItemUtil.transferItemStack(itemHandler, blockEntity.passthroughItemHandler, i))
                                return;
                        }
                    }
                }
            }
        }
    }

    private void discoverEndpoints(Level level) {
        passthroughItemHandler.setTargets(new ArrayList<>());
        visited.clear();
        pipesInChain.clear();

        walkAndFind(level, getBlockPos());

        // share with all cables in the chain
        for (BlockPos cablePos : pipesInChain) {
            BlockEntity be = level.getBlockEntity(cablePos);
            if (be instanceof ItemPipeBlockEntity pipeBlockEntity) {
                pipeBlockEntity.passthroughItemHandler.setTargets(this.passthroughItemHandler.getTargets());
                pipeBlockEntity.passthroughItemHandler.maxThroughput = this.passthroughItemHandler.maxThroughput;
                pipeBlockEntity.visited.clear();
                pipeBlockEntity.visited.addAll(this.visited);
                pipeBlockEntity.needsRediscovery = false;
            }
        }
    }

    private void walkAndFind(Level level, BlockPos origin) {
        if (!visited.add(origin))
            return;

        BlockEntity originBE = level.getBlockEntity(origin);
        if (originBE instanceof ItemPipeBlockEntity originPipe) {
            passthroughItemHandler.maxThroughput = Math.min(passthroughItemHandler.maxThroughput, originPipe.passthroughItemHandler.maxThroughput);
            pipesInChain.add(origin);
        }
        for (Direction dir : Direction.values()) {
            if (originBE instanceof ItemPipeBlockEntity originCable && (originCable.blockedDirections.contains(dir) || originCable.extractDirections.contains(dir)))
                continue;
            BlockPos neighborPos = origin.relative(dir);
            BlockEntity be = level.getBlockEntity(neighborPos);

            if (be instanceof ItemPipeBlockEntity neighborCable) {
                if (neighborCable.blockedDirections.contains(dir.getOpposite()))
                    continue;
                if (pipesInChain.add(neighborPos)) {
                    walkAndFind(level, neighborPos);
                }
            } else {
                IItemHandler storage = level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, dir.getOpposite());
                if (storage != null) {
                    passthroughItemHandler.addTarget(storage);
                }
            }
        }
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (hasCover()) {
            ResourceLocation blockLocation = ResourceLocation.parse(getCoverBlockID());
            BuiltInRegistries.BLOCK.get(blockLocation).ifPresent(blockReference -> {
                Item item = blockReference.value().asItem();
                Containers.dropItemStack(getLevel(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(item));
            });
        }
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        int[] blockArray = new int[blockedDirections.size()];
        for (int i = 0; i < blockedDirections.size(); i++) {
            blockArray[i] = blockedDirections.get(i).ordinal();
        }
        int[] extractArray = new int[extractDirections.size()];
        for (int i = 0; i < extractDirections.size(); i++) {
            extractArray[i] = extractDirections.get(i).ordinal();
        }
        output.putIntArray("blockedDirections", blockArray);
        output.putIntArray("extractDirections", extractArray);
        output.putString("coverBlockID", coverBlockID);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        input.getIntArray("blockedDirections").ifPresent(ints -> {
            for (int directionOrdinal : ints) {
                Direction direction = Direction.values()[directionOrdinal];
                blockedDirections.add(direction);
            }
        });
        input.getIntArray("extractDirections").ifPresent(ints -> {
            for (int directionOrdinal : ints) {
                Direction direction = Direction.values()[directionOrdinal];
                extractDirections.add(direction);
            }
        });
        coverBlockID = input.getStringOr("coverBlockID", "minecraft:air");
    }

    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        int[] blockArray = new int[blockedDirections.size()];
        for (int i = 0; i < blockedDirections.size(); i++) {
            blockArray[i] = blockedDirections.get(i).ordinal();
        }
        int[] extractArray = new int[extractDirections.size()];
        for (int i = 0; i < extractDirections.size(); i++) {
            extractArray[i] = extractDirections.get(i).ordinal();
        }
        tag.putIntArray("blockedDirections", blockArray);
        tag.putIntArray("extractDirections", extractArray);
        tag.putString("coverBlockID", coverBlockID);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input) {
        loadAdditional(input);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ValueInput valueInput) {
        loadAdditional(valueInput);
    }

    public void toggleBlockingDirection(Direction direction) {
        if (isDirectionBlocked(direction)) {
            blockedDirections.remove(direction);
        } else {
            extractDirections.remove(direction);
            blockedDirections.add(direction);
        }
        needsRediscovery = true;
        markUpdated();
    }

    public void toggleExtractDirection(Direction direction) {
        if (isDirectionExtracting(direction)) {
            extractDirections.remove(direction);
        } else {
            blockedDirections.remove(direction);
            extractDirections.add(direction);
        }
        markUpdated();
    }

    private void markUpdated() {
        if (!getLevel().isClientSide())
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    public boolean isDirectionBlocked(Direction direction) {
        return blockedDirections.contains(direction);
    }

    public boolean isDirectionExtracting(Direction direction) {
        return extractDirections.contains(direction);
    }

    public void forceRefreshTargets() {
        PassthroughItemHandler handler = (PassthroughItemHandler) level.getCapability(Capabilities.ItemHandler.BLOCK, getBlockPos(), null);
        if (handler != null)
            handler.setTargets(null);
    }

    public PassthroughItemHandler getPassthroughItemHandler() {
        return passthroughItemHandler;
    }

    public boolean hasCover() {
        return !coverBlockID.equalsIgnoreCase("minecraft:air");
    }

    public String getCoverBlockID() {
        return coverBlockID;
    }

    public void setCoverBlockID(String coverBlockID) {
        this.coverBlockID = coverBlockID;
    }
}
