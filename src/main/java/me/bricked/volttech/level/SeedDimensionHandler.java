package me.bricked.volttech.level;

import com.google.common.collect.ImmutableList;
import me.bricked.volttech.network.s2c.AddDimensionPayload;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.storage.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.Executor;

// A modified copy of https://github.com/Commoble/infiniverse/blob/main/src/main/java/net/commoble/infiniverse/internal/DimensionManager.java
public class SeedDimensionHandler {

    public static ServerLevel getOrCreateLevel(MinecraftServer server, OptionalLong seedOptional) {
        Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap(); // server.levels
        long seed = seedOptional.orElse(RandomSource.create().nextLong());
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("volt_seed", String.valueOf(seed)));
        ServerLevel existingLevel = map.get(key);

        return existingLevel == null
                ? createAndRegisterLevel(server, map, key, seed)
                : existingLevel;
    }

    // Most of this just comes straight from MinecraftServer::createLevels
    private static ServerLevel createAndRegisterLevel(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> map, ResourceKey<Level> levelKey, long seed) {
        // get everything we need to create the dimension and the level
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);

        // dimension keys have a 1:1 relationship with level keys, they have the same IDs as well
        ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registries.LEVEL_STEM, levelKey.location());
        Executor executor = server.executor;
        WorldData worldData = server.getWorldData();
        ChunkProgressListener chunkProgressListener = server.progressListenerFactory.create(worldData.getGameRules().getInt(GameRules.RULE_SPAWN_CHUNK_RADIUS));
        LevelStorageSource.LevelStorageAccess anvilConverter = server.storageSource;
        DerivedLevelData derivedLevelData = new DerivedLevelData(worldData, worldData.overworldData());

        // Create new LevelStem
        ChunkGenerator oldChunkGenerator = overworld.getChunkSource().getGenerator();
        Holder<NoiseGeneratorSettings> settings = Holder.direct(NoiseGeneratorSettings.dummy());
        if (oldChunkGenerator instanceof NoiseBasedChunkGenerator originalNoiseGen) {
            settings = originalNoiseGen.generatorSettings();
        }
        // Create a modified chunk generator that uses the given seed
        SeededNoiseBasedChunkGenerator chunkGenerator = new SeededNoiseBasedChunkGenerator(
                oldChunkGenerator.getBiomeSource(),
                settings,
                seed
        );
        Holder<DimensionType> typeHolder = overworld.dimensionTypeRegistration();
        LevelStem dimension = new LevelStem(typeHolder, chunkGenerator);

        // now we have everything we need to create the dimension and the level
        // this is the same order server init creates levels:
        // the dimensions are already registered when levels are created, we'll do that first
        // then instantiate level, add border listener, add to map, fire world load event

        // register the actual dimension
        Registry<LevelStem> dimensionRegistry = server.registryAccess().lookupOrThrow(Registries.LEVEL_STEM);
        if (dimensionRegistry instanceof MappedRegistry<LevelStem> writableRegistry) {
            writableRegistry.unfreeze(false);
            writableRegistry.register(dimensionKey, dimension, RegistrationInfo.BUILT_IN);
        } else {
            throw new IllegalStateException(String.format("Unable to register dimension %s -- dimension registry not writable", dimensionKey.location()));
        }

        // Create the level instance
        ServerLevel newLevel = new ServerLevel(
                server,
                executor,
                anvilConverter,
                derivedLevelData,
                levelKey,
                dimension,
                chunkProgressListener,
                worldData.isDebugWorld(),
                BiomeManager.obfuscateSeed(seed),
                ImmutableList.of(), // "special spawn list"
                false, // "tick time", true for overworld, always false for nether, end, and json dimensions
                null // as of 1.20.1 this argument is always null in vanilla, indicating the level should load the sequence from storage
        );
        // add world border listener, for parity with json dimensions
        // the vanilla behaviour is that world borders exist in every dimension simultaneously with the same size and position
        // these border listeners are automatically added to the overworld as worlds are loaded, so we should do that here too
        // TODO if world-specific world borders are ever added, change it here too
        overworld.getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(newLevel.getWorldBorder()));

        // register level
        map.put(levelKey, newLevel);

        // update forge's world cache so the new level can be ticked
        server.markWorldsDirty(); // server.worldArrayMarker++;

        // fire world load event
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(newLevel));

        // update clients' dimension lists
        AddDimensionPayload payload = new AddDimensionPayload(levelKey);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.connection.hasChannel(payload))
                PacketDistributor.sendToPlayer(player, payload);
        }

        return newLevel;
    }
}
