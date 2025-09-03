package me.bricked.volttech.network.s2c;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;

public record UpgradeablesConfigPayload(HashMap<String, UpgradeData> dataMap) implements CustomPacketPayload {
    public static final Type<UpgradeablesConfigPayload> TYPE = new Type<>(VoltTech.resourceLocation("upgradeables_config"));;
    public static final StreamCodec<ByteBuf, UpgradeablesConfigPayload> STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.STRING_UTF8,
                    UpgradeData.STREAM_CODEC
            ).map(UpgradeablesConfigPayload::new, UpgradeablesConfigPayload::dataMap);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
