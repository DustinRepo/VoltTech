package me.bricked.volttech.util;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public record Constraints(int maxCapacity, int maxInput, int maxOutput, int usageOrGeneration) {
    public static Codec<Constraints> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("maxCapacity").forGetter(Constraints::maxCapacity),
                    Codec.INT.fieldOf("maxInput").forGetter(Constraints::maxInput),
                    Codec.INT.fieldOf("maxOutput").forGetter(Constraints::maxOutput),
                    Codec.INT.fieldOf("usageOrGeneration").forGetter(Constraints::usageOrGeneration)
            ).apply(instance, Constraints::new)
    );
    public static StreamCodec<ByteBuf, Constraints> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, Constraints::maxCapacity,
            ByteBufCodecs.VAR_INT, Constraints::maxInput,
            ByteBufCodecs.VAR_INT, Constraints::maxOutput,
            ByteBufCodecs.VAR_INT, Constraints::usageOrGeneration,
            Constraints::new
    );
    public static Constraints singleValue(int value, int use) {
        return new Constraints(value, value, value, use);
    }

    private static final HashMap<String, Constraints> constraintsMap = Maps.newHashMap();
    private static final File CONFIG_DIR = new File(FMLPaths.CONFIGDIR.get().toFile(), "volt_constraints");

    public static void loadDefaults() {
        firstAdd("jetpack", new Constraints(10000, 250, 0, 1));
        firstAdd("torch_dispenser", new Constraints(1000, 100, 0, 10));
        firstAdd("infinite_torch_dispenser", new Constraints(5000, 500, 0, 10));
        firstAdd("mechanized_ender_pearl", new Constraints(5000, 250, 0, 150));
        firstAdd("homing_drive", new Constraints(25000, 500, 0, 25000));
        firstAdd("grave_transporter", new Constraints(50000, 500, 0, 50000));
        firstAdd("portal_gun", new Constraints(50000, 500, 0, 1000));
        firstAdd("dalekanium_pickaxe", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_axe", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_shovel", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_sword", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_hoe", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_helmet", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_chestplate", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_leggings", new Constraints(100000, 1000, 0, 50));
        firstAdd("dalekanium_boots", new Constraints(100000, 1000, 0, 50));
        firstAdd("demonic_crucible", new Constraints(100000, 1000, 0, 50));
        firstAdd("copper_energy_cable", singleValue(16000, 0));
        firstAdd("iron_energy_cable", singleValue(64000, 0));
        firstAdd("gold_energy_cable", singleValue(256000, 0));
        firstAdd("diamond_energy_cable", singleValue(1000000, 0));
        firstAdd("emerald_energy_cable", singleValue(5000000, 0));
        firstAdd("netherite_energy_cable", singleValue(Integer.MAX_VALUE, 0));
        firstAdd("wireless_energy_transmitter", singleValue(Integer.MAX_VALUE, 0));
        firstAdd("copper_fluid_pipe", singleValue(1000, 0));
        firstAdd("iron_fluid_pipe", singleValue(16000, 0));
        firstAdd("gold_fluid_pipe", singleValue(64000, 0));
        firstAdd("diamond_fluid_pipe", singleValue(256000, 0));
        firstAdd("emerald_fluid_pipe", singleValue(1000000, 0));
        firstAdd("netherite_fluid_pipe", singleValue(Integer.MAX_VALUE, 0));
        firstAdd("small_fluid_tank", new Constraints(16000, 0, 0, 0));
        firstAdd("medium_fluid_tank", new Constraints(64000, 0, 0, 0));
        firstAdd("large_fluid_tank", new Constraints(256000, 0, 0, 0));
        firstAdd("massive_fluid_tank", new Constraints(1024000, 0, 0, 0));
        firstAdd("creative_fluid_tank", singleValue(Integer.MAX_VALUE, 0));
        firstAdd("small_energy_cube", new Constraints(100000, 5000, 5000, 0));
        firstAdd("medium_energy_cube", new Constraints(500000, 25000, 25000, 0));
        firstAdd("large_energy_cube", new Constraints(2000000, 100000, 100000, 0));
        firstAdd("massive_energy_cube", new Constraints(10000000, 500000, 500000, 0));
        firstAdd("creative_energy_cube", singleValue(Integer.MAX_VALUE, 0));
        firstAdd("wireless_player_charger", singleValue(Integer.MAX_VALUE, 0));
        firstAdd("harvester", new Constraints(5000, 100, 0, 50));
        firstAdd("block_breaker", new Constraints(5000, 100, 0, 50));
        firstAdd("block_placer", new Constraints(5000, 100, 0, 50));
        firstAdd("solar_generator", new Constraints(10000, 0, 200, 50));
        firstAdd("combustion_generator", new Constraints(50000, 0, 50, 10));
        firstAdd("heat_generator", new Constraints(25000, 0, 1000, 40));
        firstAdd("mini_reactor", new Constraints(1000000, 0, 10000, 8000));
        firstAdd("temporal_accelorator", new Constraints(10000, 5000, 0, 1000));
        firstAdd("powered_furnace", new Constraints(10000, 250, 0, 200));
        firstAdd("crusher", new Constraints(10000, 250, 0, 200));
        firstAdd("food_masher", new Constraints(5000, 250, 0, 200));
        firstAdd("small_battery", new Constraints(50000, 1000, 500, 0));
        firstAdd("medium_battery", new Constraints(500000, 5000, 1000, 0));
        firstAdd("large_battery", new Constraints(2000000, 25000, 10000, 0));
        firstAdd("massive_battery", new Constraints(5000000, 100000, 50000, 0));
        firstAdd("creative_battery", Constraints.singleValue(Integer.MAX_VALUE, 0));
        firstAdd("atomic_battery", new Constraints(5, 0, 5, 0));
    }

    public static void writeConfigs() {
        if (!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdirs();
        }
        constraintsMap.forEach((s, constraints) -> {
            String fileName = s + ".json";
            File file = new File(CONFIG_DIR, fileName);
            try {
                if (file.exists())
                    file.delete();
                JsonElement json = Constraints.CODEC.encodeStart(JsonOps.INSTANCE, constraints).getOrThrow();
                Files.writeString(file.toPath(), StringifyUtil.GSON.toJson(json), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void readConfigs() {
        if (!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdirs();
            writeConfigs();
            return;
        }

        File[] files = CONFIG_DIR.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length <= 0) {
            writeConfigs();
            return;
        }
        for (File file : files) {
            String name = file.getName().replace(".json", "");
            try {
                String read = Files.readString(file.toPath());
                Constraints constraints = Constraints.CODEC.parse(JsonOps.INSTANCE, GsonHelper.parse(read)).getOrThrow();
                add(name, constraints);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Constraints get(Block block) {
        String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
        return constraintsMap.get(id);
    }

    public static Constraints get(Item item) {
        String id = BuiltInRegistries.ITEM.getKey(item).getPath();
        return constraintsMap.get(id);
    }

    private static void firstAdd(String name, Constraints constraints) {
        add(name, constraints);
        File file = new File(CONFIG_DIR, name + ".json");
        if (!file.exists()) {
            try {
                JsonElement json = Constraints.CODEC.encodeStart(JsonOps.INSTANCE, constraints).getOrThrow();
                Files.writeString(file.toPath(), StringifyUtil.GSON.toJson(json), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void add(String string, Constraints constraints) {
        if (constraintsMap.containsKey(string))
            constraintsMap.replace(string, constraints);
        else
            constraintsMap.put(string, constraints);
    }

    public static void setConstraintsMap(HashMap<String, Constraints> newMap) {
        newMap.forEach(Constraints::add);
    }

    public static HashMap<String, Constraints> getConstraintsMap() {
        return constraintsMap;
    }

    @Override
    public String toString() {
        return "Constraints{" +
                "maxCapacity=" + maxCapacity +
                ", maxInput=" + maxInput +
                ", maxOutput=" + maxOutput +
                '}';
    }
}
