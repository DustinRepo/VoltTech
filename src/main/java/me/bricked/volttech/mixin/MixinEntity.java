package me.bricked.volttech.mixin;

import me.bricked.volttech.register.AttributeRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(method = "isInWall", at = @At("HEAD"), cancellable = true)
    public void isInWall(CallbackInfoReturnable<Boolean> cir) {
        if (getThis() instanceof Player player) {
            if (player.getAttributeValue(AttributeRegistry.PHASE) >= 1)
                cir.setReturnValue(false);
        }
    }

    public Entity getThis() {
        return (Entity)(Object)this;
    }
}
