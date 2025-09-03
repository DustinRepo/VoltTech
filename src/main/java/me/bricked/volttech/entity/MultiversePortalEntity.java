package me.bricked.volttech.entity;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.level.SeedDimensionHandler;
import me.bricked.volttech.register.EntityRegistry;
import me.bricked.volttech.util.LevelUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalLong;

public class MultiversePortalEntity extends Entity {
    public MultiversePortalEntity(EntityType type, Level level) {
        super(type, level);
        this.seed = OptionalLong.empty();
    }
    public MultiversePortalEntity(EntityType type, Level level, OptionalLong seed) {
        super(type, level);
        this.seed = seed;
    }
    public MultiversePortalEntity(EntityType type, Level level, OptionalLong seed, Vec3 teleportLocation) {
        this(type, level, seed);
        this.teleportLocation = teleportLocation;
    }
    private static final int MAX_TICKS = 20 * 7;
    private OptionalLong seed;
    private Vec3 teleportLocation;
    private final ArrayList<Entity> ignored = new ArrayList<>();

    @Override
    public void tick() {
        if (level().isClientSide()) {
            super.tick();
            return;
        }
        super.tick();
        level().getEntities(this, this.getBoundingBox().inflate(10)).forEach(entity -> {
            if (ignored.contains(entity) && !entity.getBoundingBox().intersects(this.getBoundingBox()))
                ignored.remove(entity);
        });
        if (tickCount >= MAX_TICKS) {
            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void push(Entity entity) {
        if (ignored.contains(entity)) { // Don't let a player use this portal again until they leave it's hitbox
            return;
        }
        Level level = this.level();
        if (!(level instanceof ServerLevel currentLevel))
            return;
        MinecraftServer server = currentLevel.getServer();
        Vec3 teleportPos = teleportLocation == null ? entity.position() : teleportLocation;
        // Player just wants to go home, send them to overworld
        ServerLevel newLevel;
        if (seed.isEmpty())
            newLevel = server.overworld();
        else
            newLevel = SeedDimensionHandler.getOrCreateLevel(server, seed);

        // Create return portal
        MultiversePortalEntity existing = LevelUtil.portalInSpot(newLevel, BlockPos.containing(teleportPos));
        if (existing == null) {
            boolean isInOverworld = currentLevel.getSeed() == server.overworld().getSeed();
            MultiversePortalEntity newPortal = new MultiversePortalEntity(
                    EntityRegistry.MULTIVERSE_PORTAL.get(),
                    newLevel,
                    isInOverworld ? OptionalLong.empty() : OptionalLong.of(currentLevel.getSeed()),
                    this.position() // Set position of this portal as the new portal's teleport location
            );
            newPortal.setYRot(this.getYRot());
            newPortal.setXRot(this.getXRot());
            newPortal.getIgnored().add(entity);
            newPortal.setPos(Vec3.atBottomCenterOf(BlockPos.containing(teleportPos))); // Snap to the block grid
            if (isInOverworld)
                newPortal.setCustomName(StringifyUtil.translate("entity.multiverse_portal_home"));
            else
                newPortal.setCustomName(StringifyUtil.translate("entity.multiverse_portal_name", currentLevel.getSeed()));
            newLevel.addFreshEntity(newPortal);
        } else {
            existing.getIgnored().add(entity);
        }

        TeleportTransition transition = new TeleportTransition(newLevel, teleportPos, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), TeleportTransition.DO_NOTHING);
        entity.teleport(transition);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        Optional<Long> optional = input.getLong("seed");
        seed = optional.map(OptionalLong::of).orElseGet(OptionalLong::empty);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        if (!seed.isEmpty())
            output.putLong("seed", seed.getAsLong());
    }

    public OptionalLong getSeed() {
        return seed;
    }

    public void setSeed(OptionalLong seed) {
        this.seed = seed;
    }

    public ArrayList<Entity> getIgnored() {
        return ignored;
    }
}
