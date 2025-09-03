package me.bricked.volttech.datagen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.EquipmentAssetRegistry;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.function.BiConsumer;

public class VoltTechEquipmentAssetProvider extends EquipmentAssetProvider {
    public VoltTechEquipmentAssetProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(
                EquipmentAssetRegistry.VELTRIUM,
                onlyHumanoid(VoltTech.MODID + ":veltrium")
        );
        output.accept(
                EquipmentAssetRegistry.TREXALITE,
                onlyHumanoid(VoltTech.MODID + ":trexalite")
        );
        output.accept(
                EquipmentAssetRegistry.DALEKANIUM,
                onlyHumanoid(VoltTech.MODID + ":dalekanium")
        );
        output.accept(
                EquipmentAssetRegistry.HELLFORGED,
                onlyHumanoid(VoltTech.MODID + ":hellforged")
        );
        output.accept(
                EquipmentAssetRegistry.JETPACK,
                EquipmentClientInfo.
                        builder().
                        addMainHumanoidLayer(VoltTech.resourceLocation("jetpack"), false).
                        build()
        );
    }
}
