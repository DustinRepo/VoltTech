package me.bricked.volttech.mixin;

import me.bricked.volttech.item.charge.IChargeableItem;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.EnergyUtil;
import me.bricked.volttech.util.ItemUtil;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    public void getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (UpgradeData.isUpgradeableItem(stack.getItem())) {
            if (ItemUtil.hasUpgradeApplied(stack, ItemRegistry.ENERGY_UPGRADE_CARD.get())) {
                int energy = EnergyUtil.getStoredEnergy(stack);
                int use = ItemUtil.getEnergyUsage(stack);
                if (energy < use) {
                    cir.setReturnValue(0.f);
                    return;
                }
            }
            int mineSpeedUpgradeCount = ItemUtil.getUpgradeCount(stack, ItemRegistry.MINING_SPEED_UPGRADE_CARD.get());
            float speed = cir.getReturnValue();
            cir.setReturnValue(speed + (speed * (mineSpeedUpgradeCount * 0.2f)));
        } else if (stack.getItem() instanceof IChargeableItem chargeableItem) {
            int energy = EnergyUtil.getStoredEnergy(stack);
            if (energy < chargeableItem.getLogisticalConstraints().usageOrGeneration())
                cir.setReturnValue(0.f);
        }
    }

}
