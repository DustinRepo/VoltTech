package me.bricked.volttech;

import com.mojang.blaze3d.platform.InputConstants;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.EntityRegistry;
import me.bricked.volttech.register.FluidRegistry;
import me.bricked.volttech.render.entity.MultiversePortalEntityRenderer;
import me.bricked.volttech.render.special.ChunkLoaderSpecialRenderer;
import me.bricked.volttech.render.special.FluidTankSpecialRenderer;
import me.bricked.volttech.render.blockentity.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = VoltTech.MODID, value = Dist.CLIENT)
public class VoltTechClient {

    public static RecipeMap recipeMap;
    private static final String voltTechCategory = VoltTech.MODID + ".category.default";
    public static final Lazy<KeyMapping> JETPACK_TOGGLE_MAPPING = Lazy.of(() -> new KeyMapping(
            VoltTech.MODID + ".key.jetpack_toggle",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_V,
            voltTechCategory
    ));
    public static final Lazy<KeyMapping> JETPACK_ASCEND = Lazy.of(() -> new KeyMapping(
            VoltTech.MODID + ".key.jetpack_ascend",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_SPACE,
            voltTechCategory
    ));
    public static final Lazy<KeyMapping> JETPACK_DESCEND = Lazy.of(() -> new KeyMapping(
            VoltTech.MODID + ".key.jetpack_descend",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LSHIFT,
            voltTechCategory
    ));

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        recipeMap = event.getRecipeMap();
    }

    @SubscribeEvent
    public static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(
                VoltTech.resourceLocation("fluid_tank_special"),
                FluidTankSpecialRenderer.Unbaked.CODEC
        );
        event.register(
                VoltTech.resourceLocation("chunk_loader_special"),
                ChunkLoaderSpecialRenderer.Unbaked.CODEC
        );
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.MULTIVERSE_PORTAL.get(), MultiversePortalEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(
                createFluidTypeExtension(0xffff1b00, "argent_plasma", false),
                FluidRegistry.ARGENT_PLASMA_FLUID_TYPE
        );
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(JETPACK_TOGGLE_MAPPING.get());
        event.register(JETPACK_ASCEND.get());
        event.register(JETPACK_DESCEND.get());
    }

    private static IClientFluidTypeExtensions createFluidTypeExtension(int color, String fluidName, boolean hasOverlay) {
        return new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return color;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return VoltTech.resourceLocation("block/%s_still".formatted(fluidName));
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return VoltTech.resourceLocation("block/%s_flow".formatted(fluidName));
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                if (hasOverlay) {
                    return VoltTech.resourceLocation("block/%s_overlay".formatted(fluidName));
                }
                return IClientFluidTypeExtensions.super.getOverlayTexture();
            }
        };
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.UPGRADE_TABLE_BLOCK_ENTITY.get(), UpgradeTableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.MINI_REACTOR_BLOCK_ENTITY.get(), MiniReactorRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SMALL_FLUID_TANK_BLOCK_ENTITY.get(), FluidTankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.MEDIUM_FLUID_TANK_BLOCK_ENTITY.get(), FluidTankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.LARGE_FLUID_TANK_BLOCK_ENTITY.get(), FluidTankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.MASSIVE_FLUID_TANK_BLOCK_ENTITY.get(), FluidTankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.CREATIVE_FLUID_TANK_BLOCK_ENTITY.get(), FluidTankRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.COPPER_ENERGY_CABLE_BLOCK_ENTITY.get(), EnergyCableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.IRON_ENERGY_CABLE_BLOCK_ENTITY.get(), EnergyCableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.GOLD_ENERGY_CABLE_BLOCK_ENTITY.get(), EnergyCableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.DIAMOND_ENERGY_CABLE_BLOCK_ENTITY.get(), EnergyCableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.EMERALD_ENERGY_CABLE_BLOCK_ENTITY.get(), EnergyCableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.NETHERITE_ENERGY_CABLE_BLOCK_ENTITY.get(), EnergyCableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.COPPER_FLUID_PIPE_BLOCK_ENTITY.get(), FluidPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.IRON_FLUID_PIPE_BLOCK_ENTITY.get(), FluidPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.GOLD_FLUID_PIPE_BLOCK_ENTITY.get(), FluidPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.DIAMOND_FLUID_PIPE_BLOCK_ENTITY.get(), FluidPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.EMERALD_FLUID_PIPE_BLOCK_ENTITY.get(), FluidPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.NETHERITE_FLUID_PIPE_BLOCK_ENTITY.get(), FluidPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.ITEM_PIPE_BLOCK_ENTITY.get(), ItemPipeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.HARVESTER_BLOCK_ENTITY.get(), HarvesterRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.CHUNK_LOADER_BLOCK_ENTITY.get(), ChunkLoaderRenderer::new);
    }
}
