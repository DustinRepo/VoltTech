package me.bricked.volttech.capability.forgeenergy;

import me.bricked.volttech.VoltTech;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerChargingEnergyStorage extends EnergyStorage {
    public int maxThroughput;
    private UUID uuid;
    private MinecraftServer server;

    public PlayerChargingEnergyStorage(int maxThroughput, String uuid) {
        super(maxThroughput);
        this.maxThroughput = maxThroughput;
        if (uuid != null && !uuid.isEmpty())
            this.uuid = UUID.fromString(uuid);
    }

    @Override
    public int receiveEnergy(int maxIn, boolean simulate) {
        int moved = 0;
        maxIn = Math.min(maxIn, maxThroughput);
        if (server == null || this.uuid == null)
            return 0;
        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
        if (player == null)
            return 0;
        ArrayList<ItemStack> stacksToCharge = new ArrayList<>(player.getInventory().getNonEquipmentItems());
        stacksToCharge.add(player.getOffhandItem());
        for (EquipmentSlot value : EquipmentSlot.values()) {
            stacksToCharge.add(player.getItemBySlot(value));
        }
        for (ItemStack itemStack : stacksToCharge) {
            if (maxIn == 0)
                return moved;
            if (itemStack.isEmpty())
                continue;
            IEnergyStorage storage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (storage == null)
                continue;
            if (storage.canReceive() && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                int transfer = storage.receiveEnergy(maxIn, false);
                moved += transfer;
                maxIn -= transfer;
            }
        }
        return moved;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }
}
