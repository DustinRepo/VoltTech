package me.bricked.volttech.item;

import me.bricked.volttech.Config;
import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.util.EnergyUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class GraveTransporterItem extends ChargeableItem {
    public GraveTransporterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide())
            return InteractionResult.SUCCESS_SERVER;
        if (EnergyUtil.getStoredEnergy(itemStack) < getLogisticalConstraints().usageOrGeneration())
            return InteractionResult.SUCCESS_SERVER;
        Optional<GlobalPos> optionalPos = player.getLastDeathLocation();
        if (optionalPos.isPresent()) {
                GlobalPos globalPos = optionalPos.get();
                ServerLevel tpLevel = ((ServerLevel) level).getServer().getLevel(globalPos.dimension());
                if (tpLevel != null) {
                    TeleportTransition transition = new TeleportTransition(tpLevel, Vec3.atCenterOf(globalPos.pos()), Vec3.ZERO, player.getYRot(), player.getXRot(), TeleportTransition.DO_NOTHING);
                    player.teleport(transition);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    EnergyUtil.useStoredEnergy(itemStack, getLogisticalConstraints().usageOrGeneration());
                }
                return InteractionResult.SUCCESS_SERVER;
        } else {
            player.displayClientMessage(StringifyUtil.translate("message.no_death_location"), false);
            return InteractionResult.SUCCESS_SERVER;
        }
    }
}
