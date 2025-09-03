package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VoltTech.MODID);
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> VOLTTECH_TAB;
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> TANKS_TAB;

    public static void registerCreativeTabs(IEventBus modEventBus) {
        VOLTTECH_TAB = CREATIVE_MODE_TABS.register("volttech_tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.volttech")) //The language key for the title of your CreativeModeTab
                .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                .icon(() -> ItemRegistry.CREATIVE_ENERGY_CUBE_BLOCK_ITEM.get().getDefaultInstance())
                .displayItems(CreativeTabRegistry::addToVoltTechTab).build());
        TANKS_TAB = CREATIVE_MODE_TABS.register("volttech_tanks_tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.volttech_tank"))
                .withTabsBefore(VOLTTECH_TAB.getId())
                .icon(() -> {
                    ItemStack creativeFluidTank = new ItemStack(ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get());
                    FluidUtil.getFluidHandler(creativeFluidTank).ifPresent(iFluidHandlerItem -> {
                        iFluidHandlerItem.fill(new FluidStack(FluidRegistry.ARGENT_PLASMA, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
                    });
                    return creativeFluidTank;
                })
                .displayItems(CreativeTabRegistry::addTanksTab).build());

        CREATIVE_MODE_TABS.register(modEventBus);
    }

    private static void addTanksTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        BuiltInRegistries.FLUID.forEach(fluid -> {
            // Registry gives flowing and still versions, we only want the still version
            if (!fluid.defaultFluidState().isSource())
                return;
            ItemStack creativeFluidTank = new ItemStack(ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get());
            FluidUtil.getFluidHandler(creativeFluidTank).ifPresent(iFluidHandlerItem -> {
                iFluidHandlerItem.fill(new FluidStack(fluid, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
            });
            output.accept(creativeFluidTank);
        });
    }

    private static void addToVoltTechTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(ItemRegistry.RAW_URANIUM.get());
        output.accept(ItemRegistry.URANIUM_INGOT.get());
        output.accept(ItemRegistry.URANIUM_ORE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.DEEPSLATE_URANIUM_ORE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.RAW_URANIUM_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.URANIUM_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.HELLFORGED_SHARD.get());
        output.accept(ItemRegistry.HELLFORGED_INGOT.get());
        output.accept(ItemRegistry.HELLISH_ROCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.HELLFORGED_HELMET.get());
        output.accept(ItemRegistry.HELLFORGED_CHESTPLATE.get());
        output.accept(ItemRegistry.HELLFORGED_LEGGINGS.get());
        output.accept(ItemRegistry.HELLFORGED_BOOTS.get());
        output.accept(ItemRegistry.RAW_DALEKANIUM.get());
        output.accept(ItemRegistry.DALEKANIUM_INGOT.get());
        output.accept(ItemRegistry.DALEKANIUM_ORE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.RAW_DALEKANIUM_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.DALEKANIUM_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.DALEKANIUM_PICKAXE.get());
        output.accept(ItemRegistry.DALEKANIUM_AXE.get());
        output.accept(ItemRegistry.DALEKANIUM_SHOVEL.get());
        output.accept(ItemRegistry.DALEKANIUM_HOE.get());
        output.accept(ItemRegistry.DALEKANIUM_SWORD.get());
        output.accept(ItemRegistry.DALEKANIUM_HELMET.get());
        output.accept(ItemRegistry.DALEKANIUM_CHESTPLATE.get());
        output.accept(ItemRegistry.DALEKANIUM_LEGGINGS.get());
        output.accept(ItemRegistry.DALEKANIUM_BOOTS.get());
        output.accept(ItemRegistry.RAW_TREXALITE.get());
        output.accept(ItemRegistry.TREXALITE.get());
        output.accept(ItemRegistry.DEEPSLATE_TREXALITE_ORE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.RAW_TREXALITE_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.TREXALITE_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.TREXALITE_PICKAXE.get());
        output.accept(ItemRegistry.TREXALITE_AXE.get());
        output.accept(ItemRegistry.TREXALITE_SHOVEL.get());
        output.accept(ItemRegistry.TREXALITE_HOE.get());
        output.accept(ItemRegistry.TREXALITE_SWORD.get());
        output.accept(ItemRegistry.TREXALITE_HELMET.get());
        output.accept(ItemRegistry.TREXALITE_CHESTPLATE.get());
        output.accept(ItemRegistry.TREXALITE_LEGGINGS.get());
        output.accept(ItemRegistry.TREXALITE_BOOTS.get());
        output.accept(ItemRegistry.RAW_VELTRIUM.get());
        output.accept(ItemRegistry.VELTRIUM.get());
        output.accept(ItemRegistry.VELTRIUM_ORE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.RAW_VELTRIUM_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.VELTRIUM_BLOCK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.VELTRIUM_PICKAXE.get());
        output.accept(ItemRegistry.VELTRIUM_AXE.get());
        output.accept(ItemRegistry.VELTRIUM_SHOVEL.get());
        output.accept(ItemRegistry.VELTRIUM_HOE.get());
        output.accept(ItemRegistry.VELTRIUM_SWORD.get());
        output.accept(ItemRegistry.VELTRIUM_HELMET.get());
        output.accept(ItemRegistry.VELTRIUM_CHESTPLATE.get());
        output.accept(ItemRegistry.VELTRIUM_LEGGINGS.get());
        output.accept(ItemRegistry.VELTRIUM_BOOTS.get());
        output.accept(ItemRegistry.DEMONIC_CRUCIBLE.get());
        output.accept(ItemRegistry.COPPER_ENERGY_CABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.IRON_ENERGY_CABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.GOLD_ENERGY_CABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.DIAMOND_ENERGY_CABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.EMERALD_ENERGY_CABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.NETHERITE_ENERGY_CABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.COPPER_FLUID_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.IRON_FLUID_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.GOLD_FLUID_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.DIAMOND_FLUID_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.EMERALD_FLUID_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.NETHERITE_FLUID_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.ITEM_PIPE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.WIRELESS_ENERGY_TRANSMITTER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.SMALL_FLUID_TANK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.MEDIUM_FLUID_TANK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.LARGE_FLUID_TANK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.MASSIVE_FLUID_TANK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get());
        output.accept(ItemRegistry.UPGRADE_TABLE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.SOLAR_GENERATOR_BLOCK_ITEM.get());
        output.accept(ItemRegistry.WIRELESS_PLAYER_CHARGER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.COMBUSTION_GENERATOR_BLOCK_ITEM.get());
        output.accept(ItemRegistry.HEAT_GENERATOR_BLOCK_ITEM.get());
        output.accept(ItemRegistry.MINI_REACTOR_BLOCK_ITEM.get());
        output.accept(ItemRegistry.SPATIAL_CRATE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.TEMPORAL_ACCELERATOR_BLOCK_ITEM.get());
        output.accept(ItemRegistry.HARVESTER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.BLOCK_BREAKER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.BLOCK_PLACER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.POWERED_FURNACE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.CRUSHER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.FOOD_MASHER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.CHUNK_LOADER_BLOCK_ITEM.get());
        output.accept(ItemRegistry.SMALL_ENERGY_CUBE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.MEDIUM_ENERGY_CUBE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.LARGE_ENERGY_CUBE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.MASSIVE_ENERGY_CUBE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.CREATIVE_ENERGY_CUBE_BLOCK_ITEM.get());
        output.accept(ItemRegistry.SMALL_BATTERY.get());
        output.accept(ItemRegistry.MEDIUM_BATTERY.get());
        output.accept(ItemRegistry.LARGE_BATTERY.get());
        output.accept(ItemRegistry.MASSIVE_BATTERY.get());
        output.accept(ItemRegistry.CREATIVE_BATTERY.get());
        output.accept(ItemRegistry.ATOMIC_BATTERY.get());
        output.accept(ItemRegistry.TORCH_DISPENSER.get());
        output.accept(ItemRegistry.INFINITE_TORCH_DISPENSER.get());
        output.accept(ItemRegistry.MECHANIZED_ENDER_PEARL.get());
        output.accept(ItemRegistry.HOMING_DRIVE.get());
        output.accept(ItemRegistry.GRAVE_TRANSPORTER.get());
        output.accept(ItemRegistry.JETPACK.get());
        output.accept(ItemRegistry.PORTAL_GUN.get());
        output.accept(ItemRegistry.UTILITY_WRENCH.get());
        output.accept(ItemRegistry.LOCATION_CARD_ITEM.get());
        output.accept(ItemRegistry.ELECTRIC_COMPONENT.get());
        output.accept(ItemRegistry.SOLAR_PANEL.get());
        output.accept(ItemRegistry.BOOSTER.get());
        output.accept(ItemRegistry.EDIBLE_SLOP.get());
        output.accept(ItemRegistry.ARGENT_PLASMA_BUCKET.get());
        output.accept(ItemRegistry.CRUSHED_IRON.get());
        output.accept(ItemRegistry.CRUSHED_COPPER.get());
        output.accept(ItemRegistry.CRUSHED_GOLD.get());
        output.accept(ItemRegistry.CRUSHED_DIAMOND.get());
        output.accept(ItemRegistry.CRUSHED_EMERALD.get());
        output.accept(ItemRegistry.CRUSHED_LAPIS.get());
        output.accept(ItemRegistry.CRUSHED_REDSTONE.get());
        output.accept(ItemRegistry.CRUSHED_HELLFORGED.get());
        output.accept(ItemRegistry.CRUSHED_URANIUM.get());
        output.accept(ItemRegistry.CRUSHED_DALEKANIUM.get());
        output.accept(ItemRegistry.CRUSHED_VELTRIUM.get());
        output.accept(ItemRegistry.CRUSHED_TREXALITE.get());
        output.accept(ItemRegistry.ENERGY_UPGRADE_CARD.get());
        output.accept(ItemRegistry.SPEED_UPGRADE_CARD.get());
        output.accept(ItemRegistry.JUMP_BOOST_UPGRADE_CARD.get());
        output.accept(ItemRegistry.NIGHT_VISION_UPGRADE_CARD.get());
        output.accept(ItemRegistry.JETPACK_UPGRADE_CARD.get());
        output.accept(ItemRegistry.FEATHER_FALLING_UPGRADE_CARD.get());
        output.accept(ItemRegistry.HEALTH_UPGRADE_CARD.get());
        output.accept(ItemRegistry.WATER_BREATHING_UPGRADE_CARD.get());
        output.accept(ItemRegistry.AUTO_FEED_UPGRADE_CARD.get());
        output.accept(ItemRegistry.FIRE_RESIST_UPGRADE_CARD.get());
        output.accept(ItemRegistry.WITHER_RESIST_UPGRADE_CARD.get());
        output.accept(ItemRegistry.POISON_RESIST_UPGRADE_CARD.get());
        output.accept(ItemRegistry.MAGNET_UPGRADE_CARD.get());
        output.accept(ItemRegistry.PHASE_UPGRADE_CARD.get());
        output.accept(ItemRegistry.REGENERATION_UPGRADE_CARD.get());
        output.accept(ItemRegistry.STEP_HEIGHT_UPGRADE_CARD.get());
        output.accept(ItemRegistry.EVASION_UPGRADE_CARD.get());
        output.accept(ItemRegistry.REACH_UPGRADE_CARD.get());
        output.accept(ItemRegistry.MINING_SPEED_UPGRADE_CARD.get());
        output.accept(ItemRegistry.MINE_AREA_UPGRADE_CARD.get());
        output.accept(ItemRegistry.SMELTER_UPGRADE_CARD.get());
        output.accept(ItemRegistry.DAMAGE_UPGRADE_CARD.get());
    }
}
