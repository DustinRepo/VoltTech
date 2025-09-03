package me.bricked.volttech.datagen.tag;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;

import java.util.concurrent.CompletableFuture;

public class VoltTechFluidTagProvider extends FluidTagsProvider {
    public VoltTechFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, VoltTech.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        /*this.tag(TagRegistry.Fluids.FLUID_TANK_BANNED).
                add(
                        FluidRegistry.ARGENT_PLASMA.get()
                );*/
    }
}
