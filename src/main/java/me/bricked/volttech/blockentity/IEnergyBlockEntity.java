package me.bricked.volttech.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface IEnergyBlockEntity {
    IEnergyStorage getEnergyStorage(BlockState state, Direction direction);
}
