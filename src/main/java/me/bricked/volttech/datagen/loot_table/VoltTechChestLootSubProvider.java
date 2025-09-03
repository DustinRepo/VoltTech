package me.bricked.volttech.datagen.loot_table;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class VoltTechChestLootSubProvider implements LootTableSubProvider {
    private final HolderLookup.Provider lookupProvider;
    public VoltTechChestLootSubProvider(HolderLookup.Provider lookupProvider) {
        this.lookupProvider = lookupProvider;
    }
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        ResourceKey<LootTable> villageKey = ResourceKey.create(Registries.LOOT_TABLE, VoltTech.resourceLocation("chests/village/village_plains_house"));
        LootTable.Builder villagePool = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool().
                                setRolls(ConstantValue.exactly(1)).
                                setBonusRolls(ConstantValue.exactly(0.5f)).
                                name("volttech_village_pool").
                                add(LootItem.lootTableItem(ItemRegistry.ATOMIC_BATTERY.get()).
                                        setWeight(2).
                                        apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                )
                );
        biConsumer.accept(villageKey, villagePool);
    }
}
