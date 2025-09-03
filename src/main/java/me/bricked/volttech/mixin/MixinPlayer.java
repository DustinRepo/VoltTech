package me.bricked.volttech.mixin;

import me.bricked.volttech.register.AttributeRegistry;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "getFlyingSpeed", at = @At("RETURN"), cancellable = true)
    public void getFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        if (getThis().getAttributeValue(AttributeRegistry.JETPACK_FLIGHT) < 1.0)
            return;
        float speed = cir.getReturnValue();
        cir.setReturnValue((float) (speed + getThis().getAttributeValue(AttributeRegistry.JETPACK_SPEED)));
    }

    public Player getThis() {
        return (Player)(Object)this;
    }
}
