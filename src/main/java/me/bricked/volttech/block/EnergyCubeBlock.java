package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.EnergyCubeBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EnergyCubeBlock extends BaseEntityBlock {
    private final EnergyCubeTier tier;
    public EnergyCubeBlock(Properties properties, EnergyCubeTier tier) {
        super(properties);
        this.tier = tier;
        this.registerDefaultState(this.getStateDefinition().any().setValue(DirectionalBlock.FACING, Direction.NORTH));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(DirectionalBlock.FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(DirectionalBlock.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties1 -> new EnergyCubeBlock(properties1, tier));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyCubeBlockEntity(getType(), pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, getType(), EnergyCubeBlockEntity::tick);
    }

    public BlockEntityType<EnergyCubeBlockEntity> getType() {
        return switch (getEnergyCubeTier()) {
            case SMALL -> BlockEntityRegistry.SMALL_ENERGY_CUBE_BLOCK_ENTITY.get();
            case MEDIUM -> BlockEntityRegistry.MEDIUM_ENERGY_CUBE_BLOCK_ENTITY.get();
            case LARGE -> BlockEntityRegistry.LARGE_ENERGY_CUBE_BLOCK_ENTITY.get();
            case MASSIVE -> BlockEntityRegistry.MASSIVE_ENERGY_CUBE_BLOCK_ENTITY.get();
            case CREATIVE -> BlockEntityRegistry.CREATIVE_ENERGY_CUBE_BLOCK_ENTITY.get();
            default -> BlockEntityRegistry.SMALL_ENERGY_CUBE_BLOCK_ENTITY.get();
        };
    }

    public EnergyCubeTier getEnergyCubeTier() {
        return tier;
    }

    public enum EnergyCubeTier {
        SMALL, MEDIUM, LARGE, MASSIVE, CREATIVE
    }
}
