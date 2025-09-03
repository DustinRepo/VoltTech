package me.bricked.volttech.item;

import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.capability.forgeenergy.ItemStackEnergyStorage;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TorchDispenserItem extends ChargeableItem {
    public TorchDispenserItem(Properties properties, boolean isInfinite) {
        super(properties);
        this.isInfinite = isInfinite;
    }

    private final boolean isInfinite;

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (level.isClientSide() || !(entity instanceof Player player))
            return;
        IEnergyStorage ienergyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        int stored = getStoredTorches(stack);
        if (!(ienergyStorage instanceof ItemStackEnergyStorage energyStorage) || (!isInfinite && stored == 0))
            return;
        if (energyStorage.getEnergyStored() < getLogisticalConstraints().usageOrGeneration())
            return;
        if (player.onGround() && player.getOffhandItem() == stack || player.getMainHandItem() == stack) {
            BlockPos pos = player.blockPosition();
            BlockPos floorPos = player.getBlockPosBelowThatAffectsMyMovement();
            BlockState currentState = level.getBlockState(pos);
            BlockState belowState = level.getBlockState(floorPos);
            if (!currentState.canBeReplaced() || belowState.getBlock() == Blocks.AIR)
                return;
            if (currentState.getFluidState().getFluidType() != Fluids.EMPTY.getFluidType() || belowState.getFluidState().getFluidType() != Fluids.EMPTY.getFluidType())
                return;
            int lightLevel = level.getBrightness(LightLayer.BLOCK, pos);
            if (lightLevel <= 7) {
                BlockItem torchBlockItem = (BlockItem) Items.TORCH;
                UseOnContext ctx = new UseOnContext(player, player.getOffhandItem() == stack ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atLowerCornerOf(floorPos), Direction.UP, floorPos, false));
                InteractionResult placeResult = torchBlockItem.place(new BlockPlaceContext(ctx));
                if (placeResult.consumesAction()) {
                    if (!isInfinite) {
                        stored = Math.max(0, stored - 1);
                        setStoredTorches(stack, stored);
                        if (stored == 0)
                            player.displayClientMessage(StringifyUtil.translate("message.torch_dispenser.empty").withColor(0xffff7700), false);
                    }
                    // Placing the torch consumes the dispenser
                    if (stack.getCount() == 0)
                        stack.setCount(1);
                    energyStorage.removeEnergy(getLogisticalConstraints().usageOrGeneration(), false);
                }
            }
        }
        super.inventoryTick(stack, level, entity, slot);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        ItemStackEnergyStorage energyStorage = (ItemStackEnergyStorage)stack.getCapability(Capabilities.EnergyStorage.ITEM);
        BlockItem torchBlockItem = (BlockItem) Items.TORCH;
        int stored = getStoredTorches(stack);
        if (energyStorage == null)
            return super.useOn(context);
        if ((stored == 0 && !isInfinite) || energyStorage.getEnergyStored() < getLogisticalConstraints().usageOrGeneration())
            return InteractionResult.SUCCESS_SERVER;
        InteractionResult result = torchBlockItem.place(new BlockPlaceContext(context));
        if (result.consumesAction() && !context.getLevel().isClientSide() && !isInfinite) {
            stored = Math.max(0, stored - 1);
            setStoredTorches(stack, stored);
            if (stored == 0 && context.getPlayer() != null)
                context.getPlayer().displayClientMessage(StringifyUtil.translate("message.torch_dispenser.empty").withColor(0xffff7700), false);
        }
        if (stack.getCount() == 0)
            stack.setCount(1);
        energyStorage.removeEnergy(getLogisticalConstraints().usageOrGeneration(), false);
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide || isInfinite)
            return super.use(level, player, usedHand);
        int oldStoredTorches = getStoredTorches(stack);
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack invStack = inventory.getItem(i);
            if (invStack.getItem() == Items.TORCH) {
                addTorches(stack, invStack.getCount());
                if (!player.isCreative()) {
                    invStack.setCount(0);
                }
            }
        }
        int newTorches = getStoredTorches(stack) - oldStoredTorches;
        if (newTorches > 0) {
            player.displayClientMessage(StringifyUtil.translate("message.torch_dispenser.stored_torches_added", newTorches).withColor(0xff00aaff), false);
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        if (isInfinite)
            tooltipAdder.accept(StringifyUtil.translate("tooltip.torch_dispenser.infinite_torches").withColor(0xff55ccff));
        else
            tooltipAdder.accept(StringifyUtil.translate("message.torch_dispenser.stored_torches", getStoredTorches(stack)).withColor(0xff00aaff));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isInfinite || super.isFoil(stack);
    }

    public static void addTorches(ItemStack stack, int count) {
        int stored = getStoredTorches(stack);
        // Not likely someone will reach MAX_INT in torches but may as well cap it just incase
        setStoredTorches(stack, Mth.clamp(stored + count, 0, Integer.MAX_VALUE));
    }

    public static void setStoredTorches(ItemStack stack, int count) {
        stack.set(DataComponentRegistry.STORED_TORCHES, count);
    }

    public static int getStoredTorches(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.STORED_TORCHES, 0);
    }
}
