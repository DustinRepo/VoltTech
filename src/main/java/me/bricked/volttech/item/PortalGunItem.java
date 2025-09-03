package me.bricked.volttech.item;

import me.bricked.volttech.entity.MultiversePortalEntity;
import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.EntityRegistry;
import me.bricked.volttech.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.OptionalLong;
import java.util.function.Consumer;

public class PortalGunItem extends ChargeableItem {
    public PortalGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            int use = getLogisticalConstraints().usageOrGeneration();
            if (!player.isCrouching() && EnergyUtil.getStoredEnergy(itemStack) >= use) {
                // Get seed from portal gun
                String stringSeed = itemStack.getOrDefault(DataComponentRegistry.PORTAL_SEED, "");
                boolean home = itemStack.getOrDefault(DataComponentRegistry.PORTAL_HOME, false);
                OptionalLong seed = WorldOptions.parseSeed(stringSeed);
                // Get location to place portal
                BlockPos portalPos = findPortalSpot((ServerLevel) level, player);
                MultiversePortalEntity existing = LevelUtil.portalInSpot(level, portalPos);
                if (existing == null) {
                    // Create portal entity and place in world
                    MultiversePortalEntity multiversePortalEntity = new MultiversePortalEntity(
                            EntityRegistry.MULTIVERSE_PORTAL.get(),
                            level,
                            seed,
                            itemStack.get(DataComponentRegistry.PORTAL_LOCATION) // This will be null if the portal gun packet doesn't set location
                    );
                    if (home) {
                        multiversePortalEntity.setSeed(OptionalLong.empty());
                        multiversePortalEntity.setCustomName(StringifyUtil.translate("entity.multiverse_portal_home"));
                    } else {
                        multiversePortalEntity.setCustomName(StringifyUtil.translate("entity.multiverse_portal_name", seed.orElse(0)));
                    }
                    multiversePortalEntity.setPos(Vec3.atBottomCenterOf(portalPos));
                    // clamp Y rot to cardinal direction
                    multiversePortalEntity.setYRot(Direction.fromYRot(player.getYRot()).toYRot());
                    level.addFreshEntity(multiversePortalEntity);
                    EnergyUtil.useStoredEnergy(itemStack, use);
                }
                return InteractionResult.SUCCESS_SERVER;
            }
        } else if (player.isCrouching()) {
            ScreenOpener.openPortalGunScreen(itemStack); // Put this in a seperate class so it only tries to load the screen class on client side
        }
        return super.use(level, player, hand);
    }

    public static BlockPos findPortalSpot(ServerLevel level, Player shooter) {
        Vec3 end = shooter.getEyePosition().add(shooter.getLookAngle().scale(5));
        ClipContext context = new ClipContext(
                shooter.getEyePosition(),
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                shooter
        );

        BlockHitResult hit = level.clip(context);
        Vec3 hitPos = hit.getType() == HitResult.Type.MISS ? end : hit.getLocation();

        BlockPos basePos = BlockPos.containing(hitPos).immutable();

        for (int y = basePos.getY(); y < level.getHeight() - 2; y++) {
            BlockPos feet = new BlockPos(basePos.getX(), y, basePos.getZ());
            BlockPos head = feet.above();
            BlockPos ground = feet.below();

            if (level.getBlockState(feet).isAir()
                    && level.getBlockState(head).isAir()
                    && level.getBlockState(ground).isSolid()) {
                return new BlockPos(basePos.getX(), y, basePos.getZ());
            }
        }

        return basePos;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(StringifyUtil.translate("tooltip.portal_gun_seed", stack.getOrDefault(DataComponentRegistry.PORTAL_SEED, "home")));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
