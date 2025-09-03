package me.bricked.volttech.item.material;

import me.bricked.volttech.register.TagRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class VoltTechToolMaterials {

    public static ToolMaterial DALEKANIUM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            2048,
            15.f,
            4.f,
            15,
            TagRegistry.Items.EMPTY_TAG
    );
    public static ToolMaterial VELTRIUM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            4096,
            25.f,
            11,
            30,
            TagRegistry.Items.VELTRIUM_RAW_MATERIAL
    );
    public static ToolMaterial TREXALITE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2031,
            9.f,
            6.f,
            12,
            TagRegistry.Items.TREXALITE_GEM_TAG
    );
    public static ToolMaterial ARGENT = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            1200,
            15.f,
            12,
            22,
            TagRegistry.Items.EMPTY_TAG
    );
}
