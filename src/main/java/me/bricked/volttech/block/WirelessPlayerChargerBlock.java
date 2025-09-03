package me.bricked.volttech.block;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.blockentity.WirelessPlayerChargerBlockEntity;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WirelessPlayerChargerBlock extends BaseEntityBlock {
    public WirelessPlayerChargerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof WirelessPlayerChargerBlockEntity wirelessPlayerChargerBlockEntity && !level.isClientSide()) {
            String uuid = wirelessPlayerChargerBlockEntity.getPlayerUUID();
            if (uuid == null || uuid.isEmpty()) {
                wirelessPlayerChargerBlockEntity.setPlayerUUID(player.getStringUUID());
                player.displayClientMessage(StringifyUtil.translate("message.linked_uuid_message", player.getStringUUID()), true);
            } else {
                player.displayClientMessage(StringifyUtil.translate("message.player_uuid_message", uuid), true);
            }
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide())
            return;
        if (level.getBlockEntity(pos) instanceof WirelessPlayerChargerBlockEntity wirelessPlayerChargerBlockEntity) {
            if (stack.has(DataComponentRegistry.PLAYER_UUID)) {
                wirelessPlayerChargerBlockEntity.setPlayerUUID(stack.get(DataComponentRegistry.PLAYER_UUID));
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.box(0, 0, 0, 1.f, 5.f / 16.f, 1.f);
        shape = Shapes.join(shape, Shapes.box(5.f / 16.f, 5.f / 16.f, 5.f / 16.f, 11.f / 16.f, 1.f, 11.f / 16.f), BooleanOp.OR);
        return shape;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.WIRELESS_PLAYER_CHARGER_BLOCK_ENTITY.get(), WirelessPlayerChargerBlockEntity::tick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(WirelessPlayerChargerBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WirelessPlayerChargerBlockEntity(blockPos, blockState);
    }
}
