package me.bricked.volttech.network.c2s;

import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public record SetPortalGunSeedPayload(int slot, String seed, boolean homeDim, PortalLocation location) implements CustomPacketPayload {

    public static final Type<SetPortalGunSeedPayload> TYPE = new Type<>(VoltTech.resourceLocation("set_portal_gun_seed"));

    public static final StreamCodec<ByteBuf, SetPortalGunSeedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SetPortalGunSeedPayload::slot,
            ByteBufCodecs.STRING_UTF8, SetPortalGunSeedPayload::seed,
            ByteBufCodecs.BOOL, SetPortalGunSeedPayload::homeDim,
            PortalLocation.CODEC, SetPortalGunSeedPayload::location,
            SetPortalGunSeedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record PortalLocation(Vec3 pos, boolean usePosition) {
        public static final StreamCodec<ByteBuf, PortalLocation> CODEC = StreamCodec.composite(
                Vec3.STREAM_CODEC, PortalLocation::pos,
                ByteBufCodecs.BOOL, PortalLocation::usePosition,
                PortalLocation::new
        );
    }
}
