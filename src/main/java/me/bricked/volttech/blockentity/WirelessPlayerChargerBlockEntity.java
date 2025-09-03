package me.bricked.volttech.blockentity;

import me.bricked.volttech.Config;
import me.bricked.volttech.capability.forgeenergy.PlayerChargingEnergyStorage;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.UUID;

public class WirelessPlayerChargerBlockEntity extends BlockEntity implements IEnergyBlockEntity {
    public WirelessPlayerChargerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.WIRELESS_PLAYER_CHARGER_BLOCK_ENTITY.get(), pos, blockState);
        energyStorage = new PlayerChargingEnergyStorage(Constraints.get(blockState.getBlock()).maxCapacity(), playerUUID);
    }
    private final PlayerChargingEnergyStorage energyStorage;
    private String playerUUID = "";

    public static void tick(Level level, BlockPos pos, BlockState state, WirelessPlayerChargerBlockEntity blockEntity) {
        String uuid = blockEntity.getPlayerUUID();
        if (level.isClientSide() || uuid.isEmpty())
            return;
        PlayerChargingEnergyStorage storage = blockEntity.energyStorage;
        if (storage.getServer() == null) {
            ServerLevel serverLevel = (ServerLevel) level;
            storage.setServer(serverLevel.getServer());
        }
        UUID currentUUID = UUID.fromString(uuid);
        if (storage.getUuid() == null || storage.getUuid().compareTo(currentUUID) != 0) {
            storage.setUuid(currentUUID);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (level.getBlockEntity(pos) instanceof WirelessPlayerChargerBlockEntity wirelessPlayerChargerBlockEntity) {
            ItemStack stack = new ItemStack(state.getBlock());
            String uuid = wirelessPlayerChargerBlockEntity.getPlayerUUID();
            if (uuid != null && !uuid.isEmpty()) {
                stack.set(DataComponentRegistry.PLAYER_UUID, uuid);
            }
            Containers.dropItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.2f, pos.getZ() + 0.5f, stack);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.energyStorage.serialize(output);
        if (playerUUID != null && !playerUUID.isEmpty())
            output.putString("playerUUID", playerUUID);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.energyStorage.deserialize(input);
        this.playerUUID = input.getStringOr("playerUUID", "");
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        if (direction == Direction.DOWN || direction == null)
            return energyStorage;
        return null;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }
}
