package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.Config;
import me.bricked.volttech.blockentity.FluidTankBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidTankBlock extends BaseEntityBlock {
    private final TankTier tankTier;
    public FluidTankBlock(TankTier tankTier, Properties properties) {
        super(properties);
        this.tankTier = tankTier;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof FluidTankBlockEntity fluidTankBlockEntity))
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        Optional<IFluidHandlerItem> capabilityOptional = FluidUtil.getFluidHandler(stack);
        if (capabilityOptional.isPresent()) {
            IFluidHandlerItem fluidHandlerItem = capabilityOptional.get();
            // loop tanks of item stack to fill tank block
            for (int i = 0; i < fluidHandlerItem.getTanks(); i++) {
                FluidStack fluidStack = fluidHandlerItem.getFluidInTank(i);
                if (!fluidStack.isEmpty() && fluidTankBlockEntity.getTank().isFluidValid(fluidStack)) {
                    if (!level.isClientSide())
                        FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
            // loop tanks of item stack to fill from tank
            for (int i = 0; i < fluidHandlerItem.getTanks(); i++) {
                FluidStack fluidStack = fluidHandlerItem.getFluidInTank(i);
                FluidStack blockStack = fluidTankBlockEntity.getTank().getFluid();
                if (fluidStack.isEmpty() && fluidHandlerItem.isFluidValid(i, blockStack)) {
                    if (!level.isClientSide())
                        FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
                    return InteractionResult.SUCCESS_SERVER;
                }
            }

        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        int light = super.getLightEmission(state, level, pos);
        if (light == 15)
            return light;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidTankBlockEntity fluidTankBlockEntity) {
            FluidStack fluidStack = fluidTankBlockEntity.getTank().getFluid();
            if (!fluidStack.isEmpty())
                light = Math.max(light, fluidStack.getFluid().getFluidType().getLightLevel());
        }
        return light;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide())
            return;
        if (level.getBlockEntity(pos) instanceof FluidTankBlockEntity) {
            FluidUtil.getFluidHandler(level, pos, null).ifPresent(iFluidHandler -> {
                FluidUtil.getFluidHandler(stack).ifPresent(iFluidHandlerItem -> {
                    iFluidHandler.fill(iFluidHandlerItem.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
                });
            });
        }
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        // This gets overridden by onRemove above so that it can drop with the fluid
        super.spawnAfterBreak(state, level, pos, ItemStack.EMPTY, dropExperience);
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, getType(), FluidTankBlockEntity::tick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties1 -> new FluidTankBlock(tankTier, properties1));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidTankBlockEntity(getType(), blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(3.f / 16.f, 0.f, 3.f / 16.f, 13.f / 16.f, 1.f, 13.f / 16.f);
    }

    private BlockEntityType<FluidTankBlockEntity> getType() {
        return switch (this.tankTier) {
            case SMALL -> BlockEntityRegistry.SMALL_FLUID_TANK_BLOCK_ENTITY.get();
            case MEDIUM -> BlockEntityRegistry.MEDIUM_FLUID_TANK_BLOCK_ENTITY.get();
            case LARGE -> BlockEntityRegistry.LARGE_FLUID_TANK_BLOCK_ENTITY.get();
            case MASSIVE -> BlockEntityRegistry.MASSIVE_FLUID_TANK_BLOCK_ENTITY.get();
            case CREATIVE -> BlockEntityRegistry.CREATIVE_FLUID_TANK_BLOCK_ENTITY.get();
            default -> BlockEntityRegistry.SMALL_FLUID_TANK_BLOCK_ENTITY.get();
        };
    }

    public TankTier getTankTier() {
        return tankTier;
    }

    public enum TankTier {
        SMALL, MEDIUM, LARGE, MASSIVE, CREATIVE
    }
}
