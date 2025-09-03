package me.bricked.volttech.item.charge;

import me.bricked.volttech.util.Constraints;
import net.minecraft.world.item.Item;

public class ChargeableItem extends Item implements IChargeableItem {
    public ChargeableItem(Properties properties) {
        super(properties);
    }
    public ChargeableItem(Properties properties, boolean isInfiniteEnergy) {
        super(properties);
        this.isInfiniteEnergy = isInfiniteEnergy;
    }
    protected boolean isInfiniteEnergy;
    @Override
    public boolean isInfiniteEnergy() {
        return isInfiniteEnergy;
    }

    @Override
    public Constraints getLogisticalConstraints() {
        return Constraints.get(this);
    }
}
