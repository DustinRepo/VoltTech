package me.bricked.volttech.item.charge;

import me.bricked.volttech.util.Constraints;
import net.minecraft.world.item.*;

public class ChargeableAxeItem extends AxeItem implements IChargeableItem {
    protected boolean isInfiniteEnergy;

    public ChargeableAxeItem(ToolMaterial tier, Item.Properties properties, float attackDamage, float attackSpeed) {
        super(tier, attackDamage, attackSpeed, properties);
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
