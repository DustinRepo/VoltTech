package me.bricked.volttech.network.c2s;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record JetpackTogglePayload(int slot) implements CustomPacketPayload {

    public static final Type<JetpackTogglePayload> TYPE = new Type<>(VoltTech.resourceLocation("jetpack_toggle"));

    public static final StreamCodec<ByteBuf, JetpackTogglePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, JetpackTogglePayload::slot,
            JetpackTogglePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
