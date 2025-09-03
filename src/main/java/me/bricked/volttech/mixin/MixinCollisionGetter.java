package me.bricked.volttech.mixin;

import me.bricked.volttech.register.AttributeRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.StreamSupport;

@Mixin(CollisionGetter.class)
public interface MixinCollisionGetter {
    @Inject(method = "collidesWithSuffocatingBlock", at = @At("HEAD"), cancellable = true)
    private void collidesWithSuffocatingBlock(Entity entity, AABB box, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player) {
            if (player.getAttributeValue(AttributeRegistry.PHASE) >= 1)
                cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockCollisionsFromContext", at = @At("HEAD"), cancellable = true)
    private void getBlockCollisionsFromContext(CollisionContext context, AABB collisionBox, CallbackInfoReturnable<Iterable<VoxelShape>> cir) {
        if (!(context instanceof EntityCollisionContext entityCollisionContext))
            return;
        Entity entity = entityCollisionContext.getEntity();
        if (!(entity instanceof Player player))
            return;
        if (player.getAttributeValue(AttributeRegistry.PHASE) < 1)
            return;
        // Get original collisions and filter any blocks higher than the player out of it
        Iterable<VoxelShape> shapes = () -> new BlockCollisions<>((CollisionGetter) (Object) this, context, collisionBox, false, (blockPos, shape) -> shape);
        List<VoxelShape> ret = StreamSupport.stream(shapes.spliterator(), false).
                filter(shape -> {
                    Level level = entity.level();
                    BlockPos pos = BlockPos.containing(shape.min(Direction.Axis.X), shape.min(Direction.Axis.Y), shape.min(Direction.Axis.Z));
                    BlockState blockState = level.getBlockState(pos);
                    if (blockState.getDestroySpeed(level, pos) < 0 || blockState.is(TagRegistry.Blocks.PHASE_BANNED))
                        return true;
                    // Check for a height difference of more than 0.5 so we can still walk up half slabs
                    return Math.abs(shape.max(Direction.Axis.Y) - player.getY()) <= 0.5;
                }).
                toList();
        cir.setReturnValue(ret);
    }
}
