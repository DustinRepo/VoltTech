package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.BlockBreakerBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockBreakerBlock extends BaseEntityBlock {
    public BlockBreakerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().
                setValue(DirectionalBlock.FACING, Direction.NORTH));
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
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockBreakerBlockEntity blockBreakerBlockEntity) {
            if (neighbor.equals(pos.relative(state.getValue(DirectionalBlock.FACING).getOpposite())))
                blockBreakerBlockEntity.setBreakProgress(0.f);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(DirectionalBlock.FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BlockBreakerBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockBreakerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.BLOCK_BREAKER_BLOCK_ENTITY.get(), BlockBreakerBlockEntity::tick);
    }
}
