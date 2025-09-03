package me.bricked.volttech.event;

import com.google.common.collect.Maps;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.blockentity.FluidTankBlockEntity;
import me.bricked.volttech.blockentity.TemporalAcceleratorBlockEntity;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.Map;

@EventBusSubscriber(modid = VoltTech.MODID)
public class ServerWorldEvents {
    private static final Map<BlockPos, Integer> tickAccelerations = Maps.newHashMap();
    private static final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent blockDropsEvent) {
        Entity entity = blockDropsEvent.getBreaker();
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        ItemStack toolStack = livingEntity.getMainHandItem();
        ServerLevel level = blockDropsEvent.getLevel();
        if (level.isClientSide())
            return;
        // Auto smelt upgrade
        int smelterCount = ItemUtil.getUpgradeCount(toolStack, ItemRegistry.SMELTER_UPGRADE_CARD.get());
        if (smelterCount > 0) {
            for (ItemEntity drop : blockDropsEvent.getDrops()) {
                ItemStack possibleIngredient = drop.getItem();
                ItemStack resultItem = ItemUtil.getCookingRecipeStack(possibleIngredient, level, quickCheck);
                if (!resultItem.isEmpty())
                    drop.setItem(resultItem);
            }
        }
        // Fill fluid tanks
        if (blockDropsEvent.getBlockEntity() instanceof FluidTankBlockEntity fluidTankBlockEntity) {
            IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, fluidTankBlockEntity.getBlockPos(), fluidTankBlockEntity.getBlockState(), fluidTankBlockEntity, null);
            if (fluidHandler != null)
                blockDropsEvent.getDrops().forEach(itemEntity -> {
                    ItemStack stack = itemEntity.getItem();
                    FluidUtil.getFluidHandler(stack).ifPresent(iFluidHandlerItem -> {
                        iFluidHandlerItem.fill(fluidHandler.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
                    });
                });
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent breakEvent) {
        LevelAccessor level = breakEvent.getLevel();
        if (level.isClientSide() || breakEvent.isCanceled())
            return;
        Player player = breakEvent.getPlayer();
        ItemStack itemStack = player.getMainHandItem();
        HitResult hitResult = player.pick(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.f, false);

        if (!(hitResult instanceof BlockHitResult blockHitResult))
            return;
        Direction direction = blockHitResult.getDirection();
        if (ItemUtil.getUpgradeCount(itemStack, ItemRegistry.MINE_AREA_UPGRADE_CARD.get()) > 0) {
            ArrayList<BlockPos> area = getMineArea(direction, breakEvent.getPos());
            for (BlockPos blockPos : area) {
                BlockState state = level.getBlockState(blockPos);
                if (state.isAir() || state.getDestroySpeed(level, blockPos) < 0)
                    continue;

                if (!player.getMainHandItem().isCorrectToolForDrops(state))
                    continue;

                level.destroyBlock(blockPos, true, player);
            }
        }
    }

    public static ArrayList<BlockPos> getMineArea(Direction direction, BlockPos mainPos) {
        ArrayList<BlockPos> list = new ArrayList<>();
        list.add(mainPos);
        
        switch (direction) {
            case UP, DOWN -> {
                list.add(mainPos.offset(-1, 0, 0));
                list.add(mainPos.offset(1, 0, 0));
                list.add(mainPos.offset(0, 0, -1));
                list.add(mainPos.offset(0, 0, 1));
                list.add(mainPos.offset(1, 0, -1));
                list.add(mainPos.offset(-1, 0, 1));
                list.add(mainPos.offset(1, 0, 1));
                list.add(mainPos.offset(-1, 0, -1));
            }
            case EAST, WEST -> {
                list.add(mainPos.offset(0, 0, -1));
                list.add(mainPos.offset(0, 0, 1));
                list.add(mainPos.offset(0, -1, 0));
                list.add(mainPos.offset(0, 1, 0));
                list.add(mainPos.offset(0, 1, -1));
                list.add(mainPos.offset(0, -1, 1));
                list.add(mainPos.offset(0, 1, 1));
                list.add(mainPos.offset(0, -1, -1));
            }
            case NORTH, SOUTH -> {
                list.add(mainPos.offset(-1, 0, 0));
                list.add(mainPos.offset(1, 0, 0));
                list.add(mainPos.offset(0, -1, 0));
                list.add(mainPos.offset(0, 1, 0));
                list.add(mainPos.offset(-1, 1, 0));
                list.add(mainPos.offset(1, -1, 0));
                list.add(mainPos.offset(1, 1, 0));
                list.add(mainPos.offset(-1, -1, 0));
            }
        }
        
        return list;
    }

    @SubscribeEvent
    public static void onWorldTickPost(LevelTickEvent.Post levelTickEvent) {
        Level level = levelTickEvent.getLevel();
        if (level.isClientSide())
            return;
        tickAccelerations.forEach((blockPos, count) -> {
            BlockState blockState = level.getBlockState(blockPos);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity == null || blockEntity instanceof TemporalAcceleratorBlockEntity)
                return;

            if (blockState.getBlock() instanceof BaseEntityBlock baseEntityBlock) {
                BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>) baseEntityBlock.getTicker(level, blockState, blockEntity.getType());
                if (ticker != null) {
                    for (int i = 0; i < count - 1; i++) { // -1 because the block ticks itself
                        ticker.tick(level, blockPos, blockState, blockEntity);
                    }
                }
            }
        });
        tickAccelerations.clear();
    }

    public static void addTickAccelerations(BlockPos blockPos, int count) {
        if (tickAccelerations.containsKey(blockPos)) {
            int c = tickAccelerations.get(blockPos);
            tickAccelerations.replace(blockPos, c + count);
        } else
            tickAccelerations.put(blockPos, count);
    }

    public static Map<BlockPos, Integer> getTickAccelerations() {
        return tickAccelerations;
    }
}
