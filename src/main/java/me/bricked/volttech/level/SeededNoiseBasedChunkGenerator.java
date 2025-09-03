package me.bricked.volttech.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SeededNoiseBasedChunkGenerator extends NoiseBasedChunkGenerator {
    private final long seed;
    private RandomState newRandomState;
    private ChunkGeneratorStructureState newStructureState;
    public static final MapCodec<SeededNoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            p_255585_ -> p_255585_.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings),
                            Codec.LONG.fieldOf("seed").forGetter(SeededNoiseBasedChunkGenerator::getSeed)
                    )
                    .apply(p_255585_, p_255585_.stable(SeededNoiseBasedChunkGenerator::new))
    );
    public SeededNoiseBasedChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, long seed) {
        super(biomeSource, settings);
        this.seed = seed;
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunk) {
        if (newRandomState == null) {
            RegistryAccess registryAccess = structureManager.registryAccess();
            createStates(registryAccess);
        }
        return super.createBiomes(newRandomState, blender, structureManager, chunk);
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkAccess chunk, StructureTemplateManager structureTemplateManager, ResourceKey<Level> level) {
        if (newStructureState == null) {
            createStates(registryAccess);
        }
        super.createStructures(registryAccess, newStructureState, structureManager, chunk, structureTemplateManager, level);
    }

    @Override
    public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
        if (newRandomState == null) {
            RegistryAccess registryAccess = structureManager.registryAccess();
            createStates(registryAccess);
        }
        super.applyCarvers(level, this.seed, this.newRandomState, biomeManager, structureManager, chunk);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        if (newRandomState == null) {
            RegistryAccess registryAccess = structureManager.registryAccess();
            createStates(registryAccess);
        }
        return super.fillFromNoise(blender, newRandomState, structureManager, chunk);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        return super.getBaseHeight(x, z, type, level, newRandomState == null ? random : newRandomState);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
        return super.getBaseColumn(x, z, height, newRandomState == null ? random : newRandomState);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
        super.addDebugScreenInfo(info, newRandomState == null ? random : newRandomState, pos);
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
        if (newRandomState == null) {
            RegistryAccess registryAccess = structureManager.registryAccess();
            createStates(registryAccess);
        }
        super.buildSurface(level, structureManager, newRandomState, chunk);
    }

    @Override
    public void buildSurface(ChunkAccess chunk, WorldGenerationContext context, RandomState random, StructureManager structureManager, BiomeManager biomeManager, Registry<Biome> biomes, Blender blender) {
        if (newRandomState == null) {
            RegistryAccess registryAccess = structureManager.registryAccess();
            createStates(registryAccess);
        }
        super.buildSurface(chunk, context, newRandomState, structureManager, biomeManager, biomes, blender);
    }

    private void createStates(RegistryAccess registryAccess) {
        this.newRandomState = RandomState.create(this.generatorSettings().value(), registryAccess.lookupOrThrow(Registries.NOISE), seed);
        this.newStructureState = this.createState(registryAccess.lookupOrThrow(Registries.STRUCTURE_SET), newRandomState, seed);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public long getSeed() {
        return seed;
    }
}
