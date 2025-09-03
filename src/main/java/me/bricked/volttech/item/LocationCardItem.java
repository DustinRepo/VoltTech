package me.bricked.volttech.item;

import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class LocationCardItem extends Item {
    public LocationCardItem(Properties properties) {
        super(properties);
    }
    public static final Vector3f BAD_VEC = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
    public static final ResourceLocation BAD_DIM = ResourceLocation.fromNamespaceAndPath("bad", "fakedim");

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null)
            return super.useOn(context);
        Level level = context.getLevel();
        ItemStack itemStack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        if (player.isCrouching() && !hasData(itemStack)) {
            setStoredPos(itemStack, Vec3.atLowerCornerOf(pos).toVector3f());
            setStoredDirection(itemStack, direction);
            setStoredDimension(itemStack, level.dimension().location());
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.useOn(context);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return hasData(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        int tooltipColor = 0xffffaa00;
        if (hasData(stack)) {
            Vector3f vec = getStoredPos(stack);
            tooltipAdder.accept(StringifyUtil.translate("tooltip.marked_location").withColor(tooltipColor));
            tooltipAdder.accept(Component.literal("%d %d %d".formatted((int) vec.x(), (int) vec.y(), (int) vec.z())).withColor(tooltipColor));
            tooltipAdder.accept(Component.literal(getStoredDirection(stack).name().toUpperCase()).withColor(tooltipColor));
            tooltipAdder.accept(Component.translatable(getStoredDimension(stack).toString()).withColor(tooltipColor));
        }
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }

    public static boolean hasData(ItemStack itemStack) {
        return itemStack.has(DataComponentRegistry.STORED_DIMENSION);
    }

    private static void setStoredPos(ItemStack itemStack, Vector3f vector3f) {
        itemStack.set(DataComponentRegistry.STORED_POSITION, vector3f);
    }

    private static void setStoredDirection(ItemStack itemStack, Direction direction) {
        itemStack.set(DataComponentRegistry.STORED_DIRECTION, direction.ordinal());
    }

    private static void setStoredDimension(ItemStack itemStack, ResourceLocation dimension) {
        itemStack.set(DataComponentRegistry.STORED_DIMENSION, dimension.toString());
    }

    public static Vector3f getStoredPos(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponentRegistry.STORED_POSITION, BAD_VEC);
    }

    public static Direction getStoredDirection(ItemStack itemStack) {
        int dir = itemStack.getOrDefault(DataComponentRegistry.STORED_DIRECTION, -1);
        if (dir == -1)
            return null;
        return Direction.values()[dir];
    }

    public static ResourceLocation getStoredDimension(ItemStack itemStack) {
        String dim = itemStack.getOrDefault(DataComponentRegistry.STORED_DIMENSION, BAD_DIM.toString());
        return ResourceLocation.parse(dim);
    }
}
