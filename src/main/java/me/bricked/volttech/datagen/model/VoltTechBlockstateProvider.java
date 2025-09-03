package me.bricked.volttech.datagen.model;

import com.mojang.math.Quadrant;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.block.*;
import me.bricked.volttech.register.BlockRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.block.model.multipart.CombinedCondition;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VoltTechBlockstateProvider {
    private final static ResourceLocation CUBE = ResourceLocation.parse("block/cube");
    private BlockModelGenerators blockModels;
    public VoltTechBlockstateProvider() {
    }

    public void registerModels(BlockModelGenerators blockModelGenerators) {
        this.blockModels = blockModelGenerators;
        verySimpleBlockWithItem(BlockRegistry.DEEPSLATE_URANIUM_ORE.get());
        verySimpleBlockWithItem(BlockRegistry.RAW_URANIUM_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.URANIUM_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.URANIUM_ORE.get());
        verySimpleBlockWithItem(BlockRegistry.HELLISH_ROCK.get());
        verySimpleBlockWithItem(BlockRegistry.VELTRIUM_ORE.get());
        verySimpleBlockWithItem(BlockRegistry.VELTRIUM_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.RAW_VELTRIUM_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.RAW_DALEKANIUM_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.DALEKANIUM_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.DALEKANIUM_ORE.get());
        verySimpleBlockWithItem(BlockRegistry.DEEPSLATE_TREXALITE_ORE.get());
        verySimpleBlockWithItem(BlockRegistry.TREXALITE_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.RAW_TREXALITE_BLOCK.get());
        verySimpleBlockWithItem(BlockRegistry.SPATIAL_CRATE.get());
        verySimpleBlockWithItem(BlockRegistry.CRUSHER.get());
        simpleFluid(BlockRegistry.ARGENT_PLASMA.get());
        createTopBottomBlock(BlockRegistry.FOOD_MASHER.get());
        createSingleDirectionalFaceBlock(BlockRegistry.SMALL_ENERGY_CUBE.get());
        createSingleDirectionalFaceBlock(BlockRegistry.MEDIUM_ENERGY_CUBE.get());
        createSingleDirectionalFaceBlock(BlockRegistry.LARGE_ENERGY_CUBE.get());
        createSingleDirectionalFaceBlock(BlockRegistry.MASSIVE_ENERGY_CUBE.get());
        createSingleDirectionalFaceBlock(BlockRegistry.CREATIVE_ENERGY_CUBE.get());
        createSingleDirectionalFaceBlock(BlockRegistry.TEMPORAL_ACCELERATOR.get());
        createSingleDirectionalFaceBlock(BlockRegistry.BLOCK_BREAKER.get());
        createSingleDirectionalFaceBlock(BlockRegistry.BLOCK_PLACER.get());
        createSingleHorizontalFaceBlock(BlockRegistry.HARVESTER.get());
        createSingleFaceLitBlock(BlockRegistry.COMBUSTION_GENERATOR.get());
        verySimpleBlockWithItem(BlockRegistry.HEAT_GENERATOR.get());
        createEnergyCable(BlockRegistry.COPPER_ENERGY_CABLE.get());
        createEnergyCable(BlockRegistry.IRON_ENERGY_CABLE.get());
        createEnergyCable(BlockRegistry.GOLD_ENERGY_CABLE.get());
        createEnergyCable(BlockRegistry.DIAMOND_ENERGY_CABLE.get());
        createEnergyCable(BlockRegistry.EMERALD_ENERGY_CABLE.get());
        createEnergyCable(BlockRegistry.NETHERITE_ENERGY_CABLE.get());
        createFluidPipe(BlockRegistry.COPPER_FLUID_PIPE.get());
        createFluidPipe(BlockRegistry.IRON_FLUID_PIPE.get());
        createFluidPipe(BlockRegistry.GOLD_FLUID_PIPE.get());
        createFluidPipe(BlockRegistry.DIAMOND_FLUID_PIPE.get());
        createFluidPipe(BlockRegistry.EMERALD_FLUID_PIPE.get());
        createFluidPipe(BlockRegistry.NETHERITE_FLUID_PIPE.get());
        createFluidTank(BlockRegistry.SMALL_FLUID_TANK.get());
        createFluidTank(BlockRegistry.MEDIUM_FLUID_TANK.get());
        createFluidTank(BlockRegistry.LARGE_FLUID_TANK.get());
        createFluidTank(BlockRegistry.MASSIVE_FLUID_TANK.get());
        createFluidTank(BlockRegistry.CREATIVE_FLUID_TANK.get());
        createWirelessEnergyTransmitter();
        createMiniReactor();
        createSolarGenerator();
        createUpgradeTable();
        createPoweredFurnace();
        createWirelessPlayerCharger();
        createItemPipe();
        createChunkLoader();
    }

    private void verySimpleBlockWithItem(Block block) {
        // BlockState definition
        ModelTemplate template = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.ALL,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.textureAll(TextureSlot.ALL).allFaces((direction, f) -> f.cullface(direction));
        }).build();

        // Creates assets/volttech/models/block/<block>.json
        ResourceLocation modelRl = template.create(
                block,
                new TextureMapping().
                        put(TextureSlot.ALL, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        );
        // Creates assets/volttech/blockstates/<block>.json
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelRl)));
        blockModels.registerSimpleItemModel(block, modelRl);
    }

    private void simpleFluid(LiquidBlock block) {
        ModelTemplate template = new ModelTemplate(
                Optional.empty(),
                Optional.empty(),
                TextureSlot.PARTICLE
        );
        ResourceLocation modelResourceLocation = template.create(
                block,
                new TextureMapping().
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelResourceLocation)));
    }

    private void createSingleFaceLitBlock(Block block) {
        ModelTemplate template = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.FRONT,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    face(Direction.UP, f -> f.texture(TextureSlot.SIDE).cullface(Direction.UP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.FRONT).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        }).build();
        ResourceLocation unlitRL = template.create(
                block,
                new TextureMapping().
                        put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_front")),
                blockModels.modelOutput
        );
        ResourceLocation litRL = template.createWithSuffix(
                block,
                "_lit",
                new TextureMapping().
                        put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_lit")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_front")),
                blockModels.modelOutput
        );
        MultiVariant unlitVariant = BlockModelGenerators.plainVariant(unlitRL);
        MultiVariant litVariant = BlockModelGenerators.plainVariant(litRL);

        blockModels.registerSimpleItemModel(block, unlitRL);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.
                dispatch(block).
                with(
                        BlockModelGenerators.createBooleanModelDispatch(
                                BlockStateProperties.LIT, litVariant, unlitVariant)
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    private void createTopBottomBlock(Block block) {
        ModelTemplate template = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.TOP,
                TextureSlot.BOTTOM,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP).cullface(Direction.UP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        }).build();
        ResourceLocation modelLocation = template.create(
                block,
                new TextureMapping().
                        put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).
                        put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side")),
                blockModels.modelOutput
        );
        MultiVariant variant = BlockModelGenerators.plainVariant(modelLocation);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
        blockModels.registerSimpleItemModel(block, modelLocation);
    }

    private void createSingleDirectionalFaceBlock(Block block) {
        ExtendedModelTemplateBuilder templateBuilder = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.FRONT,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    face(Direction.UP, f -> f.texture(TextureSlot.SIDE).cullface(Direction.UP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.FRONT).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        });
        ResourceLocation modelRL = templateBuilder.build().create(
                block,
                new TextureMapping().
                        put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side")),
                blockModels.modelOutput
        );
        MultiVariant variant = BlockModelGenerators.plainVariant(modelRL);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.
                dispatch(block, variant).
                with(BlockModelGenerators.ROTATION_FACING));
        blockModels.registerSimpleItemModel(block, modelRL);
    }

    private void createSingleHorizontalFaceBlock(Block block) {
        ExtendedModelTemplateBuilder templateBuilder = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.FRONT,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    face(Direction.UP, f -> f.texture(TextureSlot.SIDE).cullface(Direction.UP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.FRONT).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        });
        ResourceLocation modelRL = templateBuilder.build().create(
                block,
                new TextureMapping().
                        put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side")),
                blockModels.modelOutput
        );
        MultiVariant variant = BlockModelGenerators.plainVariant(modelRL);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.
                dispatch(block, variant).
                with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.registerSimpleItemModel(block, modelRL);
    }

    private void createWirelessPlayerCharger() {
        WirelessPlayerChargerBlock block = BlockRegistry.WIRELESS_PLAYER_CHARGER.get();
        TextureSlot BASE = TextureSlot.create("base");
        TextureSlot ANTENNA = TextureSlot.create("antenna");
        ExtendedModelTemplateBuilder builder = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.SIDE,
                TextureSlot.PARTICLE,
                BASE,
                ANTENNA
        ).extend().
                element(elementBuilder -> {
                    elementBuilder.
                            from(0, 0, 0).
                            to(16, 5, 16).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.NORTH).cullface(Direction.NORTH)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH).cullface(Direction.SOUTH)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST).cullface(Direction.EAST)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST).cullface(Direction.WEST)).
                            face(Direction.UP, f -> f.texture(BASE)).
                            face(Direction.DOWN, f -> f.texture(BASE).cullface(Direction.DOWN));
                }).
                element(elementBuilder -> {
                    elementBuilder.
                            from(7, 5, 7).
                            to(9, 16, 9).
                            face(Direction.NORTH, f -> f.texture(ANTENNA).uvs(0, 0, 4, 11)).
                            face(Direction.SOUTH, f -> f.texture(ANTENNA).uvs(4, 0, 8, 11)).
                            face(Direction.EAST, f -> f.texture(ANTENNA).uvs(8, 0, 12, 11)).
                            face(Direction.WEST, f -> f.texture(ANTENNA).uvs(12, 0, 16, 11)).
                            face(Direction.UP, f -> f.texture(ANTENNA).uvs(0, 12, 4, 16)).
                            face(Direction.DOWN, f -> f.texture(ANTENNA).uvs(4, 12, 8, 16).cullface(Direction.DOWN));
                });
        for (int i = 0; i < 3; i++) {
            int j = i * 2;
            builder.
                    element(elementBuilder -> {
                        elementBuilder.
                                from(6, 8 + j, 5).
                                to(10, 9 + j, 6).
                                face(Direction.NORTH, f -> f.texture(ANTENNA).uvs(0, 11, 4, 12)).
                                face(Direction.SOUTH, f -> f.texture(ANTENNA).uvs(4, 11, 4, 12)).
                                face(Direction.EAST, f -> f.texture(ANTENNA).uvs(8, 11, 1, 12)).
                                face(Direction.WEST, f -> f.texture(ANTENNA).uvs(12, 11, 1, 12)).
                                face(Direction.UP, f -> f.texture(ANTENNA).uvs(0, 11, 4, 12)).
                                face(Direction.DOWN, f -> f.texture(ANTENNA).uvs(4, 11, 4, 12));
                    }).
                    element(elementBuilder -> {
                        elementBuilder.
                                from(6, 8 + j, 10).
                                to(10, 9 + j, 11).
                                face(Direction.NORTH, f -> f.texture(ANTENNA).uvs(0, 11, 4, 12)).
                                face(Direction.SOUTH, f -> f.texture(ANTENNA).uvs(4, 11, 4, 12)).
                                face(Direction.EAST, f -> f.texture(ANTENNA).uvs(8, 11, 1, 12)).
                                face(Direction.WEST, f -> f.texture(ANTENNA).uvs(12, 11, 1, 12)).
                                face(Direction.UP, f -> f.texture(ANTENNA).uvs(0, 11, 4, 12)).
                                face(Direction.DOWN, f -> f.texture(ANTENNA).uvs(4, 11, 4, 12));
                    }).
                    element(elementBuilder -> {
                        elementBuilder.
                                from(5, 8 + j, 6).
                                to(6, 9 + j, 10).
                                face(Direction.NORTH, f -> f.texture(ANTENNA).uvs(0, 11, 1, 12)).
                                face(Direction.SOUTH, f -> f.texture(ANTENNA).uvs(4, 11, 1, 12)).
                                face(Direction.EAST, f -> f.texture(ANTENNA).uvs(8, 11, 4, 12)).
                                face(Direction.WEST, f -> f.texture(ANTENNA).uvs(12, 11, 4, 12)).
                                face(Direction.UP, f -> f.texture(ANTENNA).uvs(0, 11, 4, 12)).
                                face(Direction.DOWN, f -> f.texture(ANTENNA).uvs(4, 11, 4, 12));
                    }).
                    element(elementBuilder -> {
                        elementBuilder.
                                from(10, 8 + j, 6).
                                to(11, 9 + j, 10).
                                face(Direction.NORTH, f -> f.texture(ANTENNA).uvs(0, 11, 1, 12)).
                                face(Direction.SOUTH, f -> f.texture(ANTENNA).uvs(4, 11, 1, 12)).
                                face(Direction.EAST, f -> f.texture(ANTENNA).uvs(8, 11, 4, 12)).
                                face(Direction.WEST, f -> f.texture(ANTENNA).uvs(12, 11, 4, 12)).
                                face(Direction.UP, f -> f.texture(ANTENNA).uvs(0, 11, 4, 12)).
                                face(Direction.DOWN, f -> f.texture(ANTENNA).uvs(4, 11, 4, 12));
                    });
        }
        // Actually creates the model json file
        ResourceLocation modelRL = builder.build().create(
                block,
                new TextureMapping().
                        put(BASE, TextureMapping.getBlockTexture(block, "_base")).
                        put(ANTENNA, TextureMapping.getBlockTexture(block, "_antenna")).
                        put(TextureSlot.ALL, TextureMapping.getBlockTexture(block, "_base")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_base"))
                , blockModels.modelOutput);
        MultiVariant variant = BlockModelGenerators.plainVariant(modelRL);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant));
        blockModels.registerSimpleItemModel(block, modelRL);
    }

    private void createFluidTank(Block block) {
        ExtendedModelTemplateBuilder frame = new ModelTemplate(
                Optional.empty(),
                Optional.of("_frame"),
                TextureSlot.SIDE,
                TextureSlot.TOP,
                TextureSlot.BOTTOM,
                TextureSlot.PARTICLE
        ).extend().renderType("solid");

        ExtendedModelTemplateBuilder wall = new ModelTemplate(
                Optional.empty(),
                Optional.of("_wall"),
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().renderType("translucent");

        frame.
                // Top
                element(elementBuilder -> {
                    elementBuilder.
                            from(3, 15, 3).
                            to(13, 16, 13).
                            face(Direction.UP, f -> f.texture(TextureSlot.TOP).uvs(0, 0, 10, 10).cullface(Direction.UP)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.TOP).uvs(0, 0, 10, 10)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 0, 10, 1)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 0, 10, 1)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(0, 0, 10, 1)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(0, 0, 10, 1));
                }).
                // Bottom
                element(elementBuilder -> {
                    elementBuilder.
                            from(3, 0, 3).
                            to(13, 1, 13).
                            face(Direction.UP, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 10, 10)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 10, 10).cullface(Direction.DOWN)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 15, 10, 16)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 15, 10, 16)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(0, 15, 10, 16)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(0, 15, 10, 16));
                }).
                // Frame leg 1
                element(elementBuilder -> {
                    elementBuilder.
                            from(3, 1, 3).
                            to(4, 15, 4).
                            face(Direction.UP, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 1, 1, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 1, 1, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(0, 1, 1, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(0, 1, 1, 15));
                }).
                // Frame leg 2
                element(elementBuilder -> {
                    elementBuilder.
                            from(12, 1, 12).
                            to(13, 15, 13).
                            face(Direction.UP, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15));
                }).
                // Frame leg 3
                element(elementBuilder -> {
                    elementBuilder.
                            from(3, 1, 12).
                            to(4, 15, 13).
                            face(Direction.UP, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15));
                }).
                // Frame leg 4
                element(elementBuilder -> {
                    elementBuilder.
                            from(12, 1, 3).
                            to(13, 15, 4).
                            face(Direction.UP, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).uvs(0, 0, 1, 1)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(9, 1, 10, 15));
                });
        wall.
                // North
                element(elementBuilder -> {
                    elementBuilder.
                            from(4, 1, 3).
                            to(12, 15, 4).
                            face(Direction.UP, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15));
                }).
                // South
                element(elementBuilder -> {
                    elementBuilder.
                            from(4, 1, 12).
                            to(12, 15, 13).
                            face(Direction.UP, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15));
                }).
                // West
                element(elementBuilder -> {
                    elementBuilder.
                            from(3, 1, 4).
                            to(4, 15, 12).
                            face(Direction.UP, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15));
                }).
                // East
                element(elementBuilder -> {
                    elementBuilder.
                            from(12, 1, 4).
                            to(13, 15, 12).
                            face(Direction.UP, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE)).
                            face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15)).
                            face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).uvs(1, 1, 9, 15));
                });
        ResourceLocation frameModelLocation = frame.build().create(
                block,
                new TextureMapping().
                        put(TextureSlot.TOP, VoltTech.resourceLocation("block/fluid_tank_top")).
                        put(TextureSlot.BOTTOM, VoltTech.resourceLocation("block/fluid_tank_bottom")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        );
        ResourceLocation wallModelLocation = wall.build().create(
                block,
                new TextureMapping().
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        );
        Variant frameVariant = new Variant(frameModelLocation);
        Variant wallVariant = new Variant(wallModelLocation);
        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(block).
                        with(BlockModelGenerators.variant(frameVariant)).
                        with(BlockModelGenerators.variant(wallVariant))
        );
    }

    private void createItemPipe() {
        ItemPipeBlock block = BlockRegistry.ITEM_PIPE.get();
        TextureSlot PIPE = TextureSlot.create("pipe");
        ExtendedModelTemplateBuilder defaultTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_default"),
                PIPE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6, 6, 6).
                    to(10, 10, 10).
                    allFaces((direction, f) -> f.texture(PIPE).uvs(0, 0, 8, 8));
        });

        ExtendedModelTemplateBuilder connectTemplate = new ModelTemplate(
                Optional.empty(),
                Optional.of("_connect"),
                PIPE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6, 10, 6).
                    to(10, 16, 10).
                    face(Direction.UP, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.DOWN, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.NORTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.SOUTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.EAST, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.WEST, f -> f.texture(PIPE).uvs(8, 0, 16, 12));
        });

        ExtendedModelTemplateBuilder extractTemplate = new ModelTemplate(
                Optional.empty(),
                Optional.of("_extract"),
                PIPE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(5, 15, 5).
                    to(11, 16, 11).
                    face(Direction.UP, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.DOWN, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.NORTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.SOUTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.EAST, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.WEST, f -> f.texture(PIPE).uvs(8, 0, 16, 12));
        });

        Variant defaultVariant = new Variant(defaultTemplate.build().create(
                block,
                new TextureMapping().
                        put(PIPE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        Variant connectVariant = new Variant(connectTemplate.build().create(
                block,
                new TextureMapping().
                        put(PIPE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        Variant extractVariant = new Variant(extractTemplate.build().create(
                block,
                new TextureMapping().
                        put(PIPE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(block).
                        with(BlockModelGenerators.variant(defaultVariant)).
                        // Connect and extract
                                with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.UP_CONNECTION, ItemPipeBlock.SideConnection.CONNECTED, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant)).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.DOWN_CONNECTION, ItemPipeBlock.SideConnection.CONNECTED, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R180))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.NORTH_CONNECTION, ItemPipeBlock.SideConnection.CONNECTED, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.SOUTH_CONNECTION, ItemPipeBlock.SideConnection.CONNECTED, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.EAST_CONNECTION, ItemPipeBlock.SideConnection.CONNECTED, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.WEST_CONNECTION, ItemPipeBlock.SideConnection.CONNECTED, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        // Extract only
                                with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.UP_CONNECTION, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant)).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.DOWN_CONNECTION, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R180))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.NORTH_CONNECTION, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.SOUTH_CONNECTION, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.EAST_CONNECTION, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(ItemPipeBlock.WEST_CONNECTION, ItemPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
        );
        blockModels.registerSimpleItemModel(block, defaultVariant.modelLocation());
    }

    private void createEnergyCable(Block block) {
        TextureSlot CABLE = TextureSlot.create("cable");
        ExtendedModelTemplateBuilder defaultTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                CABLE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6, 6, 6).
                    to(10, 10, 10).
                    allFaces((direction, f) -> f.texture(CABLE).uvs(0, 0, 8, 8));
        });

        ExtendedModelTemplateBuilder connectTemplate = new ModelTemplate(
                Optional.empty(),
                Optional.of("_connect"),
                CABLE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6, 10, 6).
                    to(10, 16, 10).
                    face(Direction.UP, f -> f.texture(CABLE).uvs(0, 0, 8, 8)).
                    face(Direction.DOWN, f -> f.texture(CABLE).uvs(0, 0, 8, 8)).
                    face(Direction.NORTH, f -> f.texture(CABLE).uvs(8, 0, 16, 12)).
                    face(Direction.SOUTH, f -> f.texture(CABLE).uvs(8, 0, 16, 12)).
                    face(Direction.EAST, f -> f.texture(CABLE).uvs(8, 0, 16, 12)).
                    face(Direction.WEST, f -> f.texture(CABLE).uvs(8, 0, 16, 12));
        });

        Variant defaultVariant = new Variant(defaultTemplate.build().create(
                block,
                new TextureMapping().
                        put(CABLE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        Variant connectVariant = new Variant(connectTemplate.build().create(
                block,
                new TextureMapping().
                        put(CABLE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(block).
                        with(BlockModelGenerators.variant(defaultVariant)).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(BlockStateProperties.UP, true).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant)).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(BlockStateProperties.DOWN, true).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R180))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(BlockStateProperties.NORTH, true).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(BlockStateProperties.SOUTH, true).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(BlockStateProperties.EAST, true).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(BlockStateProperties.WEST, true).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
        );
        blockModels.registerSimpleItemModel(block, defaultVariant.modelLocation());
    }

    private void createFluidPipe(Block block) {
        TextureSlot PIPE = TextureSlot.create("pipe");
        ExtendedModelTemplateBuilder defaultTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_default"),
                PIPE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6, 6, 6).
                    to(10, 10, 10).
                    allFaces((direction, f) -> f.texture(PIPE).uvs(0, 0, 8, 8));
        });
        ExtendedModelTemplateBuilder connectTemplate = new ModelTemplate(
                Optional.empty(),
                Optional.of("_connect"),
                PIPE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6, 10, 6).
                    to(10, 16, 10).
                    face(Direction.UP, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.DOWN, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.NORTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.SOUTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.EAST, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.WEST, f -> f.texture(PIPE).uvs(8, 0, 16, 12));
        });
        ExtendedModelTemplateBuilder extractTemplate = new ModelTemplate(
                Optional.empty(),
                Optional.of("extract"),
                PIPE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(5, 15, 5).
                    to(11, 16, 11).
                    face(Direction.UP, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.DOWN, f -> f.texture(PIPE).uvs(0, 0, 8, 8)).
                    face(Direction.NORTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.SOUTH, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.EAST, f -> f.texture(PIPE).uvs(8, 0, 16, 12)).
                    face(Direction.WEST, f -> f.texture(PIPE).uvs(8, 0, 16, 12));
        });

        Variant defaultVariant = new Variant(defaultTemplate.build().create(
                block,
                new TextureMapping().
                        put(PIPE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        Variant connectVariant = new Variant(connectTemplate.build().create(
                block,
                new TextureMapping().
                        put(PIPE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        Variant extractVariant = new Variant(extractTemplate.build().create(
                block,
                new TextureMapping().
                        put(PIPE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(block).
                        with(BlockModelGenerators.variant(defaultVariant)).
                        // Connect and extract
                                with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.UP_CONNECTION, FluidPipeBlock.SideConnection.CONNECTED, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant)).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.DOWN_CONNECTION, FluidPipeBlock.SideConnection.CONNECTED, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R180))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.NORTH_CONNECTION, FluidPipeBlock.SideConnection.CONNECTED, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.SOUTH_CONNECTION, FluidPipeBlock.SideConnection.CONNECTED, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.EAST_CONNECTION, FluidPipeBlock.SideConnection.CONNECTED, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.WEST_CONNECTION, FluidPipeBlock.SideConnection.CONNECTED, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        // Extract only
                                with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.UP_CONNECTION, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant)).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.DOWN_CONNECTION, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R180))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.NORTH_CONNECTION, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.SOUTH_CONNECTION, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.EAST_CONNECTION, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                        with(new CombinedCondition(
                                CombinedCondition.Operation.AND,
                                List.of(
                                        BlockModelGenerators.condition().term(FluidPipeBlock.WEST_CONNECTION, FluidPipeBlock.SideConnection.EXTRACT).build()
                                )
                        ), BlockModelGenerators.variant(extractVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
        );
        blockModels.registerSimpleItemModel(block, defaultVariant.modelLocation());
    }

    private void createPoweredFurnace() {
        PoweredFurnaceBlock block = BlockRegistry.POWERED_FURNACE.get();
        ExtendedModelTemplateBuilder templateBuilder = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.FRONT,
                TextureSlot.SIDE,
                TextureSlot.TOP,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP).cullface(Direction.UP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.SIDE).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.FRONT).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        });
        ResourceLocation unlitRL = templateBuilder.build().create(
                block,
                new TextureMapping().
                        put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_front")),
                blockModels.modelOutput
        );
        ResourceLocation litRL = templateBuilder.build().createWithSuffix(
                block,
                "_lit",
                new TextureMapping().
                        put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_lit")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_front")),
                blockModels.modelOutput
        );
        // Create both lit and unlit models
        MultiVariant unlitVariant = BlockModelGenerators.plainVariant(unlitRL);
        MultiVariant litVariant = BlockModelGenerators.plainVariant(litRL);

        blockModels.registerSimpleItemModel(block, unlitRL);
        // Create BlockState file
        blockModels.blockStateOutput.accept(MultiVariantGenerator.
                dispatch(block).
                with(
                        BlockModelGenerators.createBooleanModelDispatch(
                                BlockStateProperties.LIT, litVariant, unlitVariant)
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    private void createSolarGenerator() {
        Block block = BlockRegistry.SOLAR_GENERATOR.get();
        ModelTemplate template = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.TOP,
                TextureSlot.BOTTOM,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(0, 0, 0).
                    to(16, 6, 16).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        }).build();

        ResourceLocation modelLocation = template.create(
                block,
                new TextureMapping().
                        put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).
                        put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_top")),
                blockModels.modelOutput
        );

        blockModels.registerSimpleItemModel(block, modelLocation);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelLocation)));
    }

    private void createUpgradeTable() {
        Block block = BlockRegistry.UPGRADE_TABLE.get();
        ModelTemplate template = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.empty(),
                TextureSlot.TOP,
                TextureSlot.BOTTOM,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(0, 0, 0).
                    to(16, 8, 16).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.DOWN)).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.NORTH)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).cullface(Direction.SOUTH)).
                    face(Direction.EAST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.EAST)).
                    face(Direction.WEST, f -> f.texture(TextureSlot.SIDE).cullface(Direction.WEST));
        }).build();

        ResourceLocation modelLocation = template.create(
                block,
                new TextureMapping().
                        put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).
                        put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_top")),
                blockModels.modelOutput
        );

        blockModels.registerSimpleItemModel(block, modelLocation);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelLocation)));
    }

    private void createWirelessEnergyTransmitter() {
        Block block = BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get();
        TextureSlot BASE = TextureSlot.create("base");
        TextureSlot CONNECT = TextureSlot.create("connect");
        ExtendedModelTemplateBuilder defaultTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_default"),
                BASE,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(4.5f, 4.5f, 4.5f).
                    to(11.5f, 11.5f, 11.5f).
                    allFaces((direction, f) -> f.texture(BASE).uvs(0, 0, 11, 11));
        });

        ExtendedModelTemplateBuilder connectTemplate = new ModelTemplate(
                Optional.empty(),
                Optional.of("_connect"),
                CONNECT,
                TextureSlot.PARTICLE
        ).extend().element(elementBuilder -> {
            elementBuilder.
                    from(6.5f, 11.5f, 6.5f).
                    to(9.5f, 12, 9.5f).
                    allFaces((direction, f) -> f.texture(CONNECT));
        }).element(elementBuilder -> {
            elementBuilder.
                    from(7, 12, 7).
                    to(9, 15.5f, 9).
                    allFaces((direction, f) -> f.texture(CONNECT));
        }).element(elementBuilder -> {
            elementBuilder.
                    from(6, 15.5f, 6).
                    to(10, 16, 10).
                    allFaces((direction, f) -> f.texture(CONNECT));
        });

        Variant defaultVariant = new Variant(defaultTemplate.build().create(
                block,
                new TextureMapping().
                        put(BASE, TextureMapping.getBlockTexture(block)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModels.modelOutput
        ));

        Variant activeVariant = new Variant(defaultTemplate.build().createWithSuffix(
                block,
                "_active",
                new TextureMapping().
                        put(BASE, TextureMapping.getBlockTexture(block, "_active")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_active")),
                blockModels.modelOutput
        ));

        Variant connectVariant = new Variant(connectTemplate.build().create(
                block,
                new TextureMapping().
                        put(CONNECT, TextureMapping.getBlockTexture(Blocks.COPPER_BLOCK)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.COPPER_BLOCK)),
                blockModels.modelOutput
        ));

        blockModels.registerSimpleItemModel(block, defaultVariant.modelLocation());
        blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block).
                with(
                        new ConditionBuilder().term(WirelessEnergyTransmitterBlock.IS_ACTIVE, false).build(),
                        BlockModelGenerators.variants(defaultVariant)
                ).with(
                        new ConditionBuilder().term(WirelessEnergyTransmitterBlock.IS_ACTIVE, true).build(),
                        BlockModelGenerators.variants(activeVariant)
                ).
                with(new CombinedCondition(
                CombinedCondition.Operation.AND,
                List.of(
                        BlockModelGenerators.condition().term(BlockStateProperties.UP, true).build()
                )
                ), BlockModelGenerators.variant(connectVariant)).
                with(new CombinedCondition(
                        CombinedCondition.Operation.AND,
                        List.of(
                                BlockModelGenerators.condition().term(BlockStateProperties.DOWN, true).build()
                        )
                ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R180))).
                with(new CombinedCondition(
                        CombinedCondition.Operation.AND,
                        List.of(
                                BlockModelGenerators.condition().term(BlockStateProperties.NORTH, true).build()
                        )
                ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90))).
                with(new CombinedCondition(
                        CombinedCondition.Operation.AND,
                        List.of(
                                BlockModelGenerators.condition().term(BlockStateProperties.SOUTH, true).build()
                        )
                ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270))).
                with(new CombinedCondition(
                        CombinedCondition.Operation.AND,
                        List.of(
                                BlockModelGenerators.condition().term(BlockStateProperties.EAST, true).build()
                        )
                ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R90)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))).
                with(new CombinedCondition(
                        CombinedCondition.Operation.AND,
                        List.of(
                                BlockModelGenerators.condition().term(BlockStateProperties.WEST, true).build()
                        )
                ), BlockModelGenerators.variant(connectVariant).with(VariantMutator.X_ROT.withValue(Quadrant.R270)).with(VariantMutator.Y_ROT.withValue(Quadrant.R90))));
    }

    private void createChunkLoader() {
        ChunkLoaderBlock block = BlockRegistry.CHUNK_LOADER.get();

        ExtendedModelTemplateBuilder frameTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_frame"),
                TextureSlot.TOP,
                TextureSlot.BOTTOM,
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().renderType("translucent").element(elementBuilder -> { // Main slab
            elementBuilder.
                    from(0, 0, 0).
                    to(16, 3, 16).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.UP))
                    .allFacesExcept((direction, f) -> f.texture(TextureSlot.SIDE), Set.of(Direction.UP, Direction.DOWN));
        }).element(elementBuilder -> { // Raised center
            elementBuilder.
                    from(5, 3, 5).
                    to(11, 4, 11).
                    allFaces((direction, f) -> f.texture(TextureSlot.TOP));
        }).element(elementBuilder -> { // Post/pedestal
            elementBuilder.
                    from(7, 3, 7).
                    to(9, 6, 9).
                    allFaces((direction, f) -> f.texture(TextureSlot.TOP));
        }).element(elementBuilder -> { // Raised corners
            elementBuilder.
                    from(0, 3, 0).
                    to(2, 8, 2).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.UP))
                    .allFacesExcept((direction, f) -> f.texture(TextureSlot.SIDE), Set.of(Direction.UP, Direction.DOWN));
        }).element(elementBuilder -> { // Raised corners
            elementBuilder.
                    from(14, 3, 0).
                    to(16, 8, 2).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.UP))
                    .allFacesExcept((direction, f) -> f.texture(TextureSlot.SIDE), Set.of(Direction.UP, Direction.DOWN));
        }).element(elementBuilder -> { // Raised corners
            elementBuilder.
                    from(0, 3, 14).
                    to(2, 8, 16).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.UP))
                    .allFacesExcept((direction, f) -> f.texture(TextureSlot.SIDE), Set.of(Direction.UP, Direction.DOWN));
        }).element(elementBuilder -> { // Raised corners
            elementBuilder.
                    from(14, 3, 14).
                    to(16, 8, 16).
                    face(Direction.UP, f -> f.texture(TextureSlot.TOP)).
                    face(Direction.DOWN, f -> f.texture(TextureSlot.BOTTOM).cullface(Direction.UP))
                    .allFacesExcept((direction, f) -> f.texture(TextureSlot.SIDE), Set.of(Direction.UP, Direction.DOWN));
        });
        ResourceLocation frameRL = frameTemplate.build().create(
                block,
                new TextureMapping().
                        put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top")).
                        put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom")).
                        put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_top")),
                blockModels.modelOutput
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(frameRL)));
    }

    private void createMiniReactor() {
        MiniReactorBlock block = BlockRegistry.MINI_REACTOR.get();
        TextureSlot FRAME = TextureSlot.create("frame");
        TextureSlot INSIDE = TextureSlot.create("inside");
        ExtendedModelTemplateBuilder frameTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_frame"),
                FRAME,
                TextureSlot.PARTICLE
        ).extend().renderType("solid");
        ExtendedModelTemplateBuilder insideTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_inside"),
                INSIDE,
                TextureSlot.PARTICLE
        ).extend().renderType("translucent");
        ExtendedModelTemplateBuilder displayTemplate = new ModelTemplate(
                Optional.of(
                        CUBE
                ),
                Optional.of("_display"),
                FRAME,
                INSIDE,
                TextureSlot.PARTICLE
        ).extend();

        List.of(insideTemplate, displayTemplate).forEach(modelTemplate -> {
            modelTemplate.element(elementBuilder -> {
                elementBuilder.
                        from(1, 1, 1).
                        to(15, 15, 15).
                        allFaces((direction, f) -> f.texture(INSIDE));
            });
        });

        List.of(frameTemplate, displayTemplate).forEach(modelTemplate -> {
            modelTemplate.element(elementBuilder -> {
                elementBuilder.
                        from(0, 0, 0).
                        to(16, 1, 1).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 0, 15).
                        to(1, 16, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 0, 0).
                        to(1, 16, 1).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(15, 0, 0).
                        to(16, 16, 1).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(15, 0, 15).
                        to(16, 16, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 15, 0).
                        to(1, 16, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(15, 15, 0).
                        to(16, 16, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 15, 15).
                        to(16, 16, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 15, 0).
                        to(16, 16, 1).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 0, 0).
                        to(1, 1, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(15, 0, 0).
                        to(16, 1, 16).
                        textureAll(FRAME);
            }).element(elementBuilder -> {
                elementBuilder.
                        from(0, 0, 15).
                        to(16, 1, 16).
                        textureAll(FRAME);
            });
        });
        Variant frameVariant = new Variant(frameTemplate.build().create(
                block,
                new TextureMapping().
                        put(FRAME, TextureMapping.getBlockTexture(block, "_frame")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_frame")),
                blockModels.modelOutput
        ));
        Variant insideVariant = new Variant(insideTemplate.build().create(
                block,
                new TextureMapping().
                        put(INSIDE, TextureMapping.getBlockTexture(Blocks.GLASS)).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.GLASS)),
                blockModels.modelOutput
        ));
        ResourceLocation displayModelLocation = displayTemplate.build().create(
                block,
                new TextureMapping().
                        put(INSIDE, TextureMapping.getBlockTexture(Blocks.GLASS)).
                        put(FRAME, TextureMapping.getBlockTexture(block, "_frame")).
                        put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.GLASS)),
                blockModels.modelOutput
        );
        blockModels.registerSimpleItemModel(block, displayModelLocation);

        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(block).
                        with(BlockModelGenerators.variant(frameVariant)).
                        with(BlockModelGenerators.variant(insideVariant)));
    }
}
