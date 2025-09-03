package me.bricked.volttech.item.blockitem;

import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class WirelessPlayerChargerBlockItem extends BlockItem {
    public WirelessPlayerChargerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Player player) {
        stack.set(DataComponentRegistry.PLAYER_UUID, player.getStringUUID());
        super.onCraftedBy(stack, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        MutableComponent mutableComponent = StringifyUtil.translate("tooltip.player_uuid");
        if (stack.has(DataComponentRegistry.PLAYER_UUID))
            mutableComponent.append(stack.get(DataComponentRegistry.PLAYER_UUID)).withStyle(ChatFormatting.GOLD);
        else
            mutableComponent.append("None").withStyle(ChatFormatting.RED);
        tooltipAdder.accept(mutableComponent);
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
