package me.bricked.volttech.network.c2s;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.NetworkRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HarvesterOffsetsPayload(BlockPos blockPos, BlockPos offset, BlockPos size, boolean shouldRenderBox) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<HarvesterOffsetsPayload> TYPE = new CustomPacketPayload.Type<>(VoltTech.resourceLocation("harvester_settings"));;

    public static final StreamCodec<ByteBuf, HarvesterOffsetsPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, HarvesterOffsetsPayload::blockPos,
            BlockPos.STREAM_CODEC, HarvesterOffsetsPayload::offset,
            BlockPos.STREAM_CODEC, HarvesterOffsetsPayload::size,
            ByteBufCodecs.BOOL, HarvesterOffsetsPayload::shouldRenderBox,
            HarvesterOffsetsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
