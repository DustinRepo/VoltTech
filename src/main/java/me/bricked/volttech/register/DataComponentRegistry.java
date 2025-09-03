package me.bricked.volttech.register;

import com.mojang.serialization.Codec;
import me.bricked.volttech.VoltTech;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.function.Supplier;

public class DataComponentRegistry {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, VoltTech.MODID);
    public static Supplier<DataComponentType<Integer>> STORED_FE;
    public static Supplier<DataComponentType<Integer>> STORED_TORCHES;
    public static Supplier<DataComponentType<Vector3f>> STORED_POSITION;
    public static Supplier<DataComponentType<Integer>> STORED_DIRECTION;
    public static Supplier<DataComponentType<String>> STORED_DIMENSION;
    public static Supplier<DataComponentType<SimpleFluidContent>> FLUID;
    public static Supplier<DataComponentType<Boolean>> BUCKET_MODE;
    public static Supplier<DataComponentType<String>> PLAYER_UUID;
    public static Supplier<DataComponentType<Boolean>> ACTIVATE_JETPACK;
    public static Supplier<DataComponentType<Boolean>> SHARE_ENERGY;
    public static Supplier<DataComponentType<Boolean>> PORTAL_HOME;
    public static Supplier<DataComponentType<String>> PORTAL_SEED;
    public static Supplier<DataComponentType<Vec3>> PORTAL_LOCATION;

    public static Supplier<DataComponentType<Integer>> NUM_ENERGY_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_SPEED_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_NIGHTVISION_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_JUMPBOOST_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_JETPACK_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_FEATHER_FALLING_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_HEALTH_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_WATER_BREATH_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_AUTO_FEED_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_FIRE_RESIST_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_WITHER_RESIST_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_POISON_RESIST_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_MAGNET_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_PHASE_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_REGENERATION_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_STEP_HEIGHT_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_REACH_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_MINE_SPEED_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_MINE_AREA_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_EVASION_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_SMELTER_UPGRADES;
    public static Supplier<DataComponentType<Integer>> NUM_DAMAGE_UPGRADES;

    public static ArrayList<Supplier<DataComponentType<Integer>>> UPGRADE_COMPONENTS = new ArrayList<>();

    public static void registerDataComponents(IEventBus modEventBus) {
        STORED_FE = DATA_COMPONENTS.registerComponentType(
                "stored_fe",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.NON_NEGATIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        );
        STORED_TORCHES = DATA_COMPONENTS.registerComponentType(
                "stored_torches",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.NON_NEGATIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        );
        STORED_POSITION = DATA_COMPONENTS.registerComponentType(
                "stored_position",
                vec3Builder -> vec3Builder.
                        persistent(ExtraCodecs.VECTOR3F).
                        networkSynchronized(ByteBufCodecs.VECTOR3F)
        );
        STORED_DIRECTION = DATA_COMPONENTS.registerComponentType(
                "stored_direction",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        );
        STORED_DIMENSION = DATA_COMPONENTS.registerComponentType(
                "stored_dimension",
                stringBuilder -> stringBuilder.
                        persistent(ExtraCodecs.NON_EMPTY_STRING).
                        networkSynchronized(ByteBufCodecs.STRING_UTF8)
        );
        FLUID = DATA_COMPONENTS.registerComponentType(
                "fluid",
                simpleFluidContentBuilder -> simpleFluidContentBuilder.
                        persistent(SimpleFluidContent.CODEC).
                        networkSynchronized(SimpleFluidContent.STREAM_CODEC)
        );
        BUCKET_MODE = DATA_COMPONENTS.registerComponentType(
                "bucket_mode",
                booleanBuilder -> booleanBuilder.
                        persistent(Codec.BOOL).
                        networkSynchronized(ByteBufCodecs.BOOL)
        );
        SHARE_ENERGY = DATA_COMPONENTS.registerComponentType(
                "share_energy",
                booleanBuilder -> booleanBuilder.
                        persistent(Codec.BOOL).
                        networkSynchronized(ByteBufCodecs.BOOL)
        );
        PORTAL_HOME = DATA_COMPONENTS.registerComponentType(
                "portal_home",
                booleanBuilder -> booleanBuilder.
                        persistent(Codec.BOOL).
                        networkSynchronized(ByteBufCodecs.BOOL)
        );
        PORTAL_SEED = DATA_COMPONENTS.registerComponentType(
                "portal_seed",
                booleanBuilder -> booleanBuilder.
                        persistent(Codec.STRING).
                        networkSynchronized(ByteBufCodecs.STRING_UTF8)
        );
        PORTAL_LOCATION = DATA_COMPONENTS.registerComponentType(
                "portal_location",
                blockPosBuilder -> blockPosBuilder.
                        persistent(Vec3.CODEC).
                        networkSynchronized(Vec3.STREAM_CODEC)
        );
        ACTIVATE_JETPACK = DATA_COMPONENTS.registerComponentType(
                "activate_jetpack",
                booleanBuilder -> booleanBuilder.
                        persistent(Codec.BOOL).
                        networkSynchronized(ByteBufCodecs.BOOL)
        );
        PLAYER_UUID = DATA_COMPONENTS.registerComponentType(
                "player_uuid",
                stringBuilder -> stringBuilder.
                        persistent(ExtraCodecs.NON_EMPTY_STRING).
                        networkSynchronized(ByteBufCodecs.STRING_UTF8)
        );

        UPGRADE_COMPONENTS.add(NUM_ENERGY_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "energy_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_SPEED_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "speed_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_NIGHTVISION_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "night_vision_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_JUMPBOOST_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "jump_boost_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_JETPACK_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "jetpack_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_FEATHER_FALLING_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "feather_falling_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_HEALTH_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "health_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_WATER_BREATH_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "water_breathing_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_AUTO_FEED_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "auto_feed_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_FIRE_RESIST_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "fire_resist_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_WITHER_RESIST_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "wither_resist_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_POISON_RESIST_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "poison_resist_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_MAGNET_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "magnet_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_PHASE_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "phase_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_REGENERATION_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "regen_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_STEP_HEIGHT_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "step_height_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_REACH_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "reach_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_MINE_SPEED_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "mining_speed_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_MINE_AREA_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "mining_area_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_EVASION_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "evasion_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_SMELTER_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "smelter_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));
        UPGRADE_COMPONENTS.add(NUM_DAMAGE_UPGRADES = DATA_COMPONENTS.registerComponentType(
                "damage_upgrades",
                integerBuilder -> integerBuilder.
                        persistent(ExtraCodecs.POSITIVE_INT).
                        networkSynchronized(ByteBufCodecs.VAR_INT)
        ));

        DATA_COMPONENTS.register(modEventBus);
    }

    public static boolean isUpgradeComponent(DataComponentType<?> dataComponentType) {
        for (Supplier<DataComponentType<Integer>> upgradeComponent : UPGRADE_COMPONENTS) {
            if (upgradeComponent.get() == dataComponentType)
                return true;
        }
        return false;
    }
}
