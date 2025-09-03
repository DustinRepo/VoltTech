package me.bricked.volttech.network.s2c;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.util.Constraints;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;

public record ConstraintsConfigPayload(HashMap<String, Constraints> constraintsMap) implements CustomPacketPayload {
    public static final Type<ConstraintsConfigPayload> TYPE = new Type<>(VoltTech.resourceLocation("constraints_config"));;
    public static final StreamCodec<ByteBuf, ConstraintsConfigPayload> STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.STRING_UTF8,
                    Constraints.STREAM_CODEC
            ).map(ConstraintsConfigPayload::new, ConstraintsConfigPayload::constraintsMap);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
