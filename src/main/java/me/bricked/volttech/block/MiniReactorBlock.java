package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.MiniReactorBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MiniReactorBlock extends BaseEntityBlock {
    public MiniReactorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof MiniReactorBlockEntity miniReactorBlockEntity))
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(stack);
        if (capabilityOptional.isPresent()) {
            IFluidHandlerItem capability = capabilityOptional.get();
            FluidStack fluidStack = capability.getFluidInTank(0);
            if (!fluidStack.isEmpty() && miniReactorBlockEntity.getWaterTank().isFluidValid(fluidStack)) {
                if (!level.isClientSide())
                    FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
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
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.box(1f / 16.f, 1f / 16.f, 1f / 16.f, 15f / 16.f, 15f / 16.f, 15f / 16.f);
        // Bottom ring of frame
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1.f / 16.f, 1.f / 16.f, 1.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1.f, 1.f / 16.f, 1.f / 16.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(15.f / 16.f, 0, 0, 1.f, 1.f / 16.f, 1.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 15.f / 16.f, 1.f, 1.f / 16.f, 1.f), BooleanOp.OR);
        // Top ring of frame
        shape = Shapes.join(shape, Shapes.box(0, 15.f / 16.f, 0, 1.f / 16.f, 1.f, 1.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 15.f / 16.f, 0, 1.f, 1.f, 1.f / 16.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(15.f / 16.f, 15.f / 16.f, 0, 1.f, 1.f, 1.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 15.f / 16.f, 15.f / 16.f, 1.f, 1.f, 1.f), BooleanOp.OR);
        // Connecting rings
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1.f / 16.f, 1.f, 1.f / 16.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(15.f / 16.f, 0, 15.f / 16.f, 1.f, 1.f, 1.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 15.f / 16.f, 1.f / 16.f, 1.f, 1.f), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(15.f / 16.f, 0, 0, 1.f, 1.f, 1.f / 16.f), BooleanOp.OR);
        return shape;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(MiniReactorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MiniReactorBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.MINI_REACTOR_BLOCK_ENTITY.get(), MiniReactorBlockEntity::tick);
    }
}
