package me.bricked.volttech.datagen.tag;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class VoltTechItemTagProvider extends ItemTagsProvider {
    public VoltTechItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, VoltTech.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(Tags.Items.FOODS).add(
                ItemRegistry.EDIBLE_SLOP.get()
        );
        this.tag(ItemTags.PICKAXES).add(
                ItemRegistry.DALEKANIUM_PICKAXE.get(),
                ItemRegistry.VELTRIUM_PICKAXE.get(),
                ItemRegistry.TREXALITE_PICKAXE.get()
        );
        this.tag(ItemTags.AXES).add(
                ItemRegistry.DALEKANIUM_AXE.get(),
                ItemRegistry.VELTRIUM_AXE.get(),
                ItemRegistry.TREXALITE_AXE.get()
        );
        this.tag(ItemTags.SHOVELS).add(
                ItemRegistry.DALEKANIUM_SHOVEL.get(),
                ItemRegistry.VELTRIUM_SHOVEL.get(),
                ItemRegistry.TREXALITE_SHOVEL.get()
        );
        this.tag(ItemTags.SWORDS).add(
                ItemRegistry.DALEKANIUM_SWORD.get(),
                ItemRegistry.VELTRIUM_SWORD.get(),
                ItemRegistry.TREXALITE_SWORD.get()
        );
        this.tag(ItemTags.HOES).add(
                ItemRegistry.DALEKANIUM_HOE.get(),
                ItemRegistry.VELTRIUM_HOE.get(),
                ItemRegistry.TREXALITE_HOE.get()
        );
        this.tag(ItemTags.HEAD_ARMOR).add(
                ItemRegistry.DALEKANIUM_HELMET.get(),
                ItemRegistry.HELLFORGED_HELMET.get(),
                ItemRegistry.VELTRIUM_HELMET.get(),
                ItemRegistry.TREXALITE_HELMET.get()
        );
        this.tag(ItemTags.CHEST_ARMOR).add(
                ItemRegistry.DALEKANIUM_CHESTPLATE.get(),
                ItemRegistry.HELLFORGED_CHESTPLATE.get(),
                ItemRegistry.VELTRIUM_CHESTPLATE.get(),
                ItemRegistry.TREXALITE_CHESTPLATE.get(),
                ItemRegistry.JETPACK.get()
        );
        this.tag(ItemTags.LEG_ARMOR).add(
                ItemRegistry.DALEKANIUM_LEGGINGS.get(),
                ItemRegistry.HELLFORGED_LEGGINGS.get(),
                ItemRegistry.VELTRIUM_LEGGINGS.get(),
                ItemRegistry.TREXALITE_LEGGINGS.get()
        );
        this.tag(ItemTags.FOOT_ARMOR).add(
                ItemRegistry.DALEKANIUM_BOOTS.get(),
                ItemRegistry.HELLFORGED_BOOTS.get(),
                ItemRegistry.VELTRIUM_BOOTS.get(),
                ItemRegistry.TREXALITE_BOOTS.get()
        );
        this.tag(Tags.Items.ARMORS).add(
                ItemRegistry.DALEKANIUM_HELMET.get(),
                ItemRegistry.DALEKANIUM_CHESTPLATE.get(),
                ItemRegistry.DALEKANIUM_LEGGINGS.get(),
                ItemRegistry.DALEKANIUM_BOOTS.get(),
                ItemRegistry.HELLFORGED_HELMET.get(),
                ItemRegistry.HELLFORGED_CHESTPLATE.get(),
                ItemRegistry.HELLFORGED_LEGGINGS.get(),
                ItemRegistry.HELLFORGED_BOOTS.get(),
                ItemRegistry.VELTRIUM_HELMET.get(),
                ItemRegistry.VELTRIUM_CHESTPLATE.get(),
                ItemRegistry.VELTRIUM_LEGGINGS.get(),
                ItemRegistry.VELTRIUM_BOOTS.get(),
                ItemRegistry.TREXALITE_HELMET.get(),
                ItemRegistry.TREXALITE_CHESTPLATE.get(),
                ItemRegistry.TREXALITE_LEGGINGS.get(),
                ItemRegistry.TREXALITE_BOOTS.get(),
                ItemRegistry.JETPACK.get()
        );
        this.tag(TagRegistry.Items.ARMORS_DALEKANIUM).add(
                ItemRegistry.DALEKANIUM_HELMET.get(),
                ItemRegistry.DALEKANIUM_CHESTPLATE.get(),
                ItemRegistry.DALEKANIUM_LEGGINGS.get(),
                ItemRegistry.DALEKANIUM_BOOTS.get()
        );
        this.tag(TagRegistry.Items.ARMORS_HELLFORGED).add(
                ItemRegistry.HELLFORGED_HELMET.get(),
                ItemRegistry.HELLFORGED_CHESTPLATE.get(),
                ItemRegistry.HELLFORGED_LEGGINGS.get(),
                ItemRegistry.HELLFORGED_BOOTS.get()
        );
        this.tag(TagRegistry.Items.ARMORS_VELTRIUM).add(
                ItemRegistry.VELTRIUM_HELMET.get(),
                ItemRegistry.VELTRIUM_CHESTPLATE.get(),
                ItemRegistry.VELTRIUM_LEGGINGS.get(),
                ItemRegistry.VELTRIUM_BOOTS.get()
        );
        this.tag(TagRegistry.Items.ARMORS_TREXALITE).add(
                ItemRegistry.TREXALITE_HELMET.get(),
                ItemRegistry.TREXALITE_CHESTPLATE.get(),
                ItemRegistry.TREXALITE_LEGGINGS.get(),
                ItemRegistry.TREXALITE_BOOTS.get()
        );

        this.tag(Tags.Items.BUCKETS).add(
                ItemRegistry.ARGENT_PLASMA_BUCKET.get()
        );
        this.tag(TagRegistry.Items.BUCKETS_ARGENT_PLASMA_TAG).add(
                ItemRegistry.ARGENT_PLASMA_BUCKET.get()
        );
        this.tag(TagRegistry.Items.JETPACKS).add(
                ItemRegistry.JETPACK.get()
        );
        this.tag(TagRegistry.Items.BATTERIES).add(
                ItemRegistry.SMALL_BATTERY.get(),
                ItemRegistry.MEDIUM_BATTERY.get(),
                ItemRegistry.LARGE_BATTERY.get(),
                ItemRegistry.MASSIVE_BATTERY.get(),
                ItemRegistry.ATOMIC_BATTERY.get(),
                ItemRegistry.CREATIVE_BATTERY.get()
        );

        this.tag(TagRegistry.Items.UPGRADE_CARDS).add(
                ItemRegistry.ENERGY_UPGRADE_CARD.get(),
                ItemRegistry.SPEED_UPGRADE_CARD.get(),
                ItemRegistry.NIGHT_VISION_UPGRADE_CARD.get(),
                ItemRegistry.JUMP_BOOST_UPGRADE_CARD.get(),
                ItemRegistry.JETPACK_UPGRADE_CARD.get(),
                ItemRegistry.FEATHER_FALLING_UPGRADE_CARD.get(),
                ItemRegistry.HEALTH_UPGRADE_CARD.get(),
                ItemRegistry.WATER_BREATHING_UPGRADE_CARD.get(),
                ItemRegistry.AUTO_FEED_UPGRADE_CARD.get(),
                ItemRegistry.FIRE_RESIST_UPGRADE_CARD.get(),
                ItemRegistry.WITHER_RESIST_UPGRADE_CARD.get(),
                ItemRegistry.POISON_RESIST_UPGRADE_CARD.get(),
                ItemRegistry.MAGNET_UPGRADE_CARD.get(),
                ItemRegistry.PHASE_UPGRADE_CARD.get(),
                ItemRegistry.REGENERATION_UPGRADE_CARD.get(),
                ItemRegistry.STEP_HEIGHT_UPGRADE_CARD.get(),
                ItemRegistry.REACH_UPGRADE_CARD.get(),
                ItemRegistry.MINING_SPEED_UPGRADE_CARD.get(),
                ItemRegistry.MINE_AREA_UPGRADE_CARD.get(),
                ItemRegistry.EVASION_UPGRADE_CARD.get(),
                ItemRegistry.SMELTER_UPGRADE_CARD.get(),
                ItemRegistry.DAMAGE_UPGRADE_CARD.get()
        );

        this.tag(Tags.Items.INGOTS).add(
                ItemRegistry.URANIUM_INGOT.get(),
                ItemRegistry.DALEKANIUM_INGOT.get(),
                ItemRegistry.HELLFORGED_INGOT.get());
        this.tag(Tags.Items.GEMS).add(
                ItemRegistry.VELTRIUM.get(),
                ItemRegistry.TREXALITE.get()
        );
        this.tag(TagRegistry.Items.TREXALITE_GEM_TAG).add(
                ItemRegistry.TREXALITE.get());
        this.tag(TagRegistry.Items.VELTRIUM_GEM_TAG).add(
                ItemRegistry.VELTRIUM.get());
        this.tag(TagRegistry.Items.URANIUM_INGOT_TAG).add(
                ItemRegistry.URANIUM_INGOT.get());
        this.tag(TagRegistry.Items.BRONZE_INGOT_TAG).add(
                ItemRegistry.DALEKANIUM_INGOT.get());
        this.tag(TagRegistry.Items.HELLFORGED_INGOT_TAG).add(
                ItemRegistry.HELLFORGED_INGOT.get());

        this.tag(Tags.Items.TOOLS).add(
                ItemRegistry.UTILITY_WRENCH.get());
        this.tag(Tags.Items.TOOLS_WRENCH).add(
                ItemRegistry.UTILITY_WRENCH.get());

        this.tag(Tags.Items.STORAGE_BLOCKS).add(
                ItemRegistry.URANIUM_BLOCK_BLOCK_ITEM.get(),
                ItemRegistry.RAW_URANIUM_BLOCK_BLOCK_ITEM.get(),
                ItemRegistry.RAW_VELTRIUM_BLOCK_BLOCK_ITEM.get(),
                ItemRegistry.RAW_TREXALITE_BLOCK_BLOCK_ITEM.get()
        );
        this.tag(TagRegistry.Items.URANIUM_STORAGE_BLOCK).add(
                ItemRegistry.URANIUM_BLOCK_BLOCK_ITEM.get()
        );
        this.tag(TagRegistry.Items.RAW_URANIUM_STORAGE_BLOCK).add(
                ItemRegistry.RAW_URANIUM_BLOCK_BLOCK_ITEM.get()
        );
        this.tag(TagRegistry.Items.TREXALITE_STORAGE_BLOCK).add(
                ItemRegistry.TREXALITE_BLOCK_BLOCK_ITEM.get()
        );
        this.tag(TagRegistry.Items.RAW_TREXALITE_STORAGE_BLOCK).add(
                ItemRegistry.RAW_TREXALITE_BLOCK_BLOCK_ITEM.get()
        );

        this.tag(Tags.Items.RAW_MATERIALS).add(
                ItemRegistry.RAW_URANIUM.get(),
                ItemRegistry.RAW_VELTRIUM.get(),
                ItemRegistry.RAW_DALEKANIUM.get(),
                ItemRegistry.RAW_TREXALITE.get()
        );
        this.tag(TagRegistry.Items.URANIUM_RAW_MATERIAL).add(
                ItemRegistry.RAW_URANIUM.get()
        );
        this.tag(TagRegistry.Items.TREXALITE_RAW_MATERIAL).add(
                ItemRegistry.RAW_TREXALITE.get()
        );
        this.tag(TagRegistry.Items.VELTRIUM_RAW_MATERIAL).add(
                ItemRegistry.RAW_VELTRIUM.get()
        );

        this.tag(Tags.Items.PLAYER_WORKSTATIONS_FURNACES).
                add(
                        ItemRegistry.POWERED_FURNACE_BLOCK_ITEM.get()
                );

        this.tag(TagRegistry.Items.BLOCK_BREAKER_BANNED).
                add(
                        Items.BEDROCK
                );
        this.tag(TagRegistry.Items.BLOCK_PLACER_BANNED).
                add(
                        Items.BEDROCK
                );
    }
}
