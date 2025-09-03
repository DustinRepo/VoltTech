package me.bricked.volttech.blockentity;

import me.bricked.volttech.register.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkLoaderBlockEntity extends BlockEntity {
    public ChunkLoaderBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.CHUNK_LOADER_BLOCK_ENTITY.get(), pos, blockState);
    }
    private int checkTicks = 0;

    public static void tick(Level level, BlockPos pos, BlockState state, ChunkLoaderBlockEntity blockEntity) {
        // TODO: make a custom BlockEntityRenderer that uses an outline for base block (like Mini Reactor)
        // and have the hazard block render rotating inside
        if (level.isClientSide())
            return;
        if (blockEntity.checkTicks > 0) {
            blockEntity.checkTicks--;
            return;
        }
        blockEntity.checkTicks = 20; // Only check once per second
        ServerLevel serverLevel = (ServerLevel) level;
        LevelChunk chunk = level.getChunkAt(pos);
        for (Long chunkLong : serverLevel.getForceLoadedChunks()) {
            ChunkPos chunkPos = new ChunkPos(chunkLong);
            if (chunk.getPos().equals(chunkPos))
                return;
        }
        serverLevel.setChunkForced(chunk.getPos().x, chunk.getPos().z, true);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (getLevel() instanceof ServerLevel serverLevel) {
            LevelChunk chunk = serverLevel.getChunkAt(pos);
            serverLevel.setChunkForced(chunk.getPos().x, chunk.getPos().z, false);
        }
    }
}
