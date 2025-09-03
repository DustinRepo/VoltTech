package me.bricked.volttech.register;

import me.bricked.volttech.screen.*;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ScreenRegistry {

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuRegistry.COMBUSTION_GENERATOR_MENU.get(), CombustionGeneratorScreen::new);
        event.register(MenuRegistry.HEAT_GENERATOR_MENU.get(), HeatGeneratorScreen::new);
        event.register(MenuRegistry.SOLAR_GENERATOR_MENU.get(), SolarGeneratorScreen::new);
        event.register(MenuRegistry.MINI_REACTOR_MENU.get(), MiniReactorScreen::new);
        event.register(MenuRegistry.WIRELESS_ENERGY_TRANSMITTER_MENU.get(), WirelessEnergyTransmitterScreen::new);
        event.register(MenuRegistry.SPATIAL_CRATE_MENU.get(), SpatialCrateScreen::new);
        event.register(MenuRegistry.FLUID_TANK_MENU.get(), FluidTankScreen::new);
        event.register(MenuRegistry.UPGRADE_TABLE_MENU.get(), UpgradeTableScreen::new);
        event.register(MenuRegistry.ENERGY_CUBE_MENU.get(), EnergyCubeScreen::new);
        event.register(MenuRegistry.HARVESTER_MENU.get(), HarvesterScreen::new);
        event.register(MenuRegistry.POWERED_FURNACE_MENU.get(), PoweredFurnaceScreen::new);
        event.register(MenuRegistry.CRUSHER_MENU.get(), CrusherScreen::new);
        event.register(MenuRegistry.FOOD_MASHER_MENU.get(), FoodMasherScreen::new);
        event.register(MenuRegistry.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
        event.register(MenuRegistry.BLOCK_PLACER_MENU.get(), BlockPlacerScreen::new);
    }
}
