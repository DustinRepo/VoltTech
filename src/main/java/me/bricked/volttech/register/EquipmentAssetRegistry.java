package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class EquipmentAssetRegistry {
    private static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("equipment_asset"));
    public static ResourceKey<EquipmentAsset> VELTRIUM = createKey("veltrium");
    public static ResourceKey<EquipmentAsset> TREXALITE = createKey("trexalite");
    public static ResourceKey<EquipmentAsset> DALEKANIUM = createKey("dalekanium");
    public static ResourceKey<EquipmentAsset> HELLFORGED = createKey("hellforged");
    public static ResourceKey<EquipmentAsset> JETPACK = createKey("jetpack");

    private static ResourceKey<EquipmentAsset> createKey(String name) {
        return ResourceKey.create(ROOT_ID, VoltTech.resourceLocation(name));
    }
}
