package me.bricked.volttech.item.charge;

import me.bricked.volttech.util.Constraints;

public interface IChargeableItem {
    boolean isInfiniteEnergy();
    Constraints getLogisticalConstraints();
}
