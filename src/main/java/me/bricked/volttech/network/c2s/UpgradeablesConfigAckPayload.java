package me.bricked.volttech.network.c2s;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpgradeablesConfigAckPayload() implements CustomPacketPayload {
    public static final Type<UpgradeablesConfigAckPayload> TYPE = new Type<>(VoltTech.resourceLocation("upgradeables_ack"));
    
    // Unit codec with no data to write
    public static final StreamCodec<ByteBuf, UpgradeablesConfigAckPayload> STREAM_CODEC = StreamCodec.unit(new UpgradeablesConfigAckPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}