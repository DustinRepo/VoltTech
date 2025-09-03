package me.bricked.volttech.item.material;

import me.bricked.volttech.register.EquipmentAssetRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

public class VoltTechArmorMaterials {
    public static ArmorMaterial DALEKANIUM_ARMOR_MATERIAL = new ArmorMaterial(
            30,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 4);
                map.put(ArmorType.LEGGINGS, 7);
                map.put(ArmorType.CHESTPLATE, 9);
                map.put(ArmorType.HELMET, 4);
                map.put(ArmorType.BODY, 12);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0,
            0.2f,
            TagRegistry.Items.EMPTY_TAG,
            EquipmentAssetRegistry.DALEKANIUM
    );
    public static ArmorMaterial HELLFORGED_ARMOR_MATERIAL = new ArmorMaterial(
            30,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 3);
                map.put(ArmorType.LEGGINGS, 6);
                map.put(ArmorType.CHESTPLATE, 8);
                map.put(ArmorType.HELMET, 3);
                map.put(ArmorType.BODY, 11);
            }),
            20,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0,
            0.0f,
            TagRegistry.Items.HELLFORGED_INGOT_TAG,
            EquipmentAssetRegistry.HELLFORGED
    );
    public static ArmorMaterial VELTRIUM_ARMOR_MATERIAL = new ArmorMaterial(
            50,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 6);
                map.put(ArmorType.LEGGINGS, 9);
                map.put(ArmorType.CHESTPLATE, 11);
                map.put(ArmorType.HELMET, 6);
                map.put(ArmorType.BODY, 14);
            }),
            30,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0,
            0.0f,
            TagRegistry.Items.VELTRIUM_GEM_TAG,
            EquipmentAssetRegistry.VELTRIUM
    );
    public static ArmorMaterial TREXALITE_ARMOR_MATERIAL = new ArmorMaterial(
            30,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, 3);
                map.put(ArmorType.LEGGINGS, 6);
                map.put(ArmorType.CHESTPLATE, 8);
                map.put(ArmorType.HELMET, 3);
                map.put(ArmorType.BODY, 11);
            }),
            12,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0,
            0.0f,
            TagRegistry.Items.TREXALITE_GEM_TAG,
            EquipmentAssetRegistry.TREXALITE
    );

}
