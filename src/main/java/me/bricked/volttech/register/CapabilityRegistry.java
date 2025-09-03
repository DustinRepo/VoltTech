package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.blockentity.*;
import me.bricked.volttech.capability.item.ContainerItemHandler;
import me.bricked.volttech.capability.item.WorldlyContainerItemHandler;
import me.bricked.volttech.capability.fluid.SingleTankFluidHandler;
import me.bricked.volttech.capability.forgeenergy.UpgradeableItemStackEnergyStorage;
import me.bricked.volttech.item.blockitem.FluidTankBlockItem;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.ItemUtil;
import me.bricked.volttech.capability.forgeenergy.ItemStackEnergyStorage;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;


@EventBusSubscriber(modid = VoltTech.MODID)
public class CapabilityRegistry {

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        // Energy
        event.registerBlock(
                Capabilities.EnergyStorage.BLOCK,
                ((level, blockPos, blockState, blockEntity, direction) -> {
                    IEnergyBlockEntity iEnergyBlockEntity = (IEnergyBlockEntity)blockEntity;
                    if (iEnergyBlockEntity == null)
                        return null;
                    return iEnergyBlockEntity.getEnergyStorage(blockState, direction);
                }),
                BlockRegistry.COPPER_ENERGY_CABLE.get(),
                BlockRegistry.IRON_ENERGY_CABLE.get(),
                BlockRegistry.GOLD_ENERGY_CABLE.get(),
                BlockRegistry.DIAMOND_ENERGY_CABLE.get(),
                BlockRegistry.EMERALD_ENERGY_CABLE.get(),
                BlockRegistry.NETHERITE_ENERGY_CABLE.get(),
                BlockRegistry.CREATIVE_ENERGY_CUBE.get(),
                BlockRegistry.MINI_REACTOR.get(),
                BlockRegistry.SOLAR_GENERATOR.get(),
                BlockRegistry.COMBUSTION_GENERATOR.get(),
                BlockRegistry.HEAT_GENERATOR.get(),
                BlockRegistry.TEMPORAL_ACCELERATOR.get(),
                BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get(),
                BlockRegistry.SMALL_ENERGY_CUBE.get(),
                BlockRegistry.MEDIUM_ENERGY_CUBE.get(),
                BlockRegistry.LARGE_ENERGY_CUBE.get(),
                BlockRegistry.MASSIVE_ENERGY_CUBE.get(),
                BlockRegistry.CREATIVE_ENERGY_CUBE.get(),
                BlockRegistry.HARVESTER.get(),
                BlockRegistry.POWERED_FURNACE.get(),
                BlockRegistry.CRUSHER.get(),
                BlockRegistry.WIRELESS_PLAYER_CHARGER.get(),
                BlockRegistry.FOOD_MASHER.get(),
                BlockRegistry.BLOCK_BREAKER.get(),
                BlockRegistry.BLOCK_PLACER.get()
        );
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (itemStack, context) -> {
                    return new ItemStackEnergyStorage(itemStack, DataComponentRegistry.STORED_FE.get(), Constraints.get(itemStack.getItem()));
                },
                ItemRegistry.DEMONIC_CRUCIBLE.get(),
                ItemRegistry.TORCH_DISPENSER.get(),
                ItemRegistry.INFINITE_TORCH_DISPENSER.get(),
                ItemRegistry.MECHANIZED_ENDER_PEARL.get(),
                ItemRegistry.HOMING_DRIVE.get(),
                ItemRegistry.GRAVE_TRANSPORTER.get(),
                ItemRegistry.DALEKANIUM_PICKAXE.get(),
                ItemRegistry.DALEKANIUM_AXE.get(),
                ItemRegistry.DALEKANIUM_SHOVEL.get(),
                ItemRegistry.DALEKANIUM_SWORD.get(),
                ItemRegistry.DALEKANIUM_HOE.get(),
                ItemRegistry.DALEKANIUM_HELMET.get(),
                ItemRegistry.DALEKANIUM_CHESTPLATE.get(),
                ItemRegistry.DALEKANIUM_LEGGINGS.get(),
                ItemRegistry.DALEKANIUM_BOOTS.get(),
                ItemRegistry.JETPACK.get(),
                ItemRegistry.SMALL_BATTERY.get(),
                ItemRegistry.MEDIUM_BATTERY.get(),
                ItemRegistry.LARGE_BATTERY.get(),
                ItemRegistry.MASSIVE_BATTERY.get(),
                ItemRegistry.CREATIVE_BATTERY.get(),
                ItemRegistry.ATOMIC_BATTERY.get(),
                ItemRegistry.PORTAL_GUN.get()
        );
        // Register all upgradeable items as energy capable based on the custom config system
        // has to use this custom system so it loads before this part
        BuiltInRegistries.ITEM.forEach(item -> {
            if (!event.isItemRegistered(Capabilities.EnergyStorage.ITEM, item) && UpgradeData.isUpgradeableItem(item)) {
                event.registerItem(
                        Capabilities.EnergyStorage.ITEM,
                        (itemStack, context) -> {
                            if (ItemUtil.getUpgradeCount(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get()) > 0)
                                return new UpgradeableItemStackEnergyStorage(itemStack, DataComponentRegistry.STORED_FE.get());
                            return null;
                        },
                        item
                );
            }
        });

        // Fluids
        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                (level, blockPos, blockState, blockEntity, direction) -> new SingleTankFluidHandler(((MiniReactorBlockEntity) blockEntity).getWaterTank(), Integer.MAX_VALUE, Integer.MAX_VALUE),
                BlockRegistry.MINI_REACTOR.get()
        );
        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                (level, blockPos, blockState, blockEntity, direction) -> new SingleTankFluidHandler(((FluidTankBlockEntity) blockEntity).getTank(), Integer.MAX_VALUE, Integer.MAX_VALUE),
                BlockRegistry.SMALL_FLUID_TANK.get(),
                BlockRegistry.MEDIUM_FLUID_TANK.get(),
                BlockRegistry.LARGE_FLUID_TANK.get(),
                BlockRegistry.MASSIVE_FLUID_TANK.get(),
                BlockRegistry.CREATIVE_FLUID_TANK.get()
        );
        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                (level, blockPos, blockState, blockEntity, direction) -> {
                    FluidPipeBlockEntity fluidPipeBlockEntity = ((FluidPipeBlockEntity)blockEntity);
                    return fluidPipeBlockEntity.getPassthroughFluidHandler();
                },
                BlockRegistry.COPPER_FLUID_PIPE.get(),
                BlockRegistry.IRON_FLUID_PIPE.get(),
                BlockRegistry.GOLD_FLUID_PIPE.get(),
                BlockRegistry.DIAMOND_FLUID_PIPE.get(),
                BlockRegistry.EMERALD_FLUID_PIPE.get(),
                BlockRegistry.NETHERITE_FLUID_PIPE.get()
        );
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (o, unused) -> new FluidHandlerItemStack(DataComponentRegistry.FLUID, o, ((FluidTankBlockItem)o.getItem()).getFluidCapacity()),
                ItemRegistry.SMALL_FLUID_TANK_BLOCK_ITEM.get(),
                ItemRegistry.MEDIUM_FLUID_TANK_BLOCK_ITEM.get(),
                ItemRegistry.LARGE_FLUID_TANK_BLOCK_ITEM.get(),
                ItemRegistry.MASSIVE_FLUID_TANK_BLOCK_ITEM.get(),
                ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get()
        );

        // Items
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, blockPos, blockState, blockEntity, direction) -> ((ItemPipeBlockEntity)blockEntity).getPassthroughItemHandler(),
                BlockRegistry.ITEM_PIPE.get()
        );
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, blockPos, blockState, blockEntity, direction) -> new ContainerItemHandler(((Container) blockEntity)),
                BlockRegistry.MINI_REACTOR.get(),
                BlockRegistry.SPATIAL_CRATE.get(),
                BlockRegistry.HARVESTER.get(),
                BlockRegistry.BLOCK_PLACER.get()
        );
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, blockPos, blockState, blockEntity, direction) -> {
                    WorldlyContainer worldlyContainer = (WorldlyContainer) blockEntity;
                    int[] slots = worldlyContainer.getSlotsForFace(direction);
                    if (slots.length > 0)
                        return new WorldlyContainerItemHandler(worldlyContainer, direction, slots);
                    return null;
                },
                BlockRegistry.POWERED_FURNACE.get(),
                BlockRegistry.CRUSHER.get(),
                BlockRegistry.FOOD_MASHER.get(),
                BlockRegistry.BLOCK_BREAKER.get()
        );
    }
}
