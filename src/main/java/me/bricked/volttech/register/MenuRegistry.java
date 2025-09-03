package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, VoltTech.MODID);
    public static Supplier<MenuType<HarvesterMenu>> HARVESTER_MENU;
    public static Supplier<MenuType<SolarGeneratorMenu>> SOLAR_GENERATOR_MENU;
    public static Supplier<MenuType<CombustionGeneratorMenu>> COMBUSTION_GENERATOR_MENU;
    public static Supplier<MenuType<HeatGeneratorMenu>> HEAT_GENERATOR_MENU;
    public static Supplier<MenuType<MiniReactorMenu>> MINI_REACTOR_MENU;
    public static Supplier<MenuType<WirelessEnergyTransmitterMenu>> WIRELESS_ENERGY_TRANSMITTER_MENU;
    public static Supplier<MenuType<SpatialCrateMenu>> SPATIAL_CRATE_MENU;
    public static Supplier<MenuType<FluidTankMenu>> FLUID_TANK_MENU;
    public static Supplier<MenuType<UpgradeTableMenu>> UPGRADE_TABLE_MENU;
    public static Supplier<MenuType<EnergyCubeMenu>> ENERGY_CUBE_MENU;
    public static Supplier<MenuType<PoweredFurnaceMenu>> POWERED_FURNACE_MENU;
    public static Supplier<MenuType<CrusherMenu>> CRUSHER_MENU;
    public static Supplier<MenuType<FoodMasherMenu>> FOOD_MASHER_MENU;
    public static Supplier<MenuType<BlockBreakerMenu>> BLOCK_BREAKER_MENU;
    public static Supplier<MenuType<BlockPlacerMenu>> BLOCK_PLACER_MENU;

    public static void registerMenus(IEventBus modEventBus) {
        HARVESTER_MENU = MENUS.register(
                "harvester_menu",
                () -> IMenuTypeExtension.create(HarvesterMenu::new));
        SOLAR_GENERATOR_MENU = MENUS.register(
                "solar_generator_menu",
                () -> new MenuType<>(SolarGeneratorMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        COMBUSTION_GENERATOR_MENU = MENUS.register(
                "combustion_generator_menu",
                () -> new MenuType<>(CombustionGeneratorMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        HEAT_GENERATOR_MENU = MENUS.register(
                "heat_generator_menu",
                () -> new MenuType<>(HeatGeneratorMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        WIRELESS_ENERGY_TRANSMITTER_MENU = MENUS.register(
                "wireless_energy_transmitter_menu",
                () -> new MenuType<>(WirelessEnergyTransmitterMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        SPATIAL_CRATE_MENU = MENUS.register(
                "spatial_crate_menu",
                () -> new MenuType<>(SpatialCrateMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        MINI_REACTOR_MENU = MENUS.register(
                "mini_reactor_menu",
                () -> IMenuTypeExtension.create(MiniReactorMenu::new));
        FLUID_TANK_MENU = MENUS.register("fluid_tank_menu", () ->
                IMenuTypeExtension.create(FluidTankMenu::new));
        UPGRADE_TABLE_MENU = MENUS.register("upgrade_table_menu",
                () -> new MenuType<>(UpgradeTableMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        ENERGY_CUBE_MENU = MENUS.register("energy_cube_menu",
                () -> new MenuType<>(EnergyCubeMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        POWERED_FURNACE_MENU = MENUS.register("powered_furnace_menu",
                () -> new MenuType<>(PoweredFurnaceMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        CRUSHER_MENU = MENUS.register("crusher_menu",
                () -> new MenuType<>(CrusherMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        FOOD_MASHER_MENU = MENUS.register("food_masher_menu",
                () -> new MenuType<>(FoodMasherMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        BLOCK_BREAKER_MENU = MENUS.register("block_breaker_menu",
                () -> new MenuType<>(BlockBreakerMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));
        BLOCK_PLACER_MENU = MENUS.register("block_placer_menu",
                () -> new MenuType<>(BlockPlacerMenu::new,
                        FeatureFlags.DEFAULT_FLAGS));

        MENUS.register(modEventBus);
    }
}
