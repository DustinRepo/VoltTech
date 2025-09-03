package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CustomModelRegistry {
    public static final ItemStack CHUNK_LOADER_CORE = Items.STICK.getDefaultInstance().copy();
    public static final ItemStack MULTIVERSE_PORTAL = Items.STICK.getDefaultInstance().copy();

    static {
        // Store ItemStacks that have their model changed
        // This requires a json for the model in assets/models/item/<model>.json
        // and a json in assets/items/<model_name>.json
        // The ResourceLocation being set should be from the items folder
        CHUNK_LOADER_CORE.set(DataComponents.ITEM_MODEL, VoltTech.resourceLocation("chunk_loader_core"));
        MULTIVERSE_PORTAL.set(DataComponents.ITEM_MODEL, VoltTech.resourceLocation("multiverse_portal"));
    }
}
