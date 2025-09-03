package me.bricked.volttech.item.charge;

import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

public class ChargeableHoeItem extends HoeItem implements IChargeableItem {
    protected boolean isInfiniteEnergy;

    public ChargeableHoeItem(ToolMaterial tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        int energy = EnergyUtil.getStoredEnergy(stack);
        Constraints constraints = getLogisticalConstraints();
        if (energy < constraints.maxOutput())
            return false;
        if (!entity.level().isClientSide())
            EnergyUtil.useStoredEnergy(stack, constraints.maxOutput());
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public boolean isInfiniteEnergy() {
        return isInfiniteEnergy;
    }

    @Override
    public Constraints getLogisticalConstraints() {
        return Constraints.get(this);
    }
}
