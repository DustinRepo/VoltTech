package me.bricked.volttech.item;

import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;

public class HomingDriveItem extends ChargeableItem {
    public HomingDriveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS_SERVER;
        ServerLevel serverLevel = ((ServerLevel)level);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack itemStack = player.getItemInHand(hand);
        if (EnergyUtil.getStoredEnergy(itemStack) >= getLogisticalConstraints().usageOrGeneration()) {
            TeleportTransition transition = ((ServerPlayer) player).findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
            // Fire neoforge event incase another mod changes the respawn pos
            PlayerRespawnPositionEvent event = EventHooks.firePlayerRespawnPositionEvent(serverPlayer, transition, true);
            transition = event.getTeleportTransition();
            if (!transition.newLevel().getDescriptionKey().equalsIgnoreCase(serverLevel.getDescriptionKey())) {
                CriteriaTriggers.CHANGED_DIMENSION.trigger(serverPlayer, serverLevel.dimension(), transition.newLevel().dimension());
            }
            serverPlayer.teleport(transition);
            level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            EnergyUtil.useStoredEnergy(itemStack, getLogisticalConstraints().usageOrGeneration());
        }
        return InteractionResult.SUCCESS_SERVER;
    }
}
