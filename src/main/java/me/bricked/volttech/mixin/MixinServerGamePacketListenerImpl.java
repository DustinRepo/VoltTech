package me.bricked.volttech.mixin;

import me.bricked.volttech.register.AttributeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

    @Shadow public abstract ServerPlayer getPlayer();

    @Shadow private int aboveGroundTickCount;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) { // Prevent player from being fly kicked while jetpack flying
        if (this.getPlayer().getAttributeValue(AttributeRegistry.JETPACK_FLIGHT) >= 1.0)
            this.aboveGroundTickCount = 0;
    }
}
