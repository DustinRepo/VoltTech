package me.bricked.volttech.fluid;

import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.FluidRegistry;
import me.bricked.volttech.register.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Optional;

public abstract class ArgentPlasmaFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return FluidRegistry.FLOWING_ARGENT_PLASMA.get();
    }

    @Override
    public Fluid getSource() {
        return FluidRegistry.ARGENT_PLASMA.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidRegistry.ARGENT_PLASMA_FLUID_TYPE;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {

    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return ItemRegistry.ARGENT_PLASMA_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.f;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return BlockRegistry.ARGENT_PLASMA.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == FluidRegistry.ARGENT_PLASMA.get() || fluid == FluidRegistry.FLOWING_ARGENT_PLASMA.get();
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    public boolean canConvertToSource(FluidState state, ServerLevel level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canConvertToSource(ServerLevel level) {
        return false;
    }

    public static class Source extends ArgentPlasmaFluid {
        public Source() {
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends ArgentPlasmaFluid {
        public Flowing() {
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }
}
