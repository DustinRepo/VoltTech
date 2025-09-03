package me.bricked.volttech.datagen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class VoltTechLanguageProvider extends LanguageProvider {
    public VoltTechLanguageProvider(PackOutput output) {
        super(output, VoltTech.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        String messagePrefix = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.AQUA + "VoltTech" + ChatFormatting.DARK_GRAY + "]" + ChatFormatting.RESET;
        this.addTooltip("torch_dispenser.infinite_torches", ChatFormatting.ITALIC + "Infinite Torches");
        this.addTooltip("marked_location", "Marked Location:");
        this.addTooltip("contained_fluid", "%1$s" + ChatFormatting.GRAY + ": %2$s");
        this.addTooltip("fluid_capacity", ChatFormatting.GRAY + "Capacity:" + ChatFormatting.RESET + " %1$s");
        this.addTooltip("infinite_contained_fluid", "%1$s" + ChatFormatting.GRAY + ": Infinite");
        this.addTooltip("infinite_fluid_capacity", ChatFormatting.GRAY + "Capacity: " + ChatFormatting.RESET + "Infinite");
        this.addTooltip("bucket_mode_on", ChatFormatting.GRAY +"Bucket mode: " + ChatFormatting.GREEN + "On");
        this.addTooltip("bucket_mode_off", ChatFormatting.GRAY +"Bucket mode: " + ChatFormatting.RED + "Off");
        this.addMessage("torch_dispenser.empty", "Your Torch Dispenser is now empty.");
        this.addMessage("torch_dispenser.stored_torches", ChatFormatting.ITALIC + "Stored torches: %1$s");
        this.addMessage("torch_dispenser.stored_torches_added", "Stored %1$s torches");
        this.addMessage("cable_endpoints", "Passing energy to %1$s endpoint(s)");
        this.addMessage("pipe_endpoints", "Passing fluid to %1$s endpoint(s)");
        this.addMessage("item_pipe_endpoints", "Passing items to %1$s endpoint(s)");
        this.addMessage("now_extracting", "Pipe set to: " + ChatFormatting.AQUA + "Extract");
        this.addMessage("now_normal", "Pipe set to: " + ChatFormatting.GREEN + "Normal");
        this.addMessage("now_blocked", "Pipe set to: " + ChatFormatting.RED + "Blocked");
        this.addMessage("player_uuid_message", "Player's UUID: " + ChatFormatting.RED + "%1$s");
        this.addMessage("linked_uuid_message", "Set UUID to: " + ChatFormatting.RED + "%1$s");
        this.addMessage("no_death_location", "You don't have a previous death location.");
        this.addMessage("jetpack_enabled", "Jetpack Enabled");
        this.addMessage("jetpack_disabled", "Jetpack Disabled");
        this.addMessage("mismatch_upgradeables", messagePrefix + ChatFormatting.GRAY + " Mismatched upgradeable items between server and client. This can cause issues if the item uses energy.\n" + ChatFormatting.GRAY + "You can fix this by using the same config as the server\n" + ChatFormatting.GRAY + "Missing items:\n" + ChatFormatting.GRAY + "%1$s");
        this.addGUI("empty", "Empty");
        this.addGUI("reactor_running", "Reactor running");
        this.addGUI("reactor_idle", "Reactor Idle");
        this.addGUI("generator_status", "Status:");
        this.addGUI("generator_full", "Energy filled");
        this.addGUI("generating_pertick", "Generating %1$s/t");
        this.addGUI("coal_generator_left_to_burn", "%1$s left to burn");
        this.addGUI("coal_generator_no_fuel", "No fuel");
        this.addGUI("heat_generator_no_heat", "No heated fluid");
        this.addGUI("solar_no_sky", "No sky access");
        this.addGUI("solar_no_sun", "Sun not visible");
        this.addGUI("max_in", "Max In: %1$s");
        this.addGUI("max_out", "Max Out: %1$s");
        this.addGUI("ticks_left", "Ticks Left: %1$s");
        this.addGUI("infinite_energy", "FE: Infinite");
        this.addGUI("energy_out_of", "%1$s / %2$s");
        this.addGUI("place_location_cards", "Insert marked Location Cards");
        this.addGUI("offsets", "Offsets");
        this.addGUI("size", "Size");
        this.addGUI("show_box_true", "Show Box: " + ChatFormatting.GREEN + "true");
        this.addGUI("show_box_false", "Show Box: " + ChatFormatting.RED + "false");
        this.addGUI("chance_percent", "Chance: ");
        this.addGUI("portal_gun_title", "Portal Gun");
        this.addGUI("portal_gun_seed", "Dimension Seed:");
        this.addGUI("portal_gun_set_home", "Home");
        this.addGUI("portal_gun_coordinates", "Coordinates: (Optional)");
        this.addGUI("portal_gun_coordinates_error", "There was an error with your coordinates. Please ensure they are valid.");
        this.addGUI("portal_gun_slot_error", "Error finding slot for portal gun. Please take a screenshot of your inventory and upload it to a Github Issue.");
        this.add("itemGroup.volttech", "VoltTech");
        this.add("itemGroup.volttech_tank", "Creative Tanks");
        this.addString("phase", "Phase");
        this.addString("evasion", "Evasion");
        this.addString("jetpack_flight", "Jetpack Flight");
        this.addString("jetpack_speed", "Jetpack Speed");
        this.addString("key.jetpack_toggle", "Toggle Jetpack");
        this.addString("key.jetpack_ascend", "Jetpack Ascend");
        this.addString("key.jetpack_descend", "Jetpack Descend");
        this.addString("entity.multiverse_portal_name", "Seed: %1$s");
        this.addString("entity.multiverse_portal_home", "Default Overworld");
        this.addString("category.default", "VoltTech");
        this.addDisconnect("invalid_jetpack_slot", "Invalid data in toggle jetpack payload.");
        this.addDisconnect("invalid_portal_gun_slot", "Invalid data in Portal Gun payload (slot).");
        this.addDisconnect("invalid_portal_gun_seed", "Invalid data in Portal Gun payload (seed).");
        this.addTooltip("show_description", "Hold §aLSHIFT§r to show description");
        this.addTooltip("show_upgrades", "Hold §aLSHIFT§r to show upgrades");
        this.addTooltip("item_upgrades", "Upgrades: ");
        this.addTooltip("player_uuid", "UUID: ");
        this.addTooltip("sharing_energy", ChatFormatting.GREEN + "Sharing energy with inventory");
        this.addTooltip("not_sharing_energy", ChatFormatting.RED + "Not sharing energy with inventory");
        this.addTooltip("portal_gun_seed", "Dimension: " + ChatFormatting.GOLD + "%1$s");
        this.addTooltip("charge_speed", "Charge: %1$s/t");
        this.addTooltip("energy_usage", "Use: %1$s");

        this.addTooltip(ItemRegistry.HEALTH_UPGRADE_CARD.get(), "Adds 1 Heart per card applied");
        this.addTooltip(ItemRegistry.JETPACK_UPGRADE_CARD.get(), "Allows Jetpack flight when applied. Number of upgrades increases speed.");
        this.addTooltip(ItemRegistry.FEATHER_FALLING_UPGRADE_CARD.get(), "Removes 10% of fall damage per card applied");
        this.addTooltip(ItemRegistry.JUMP_BOOST_UPGRADE_CARD.get(), "Increases jump height");
        this.addTooltip(ItemRegistry.SPEED_UPGRADE_CARD.get(), "Increases move speed");
        this.addTooltip(ItemRegistry.NIGHT_VISION_UPGRADE_CARD.get(), "Applies Night Vision");
        this.addTooltip(ItemRegistry.ENERGY_UPGRADE_CARD.get(), "Converts items to use energy. More cards increases capacity and lowers charge time");
        this.addTooltip(ItemRegistry.WATER_BREATHING_UPGRADE_CARD.get(), "Allows breathing underwater when applied");
        this.addTooltip(ItemRegistry.AUTO_FEED_UPGRADE_CARD.get(), "Automatically eats Edible Slop in your inventory when hungry");
        this.addTooltip(ItemRegistry.FIRE_RESIST_UPGRADE_CARD.get(), "Automatically removes fire from the player");
        this.addTooltip(ItemRegistry.WITHER_RESIST_UPGRADE_CARD.get(), "Automatically removes Wither effect from the player");
        this.addTooltip(ItemRegistry.POISON_RESIST_UPGRADE_CARD.get(), "Automatically removes Poison effect from the player");
        this.addTooltip(ItemRegistry.MAGNET_UPGRADE_CARD.get(), "Picks up items from farther away when applied. More upgrades increases range");
        this.addTooltip(ItemRegistry.PHASE_UPGRADE_CARD.get(), "Allows walking through walls while applied");
        this.addTooltip(ItemRegistry.REGENERATION_UPGRADE_CARD.get(), "Adds 1 level of Regeneration when applied");
        this.addTooltip(ItemRegistry.STEP_HEIGHT_UPGRADE_CARD.get(), "Adds 1 block to step height when applied");
        this.addTooltip(ItemRegistry.REACH_UPGRADE_CARD.get(), "Adds 1 block reach for each card applied");
        this.addTooltip(ItemRegistry.MINING_SPEED_UPGRADE_CARD.get(), "Increases mining speed when applied");
        this.addTooltip(ItemRegistry.MINE_AREA_UPGRADE_CARD.get(), "Increases mining area to a 3x3 space");
        this.addTooltip(ItemRegistry.EVASION_UPGRADE_CARD.get(), "Gives a 10% chance to dodge attacks per card applied");
        this.addTooltip(ItemRegistry.SMELTER_UPGRADE_CARD.get(), "Automatically smelts item drops when applied");
        this.addTooltip(ItemRegistry.DAMAGE_UPGRADE_CARD.get(), "Adds attack damage when applied");
        this.addTooltip(ItemRegistry.HOMING_DRIVE.get(), "Teleports back to spawn location");
        this.addTooltip(ItemRegistry.GRAVE_TRANSPORTER.get(), "Teleports to last death location");
        this.addTooltip(ItemRegistry.ATOMIC_BATTERY.get(), "Infinite energy with low output");
        this.addTooltip(ItemRegistry.CHUNK_LOADER_BLOCK_ITEM.get(), "Force loads chunks in a 3x3 area");

        this.addBlock(BlockRegistry.ITEM_PIPE, "Item Pipe");
        this.addBlock(BlockRegistry.COPPER_ENERGY_CABLE, "Copper Energy Cable");
        this.addBlock(BlockRegistry.IRON_ENERGY_CABLE, "Iron Energy Cable");
        this.addBlock(BlockRegistry.GOLD_ENERGY_CABLE, "Gold Energy Cable");
        this.addBlock(BlockRegistry.DIAMOND_ENERGY_CABLE, "Diamond Energy Cable");
        this.addBlock(BlockRegistry.EMERALD_ENERGY_CABLE, "Emerald Energy Cable");
        this.addBlock(BlockRegistry.NETHERITE_ENERGY_CABLE, "Netherite Energy Cable");
        this.addBlock(BlockRegistry.COPPER_FLUID_PIPE, "Copper Fluid Pipe");
        this.addBlock(BlockRegistry.IRON_FLUID_PIPE, "Iron Fluid Pipe");
        this.addBlock(BlockRegistry.GOLD_FLUID_PIPE, "Gold Fluid Pipe");
        this.addBlock(BlockRegistry.DIAMOND_FLUID_PIPE, "Diamond Fluid Pipe");
        this.addBlock(BlockRegistry.EMERALD_FLUID_PIPE, "Emerald Fluid Pipe");
        this.addBlock(BlockRegistry.NETHERITE_FLUID_PIPE, "Netherite Fluid Pipe");
        this.addBlock(BlockRegistry.SMALL_FLUID_TANK, "Small Fluid Tank");
        this.addBlock(BlockRegistry.MEDIUM_FLUID_TANK, "Medium Fluid Tank");
        this.addBlock(BlockRegistry.LARGE_FLUID_TANK, "Large Fluid Tank");
        this.addBlock(BlockRegistry.MASSIVE_FLUID_TANK, "Massive Fluid Tank");
        this.addBlock(BlockRegistry.CREATIVE_FLUID_TANK, "Creative Fluid Tank");
        this.addBlock(BlockRegistry.HARVESTER, "Harvester");
        this.addBlock(BlockRegistry.BLOCK_BREAKER, "Block Breaker");
        this.addBlock(BlockRegistry.BLOCK_PLACER, "Block Placer");
        this.addBlock(BlockRegistry.SOLAR_GENERATOR, "Solar Generator");
        this.addBlock(BlockRegistry.COMBUSTION_GENERATOR, "Combustion Generator");
        this.addBlock(BlockRegistry.HEAT_GENERATOR, "Heat Generator");
        this.addBlock(BlockRegistry.TEMPORAL_ACCELERATOR, "Temporal Accelerator");
        this.addBlock(BlockRegistry.MINI_REACTOR, "Mini Reactor");
        this.addBlock(BlockRegistry.SPATIAL_CRATE, "Spatial Crate");
        this.addBlock(BlockRegistry.UPGRADE_TABLE, "Upgrade Table");
        this.addBlock(BlockRegistry.POWERED_FURNACE, "Powered Furnace");
        this.addBlock(BlockRegistry.CRUSHER, "Crusher");
        this.addBlock(BlockRegistry.FOOD_MASHER, "Food Masher");
        this.addBlock(BlockRegistry.CHUNK_LOADER, "Chunk Loader");
        this.addItem(ItemRegistry.SOLAR_PANEL, "Solar Panel");
        this.addBlock(BlockRegistry.WIRELESS_PLAYER_CHARGER, "Wireless Player Charger");
        this.addBlock(BlockRegistry.WIRELESS_ENERGY_TRANSMITTER, "Wireless Energy Transmitter");
        this.addBlock(BlockRegistry.SMALL_ENERGY_CUBE, "Small Energy Cube");
        this.addBlock(BlockRegistry.MEDIUM_ENERGY_CUBE, "Medium Energy Cube");
        this.addBlock(BlockRegistry.LARGE_ENERGY_CUBE, "Large Energy Cube");
        this.addBlock(BlockRegistry.MASSIVE_ENERGY_CUBE, "Massive Energy Cube");
        this.addBlock(BlockRegistry.CREATIVE_ENERGY_CUBE, "Creative Energy Cube");
        this.addBlock(BlockRegistry.URANIUM_ORE, "Uranium Ore");
        this.addBlock(BlockRegistry.DEEPSLATE_URANIUM_ORE, "Deepslate Uranium Ore");
        this.addBlock(BlockRegistry.DEEPSLATE_TREXALITE_ORE, "Deepslate Trexalite Ore");
        this.addBlock(BlockRegistry.URANIUM_BLOCK, "Block of Uranium");
        this.addBlock(BlockRegistry.RAW_URANIUM_BLOCK, "Block of Raw Uranium");
        this.addBlock(BlockRegistry.DALEKANIUM_ORE, "Dalekanium Ore");
        this.addBlock(BlockRegistry.DALEKANIUM_BLOCK, "Block of Dalekanium");
        this.addBlock(BlockRegistry.RAW_DALEKANIUM_BLOCK, "Block of Raw Dalekanium");
        this.addBlock(BlockRegistry.HELLISH_ROCK, "Hellish Rock");
        this.addBlock(BlockRegistry.ARGENT_PLASMA, "Argent Plasma");
        this.addBlock(BlockRegistry.VELTRIUM_ORE, "Veltrium Ore");
        this.addBlock(BlockRegistry.VELTRIUM_BLOCK, "Block of Veltrium");
        this.addBlock(BlockRegistry.RAW_VELTRIUM_BLOCK, "Block of Raw Veltrium");
        this.addBlock(BlockRegistry.TREXALITE_BLOCK, "Block of Trexalite");
        this.addBlock(BlockRegistry.RAW_TREXALITE_BLOCK, "Block of Raw Trexalite");
        this.addItem(ItemRegistry.ARGENT_PLASMA_BUCKET, "Bucket of Argent Plasma");
        this.addItem(ItemRegistry.HELLFORGED_SHARD, "HellForged Shard");
        this.addItem(ItemRegistry.HELLFORGED_INGOT, "HellForged Ingot");
        this.addItem(ItemRegistry.RAW_DALEKANIUM, "Raw Dalekanium");
        this.addItem(ItemRegistry.DALEKANIUM_INGOT, "Dalekanium Ingot");
        this.addItem(ItemRegistry.RAW_URANIUM, "Raw Uranium");
        this.addItem(ItemRegistry.URANIUM_INGOT, "Uranium Ingot");
        this.addItem(ItemRegistry.HELLFORGED_HELMET, "HellForged Helmet");
        this.addItem(ItemRegistry.HELLFORGED_CHESTPLATE, "HellForged Chestplate");
        this.addItem(ItemRegistry.HELLFORGED_LEGGINGS, "HellForged Leggings");
        this.addItem(ItemRegistry.HELLFORGED_BOOTS, "HellForged Boots");
        this.addItem(ItemRegistry.DALEKANIUM_HELMET, "Dalekanium Helmet");
        this.addItem(ItemRegistry.DALEKANIUM_CHESTPLATE, "Dalekanium Chestplate");
        this.addItem(ItemRegistry.DALEKANIUM_LEGGINGS, "Dalekanium Leggings");
        this.addItem(ItemRegistry.DALEKANIUM_BOOTS, "Dalekanium Boots");
        this.addItem(ItemRegistry.DEMONIC_CRUCIBLE, "Demonic Crucible");
        this.addItem(ItemRegistry.DALEKANIUM_PICKAXE, "Dalekanium Pickaxe");
        this.addItem(ItemRegistry.DALEKANIUM_AXE, "Dalekanium Axe");
        this.addItem(ItemRegistry.DALEKANIUM_SHOVEL, "Dalekanium Shovel");
        this.addItem(ItemRegistry.DALEKANIUM_HOE, "Dalekanium Hoe");
        this.addItem(ItemRegistry.DALEKANIUM_SWORD, "Dalekanium Sword");
        this.addItem(ItemRegistry.VELTRIUM_HELMET, "Veltrium Helmet");
        this.addItem(ItemRegistry.VELTRIUM_CHESTPLATE, "Veltrium Chestplate");
        this.addItem(ItemRegistry.VELTRIUM_LEGGINGS, "Veltrium Leggings");
        this.addItem(ItemRegistry.VELTRIUM_BOOTS, "Veltrium Boots");
        this.addItem(ItemRegistry.VELTRIUM_PICKAXE, "Veltrium Pickaxe");
        this.addItem(ItemRegistry.VELTRIUM_AXE, "Veltrium Axe");
        this.addItem(ItemRegistry.VELTRIUM_SHOVEL, "Veltrium Shovel");
        this.addItem(ItemRegistry.VELTRIUM_HOE, "Veltrium Hoe");
        this.addItem(ItemRegistry.VELTRIUM_SWORD, "Veltrium Sword");
        this.addItem(ItemRegistry.VELTRIUM, "Veltrium");
        this.addItem(ItemRegistry.RAW_VELTRIUM, "Raw Veltrium");
        this.addItem(ItemRegistry.TREXALITE, "Trexalite");
        this.addItem(ItemRegistry.RAW_TREXALITE, "Raw Trexalite");
        this.addItem(ItemRegistry.TREXALITE_HELMET, "Trexalite Helmet");
        this.addItem(ItemRegistry.TREXALITE_CHESTPLATE, "Trexalite Chestplate");
        this.addItem(ItemRegistry.TREXALITE_LEGGINGS, "Trexalite Leggings");
        this.addItem(ItemRegistry.TREXALITE_BOOTS, "Trexalite Boots");
        this.addItem(ItemRegistry.TREXALITE_PICKAXE, "Trexalite Pickaxe");
        this.addItem(ItemRegistry.TREXALITE_AXE, "Trexalite Axe");
        this.addItem(ItemRegistry.TREXALITE_SHOVEL, "Trexalite Shovel");
        this.addItem(ItemRegistry.TREXALITE_HOE, "Trexalite Hoe");
        this.addItem(ItemRegistry.TREXALITE_SWORD, "Trexalite Sword");
        this.addItem(ItemRegistry.SMALL_BATTERY, "Small Battery");
        this.addItem(ItemRegistry.MEDIUM_BATTERY, "Medium Battery");
        this.addItem(ItemRegistry.LARGE_BATTERY, "Large Battery");
        this.addItem(ItemRegistry.MASSIVE_BATTERY, "Massive Battery");
        this.addItem(ItemRegistry.CREATIVE_BATTERY, "Creative Battery");
        this.addItem(ItemRegistry.ATOMIC_BATTERY, "Atomic Battery");
        this.addItem(ItemRegistry.TORCH_DISPENSER, "Torch Dispenser");
        this.addItem(ItemRegistry.INFINITE_TORCH_DISPENSER, "Infinite Torch Dispenser");
        this.addItem(ItemRegistry.MECHANIZED_ENDER_PEARL, "Mechanized Ender Pearl");
        this.addItem(ItemRegistry.HOMING_DRIVE, "Homing Drive");
        this.addItem(ItemRegistry.GRAVE_TRANSPORTER, "Grave Transporter");
        this.addItem(ItemRegistry.JETPACK, "Jetpack");
        this.addItem(ItemRegistry.PORTAL_GUN, "Portal Gun");
        this.addItem(ItemRegistry.BOOSTER, "Booster");
        this.addItem(ItemRegistry.UTILITY_WRENCH, "Utility Wrench");
        this.addItem(ItemRegistry.LOCATION_CARD_ITEM, "Location Card");
        this.addItem(ItemRegistry.ENERGY_UPGRADE_CARD, "Energy Upgrade Card");
        this.addItem(ItemRegistry.JUMP_BOOST_UPGRADE_CARD, "Jump Boost Upgrade Card");
        this.addItem(ItemRegistry.JETPACK_UPGRADE_CARD, "Jetpack Upgrade Card");
        this.addItem(ItemRegistry.FEATHER_FALLING_UPGRADE_CARD, "Feather Falling Upgrade Card");
        this.addItem(ItemRegistry.HEALTH_UPGRADE_CARD, "Health Upgrade Card");
        this.addItem(ItemRegistry.SPEED_UPGRADE_CARD, "Speed Upgrade Card");
        this.addItem(ItemRegistry.NIGHT_VISION_UPGRADE_CARD, "Night Vision Upgrade Card");
        this.addItem(ItemRegistry.WATER_BREATHING_UPGRADE_CARD, "Water Breathing Upgrade Card");
        this.addItem(ItemRegistry.AUTO_FEED_UPGRADE_CARD, "Auto Feed Upgrade Card");
        this.addItem(ItemRegistry.FIRE_RESIST_UPGRADE_CARD, "Fire Resist Upgrade Card");
        this.addItem(ItemRegistry.WITHER_RESIST_UPGRADE_CARD, "Wither Resist Upgrade Card");
        this.addItem(ItemRegistry.POISON_RESIST_UPGRADE_CARD, "Poison Resist Upgrade Card");
        this.addItem(ItemRegistry.MAGNET_UPGRADE_CARD, "Magnet Upgrade Card");
        this.addItem(ItemRegistry.PHASE_UPGRADE_CARD, "Phase Upgrade Card");
        this.addItem(ItemRegistry.REGENERATION_UPGRADE_CARD, "Regeneration Upgrade Card");
        this.addItem(ItemRegistry.STEP_HEIGHT_UPGRADE_CARD, "Step Height Upgrade Card");
        this.addItem(ItemRegistry.REACH_UPGRADE_CARD, "Reach Upgrade Card");
        this.addItem(ItemRegistry.MINING_SPEED_UPGRADE_CARD, "Mining Speed Upgrade Card");
        this.addItem(ItemRegistry.MINE_AREA_UPGRADE_CARD, "Mine Area Upgrade Card");
        this.addItem(ItemRegistry.EVASION_UPGRADE_CARD, "Evasion Upgrade Card");
        this.addItem(ItemRegistry.SMELTER_UPGRADE_CARD, "Smelter Upgrade Card");
        this.addItem(ItemRegistry.DAMAGE_UPGRADE_CARD, "Damage Upgrade Card");
        this.addItem(ItemRegistry.ELECTRIC_COMPONENT, "Electric Component");
        this.addItem(ItemRegistry.EDIBLE_SLOP, "Edible Slop");
        this.addItem(ItemRegistry.CRUSHED_COPPER, "Crushed Copper");
        this.addItem(ItemRegistry.CRUSHED_GOLD, "Crushed Gold");
        this.addItem(ItemRegistry.CRUSHED_IRON, "Crushed Iron");
        this.addItem(ItemRegistry.CRUSHED_DIAMOND, "Crushed Diamond");
        this.addItem(ItemRegistry.CRUSHED_EMERALD, "Crushed Emerald");
        this.addItem(ItemRegistry.CRUSHED_REDSTONE, "Crushed Redstone");
        this.addItem(ItemRegistry.CRUSHED_LAPIS, "Crushed Lapis");
        this.addItem(ItemRegistry.CRUSHED_HELLFORGED, "Crushed Hellforged");
        this.addItem(ItemRegistry.CRUSHED_DALEKANIUM, "Crushed Dalekanium");
        this.addItem(ItemRegistry.CRUSHED_VELTRIUM, "Crushed Veltrium");
        this.addItem(ItemRegistry.CRUSHED_URANIUM, "Crushed Uranium");
        this.addItem(ItemRegistry.CRUSHED_TREXALITE, "Crushed Trexalite");
    }

    private void addString(String key, String value) {
        this.add(VoltTech.MODID + "." + key, value);
    }

    private void addTooltip(String key, String value) {
        this.addString("tooltip." + key, value);
    }

    private void addDisconnect(String key, String value) {
        this.addString("disconnect." + key, value);
    }

    private void addTooltip(Item key, String value) {
        this.add("tooltip." + key.getDescriptionId(), value);
    }

    private void addMessage(String key, String value) {
        this.addString("message." + key, value);
    }

    private void addGUI(String key, String value) {
        this.addString("gui." + key, value);
    }
}
