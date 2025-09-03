package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.FluidPipeBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidPipeBlock extends BaseEntityBlock {
    private final FluidPipeTier pipeTier;
    public FluidPipeBlock(Properties properties, FluidPipeTier tier) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(UP_CONNECTION, SideConnection.NONE)
                .setValue(DOWN_CONNECTION, SideConnection.NONE)
                .setValue(NORTH_CONNECTION, SideConnection.NONE)
                .setValue(SOUTH_CONNECTION, SideConnection.NONE)
                .setValue(EAST_CONNECTION, SideConnection.NONE)
                .setValue(WEST_CONNECTION, SideConnection.NONE)
        );
        this.pipeTier = tier;
    }
    public static final EnumProperty<SideConnection> UP_CONNECTION = EnumProperty.create("connection_up", SideConnection.class);
    public static final EnumProperty<SideConnection> DOWN_CONNECTION = EnumProperty.create("connection_down", SideConnection.class);
    public static final EnumProperty<SideConnection> NORTH_CONNECTION = EnumProperty.create("connection_north", SideConnection.class);
    public static final EnumProperty<SideConnection> SOUTH_CONNECTION = EnumProperty.create("connection_south", SideConnection.class);
    public static final EnumProperty<SideConnection> EAST_CONNECTION = EnumProperty.create("connection_east", SideConnection.class);
    public static final EnumProperty<SideConnection> WEST_CONNECTION = EnumProperty.create("connection_west", SideConnection.class);
    public static final EnumProperty<SideConnection>[] SIDES = new EnumProperty[]{DOWN_CONNECTION, UP_CONNECTION, NORTH_CONNECTION, SOUTH_CONNECTION, WEST_CONNECTION, EAST_CONNECTION};

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.
                add(UP_CONNECTION).
                add(DOWN_CONNECTION).
                add(NORTH_CONNECTION).
                add(SOUTH_CONNECTION).
                add(EAST_CONNECTION).
                add(WEST_CONNECTION));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity) {
            if (stack.is(Tags.Items.TOOLS_WRENCH)) {
                Vec3 offset = hitResult.getLocation().subtract(Vec3.atLowerCornerOf(pos));
                Direction clickedDirection = getClickedDirectionFromCenter(offset);
                if (!level.isClientSide()) {
                    EnumProperty<SideConnection> property = getConnectionProperty(clickedDirection);
                    SideConnection connection = state.getValue(property);
                    switch (connection) {
                        case NONE, EXTRACT -> {
                            fluidPipeBlockEntity.toggleExtractDirection(clickedDirection);
                            if (fluidPipeBlockEntity.isDirectionExtracting(clickedDirection))
                                player.displayClientMessage(StringifyUtil.translate("message.now_extracting"), true);
                            else
                                player.displayClientMessage(StringifyUtil.translate("message.now_normal"), true);
                        }
                        case CONNECTED -> {
                            fluidPipeBlockEntity.toggleBlockingDirection(clickedDirection);
                            if (fluidPipeBlockEntity.isDirectionBlocked(clickedDirection))
                                player.displayClientMessage(StringifyUtil.translate("message.now_blocked"), true);
                            else
                                player.displayClientMessage(StringifyUtil.translate("message.now_normal"), true);
                        }
                    }
                    forceUpdate(level, state, pos);
                }
                return InteractionResult.SUCCESS_SERVER;
            } else if (stack.getItem() instanceof BlockItem blockItem && !blockItem.getBlock().defaultBlockState().is(TagRegistry.Blocks.CABLE_COVERS_BANNED)) {
                if (!fluidPipeBlockEntity.hasCover()) {
                    stack.consume(1, player);
                    ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock());
                    fluidPipeBlockEntity.setCoverBlockID(blockLocation.toString());
                } else {
                    ResourceLocation blockLocation = ResourceLocation.parse(fluidPipeBlockEntity.getCoverBlockID());
                    BuiltInRegistries.BLOCK.get(blockLocation).ifPresent(blockReference -> {
                        Item item = blockReference.value().asItem();
                        player.getInventory().add(new ItemStack(item));
                    });
                    fluidPipeBlockEntity.setCoverBlockID("minecraft:air");
                }
                forceUpdate(level, state, pos);
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity) {
            if (!level.isClientSide()) {
                player.displayClientMessage(StringifyUtil.translate("message.pipe_endpoints", fluidPipeBlockEntity.getPassthroughFluidHandler().getTargets().size()).withColor(0xffff7000), true);
                if (player.isCrouching()) {
                    for (IFluidHandler target : fluidPipeBlockEntity.getPassthroughFluidHandler().getTargets()) {
                        player.displayClientMessage(Component.literal(target.getClass().getTypeName()), false);
                    }
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    private void forceUpdate(Level level, BlockState blockState, BlockPos blockPos) {
        level.setBlockAndUpdate(blockPos, updateSides(blockState, level, blockPos));
        level.updateNeighborsAt(blockPos, blockState.getBlock());
        level.neighborChanged(blockPos, blockState.getBlock(), null);
    }

    private Direction getClickedDirectionFromCenter(Vec3 offset) {
        // Get axis with number farthest from 0.5 as the clicked axis
        float absX = (float) Math.abs(0.5f - offset.x);
        float absY = (float) Math.abs(0.5f - offset.y);
        float absZ = (float) Math.abs(0.5f - offset.z);

        if (absX > absY && absX > absZ)
            return Direction.fromAxisAndDirection(Direction.Axis.X, offset.x > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
        else if (absY > absX && absY > absZ)
            return Direction.fromAxisAndDirection(Direction.Axis.Y, offset.y > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
        else
            return Direction.fromAxisAndDirection(Direction.Axis.Z, offset.z > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
    }

    public static EnumProperty<SideConnection> getConnectionProperty(Direction direction) {
        return switch (direction) {
            case UP -> UP_CONNECTION;
            case DOWN -> DOWN_CONNECTION;
            case NORTH -> NORTH_CONNECTION;
            case SOUTH -> SOUTH_CONNECTION;
            case EAST -> EAST_CONNECTION;
            case WEST -> WEST_CONNECTION;
            default -> UP_CONNECTION;
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = getShape(state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity) {
            if (fluidPipeBlockEntity.hasCover()) {
                Optional<Holder.Reference<Block>> optionalBlockReference = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(fluidPipeBlockEntity.getCoverBlockID()));
                if (optionalBlockReference.isPresent()) {
                    Block block = optionalBlockReference.get().value();
                    return Shapes.join(shape, block.defaultBlockState().getShape(level, pos, context), BooleanOp.OR);
                }
            }
        }
        return shape;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((properties) -> new FluidPipeBlock(properties, pipeTier));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPipeBlockEntity(getType(), blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, getType(), FluidPipeBlockEntity::tick);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateSides(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        level.setBlockAndUpdate(pos, updateSides(state, level, pos));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity)
            fluidPipeBlockEntity.forceRefreshTargets();
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.setBlockAndUpdate(pos, updateSides(state, level, pos));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity)
            fluidPipeBlockEntity.forceRefreshTargets();
    }

    public BlockState updateSides(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity))
            return state;
        for (int i = 0; i < Direction.values().length; i++) {
            EnumProperty<SideConnection> side = SIDES[i];
            Direction direction = Direction.values()[i];
            if (fluidPipeBlockEntity.isDirectionBlocked(direction)) {
                state = state.setValue(side, SideConnection.NONE);
                continue;
            }
            BlockPos connectedPos = pos.relative(direction);
            BlockEntity connectedBlockEntity = level.getBlockEntity(connectedPos);
            if (connectedBlockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity1) {
                if (fluidPipeBlockEntity1.isDirectionBlocked(direction.getOpposite())) {
                    state = state.setValue(side, SideConnection.NONE);
                    continue;
                }
            }
            if (level.getCapability(Capabilities.FluidHandler.BLOCK, connectedPos, direction.getOpposite()) != null) {
                state = state.setValue(side, fluidPipeBlockEntity.isDirectionExtracting(direction) ? SideConnection.EXTRACT : SideConnection.CONNECTED);
            } else
                state = state.setValue(side, SideConnection.NONE);
        }
        return state;
    }

    public VoxelShape getShape(BlockState state) {
        VoxelShape shape = cube(6, 6, 6, 10, 10, 10);
        if (state.getValue(UP_CONNECTION) != SideConnection.NONE)
            shape = Shapes.join(shape, cube(6, 10, 6, 10, 16, 10), BooleanOp.OR);
        if (state.getValue(DOWN_CONNECTION) != SideConnection.NONE)
            shape = Shapes.join(shape, cube(6, 0, 6, 10, 6, 10), BooleanOp.OR);
        if (state.getValue(NORTH_CONNECTION) != SideConnection.NONE)
            shape = Shapes.join(shape, cube(6, 6, 0, 10, 10, 6), BooleanOp.OR);
        if (state.getValue(SOUTH_CONNECTION) != SideConnection.NONE)
            shape = Shapes.join(shape, cube(6, 6, 10, 10, 10, 16), BooleanOp.OR);
        if (state.getValue(EAST_CONNECTION) != SideConnection.NONE)
            shape = Shapes.join(shape, cube(10, 6, 6, 16, 10, 10), BooleanOp.OR);
        if (state.getValue(WEST_CONNECTION) != SideConnection.NONE)
            shape = Shapes.join(shape, cube(0, 6, 6, 6, 10, 10), BooleanOp.OR);
        return shape;
    }

    private VoxelShape cube(int x, int y, int z, int xx, int yy, int zz) {
        return Shapes.box(x / 16.0, y / 16.0, z / 16.0, xx / 16.0, yy / 16.0, zz / 16.0);
    }

    public BlockEntityType<FluidPipeBlockEntity> getType() {
        BlockEntityType<FluidPipeBlockEntity> type = BlockEntityRegistry.COPPER_FLUID_PIPE_BLOCK_ENTITY.get();
        switch (pipeTier) {
            case COPPER -> type = BlockEntityRegistry.COPPER_FLUID_PIPE_BLOCK_ENTITY.get();
            case IRON -> type = BlockEntityRegistry.IRON_FLUID_PIPE_BLOCK_ENTITY.get();
            case GOLD -> type = BlockEntityRegistry.GOLD_FLUID_PIPE_BLOCK_ENTITY.get();
            case DIAMOND -> type = BlockEntityRegistry.DIAMOND_FLUID_PIPE_BLOCK_ENTITY.get();
            case EMERALD -> type = BlockEntityRegistry.EMERALD_FLUID_PIPE_BLOCK_ENTITY.get();
            case NETHERITE -> type = BlockEntityRegistry.NETHERITE_FLUID_PIPE_BLOCK_ENTITY.get();
        }
        return type;
    }

    public enum SideConnection implements StringRepresentable {
        NONE("none"),
        CONNECTED("connected"),
        EXTRACT("extract");

        private final String name;

        private SideConnection(String name) {
            this.name = name;
        }

        public String toString() {
            return this.getSerializedName();
        }

        public String getSerializedName() {
            return this.name;
        }

        public boolean isConnected() {
            return this != NONE;
        }
    }

    public enum FluidPipeTier {
        COPPER, IRON, GOLD, DIAMOND, EMERALD, NETHERITE
    }
}
