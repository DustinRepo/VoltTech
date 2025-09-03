package me.bricked.volttech.mixin;

import me.bricked.volttech.item.charge.IChargeableItem;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.EnergyUtil;
import me.bricked.volttech.util.ItemUtil;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow public abstract boolean is(TagKey<Item> tag);

    @Shadow public abstract Item getItem();

    @Inject(method = "applyDamage(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
    public void applyDamage(int damage, LivingEntity livingEntity, Consumer<Item> onBreak, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object)this;
        if (hasEnergyUpgrades() || this.getItem() instanceof IChargeableItem) {
            IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (storage != null) {
                int use = Math.min(storage.getEnergyStored(), damage * ItemUtil.getEnergyUsage(stack));
                EnergyUtil.useStoredEnergy(stack, use);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> cir) {
        if (!hasEnergyUpgrades() && !(this.getItem() instanceof IChargeableItem))
            return;
        IEnergyStorage storage = getThis().getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage == null)
            return;
        int capacity = storage.getMaxEnergyStored();
        int used = capacity - storage.getEnergyStored();
        float f = Math.max(0.0F, ((float)capacity - (float)used) / (float)capacity);
        cir.setReturnValue(Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F));
    }

    @Inject(method = "getBarWidth", at = @At("HEAD"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (!hasEnergyUpgrades() && !(this.getItem() instanceof IChargeableItem))
            return;
        IEnergyStorage storage = getThis().getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage == null)
            return;
        int capacity = storage.getMaxEnergyStored();
        int used = capacity - storage.getEnergyStored();
        cir.setReturnValue(Mth.clamp(Math.round(13.0F - (float)used * 13.0F / (float)capacity), 0, 13));
    }

    @Inject(method = "isBarVisible", at = @At("HEAD"), cancellable = true)
    public void isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (!hasEnergyUpgrades() && !(this.getItem() instanceof IChargeableItem))
            return;
        if (this.getItem() instanceof IChargeableItem iChargeableItem && iChargeableItem.isInfiniteEnergy())
            return;
        cir.setReturnValue(true);
    }

    @Unique
    private boolean hasEnergyUpgrades() {
        return UpgradeData.isUpgradeableItem(this.getItem()) && ItemUtil.hasUpgradeApplied(getThis(), ItemRegistry.ENERGY_UPGRADE_CARD.get());
    }

    @Unique
    private ItemStack getThis() {
        return (ItemStack)(Object)this;
    }
}
