package me.bricked.volttech.util;

import me.bricked.volttech.entity.MultiversePortalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;

public class LevelUtil {
    public static MultiversePortalEntity portalInSpot(Level level, BlockPos pos) {
        for (Entity entity : level.getEntities(null, Shapes.box(-2, -4, -2, 2, 4, 2).bounds().move(pos))) {
            if (entity instanceof MultiversePortalEntity multiversePortalEntity)
                return multiversePortalEntity;
        }
        return null;
    }
}
