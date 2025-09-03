package me.bricked.volttech.blockentity;

import me.bricked.volttech.capability.forgeenergy.PassthroughDirectionalEnergyStorage;
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
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EnergyCableBlockEntity extends BlockEntity implements IEnergyBlockEntity {
    public EnergyCableBlockEntity(BlockEntityType<EnergyCableBlockEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.energyStorage = new PassthroughDirectionalEnergyStorage(Constraints.get(blockState.getBlock()), this::forceRefreshTargets) {
            @Override
            public int getMaxInsert(Direction direction) {
                int insert = super.getMaxInsert(direction);
                return isDirectionBlocked(direction) ? 0 : insert;
            }
        };
    }
    private final PassthroughDirectionalEnergyStorage energyStorage;
    private final Set<BlockPos> visited = new HashSet<>();
    private final Set<BlockPos> cablesInChain = new HashSet<>();
    private final ArrayList<Direction> blockedDirections = new ArrayList<>();
    private String coverBlockID = "minecraft:air";
    private boolean needsRediscovery = true;

    // loop each direction that's not blocked and check if it accepts energy
    // if it does and it's not a cable we add it to endpoints
    // if it is a cable that's not blocked toward us we move on to it and do the same
    private void discoverEndpoints(Level level) {
        energyStorage.setTargets(new ArrayList<>());
        energyStorage.cableMaxMove = energyStorage.getLogisticalConstraints().maxInput();
        visited.clear();
        cablesInChain.clear();

        walkAndFind(level, getBlockPos());

        // share with all cables in the chain
        for (BlockPos cablePos : cablesInChain) {
            BlockEntity be = level.getBlockEntity(cablePos);
            if (be instanceof EnergyCableBlockEntity cable) {
                cable.energyStorage.setTargets(this.energyStorage.getTargets());
                cable.energyStorage.cableMaxMove = this.energyStorage.cableMaxMove;
                cable.visited.clear();
                cable.visited.addAll(this.visited);
                cable.needsRediscovery = false;
            }
        }
    }

    private void walkAndFind(Level level, BlockPos origin) {
        if (!visited.add(origin))
            return;

        BlockEntity originBE = level.getBlockEntity(origin);
        if (originBE instanceof EnergyCableBlockEntity originCable) {
            // Set the max FE the cable can move to the lowest tier cable in the network
            energyStorage.cableMaxMove = Math.min(energyStorage.cableMaxMove, originCable.energyStorage.getMaxInsert(null));
            cablesInChain.add(origin);
        }
        for (Direction dir : Direction.values()) {
            if (originBE instanceof EnergyCableBlockEntity originCable && originCable.blockedDirections.contains(dir))
                continue;
            BlockPos neighborPos = origin.relative(dir);
            BlockEntity be = level.getBlockEntity(neighborPos);

            if (be instanceof EnergyCableBlockEntity neighborCable) {
                if (neighborCable.blockedDirections.contains(dir.getOpposite()))
                    continue;
                if (cablesInChain.add(neighborPos)) {
                    walkAndFind(level, neighborPos);
                }
            } else {
                IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, dir.getOpposite());
                if (storage != null && storage.canReceive()) {
                    energyStorage.addTarget(storage);
                }
            }
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyCableBlockEntity blockEntity) {
        if (level == null || level.isClientSide())
            return;
        if (blockEntity.needsRediscovery || blockEntity.energyStorage.getTargets() == null) {
            blockEntity.discoverEndpoints(level);
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
        int[] array = new int[blockedDirections.size()];
        for (int i = 0; i < blockedDirections.size(); i++) {
            array[i] = blockedDirections.get(i).ordinal();
        }
        output.putIntArray("blockedDirections", array);
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
        this.coverBlockID = input.getStringOr("coverBlockID", "minecraft:air");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag compoundTag = new CompoundTag();
        int[] array = new int[blockedDirections.size()];
        for (int i = 0; i < blockedDirections.size(); i++) {
            array[i] = blockedDirections.get(i).ordinal();
        }
        compoundTag.putIntArray("blockedDirections", array);
        compoundTag.putString("coverBlockID", this.coverBlockID);
        return compoundTag;
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

    public void forceRefreshTargets() {
        energyStorage.setTargets(null);
    }

    public void toggleBlockingDirection(Direction direction) {
        if (isDirectionBlocked(direction)) {
            blockedDirections.remove(direction);
        } else {
            blockedDirections.add(direction);
        }
        needsRediscovery = true;
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

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage.getEnergyStorage(direction);
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
