package me.bricked.volttech.network.s2c;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record AddDimensionPayload(ResourceKey<Level> key) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddDimensionPayload> TYPE = new CustomPacketPayload.Type<>(VoltTech.resourceLocation("update_dimensions"));;
    public static final StreamCodec<ByteBuf, AddDimensionPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), AddDimensionPayload::key,
            AddDimensionPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
