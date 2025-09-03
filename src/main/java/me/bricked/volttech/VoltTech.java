package me.bricked.volttech;

import me.bricked.volttech.datagen.*;
import me.bricked.volttech.datagen.model.VoltTechModelProvider;
import me.bricked.volttech.datagen.tag.VoltTechBlockTagProvider;
import me.bricked.volttech.datagen.tag.VoltTechFluidTagProvider;
import me.bricked.volttech.datagen.loot_table.VoltTechLootTableProvider;
import me.bricked.volttech.datagen.tag.VoltTechItemTagProvider;
import me.bricked.volttech.register.*;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(VoltTech.MODID)
public class VoltTech {
    public static final String MODID = "volttech";
    private static final Logger LOGGER = LogUtils.getLogger();

    public VoltTech(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherDataClient);
        modEventBus.addListener(ScreenRegistry::registerScreens);

        DataComponentRegistry.registerDataComponents(modEventBus);
        BlockRegistry.registerBlocks(modEventBus);
        BlockEntityRegistry.registerBlockEntities(modEventBus);
        FluidRegistry.registerFluids(modEventBus);
        ItemRegistry.registerItems(modEventBus);
        AttributeRegistry.registerAttributes(modEventBus);
        EntityRegistry.registerEntities(modEventBus);
        CreativeTabRegistry.registerCreativeTabs(modEventBus);
        MenuRegistry.registerMenus(modEventBus);
        RecipeRegistry.registerRecipeTypes(modEventBus);
        ChunkGeneratorRegistry.registerChunkGenerators(modEventBus);
        // Read energy/fluid config values (before normal config is loaded)
        Constraints.loadDefaults();
        Constraints.readConfigs();
        // Read upgradeable items
        UpgradeData.loadDefaults();
        UpgradeData.readConfig();
    }

    @SubscribeEvent
    public void onDataPackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(RecipeRegistry.CRUSHER_RECIPE.get());
        event.sendRecipes(RecipeRegistry.FOOD_MASHER_RECIPE.get());
    }

    public void gatherDataClient(GatherDataEvent.Client event) {
        event.createProvider(VoltTechRecipeProvider.Runner::new);
        event.createProvider(VoltTechModelProvider::new);
        event.createProvider(VoltTechEquipmentAssetProvider::new);
        event.createProvider(VoltTechDatapackProvider::new);
        event.createProvider(VoltTechBlockTagProvider::new );
        event.createProvider(VoltTechFluidTagProvider::new);
        event.createProvider(VoltTechItemTagProvider::new);
        event.createProvider(VoltTechLootTableProvider::new);
        event.createProvider(VoltTechLanguageProvider::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
