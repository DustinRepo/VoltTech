package me.bricked.volttech.register;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.level.SeededNoiseBasedChunkGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ChunkGeneratorRegistry {
    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(BuiltInRegistries.CHUNK_GENERATOR, VoltTech.MODID);

    public static void registerChunkGenerators(IEventBus modEventBus) {
        CHUNK_GENERATORS.register(
                "seeded_noise",
                () -> SeededNoiseBasedChunkGenerator.CODEC
        );
        CHUNK_GENERATORS.register(modEventBus);
    }
}
