package me.bricked.volttech.item.blockitem;

import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.RenderUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class FluidTankBlockItem extends BlockItem {
    public FluidTankBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    // Pickup without a block behind
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        boolean sneaking = player.isCrouching();
        if (sneaking) {
            boolean bucketMode = isBucketMode(itemStack);
            setBucketMode(itemStack, !bucketMode);
            bucketMode = !bucketMode;
            if (bucketMode)
                player.displayClientMessage(StringifyUtil.translate("tooltip.bucket_mode_on"), true);
            else
                player.displayClientMessage(StringifyUtil.translate("tooltip.bucket_mode_off"), true);
            return InteractionResult.PASS;
        }

        Optional<IFluidHandlerItem> optionalIFluidHandler = FluidUtil.getFluidHandler(itemStack);
        if (optionalIFluidHandler.isEmpty() || !isBucketMode(itemStack))
            return super.use(level, player, usedHand);
        IFluidHandler fluidHandler = optionalIFluidHandler.get();
        FluidStack fluidStack = fluidHandler.getFluidInTank(0);
        int capacity = fluidHandler.getTankCapacity(0);
        if (capacity - fluidStack.getAmount() < 1000)
            return InteractionResult.FAIL;
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos relativePos = blockpos.relative(direction);
            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(relativePos, direction, itemStack)) {
                BlockState blockState = level.getBlockState(blockpos);
                Block block = blockState.getBlock();
                if (block instanceof BucketPickup bucketPickup) {
                    FluidActionResult result = FluidUtil.tryPickUpFluid(itemStack, player, level, blockpos, direction);
                    if (result.isSuccess()) {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        bucketPickup.getPickupSound(blockState).ifPresent((soundEvent) -> player.playSound(soundEvent, 1.0F, 1.0F));
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                        player.setItemInHand(usedHand, result.getResult());
                        return InteractionResult.SUCCESS_SERVER;
                    }
                }
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand usedHand = context.getHand();
        Direction direction = context.getClickedFace();
        BlockPos blockPos = context.getClickedPos();
        BlockPos relativePos = blockPos.relative(direction);
        boolean sneaking = player.isCrouching();

        if (!isBucketMode(stack))
            return InteractionResult.PASS;

        Optional<IFluidHandlerItem> optionalHandler = FluidUtil.getFluidHandler(stack);
        if (optionalHandler.isEmpty())
            return InteractionResult.FAIL;

        IFluidHandler fluidHandler = optionalHandler.get();

        if (sneaking) {
            // Place fluid
            FluidStack fluidStack = fluidHandler.getFluidInTank(0);
            if (fluidStack.isEmpty())
                return InteractionResult.FAIL;

            if (level.mayInteract(player, blockPos) && player.mayUseItemAt(relativePos, direction, stack)) {
                BlockState blockState = level.getBlockState(blockPos);
                BlockPos finalPos = this.canBlockContainFluid(player, level, blockPos, blockState, fluidStack.getFluid()) ? blockPos : relativePos;
                FluidActionResult result = FluidUtil.tryPlaceFluid(player, level, usedHand, finalPos, stack, fluidStack);

                if (result.isSuccess()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, finalPos, stack);
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (!level.isClientSide())
                        player.setItemInHand(usedHand, result.getResult());
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.FAIL;
        } else {
            // Pickup fluid
            int capacity = fluidHandler.getTankCapacity(0);
            if (capacity - fluidHandler.getFluidInTank(0).getAmount() < 1000)
                return InteractionResult.FAIL;

            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if (hitResult.getType() != HitResult.Type.BLOCK)
                return InteractionResult.PASS;

            BlockPos targetPos = hitResult.getBlockPos();
            Direction hitDirection = hitResult.getDirection();
            BlockPos usePos = targetPos.relative(hitDirection);

            if (level.mayInteract(player, targetPos) && player.mayUseItemAt(usePos, hitDirection, stack)) {
                BlockState targetState = level.getBlockState(targetPos);
                Block block = targetState.getBlock();
                if (block instanceof BucketPickup bucketPickup) {
                    FluidActionResult result = FluidUtil.tryPickUpFluid(stack, player, level, targetPos, hitDirection);
                    if (result.isSuccess()) {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        bucketPickup.getPickupSound(targetState).ifPresent(sound -> player.playSound(sound, 1.0F, 1.0F));
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, targetPos);
                        if (!level.isClientSide())
                            player.setItemInHand(usedHand, result.getResult());
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.FAIL;
            }
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (stack.getItem() == ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get()) {
            FluidUtil.getFluidHandler(stack).ifPresent(iFluidHandlerItem -> {
                for (int i = 0; i < iFluidHandlerItem.getTanks(); i++) { // Should only ever be 1 tank but might as well
                    if (!iFluidHandlerItem.getFluidInTank(i).isEmpty()) {
                        iFluidHandlerItem.fill(new FluidStack(iFluidHandlerItem.getFluidInTank(i).getFluid(), Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            });
        }
        super.inventoryTick(stack, level, entity, slot);
    }

    @Override
    public Component getName(ItemStack stack) {
        Optional<FluidStack> fluidStackOptional = FluidUtil.getFluidContained(stack);
        if (fluidStackOptional.isPresent()) {
            FluidStack fluidStack = fluidStackOptional.get();
            RenderUtil.FluidSpriteInfo fluidSpriteInfo = RenderUtil.getSpriteInfo(fluidStack);
            MutableComponent description = (MutableComponent) fluidStack.getFluidType().getDescription();
            return super.getName(stack).copy().
                    append(" \2477(").
                    append(description.withColor(fluidSpriteInfo.tintColor())).
                    append("\2477)");
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        boolean isCreativeItem = this == ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get();
        Optional<FluidStack> fluidStackOptional = FluidUtil.getFluidContained(stack);
        fluidStackOptional.ifPresentOrElse(fluidStack -> {
                    RenderUtil.FluidSpriteInfo fluidSpriteInfo = RenderUtil.getSpriteInfo(fluidStack);
                    if (isCreativeItem)
                        tooltipAdder.accept(StringifyUtil.translate("tooltip.infinite_contained_fluid", fluidStack.getFluidType().getDescription()).withColor(fluidSpriteInfo.tintColor()));
                    else
                        tooltipAdder.accept(StringifyUtil.translate("tooltip.contained_fluid", fluidStack.getFluidType().getDescription(), "\2477" + StringifyUtil.stringifyFluid(fluidStack.getAmount())).withColor(fluidSpriteInfo.tintColor()));
                },
                () -> tooltipAdder.accept(StringifyUtil.translate("gui.empty").withColor(0xffff0000)));
        if (isCreativeItem)
            tooltipAdder.accept(StringifyUtil.translate("tooltip.infinite_fluid_capacity").withColor(0xff00ff50));
        else
            tooltipAdder.accept(StringifyUtil.translate("tooltip.fluid_capacity", StringifyUtil.stringifyFluid(getFluidCapacity())).withColor(0xff00ff50));
        if (isBucketMode(stack))
            tooltipAdder.accept(StringifyUtil.translate("tooltip.bucket_mode_on"));
        else
            tooltipAdder.accept(StringifyUtil.translate("tooltip.bucket_mode_off"));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }

    public int getFluidCapacity() {
        return Constraints.get(this.getBlock()).maxCapacity();
    }

    protected boolean canBlockContainFluid(Player player, Level worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, fluid);
    }

    public static void setBucketMode(ItemStack itemStack, boolean bucket) {
        itemStack.set(DataComponentRegistry.BUCKET_MODE, bucket);
    }

    public static boolean isBucketMode(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponentRegistry.BUCKET_MODE, false);
    }
}
