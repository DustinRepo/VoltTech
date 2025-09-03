package me.bricked.volttech.item;

import me.bricked.volttech.item.charge.ChargeableItem;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class MechanizedEnderPearlItem extends ChargeableItem {
    public MechanizedEnderPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getMainHandItem();
        IEnergyStorage storage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage != null) {
            int use = getLogisticalConstraints().usageOrGeneration();
            if (storage.getEnergyStored() >= use) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                if (level instanceof ServerLevel serverlevel) {
                    EnergyUtil.useStoredEnergy(itemStack, use);
                    Projectile.spawnProjectileFromRotation(ThrownEnderpearl::new, serverlevel, itemStack, player, 0.0F, 1.5f, 1.0F);
                }
            } else
                return InteractionResult.FAIL;
        }
        return InteractionResult.SUCCESS_SERVER;
    }
}
