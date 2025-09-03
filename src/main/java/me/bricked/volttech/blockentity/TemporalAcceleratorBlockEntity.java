package me.bricked.volttech.blockentity;

import me.bricked.volttech.Config;
import me.bricked.volttech.event.ServerWorldEvents;
import me.bricked.volttech.capability.forgeenergy.SimpleDirectionalEnergyStorage;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class TemporalAcceleratorBlockEntity extends BlockEntity implements IEnergyBlockEntity {
    public TemporalAcceleratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.TEMPORAL_ACCELERATOR_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        energyStorage = new SimpleDirectionalEnergyStorage(constraints) {
            @Override
            public int getMaxInsert(Direction direction) {
                if (direction == TemporalAcceleratorBlockEntity.this.getBlockState().getValue(DirectionalBlock.FACING))
                    return 0;
                return this.getLogisticalConstraints().maxInput();
            }

            @Override
            public int getMaxOutput(Direction direction) {
                return 0;
            }
        };
    }
    private final Constraints constraints;
    private final SimpleDirectionalEnergyStorage energyStorage;

    public static void tick(Level level, BlockPos pos, BlockState state, TemporalAcceleratorBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        if (blockEntity.energyStorage.getEnergyStored() < blockEntity.constraints.usageOrGeneration())
            return;
        Direction facing = state.getValue(DirectionalBlock.FACING);
        BlockPos targetPos = pos.relative(facing);
        BlockState targetState = level.getBlockState(targetPos);
        BlockEntity targetBlockEntity = level.getBlockEntity(targetPos);
        if (targetBlockEntity == null || targetState.is(TagRegistry.Blocks.TEMPORAL_ACCELERATOR_BANNED))
            return;

        if (targetState.getBlock() instanceof BaseEntityBlock baseEntityBlock && baseEntityBlock.getTicker(level, targetState, targetBlockEntity.getType()) != null) {
            ServerWorldEvents.addTickAccelerations(targetPos, Config.TEMPORAL_ACCELERATOR_ACCELERATE_MULT.get());
        }

        blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.energyStorage.serialize(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.energyStorage.deserialize(input);
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage.getEnergyStorage(direction);
    }
}
