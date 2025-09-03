package me.bricked.volttech.util;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.item.UpgradeCardItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public record UpgradeData(String itemID, Constraints baseEnergy, EnergyCardData energyCardData, List<String> bannedCards) {
    public static Codec<UpgradeData> CODEC = RecordCodecBuilder.create(upgradeDataInstance ->
            upgradeDataInstance.group(
                    Codec.STRING.fieldOf("itemID").forGetter(UpgradeData::itemID),
                    Constraints.CODEC.fieldOf("baseEnergy").forGetter(UpgradeData::baseEnergy),
                    EnergyCardData.CODEC.fieldOf("energyCardData").forGetter(UpgradeData::energyCardData),
                    Codec.STRING.listOf().fieldOf("bannedCards").forGetter(UpgradeData::bannedCards)
            ).
            apply(upgradeDataInstance, UpgradeData::new));
    public static StreamCodec<ByteBuf, UpgradeData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, UpgradeData::itemID,
            Constraints.STREAM_CODEC, UpgradeData::baseEnergy,
            EnergyCardData.STREAM_CODEC, UpgradeData::energyCardData,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), UpgradeData::bannedCards,
            UpgradeData::new
    );
    private static final HashMap<String, UpgradeData> dataMap = Maps.newHashMap();
    private static final ArrayList<String> mismatches = new ArrayList<>();

    public boolean isUpgradeBlocked(UpgradeCardItem item) {
        String id = BuiltInRegistries.ITEM.getKey(item).toString();
        return this.bannedCards().contains(id);
    }

    public static boolean isUpgradeableItem(Item item) {
        return get(item) != null;
    }

    public static UpgradeData get(Item item) {
        String id = BuiltInRegistries.ITEM.getKey(item).toString();
        UpgradeData data = dataMap.get(id);
        if (data != null && data.baseEnergy().maxCapacity() > 0) // anything less than 0 is a default upgradeable item the user doesn't want upgradeable
            return data;
        return null;
    }

    public static void loadDefaults() {
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_helmet").toString(),
                new Constraints(50000, 1000, 0, 150),
                new EnergyCardData(5000, 100, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_chestplate").toString(),
                new Constraints(50000, 1000, 0, 150),
                new EnergyCardData(5000, 100, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_leggings").toString(),
                new Constraints(50000, 1000, 0, 150),
                new EnergyCardData(5000, 100, 5, 10),
                List.of(
                        VoltTech.resourceLocation("phase_upgrade_card").toString()
                )
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_boots").toString(),
                new Constraints(50000, 1000, 0, 150),
                new EnergyCardData(5000, 100, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_pickaxe").toString(),
                new Constraints(30000, 500, 0, 150),
                new EnergyCardData(3000, 50, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_axe").toString(),
                new Constraints(30000, 500, 0, 150),
                new EnergyCardData(3000, 50, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_shovel").toString(),
                new Constraints(30000, 500, 0, 150),
                new EnergyCardData(3000, 50, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_sword").toString(),
                new Constraints(30000, 500, 0, 150),
                new EnergyCardData(3000, 50, 5, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("trexalite_hoe").toString(),
                new Constraints(30000, 500, 0, 150),
                new EnergyCardData(3000, 50, 5, 10),
                List.of()
        ));

        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_helmet").toString(),
                new Constraints(100000, 2500, 0, 100),
                new EnergyCardData(10000, 250, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_chestplate").toString(),
                new Constraints(100000, 2500, 0, 100),
                new EnergyCardData(10000, 250, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_leggings").toString(),
                new Constraints(100000, 2500, 0, 100),
                new EnergyCardData(10000, 250, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_boots").toString(),
                new Constraints(100000, 2500, 0, 100),
                new EnergyCardData(10000, 250, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_pickaxe").toString(),
                new Constraints(75000, 1000, 0, 100),
                new EnergyCardData(7500, 100, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_axe").toString(),
                new Constraints(75000, 1000, 0, 100),
                new EnergyCardData(7500, 100, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_shovel").toString(),
                new Constraints(75000, 1000, 0, 100),
                new EnergyCardData(7500, 100, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_sword").toString(),
                new Constraints(75000, 1000, 0, 100),
                new EnergyCardData(7500, 100, 7, 10),
                List.of()
        ));
        add(new UpgradeData(
                VoltTech.resourceLocation("veltrium_hoe").toString(),
                new Constraints(75000, 1000, 0, 100),
                new EnergyCardData(7500, 100, 7, 10),
                List.of()
        ));
    }

    public static void writeConfig() {
        File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "volttech_upgradeables.json");
        try {
            if (file.exists())
                file.delete();
            JsonElement json = UpgradeData.CODEC.listOf().encodeStart(JsonOps.INSTANCE, dataMap.values().stream().toList()).getOrThrow();
            Files.writeString(file.toPath(), StringifyUtil.GSON.toJson(json), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        File file = new File(FMLPaths.CONFIGDIR.get().toFile(), "volttech_upgradeables.json");
        if (!file.exists()) {
            writeConfig();
            return;
        }
        try {
            String read = Files.readString(file.toPath());
            List<UpgradeData> dataList = UpgradeData.CODEC.listOf().parse(JsonOps.INSTANCE, GsonHelper.parseArray(read)).getOrThrow();
            dataMap.clear();
            loadDefaults(); // Load defaults first to get any potential items in an update
            for (UpgradeData upgradeData : dataList) {
                add(upgradeData.itemID(), upgradeData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void add(UpgradeData data) {
        String string = data.itemID();
        add(string, data);
    }

    private static void add(String string, UpgradeData data) {
        if (dataMap.containsKey(string))
            dataMap.replace(string, data);
        else
            dataMap.put(string, data);
    }

    public static void setDataMap(HashMap<String, UpgradeData> newMap) {
        dataMap.clear(); // Clear before setting from packet
        newMap.forEach(UpgradeData::add);
    }

    public static HashMap<String, UpgradeData> getDataMap() {
        return dataMap;
    }

    public static ArrayList<String> getMismatches() {
        return mismatches;
    }

    @Override
    public String toString() {
        return "UpgradeData{" +
                "itemID='" + itemID + '\'' +
                ", constraints=" + baseEnergy.toString() +
                ", bannedCards=" + Arrays.toString(bannedCards.toArray()) +
                '}';
    }

    public record EnergyCardData(int energyAdd, int energyInputAdd, int useRemoval, int maxCards) {
        public static Codec<EnergyCardData> CODEC = RecordCodecBuilder.create(energyCardDataInstance -> energyCardDataInstance.group(
                Codec.INT.fieldOf("energyAdd").forGetter(EnergyCardData::energyAdd),
                Codec.INT.fieldOf("energyInputAdd").forGetter(EnergyCardData::energyInputAdd),
                Codec.INT.fieldOf("useRemoval").forGetter(EnergyCardData::useRemoval),
                Codec.INT.fieldOf("maxCards").forGetter(EnergyCardData::maxCards)
        ).apply(energyCardDataInstance, EnergyCardData::new));

        public static StreamCodec<ByteBuf, EnergyCardData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, EnergyCardData::energyAdd,
                ByteBufCodecs.VAR_INT, EnergyCardData::energyInputAdd,
                ByteBufCodecs.VAR_INT, EnergyCardData::useRemoval,
                ByteBufCodecs.VAR_INT, EnergyCardData::maxCards,
                EnergyCardData::new
        );
    }
}