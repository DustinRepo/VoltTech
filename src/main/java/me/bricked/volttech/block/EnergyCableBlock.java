package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.EnergyCableBlockEntity;
import me.bricked.volttech.capability.forgeenergy.PassthroughDirectionalEnergyStorage;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnergyCableBlock extends BaseEntityBlock {
    public EnergyCableBlock(Properties properties, CableTier cableTier) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BlockStateProperties.UP, false)
                .setValue(BlockStateProperties.DOWN, false)
                .setValue(BlockStateProperties.NORTH, false)
                .setValue(BlockStateProperties.SOUTH, false)
                .setValue(BlockStateProperties.EAST, false)
                .setValue(BlockStateProperties.WEST, false)
        );
        this.cableTier = cableTier;
    }

    private final CableTier cableTier;

    public static final BooleanProperty[] SIDES = {BlockStateProperties.DOWN, BlockStateProperties.UP, BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.WEST, BlockStateProperties.EAST};

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity) {
            if (stack.is(Tags.Items.TOOLS_WRENCH)) {
                Vec3 offset = hitResult.getLocation().subtract(Vec3.atLowerCornerOf(pos));
                Direction clickedDirection = getClickedDirectionFromCenter(offset);
                if (!level.isClientSide()) {
                    energyCableBlockEntity.toggleBlockingDirection(clickedDirection);
                    forceUpdate(level, state, pos);
                }
            } else if (stack.getItem() instanceof BlockItem blockItem && !blockItem.getBlock().defaultBlockState().is(TagRegistry.Blocks.CABLE_COVERS_BANNED)) {
                if (!energyCableBlockEntity.hasCover()) {
                    stack.consume(1, player);
                    ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock());
                    energyCableBlockEntity.setCoverBlockID(blockLocation.toString());
                } else {
                    ResourceLocation blockLocation = ResourceLocation.parse(energyCableBlockEntity.getCoverBlockID());
                    BuiltInRegistries.BLOCK.get(blockLocation).ifPresent(blockReference -> {
                        Item item = blockReference.value().asItem();
                        player.getInventory().add(new ItemStack(item));
                    });
                    energyCableBlockEntity.setCoverBlockID("minecraft:air");
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
        if (blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity) {
            if (!level.isClientSide()) {
                PassthroughDirectionalEnergyStorage passthrough = ((PassthroughDirectionalEnergyStorage.DirectionalStorage) energyCableBlockEntity.getEnergyStorage(state, null)).getPassthrough();
                if (passthrough.getTargets() != null) {
                    player.displayClientMessage(StringifyUtil.translate("message.cable_endpoints", passthrough.getTargets().size()).withColor(0xffff7000), true);
                    if (player.isCrouching()) {
                        for (IEnergyStorage target : passthrough.getTargets()) {
                            player.displayClientMessage(Component.literal(target.getClass().getTypeName()), false);
                        }
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.
                add(BlockStateProperties.UP).
                add(BlockStateProperties.DOWN).
                add(BlockStateProperties.NORTH).
                add(BlockStateProperties.SOUTH).
                add(BlockStateProperties.EAST).
                add(BlockStateProperties.WEST));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = getShape(state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity) {
            if (energyCableBlockEntity.hasCover()) {
                Optional<Holder.Reference<Block>> optionalBlockReference = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(energyCableBlockEntity.getCoverBlockID()));
                if (optionalBlockReference.isPresent()) {
                    Block block = optionalBlockReference.get().value();
                    return Shapes.join(shape, block.defaultBlockState().getShape(level, pos, context), BooleanOp.OR);
                }
            }
        }
        return shape;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((properties) -> new EnergyCableBlock(properties, cableTier));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnergyCableBlockEntity(getType(), blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, getType(), EnergyCableBlockEntity::tick);
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
        if (blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity)
            energyCableBlockEntity.forceRefreshTargets();
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.setBlockAndUpdate(pos, updateSides(state, level, pos));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity)
            energyCableBlockEntity.forceRefreshTargets();
    }

    public BlockState updateSides(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity))
            return state;
        for (int i = 0; i < Direction.values().length; i++) {
            BooleanProperty side = SIDES[i];
            Direction direction = Direction.values()[i];
            if (energyCableBlockEntity.isDirectionBlocked(direction)) {
                state = state.setValue(side, false);
                continue;
            }
            BlockPos connectedPos = pos.relative(direction);
            BlockEntity connectedBlockEntity = level.getBlockEntity(connectedPos);
            if (connectedBlockEntity instanceof EnergyCableBlockEntity connectedEnergyCableBlockEntity) {
                if (connectedEnergyCableBlockEntity.isDirectionBlocked(direction.getOpposite())) {
                    state = state.setValue(side, false);
                    continue;
                }
            }
            IEnergyStorage energyStorageCapability = level.getCapability(Capabilities.EnergyStorage.BLOCK, connectedPos, direction.getOpposite());
            if (energyStorageCapability != null && (energyStorageCapability.canReceive() || energyStorageCapability.canExtract()))
                state = state.setValue(side, true);
            else
                state = state.setValue(side, false);
        }
        return state;
    }

    public VoxelShape getShape(BlockState state) {
        VoxelShape shape = cube(6, 6, 6, 10, 10, 10);
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

    private VoxelShape cube(int x, int y, int z, int xx, int yy, int zz) {
        return Shapes.box(x / 16.0, y / 16.0, z / 16.0, xx / 16.0, yy / 16.0, zz / 16.0);
    }

    public BlockEntityType<EnergyCableBlockEntity> getType() {
        BlockEntityType<EnergyCableBlockEntity> type = BlockEntityRegistry.COPPER_ENERGY_CABLE_BLOCK_ENTITY.get();
        switch (cableTier) {
            case COPPER -> type = BlockEntityRegistry.COPPER_ENERGY_CABLE_BLOCK_ENTITY.get();
            case IRON -> type = BlockEntityRegistry.IRON_ENERGY_CABLE_BLOCK_ENTITY.get();
            case GOLD -> type = BlockEntityRegistry.GOLD_ENERGY_CABLE_BLOCK_ENTITY.get();
            case DIAMOND -> type = BlockEntityRegistry.DIAMOND_ENERGY_CABLE_BLOCK_ENTITY.get();
            case EMERALD -> type = BlockEntityRegistry.EMERALD_ENERGY_CABLE_BLOCK_ENTITY.get();
            case NETHERITE -> type = BlockEntityRegistry.NETHERITE_ENERGY_CABLE_BLOCK_ENTITY.get();
        }
        return type;
    }

    public enum CableTier {
        COPPER, IRON, GOLD, DIAMOND, EMERALD, NETHERITE
    }
}
