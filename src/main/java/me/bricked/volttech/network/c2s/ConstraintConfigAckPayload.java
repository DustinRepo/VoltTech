package me.bricked.volttech.network.c2s;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ConstraintConfigAckPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ConstraintConfigAckPayload> TYPE = new CustomPacketPayload.Type<>(VoltTech.resourceLocation("cc_ack"));
    
    // Unit codec with no data to write
    public static final StreamCodec<ByteBuf, ConstraintConfigAckPayload> STREAM_CODEC = StreamCodec.unit(new ConstraintConfigAckPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}