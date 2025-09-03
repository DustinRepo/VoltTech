package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.fluid.ArgentPlasmaFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FluidRegistry {
    public static DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, VoltTech.MODID);
    public static DeferredHolder<Fluid, ArgentPlasmaFluid.Source> ARGENT_PLASMA;
    public static DeferredHolder<Fluid, ArgentPlasmaFluid.Flowing> FLOWING_ARGENT_PLASMA;

    public static FluidType ARGENT_PLASMA_FLUID_TYPE = new FluidType(FluidType.Properties.create().
            canConvertToSource(false).
            canSwim(false).
            temperature(2000).
            lightLevel(15).
            density(1024).
            viscosity(1024).
            descriptionId("block.volttech.argent_plasma"));

    public static void registerFluids(IEventBus modEventBus) {
        ARGENT_PLASMA = FLUIDS.register("argent_plasma", ArgentPlasmaFluid.Source::new);
        FLOWING_ARGENT_PLASMA = FLUIDS.register("flowing_argent_plasma", ArgentPlasmaFluid.Flowing::new);

        FLUIDS.register(modEventBus);
    }
}
