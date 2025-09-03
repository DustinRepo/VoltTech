package me.bricked.volttech.blockentity;

import me.bricked.volttech.capability.fluid.PassthroughFluidHandler;
import me.bricked.volttech.util.Constraints;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FluidPipeBlockEntity extends BlockEntity {
    private final Constraints constraints;
    public FluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.passthroughFluidHandler = new PassthroughFluidHandler(constraints, this::forceRefreshTargets);
    }
    private final PassthroughFluidHandler passthroughFluidHandler;
    private final Set<BlockPos> visited = new HashSet<>();
    private final Set<BlockPos> pipesInChain = new HashSet<>();
    private boolean needsRediscovery = true;
    private final ArrayList<Direction> blockedDirections = new ArrayList<>();
    private final ArrayList<Direction> extractDirections = new ArrayList<>();
    private String coverBlockID = "minecraft:air";

    public static void tick(Level level, BlockPos pos, BlockState state, FluidPipeBlockEntity blockEntity) {
        if (level == null || level.isClientSide())
            return;
        if (blockEntity.needsRediscovery || blockEntity.passthroughFluidHandler.getTargets() == null) {
            blockEntity.discoverEndpoints(level);
        }

        for (Direction direction : Direction.values()) {
            if (blockEntity.isDirectionExtracting(direction)) {
                IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(direction), direction.getOpposite());
                if (fluidHandler != null) {
                    FluidUtil.tryFluidTransfer(blockEntity.passthroughFluidHandler, fluidHandler, blockEntity.constraints.maxInput(), true);
                }
            }
        }
    }

    private void discoverEndpoints(Level level) {
        passthroughFluidHandler.setTargets(new ArrayList<>());
        passthroughFluidHandler.maxThroughput = constraints.maxInput();
        visited.clear();
        pipesInChain.clear();

        walkAndFind(level, getBlockPos());

        // share with all cables in the chain
        for (BlockPos cablePos : pipesInChain) {
            BlockEntity be = level.getBlockEntity(cablePos);
            if (be instanceof FluidPipeBlockEntity pipeBlockEntity) {
                pipeBlockEntity.passthroughFluidHandler.setTargets(this.passthroughFluidHandler.getTargets());
                pipeBlockEntity.passthroughFluidHandler.maxThroughput = this.passthroughFluidHandler.maxThroughput;
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
        if (originBE instanceof FluidPipeBlockEntity originPipe) {
            // Set the max FE the cable can move to the lowest tier cable in the network
            passthroughFluidHandler.maxThroughput = Math.min(passthroughFluidHandler.maxThroughput, originPipe.passthroughFluidHandler.maxThroughput);
            pipesInChain.add(origin);
        }
        for (Direction dir : Direction.values()) {
            if (originBE instanceof FluidPipeBlockEntity originCable && (originCable.blockedDirections.contains(dir) || originCable.extractDirections.contains(dir)))
                continue;
            BlockPos neighborPos = origin.relative(dir);
            BlockEntity be = level.getBlockEntity(neighborPos);

            if (be instanceof FluidPipeBlockEntity neighborCable) {
                if (neighborCable.blockedDirections.contains(dir.getOpposite()))
                    continue;
                if (pipesInChain.add(neighborPos)) {
                    walkAndFind(level, neighborPos);
                }
            } else {
                IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, dir.getOpposite());
                if (storage != null) {
                    passthroughFluidHandler.addTarget(storage);
                }
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
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
    protected void saveAdditional(ValueOutput output) {
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
    protected void loadAdditional(ValueInput input) {
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
        this.coverBlockID = input.getStringOr("coverBlockID", "minecraft:air");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
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
        PassthroughFluidHandler passthroughFluidHandler = (PassthroughFluidHandler) level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos(), null);
        if (passthroughFluidHandler != null)
            passthroughFluidHandler.setTargets(null);
    }

    public PassthroughFluidHandler getPassthroughFluidHandler() {
        return passthroughFluidHandler;
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
