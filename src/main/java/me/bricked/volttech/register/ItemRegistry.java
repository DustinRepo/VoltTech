package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.item.*;
import me.bricked.volttech.item.blockitem.FluidTankBlockItem;
import me.bricked.volttech.item.blockitem.WirelessPlayerChargerBlockItem;
import me.bricked.volttech.item.charge.*;
import me.bricked.volttech.item.UpgradeCardItem;
import me.bricked.volttech.item.material.VoltTechToolMaterials;
import me.bricked.volttech.item.material.VoltTechArmorMaterials;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VoltTech.MODID);
    public static DeferredItem<BlockItem> SMALL_ENERGY_CUBE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> MEDIUM_ENERGY_CUBE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> LARGE_ENERGY_CUBE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> MASSIVE_ENERGY_CUBE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> CREATIVE_ENERGY_CUBE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> COPPER_ENERGY_CABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> IRON_ENERGY_CABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> GOLD_ENERGY_CABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> DIAMOND_ENERGY_CABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> EMERALD_ENERGY_CABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> NETHERITE_ENERGY_CABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> COPPER_FLUID_PIPE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> IRON_FLUID_PIPE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> GOLD_FLUID_PIPE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> DIAMOND_FLUID_PIPE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> EMERALD_FLUID_PIPE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> NETHERITE_FLUID_PIPE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> ITEM_PIPE_BLOCK_ITEM;
    public static DeferredItem<FluidTankBlockItem> SMALL_FLUID_TANK_BLOCK_ITEM;
    public static DeferredItem<FluidTankBlockItem> MEDIUM_FLUID_TANK_BLOCK_ITEM;
    public static DeferredItem<FluidTankBlockItem> LARGE_FLUID_TANK_BLOCK_ITEM;
    public static DeferredItem<FluidTankBlockItem> MASSIVE_FLUID_TANK_BLOCK_ITEM;
    public static DeferredItem<FluidTankBlockItem> CREATIVE_FLUID_TANK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> FOOD_MASHER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> HARVESTER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> BLOCK_BREAKER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> BLOCK_PLACER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> SOLAR_GENERATOR_BLOCK_ITEM;
    public static DeferredItem<BlockItem> COMBUSTION_GENERATOR_BLOCK_ITEM;
    public static DeferredItem<BlockItem> HEAT_GENERATOR_BLOCK_ITEM;
    public static DeferredItem<BlockItem> MINI_REACTOR_BLOCK_ITEM;
    public static DeferredItem<BlockItem> TEMPORAL_ACCELERATOR_BLOCK_ITEM;
    public static DeferredItem<BlockItem> SPATIAL_CRATE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> UPGRADE_TABLE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> WIRELESS_ENERGY_TRANSMITTER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> POWERED_FURNACE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> CRUSHER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> CHUNK_LOADER_BLOCK_ITEM;
    public static DeferredItem<WirelessPlayerChargerBlockItem> WIRELESS_PLAYER_CHARGER_BLOCK_ITEM;
    public static DeferredItem<BlockItem> URANIUM_ORE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> DEEPSLATE_URANIUM_ORE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> RAW_URANIUM_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> URANIUM_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> DALEKANIUM_ORE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> RAW_DALEKANIUM_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> DALEKANIUM_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> HELLISH_ROCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> VELTRIUM_ORE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> VELTRIUM_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> RAW_VELTRIUM_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> DEEPSLATE_TREXALITE_ORE_BLOCK_ITEM;
    public static DeferredItem<BlockItem> TREXALITE_BLOCK_BLOCK_ITEM;
    public static DeferredItem<BlockItem> RAW_TREXALITE_BLOCK_BLOCK_ITEM;

    public static DeferredItem<TorchDispenserItem> TORCH_DISPENSER;
    public static DeferredItem<TorchDispenserItem> INFINITE_TORCH_DISPENSER;
    public static DeferredItem<MechanizedEnderPearlItem> MECHANIZED_ENDER_PEARL;
    public static DeferredItem<HomingDriveItem> HOMING_DRIVE;
    public static DeferredItem<GraveTransporterItem> GRAVE_TRANSPORTER;
    public static DeferredItem<Item> UTILITY_WRENCH;
    public static DeferredItem<LocationCardItem> LOCATION_CARD_ITEM;
    public static DeferredItem<Item> EDIBLE_SLOP;
    public static DeferredItem<ChargeableItem> JETPACK;
    public static DeferredItem<PortalGunItem> PORTAL_GUN;

    public static DeferredItem<UpgradeCardItem> ENERGY_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> SPEED_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> NIGHT_VISION_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> JUMP_BOOST_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> JETPACK_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> HEALTH_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> FEATHER_FALLING_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> WATER_BREATHING_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> AUTO_FEED_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> FIRE_RESIST_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> WITHER_RESIST_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> POISON_RESIST_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> MAGNET_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> PHASE_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> REGENERATION_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> STEP_HEIGHT_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> REACH_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> EVASION_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> MINING_SPEED_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> MINE_AREA_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> SMELTER_UPGRADE_CARD;
    public static DeferredItem<UpgradeCardItem> DAMAGE_UPGRADE_CARD;

    public static DeferredItem<BucketItem> ARGENT_PLASMA_BUCKET;

    public static DeferredItem<ChargeableItem> DEMONIC_CRUCIBLE;
    public static DeferredItem<ChargeableItem> DALEKANIUM_PICKAXE;
    public static DeferredItem<ChargeableShovelItem> DALEKANIUM_SHOVEL;
    public static DeferredItem<ChargeableAxeItem> DALEKANIUM_AXE;
    public static DeferredItem<ChargeableItem> DALEKANIUM_SWORD;
    public static DeferredItem<ChargeableHoeItem> DALEKANIUM_HOE;
    public static DeferredItem<Item> VELTRIUM_PICKAXE;
    public static DeferredItem<ShovelItem> VELTRIUM_SHOVEL;
    public static DeferredItem<AxeItem> VELTRIUM_AXE;
    public static DeferredItem<Item> VELTRIUM_SWORD;
    public static DeferredItem<HoeItem> VELTRIUM_HOE;
    public static DeferredItem<Item> TREXALITE_PICKAXE;
    public static DeferredItem<ShovelItem> TREXALITE_SHOVEL;
    public static DeferredItem<AxeItem> TREXALITE_AXE;
    public static DeferredItem<Item> TREXALITE_SWORD;
    public static DeferredItem<HoeItem> TREXALITE_HOE;

    public static DeferredItem<ChargeableItem> DALEKANIUM_HELMET;
    public static DeferredItem<ChargeableItem> DALEKANIUM_CHESTPLATE;
    public static DeferredItem<ChargeableItem> DALEKANIUM_LEGGINGS;
    public static DeferredItem<ChargeableItem> DALEKANIUM_BOOTS;
    public static DeferredItem<Item> HELLFORGED_HELMET;
    public static DeferredItem<Item> HELLFORGED_CHESTPLATE;
    public static DeferredItem<Item> HELLFORGED_LEGGINGS;
    public static DeferredItem<Item> HELLFORGED_BOOTS;
    public static DeferredItem<Item> VELTRIUM_HELMET;
    public static DeferredItem<Item> VELTRIUM_CHESTPLATE;
    public static DeferredItem<Item> VELTRIUM_LEGGINGS;
    public static DeferredItem<Item> VELTRIUM_BOOTS;
    public static DeferredItem<Item> TREXALITE_HELMET;
    public static DeferredItem<Item> TREXALITE_CHESTPLATE;
    public static DeferredItem<Item> TREXALITE_LEGGINGS;
    public static DeferredItem<Item> TREXALITE_BOOTS;

    public static DeferredItem<Item> ELECTRIC_COMPONENT;
    public static DeferredItem<Item> SOLAR_PANEL;
    public static DeferredItem<Item> BOOSTER;

    public static DeferredItem<Item> RAW_URANIUM;
    public static DeferredItem<Item> URANIUM_INGOT;
    public static DeferredItem<Item> RAW_DALEKANIUM;
    public static DeferredItem<Item> DALEKANIUM_INGOT;
    public static DeferredItem<Item> HELLFORGED_SHARD;
    public static DeferredItem<Item> HELLFORGED_INGOT;
    public static DeferredItem<Item> RAW_VELTRIUM;
    public static DeferredItem<Item> VELTRIUM;
    public static DeferredItem<Item> RAW_TREXALITE;
    public static DeferredItem<Item> TREXALITE;

    public static DeferredItem<Item> CRUSHED_IRON;
    public static DeferredItem<Item> CRUSHED_GOLD;
    public static DeferredItem<Item> CRUSHED_COPPER;
    public static DeferredItem<Item> CRUSHED_DIAMOND;
    public static DeferredItem<Item> CRUSHED_EMERALD;
    public static DeferredItem<Item> CRUSHED_REDSTONE;
    public static DeferredItem<Item> CRUSHED_LAPIS;
    public static DeferredItem<Item> CRUSHED_HELLFORGED;
    public static DeferredItem<Item> CRUSHED_VELTRIUM;
    public static DeferredItem<Item> CRUSHED_DALEKANIUM;
    public static DeferredItem<Item> CRUSHED_URANIUM;
    public static DeferredItem<Item> CRUSHED_TREXALITE;

    public static DeferredItem<BatteryItem> SMALL_BATTERY;
    public static DeferredItem<BatteryItem> MEDIUM_BATTERY;
    public static DeferredItem<BatteryItem> LARGE_BATTERY;
    public static DeferredItem<BatteryItem> MASSIVE_BATTERY;
    public static DeferredItem<BatteryItem> CREATIVE_BATTERY;
    public static DeferredItem<BatteryItem> ATOMIC_BATTERY;

    public static void registerItems(IEventBus modEventBus) {
        RAW_URANIUM = ITEMS.registerSimpleItem("raw_uranium");
        URANIUM_INGOT = ITEMS.registerSimpleItem("uranium_ingot");
        RAW_DALEKANIUM = ITEMS.registerSimpleItem("raw_dalekanium");
        DALEKANIUM_INGOT = ITEMS.registerSimpleItem("dalekanium_ingot");
        HELLFORGED_SHARD = ITEMS.registerSimpleItem("hellforged_shard", new Item.Properties().fireResistant());
        HELLFORGED_INGOT = ITEMS.registerSimpleItem("hellforged_ingot", new Item.Properties().fireResistant());
        RAW_VELTRIUM = ITEMS.registerSimpleItem("raw_veltrium");
        VELTRIUM = ITEMS.registerSimpleItem("veltrium");
        RAW_TREXALITE = ITEMS.registerSimpleItem("raw_trexalite");
        TREXALITE = ITEMS.registerSimpleItem("trexalite");
        CRUSHED_IRON = ITEMS.registerSimpleItem("crushed_iron");
        CRUSHED_GOLD = ITEMS.registerSimpleItem("crushed_gold");
        CRUSHED_COPPER = ITEMS.registerSimpleItem("crushed_copper");
        CRUSHED_DIAMOND = ITEMS.registerSimpleItem("crushed_diamond");
        CRUSHED_EMERALD = ITEMS.registerSimpleItem("crushed_emerald");
        CRUSHED_REDSTONE = ITEMS.registerSimpleItem("crushed_redstone");
        CRUSHED_LAPIS = ITEMS.registerSimpleItem("crushed_lapis");
        CRUSHED_VELTRIUM = ITEMS.registerSimpleItem("crushed_veltrium");
        CRUSHED_DALEKANIUM = ITEMS.registerSimpleItem("crushed_dalekanium");
        CRUSHED_URANIUM = ITEMS.registerSimpleItem("crushed_uranium");
        CRUSHED_TREXALITE = ITEMS.registerSimpleItem("crushed_trexalite");
        CRUSHED_HELLFORGED = ITEMS.registerSimpleItem("crushed_hellforged");
        EDIBLE_SLOP = ITEMS.registerSimpleItem("edible_slop", new Item.Properties().
                food(new FoodProperties(1, 1, false)));

        ENERGY_UPGRADE_CARD = registerUpgradeCard("energy_upgrade_card", DataComponentRegistry.NUM_ENERGY_UPGRADES, 10, EquipmentSlotGroup.ANY, itemStack -> true);
        SPEED_UPGRADE_CARD = registerUpgradeCard("speed_upgrade_card", DataComponentRegistry.NUM_SPEED_UPGRADES, 5, EquipmentSlotGroup.LEGS, ItemUtil::isArmor);
        NIGHT_VISION_UPGRADE_CARD = registerUpgradeCard("night_vision_upgrade_card", DataComponentRegistry.NUM_NIGHTVISION_UPGRADES, 1, EquipmentSlotGroup.HEAD, ItemUtil::isArmor);
        JUMP_BOOST_UPGRADE_CARD = registerUpgradeCard("jump_boost_upgrade_card", DataComponentRegistry.NUM_JUMPBOOST_UPGRADES, 5, EquipmentSlotGroup.FEET, ItemUtil::isArmor);
        JETPACK_UPGRADE_CARD = registerUpgradeCard("jetpack_upgrade_card", DataComponentRegistry.NUM_JETPACK_UPGRADES, 3, EquipmentSlotGroup.CHEST, ItemUtil::isArmor);
        FEATHER_FALLING_UPGRADE_CARD = registerUpgradeCard("feather_falling_upgrade_card", DataComponentRegistry.NUM_FEATHER_FALLING_UPGRADES, 10, EquipmentSlotGroup.FEET, ItemUtil::isArmor);
        HEALTH_UPGRADE_CARD = registerUpgradeCard("health_upgrade_card", DataComponentRegistry.NUM_HEALTH_UPGRADES, 10, EquipmentSlotGroup.CHEST, ItemUtil::isArmor);
        WATER_BREATHING_UPGRADE_CARD = registerUpgradeCard("water_breathing_upgrade_card", DataComponentRegistry.NUM_WATER_BREATH_UPGRADES, 1, EquipmentSlotGroup.HEAD, ItemUtil::isArmor);
        AUTO_FEED_UPGRADE_CARD = registerUpgradeCard("auto_feed_upgrade_card", DataComponentRegistry.NUM_AUTO_FEED_UPGRADES, 1, EquipmentSlotGroup.HEAD, ItemUtil::isArmor);
        FIRE_RESIST_UPGRADE_CARD = registerUpgradeCard("fire_resist_upgrade_card", DataComponentRegistry.NUM_FIRE_RESIST_UPGRADES, 1, EquipmentSlotGroup.HEAD, ItemUtil::isArmor);
        WITHER_RESIST_UPGRADE_CARD = registerUpgradeCard("wither_resist_upgrade_card", DataComponentRegistry.NUM_WITHER_RESIST_UPGRADES, 1, EquipmentSlotGroup.HEAD, ItemUtil::isArmor);
        POISON_RESIST_UPGRADE_CARD = registerUpgradeCard("poison_resist_upgrade_card", DataComponentRegistry.NUM_POISON_RESIST_UPGRADES, 1, EquipmentSlotGroup.HEAD, ItemUtil::isArmor);
        MAGNET_UPGRADE_CARD = registerUpgradeCard("magnet_upgrade_card", DataComponentRegistry.NUM_MAGNET_UPGRADES, 5, EquipmentSlotGroup.CHEST, ItemUtil::isArmor);
        PHASE_UPGRADE_CARD = registerUpgradeCard("phase_upgrade_card", DataComponentRegistry.NUM_PHASE_UPGRADES, 1, EquipmentSlotGroup.LEGS, ItemUtil::isArmor);
        REGENERATION_UPGRADE_CARD = registerUpgradeCard("regeneration_upgrade_card", DataComponentRegistry.NUM_REGENERATION_UPGRADES, 5, EquipmentSlotGroup.CHEST, ItemUtil::isArmor);
        STEP_HEIGHT_UPGRADE_CARD = registerUpgradeCard("step_height_upgrade_card", DataComponentRegistry.NUM_STEP_HEIGHT_UPGRADES, 5, EquipmentSlotGroup.FEET, ItemUtil::isArmor);
        REACH_UPGRADE_CARD = registerUpgradeCard("reach_upgrade_card", DataComponentRegistry.NUM_REACH_UPGRADES, 7, EquipmentSlotGroup.CHEST, ItemUtil::isArmor);
        EVASION_UPGRADE_CARD = registerUpgradeCard("evasion_upgrade_card", DataComponentRegistry.NUM_EVASION_UPGRADES, 5, EquipmentSlotGroup.LEGS, ItemUtil::isArmor);
        MINING_SPEED_UPGRADE_CARD = registerUpgradeCard("mining_speed_upgrade_card", DataComponentRegistry.NUM_MINE_SPEED_UPGRADES, 5, EquipmentSlotGroup.HAND, ItemUtil::isDiggerItem);
        MINE_AREA_UPGRADE_CARD = registerUpgradeCard("mine_area_upgrade_card", DataComponentRegistry.NUM_MINE_AREA_UPGRADES, 1, EquipmentSlotGroup.HAND, ItemUtil::isDiggerItem);
        SMELTER_UPGRADE_CARD = registerUpgradeCard("smelter_upgrade_card", DataComponentRegistry.NUM_SMELTER_UPGRADES, 1, EquipmentSlotGroup.HAND, ItemUtil::isDiggerItem);
        DAMAGE_UPGRADE_CARD = registerUpgradeCard("damage_upgrade_card", DataComponentRegistry.NUM_DAMAGE_UPGRADES, 5, EquipmentSlotGroup.HAND, ItemUtil::isMeleeItem);

        ELECTRIC_COMPONENT = ITEMS.registerSimpleItem("electric_component");
        SOLAR_PANEL = ITEMS.registerSimpleItem("solar_panel");
        BOOSTER = ITEMS.registerSimpleItem("booster");

        TORCH_DISPENSER = ITEMS.registerItem(
                "torch_dispenser",
                properties -> new TorchDispenserItem(properties, false),
                new Item.Properties().stacksTo(1)
        );
        INFINITE_TORCH_DISPENSER = ITEMS.registerItem(
                "infinite_torch_dispenser",
                properties -> new TorchDispenserItem(properties, true),
                new Item.Properties().stacksTo(1)
        );
        MECHANIZED_ENDER_PEARL = ITEMS.registerItem(
                "mechanized_ender_pearl",
                MechanizedEnderPearlItem::new,
                new Item.Properties().stacksTo(1).useCooldown(1.0F)
        );
        HOMING_DRIVE = ITEMS.registerItem(
                "homing_drive",
                HomingDriveItem::new,
                new Item.Properties().stacksTo(1).useCooldown(1.0F)
        );
        GRAVE_TRANSPORTER = ITEMS.registerItem(
                "grave_transporter",
                GraveTransporterItem::new,
                new Item.Properties().stacksTo(1).useCooldown(1.0F)
        );
        UTILITY_WRENCH = ITEMS.registerItem(
                "utility_wrench",
                Item::new,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
        );
        LOCATION_CARD_ITEM = ITEMS.registerItem(
                "location_card",
                LocationCardItem::new,
                new Item.Properties().stacksTo(1).rarity(Rarity.RARE)
        );
        JETPACK = ITEMS.registerItem(
                "jetpack",
                properties -> new ChargeableItem(
                        properties.component(
                        DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.CHEST).
                                setEquipSound(SoundEvents.ARMOR_EQUIP_GENERIC).
                                setAsset(EquipmentAssetRegistry.JETPACK).
                                setAllowedEntities(EntityType.PLAYER).
                                setDamageOnHurt(false).
                                build()).
                                component(DataComponentRegistry.ACTIVATE_JETPACK, false)
                ),
                new Item.Properties().stacksTo(1)
        );
        PORTAL_GUN = ITEMS.registerItem(
                "portal_gun",
                PortalGunItem::new,
                new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)
        );
        DEMONIC_CRUCIBLE = ITEMS.registerItem(
                "demonic_crucible",
                properties -> new ChargeableItem(
                        properties.sword(VoltTechToolMaterials.ARGENT, 3, -2.4F)
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_PICKAXE = ITEMS.registerItem(
                "dalekanium_pickaxe",
                properties -> new ChargeableItem(
                        properties.pickaxe(VoltTechToolMaterials.DALEKANIUM, 1.f, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_AXE = ITEMS.registerItem(
                "dalekanium_axe",
                properties -> new ChargeableAxeItem(
                        VoltTechToolMaterials.DALEKANIUM,
                        properties.axe(VoltTechToolMaterials.DALEKANIUM, 5.f, -2.4f),
                        5.f,
                        -2.4f
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_SHOVEL = ITEMS.registerItem(
                "dalekanium_shovel",
                properties -> new ChargeableShovelItem(
                        VoltTechToolMaterials.DALEKANIUM,
                        properties.shovel(VoltTechToolMaterials.DALEKANIUM, -4.f, -2.4f),
                        -4.f,
                        -2.4f
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_SWORD = ITEMS.registerItem(
                "dalekanium_sword",
                properties -> new ChargeableItem(
                        properties.sword(VoltTechToolMaterials.DALEKANIUM, 3.f, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_HOE = ITEMS.registerItem(
                "dalekanium_hoe",
                properties -> new ChargeableHoeItem(
                        VoltTechToolMaterials.DALEKANIUM,
                        -4.f,
                        -2.4f,
                        properties.hoe(VoltTechToolMaterials.DALEKANIUM, -3.f, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );

        DALEKANIUM_HELMET = ITEMS.registerItem(
                "dalekanium_helmet",
                properties -> new ChargeableItem(
                        properties.humanoidArmor(VoltTechArmorMaterials.DALEKANIUM_ARMOR_MATERIAL, ArmorType.HELMET)
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_CHESTPLATE = ITEMS.registerItem(
                "dalekanium_chestplate",
                properties -> new ChargeableItem(
                        properties.humanoidArmor(VoltTechArmorMaterials.DALEKANIUM_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_LEGGINGS = ITEMS.registerItem(
                "dalekanium_leggings",
                properties -> new ChargeableItem(
                        properties.humanoidArmor(VoltTechArmorMaterials.DALEKANIUM_ARMOR_MATERIAL, ArmorType.LEGGINGS)
                ),
                new Item.Properties().stacksTo(1)
        );
        DALEKANIUM_BOOTS = ITEMS.registerItem(
                "dalekanium_boots",
                properties -> new ChargeableItem(
                        properties.humanoidArmor(VoltTechArmorMaterials.DALEKANIUM_ARMOR_MATERIAL, ArmorType.BOOTS)
                ),
                new Item.Properties().stacksTo(1)
        );
        HELLFORGED_HELMET = ITEMS.registerItem(
                "hellforged_helmet",
                properties -> new Item(properties.humanoidArmor(VoltTechArmorMaterials.HELLFORGED_ARMOR_MATERIAL, ArmorType.HELMET)),
                new Item.Properties().stacksTo(1).fireResistant()
        );
        HELLFORGED_CHESTPLATE = ITEMS.registerItem(
                "hellforged_chestplate",
                properties -> new Item(properties.humanoidArmor(VoltTechArmorMaterials.HELLFORGED_ARMOR_MATERIAL, ArmorType.CHESTPLATE)),
                new Item.Properties().stacksTo(1).fireResistant()
        );
        HELLFORGED_LEGGINGS = ITEMS.registerItem(
                "hellforged_leggings",
                properties -> new Item(properties.humanoidArmor(VoltTechArmorMaterials.HELLFORGED_ARMOR_MATERIAL, ArmorType.LEGGINGS)),
                new Item.Properties().stacksTo(1).fireResistant()
        );
        HELLFORGED_BOOTS = ITEMS.registerItem(
                "hellforged_boots",
                properties -> new Item(properties.humanoidArmor(VoltTechArmorMaterials.HELLFORGED_ARMOR_MATERIAL, ArmorType.BOOTS)),
                new Item.Properties().stacksTo(1).fireResistant()
        );
        VELTRIUM_PICKAXE = ITEMS.registerItem(
                "veltrium_pickaxe",
                properties -> new Item(
                        properties.pickaxe(VoltTechToolMaterials.VELTRIUM, 2, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_AXE = ITEMS.registerItem(
                "veltrium_axe",
                properties -> new AxeItem(
                        VoltTechToolMaterials.VELTRIUM,
                        7.f,
                        -2.4f,
                        properties.axe(VoltTechToolMaterials.VELTRIUM, 7, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_SHOVEL = ITEMS.registerItem(
                "veltrium_shovel",
                properties -> new ShovelItem(
                        VoltTechToolMaterials.VELTRIUM,
                        -10.f,
                        -2.4f,
                        properties.shovel(VoltTechToolMaterials.VELTRIUM, -10.f, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_SWORD = ITEMS.registerItem(
                "veltrium_sword",
                properties -> new Item(
                        properties.sword(VoltTechToolMaterials.VELTRIUM, 3, -2.4F)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_HOE = ITEMS.registerItem(
                "veltrium_hoe",
                properties -> new HoeItem(
                        VoltTechToolMaterials.VELTRIUM,
                        -10.f,
                        -2.4f,
                        properties.hoe(VoltTechToolMaterials.VELTRIUM, -10.f, -2.4F)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_HELMET = ITEMS.registerItem(
                "veltrium_helmet",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.VELTRIUM_ARMOR_MATERIAL, ArmorType.HELMET)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_CHESTPLATE = ITEMS.registerItem(
                "veltrium_chestplate",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.VELTRIUM_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_LEGGINGS = ITEMS.registerItem(
                "veltrium_leggings",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.VELTRIUM_ARMOR_MATERIAL, ArmorType.LEGGINGS)
                ),
                new Item.Properties().stacksTo(1)
        );
        VELTRIUM_BOOTS = ITEMS.registerItem(
                "veltrium_boots",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.VELTRIUM_ARMOR_MATERIAL, ArmorType.BOOTS)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_PICKAXE = ITEMS.registerItem(
                "trexalite_pickaxe",
                properties -> new Item(
                        properties.pickaxe(VoltTechToolMaterials.TREXALITE, 2, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_AXE = ITEMS.registerItem(
                "trexalite_axe",
                properties -> new AxeItem(
                        VoltTechToolMaterials.TREXALITE,
                        7.f,
                        -2.4f,
                        properties.axe(VoltTechToolMaterials.TREXALITE, 7, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_SHOVEL = ITEMS.registerItem(
                "trexalite_shovel",
                properties -> new ShovelItem(
                        VoltTechToolMaterials.TREXALITE,
                        -5.f,
                        -2.4f,
                        properties.shovel(VoltTechToolMaterials.TREXALITE, -5.f, -2.4f)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_SWORD = ITEMS.registerItem(
                "trexalite_sword",
                properties -> new Item(
                        properties.sword(VoltTechToolMaterials.TREXALITE, 3, -2.4F)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_HOE = ITEMS.registerItem(
                "trexalite_hoe",
                properties -> new HoeItem(
                        VoltTechToolMaterials.TREXALITE,
                        -5.f,
                        -2.4F,
                        properties.hoe(VoltTechToolMaterials.TREXALITE, -5.f, -2.4F)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_HELMET = ITEMS.registerItem(
                "trexalite_helmet",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.TREXALITE_ARMOR_MATERIAL, ArmorType.HELMET)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_CHESTPLATE = ITEMS.registerItem(
                "trexalite_chestplate",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.TREXALITE_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_LEGGINGS = ITEMS.registerItem(
                "trexalite_leggings",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.TREXALITE_ARMOR_MATERIAL, ArmorType.LEGGINGS)
                ),
                new Item.Properties().stacksTo(1)
        );
        TREXALITE_BOOTS = ITEMS.registerItem(
                "trexalite_boots",
                properties -> new Item(
                        properties.humanoidArmor(VoltTechArmorMaterials.TREXALITE_ARMOR_MATERIAL, ArmorType.BOOTS)
                ),
                new Item.Properties().stacksTo(1)
        );
        SMALL_BATTERY = ITEMS.registerItem(
                "small_battery",
                BatteryItem::new,
                new Item.Properties().stacksTo(1)
        );
        MEDIUM_BATTERY = ITEMS.registerItem(
                "medium_battery",
                BatteryItem::new,
                new Item.Properties().stacksTo(1)
        );
        LARGE_BATTERY = ITEMS.registerItem(
                "large_battery",
                BatteryItem::new,
                new Item.Properties().stacksTo(1)
        );
        MASSIVE_BATTERY = ITEMS.registerItem(
                "massive_battery",
                BatteryItem::new,
                new Item.Properties().stacksTo(1)
        );
        CREATIVE_BATTERY = ITEMS.registerItem(
                "creative_battery",
                properties -> new BatteryItem(properties, true),
                new Item.Properties().stacksTo(1)
        );
        ATOMIC_BATTERY = ITEMS.registerItem(
                "atomic_battery",
                properties -> new BatteryItem(
                        properties,
                        true
                ),
                new Item.Properties().stacksTo(1)
        );
        ARGENT_PLASMA_BUCKET = ITEMS.registerItem(
                "argent_plasma_bucket",
                properties -> new BucketItem(FluidRegistry.ARGENT_PLASMA.get(), properties),
                new Item.Properties()
        );

        SMALL_ENERGY_CUBE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "small_energy_cube",
                BlockRegistry.SMALL_ENERGY_CUBE);
        MEDIUM_ENERGY_CUBE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "medium_energy_cube",
                BlockRegistry.MEDIUM_ENERGY_CUBE);
        LARGE_ENERGY_CUBE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "large_energy_cube",
                BlockRegistry.LARGE_ENERGY_CUBE);
        MASSIVE_ENERGY_CUBE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "massive_energy_cube",
                BlockRegistry.MASSIVE_ENERGY_CUBE);
        CREATIVE_ENERGY_CUBE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "creative_energy_cube",
                BlockRegistry.CREATIVE_ENERGY_CUBE);
        COPPER_ENERGY_CABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "copper_energy_cable",
                BlockRegistry.COPPER_ENERGY_CABLE);
        IRON_ENERGY_CABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "iron_energy_cable",
                BlockRegistry.IRON_ENERGY_CABLE);
        GOLD_ENERGY_CABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "gold_energy_cable",
                BlockRegistry.GOLD_ENERGY_CABLE);
        DIAMOND_ENERGY_CABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "diamond_energy_cable",
                BlockRegistry.DIAMOND_ENERGY_CABLE);
        EMERALD_ENERGY_CABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "emerald_energy_cable",
                BlockRegistry.EMERALD_ENERGY_CABLE);
        NETHERITE_ENERGY_CABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "netherite_energy_cable",
                BlockRegistry.NETHERITE_ENERGY_CABLE);
        COPPER_FLUID_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "copper_fluid_pipe",
                BlockRegistry.COPPER_FLUID_PIPE);
        IRON_FLUID_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "iron_fluid_pipe",
                BlockRegistry.IRON_FLUID_PIPE);
        GOLD_FLUID_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "gold_fluid_pipe",
                BlockRegistry.GOLD_FLUID_PIPE);
        DIAMOND_FLUID_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "diamond_fluid_pipe",
                BlockRegistry.DIAMOND_FLUID_PIPE);
        EMERALD_FLUID_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "emerald_fluid_pipe",
                BlockRegistry.EMERALD_FLUID_PIPE);
        NETHERITE_FLUID_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "netherite_fluid_pipe",
                BlockRegistry.NETHERITE_FLUID_PIPE);
        ITEM_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "item_pipe",
                BlockRegistry.ITEM_PIPE);
        SMALL_FLUID_TANK_BLOCK_ITEM = ITEMS.register(
                "small_fluid_tank",
                resourceLocation -> new FluidTankBlockItem(BlockRegistry.SMALL_FLUID_TANK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, resourceLocation)).useBlockDescriptionPrefix().rarity(Rarity.COMMON))
        );
        MEDIUM_FLUID_TANK_BLOCK_ITEM = ITEMS.register(
                "medium_fluid_tank",
                resourceLocation -> new FluidTankBlockItem(BlockRegistry.MEDIUM_FLUID_TANK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, resourceLocation)).useBlockDescriptionPrefix().rarity(Rarity.COMMON))
        );
        LARGE_FLUID_TANK_BLOCK_ITEM = ITEMS.register(
                "large_fluid_tank",
                resourceLocation -> new FluidTankBlockItem(BlockRegistry.LARGE_FLUID_TANK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, resourceLocation)).useBlockDescriptionPrefix().rarity(Rarity.COMMON))
        );
        MASSIVE_FLUID_TANK_BLOCK_ITEM = ITEMS.register(
                "massive_fluid_tank",
                resourceLocation -> new FluidTankBlockItem(BlockRegistry.MASSIVE_FLUID_TANK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, resourceLocation)).useBlockDescriptionPrefix().rarity(Rarity.COMMON))
        );
        CREATIVE_FLUID_TANK_BLOCK_ITEM = ITEMS.register(
                "creative_fluid_tank",
                resourceLocation -> new FluidTankBlockItem(BlockRegistry.CREATIVE_FLUID_TANK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, resourceLocation)).useBlockDescriptionPrefix().rarity(Rarity.COMMON))
        );
        WIRELESS_PLAYER_CHARGER_BLOCK_ITEM = ITEMS.register(
                "wireless_player_charger",
                resourceLocation -> new WirelessPlayerChargerBlockItem(BlockRegistry.WIRELESS_PLAYER_CHARGER.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, resourceLocation)).useBlockDescriptionPrefix())
        );
        FOOD_MASHER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "food_masher",
                BlockRegistry.FOOD_MASHER);
        HARVESTER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "harvester",
                BlockRegistry.HARVESTER);
        BLOCK_BREAKER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "block_breaker",
                BlockRegistry.BLOCK_BREAKER);
        BLOCK_PLACER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "block_placer",
                BlockRegistry.BLOCK_PLACER);
        SOLAR_GENERATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "solar_generator",
                BlockRegistry.SOLAR_GENERATOR);
        COMBUSTION_GENERATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "combustion_generator",
                BlockRegistry.COMBUSTION_GENERATOR);
        HEAT_GENERATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "heat_generator",
                BlockRegistry.HEAT_GENERATOR);
        MINI_REACTOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "mini_reactor",
                BlockRegistry.MINI_REACTOR);
        TEMPORAL_ACCELERATOR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "temporal_accelerator",
                BlockRegistry.TEMPORAL_ACCELERATOR);
        WIRELESS_ENERGY_TRANSMITTER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "wireless_energy_transmitter",
                BlockRegistry.WIRELESS_ENERGY_TRANSMITTER);
        POWERED_FURNACE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "powered_furnace",
                BlockRegistry.POWERED_FURNACE);
        CRUSHER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "crusher",
                BlockRegistry.CRUSHER);
        CHUNK_LOADER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "chunk_loader",
                BlockRegistry.CHUNK_LOADER);
        SPATIAL_CRATE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "spatial_crate",
                BlockRegistry.SPATIAL_CRATE);
        UPGRADE_TABLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "upgrade_table",
                BlockRegistry.UPGRADE_TABLE);
        URANIUM_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "uranium_ore",
                BlockRegistry.URANIUM_ORE);
        DEEPSLATE_URANIUM_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "deepslate_uranium_ore",
                BlockRegistry.DEEPSLATE_URANIUM_ORE);
        RAW_URANIUM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "raw_uranium_block",
                BlockRegistry.RAW_URANIUM_BLOCK);
        URANIUM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "uranium_block",
                BlockRegistry.URANIUM_BLOCK);
        DALEKANIUM_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "dalekanium_ore",
                BlockRegistry.DALEKANIUM_ORE);
        RAW_DALEKANIUM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "raw_dalekanium_block",
                BlockRegistry.RAW_DALEKANIUM_BLOCK);
        DALEKANIUM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "dalekanium_block",
                BlockRegistry.DALEKANIUM_BLOCK);
        HELLISH_ROCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "hellish_rock",
                BlockRegistry.HELLISH_ROCK);
        VELTRIUM_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "veltrium_ore",
                BlockRegistry.VELTRIUM_ORE);
        VELTRIUM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "veltrium_block",
                BlockRegistry.VELTRIUM_BLOCK);
        RAW_VELTRIUM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "raw_veltrium_block",
                BlockRegistry.RAW_VELTRIUM_BLOCK);
        DEEPSLATE_TREXALITE_ORE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "deepslate_trexalite_ore",
                BlockRegistry.DEEPSLATE_TREXALITE_ORE);
        TREXALITE_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "trexalite_block",
                BlockRegistry.TREXALITE_BLOCK);
        RAW_TREXALITE_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                "raw_trexalite_block",
                BlockRegistry.RAW_TREXALITE_BLOCK);

        ITEMS.register(modEventBus);
    }

    private static DeferredItem<UpgradeCardItem> registerUpgradeCard(String name, Supplier<DataComponentType<Integer>> dataComponent, int maxCount, EquipmentSlotGroup equipmentSlotGroup, Function<ItemStack, Boolean> itemCheckFunction) {
        return ITEMS.registerItem(
                name,
                properties -> new UpgradeCardItem(properties, dataComponent, maxCount, equipmentSlotGroup, itemCheckFunction),
                new Item.Properties()
        );
    }
}
