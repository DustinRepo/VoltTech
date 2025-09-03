package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.WirelessTransmitterBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

public class WirelessEnergyTransmitterBlock extends BaseEntityBlock {
    public WirelessEnergyTransmitterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(IS_ACTIVE, false)
                .setValue(BlockStateProperties.UP, false)
                .setValue(BlockStateProperties.DOWN, false)
                .setValue(BlockStateProperties.NORTH, false)
                .setValue(BlockStateProperties.SOUTH, false)
                .setValue(BlockStateProperties.EAST, false)
                .setValue(BlockStateProperties.WEST, false)
        );
    }
    public static final BooleanProperty IS_ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty[] SIDES = {BlockStateProperties.DOWN, BlockStateProperties.UP, BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.WEST, BlockStateProperties.EAST};

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.
                add(IS_ACTIVE).
                add(BlockStateProperties.UP).
                add(BlockStateProperties.DOWN).
                add(BlockStateProperties.NORTH).
                add(BlockStateProperties.SOUTH).
                add(BlockStateProperties.EAST).
                add(BlockStateProperties.WEST));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MenuProvider provider) {
            player.openMenu(provider, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(WirelessEnergyTransmitterBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WirelessTransmitterBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.WIRELESS_ENERGY_TRANSMITTER_BLOCK_ENTITY.get(), WirelessTransmitterBlockEntity::tick);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateSides(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        level.setBlockAndUpdate(pos, updateSides(state, level, pos));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.setBlockAndUpdate(pos, updateSides(state, level, pos));
    }

    public BlockState updateSides(BlockState state, Level level, BlockPos pos) {
        for (int i = 0; i < Direction.values().length; i++) {
            Direction direction = Direction.values()[i];
            BooleanProperty side = SIDES[i];
            BlockPos connectedPos = pos.relative(direction);
            if (level.getCapability(Capabilities.EnergyStorage.BLOCK, connectedPos, direction.getOpposite()) != null)
                state = state.setValue(side, true);
            else
                state = state.setValue(side, false);
        }
        return state;
    }

    public VoxelShape getShape(BlockState state) {
        VoxelShape shape = cube(4.5f, 4.5f, 4.5f, 11.5f, 11.5f, 11.5f);
        if (state.getValue(BlockStateProperties.UP))
            shape = Shapes.join(shape, cube(6, 10, 6, 10, 16, 10), BooleanOp.OR);
        if (state.getValue(BlockStateProperties.DOWN))
            shape = Shapes.join(shape, cube(6, 0, 6, 10, 6, 10), BooleanOp.OR);
        if (state.getValue(BlockStateProperties.NORTH))
            shape = Shapes.join(shape, cube(6, 6, 0, 10, 10, 6), BooleanOp.OR);
        if (state.getValue(BlockStateProperties.SOUTH))
            shape = Shapes.join(shape, cube(6, 6, 10, 10, 10, 16), BooleanOp.OR);
        if (state.getValue(BlockStateProperties.EAST))
            shape = Shapes.join(shape, cube(10, 6, 6, 16, 10, 10), BooleanOp.OR);
        if (state.getValue(BlockStateProperties.WEST))
            shape = Shapes.join(shape, cube(0, 6, 6, 6, 10, 10), BooleanOp.OR);
        return shape;
    }

    private VoxelShape cube(float x, float y, float z, float xx, float yy, float zz) {
        return Shapes.box(x / 16.0, y / 16.0, z / 16.0, xx / 16.0, yy / 16.0, zz / 16.0);
    }
}
