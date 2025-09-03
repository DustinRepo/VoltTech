package me.bricked.volttech.item;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.function.Function;
import java.util.function.Supplier;

public class UpgradeCardItem extends Item {
    private final Supplier<DataComponentType<Integer>> dataComponent;
    private final EquipmentSlotGroup equipmentSlotGroup;
    private final Function<ItemStack, Boolean> itemCheckFunction;
    private final int maxCount;
    public UpgradeCardItem(Properties properties, Supplier<DataComponentType<Integer>> dataComponent, int maxCount, EquipmentSlotGroup equipmentSlotGroup, Function<ItemStack, Boolean> itemCheckFunction) {
        super(properties);
        this.dataComponent = dataComponent;
        this.itemCheckFunction = itemCheckFunction;
        this.equipmentSlotGroup = equipmentSlotGroup;
        this.maxCount = maxCount;
    }

    public boolean canApply(ItemStack itemStack) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable != null) {
            EquipmentSlot slot = equippable.slot();
            if (!equipmentSlotGroup.test(slot))
                return false;
        }
        return itemCheckFunction.apply(itemStack);
    }

    public static UpgradeCardItem from(DataComponentType<?> dataComponent) {
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (!key.getNamespace().equalsIgnoreCase(VoltTech.MODID))
                continue;
            if (item instanceof UpgradeCardItem upgradeCardItem && upgradeCardItem.dataComponent.get() == dataComponent)
                return upgradeCardItem;
        }
        return null;
    }

    public Supplier<DataComponentType<Integer>> getDataComponent() {
        return dataComponent;
    }

    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return equipmentSlotGroup;
    }

    public int getMaxCount() {
        return maxCount;
    }
}
