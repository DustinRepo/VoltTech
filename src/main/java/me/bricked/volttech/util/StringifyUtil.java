package me.bricked.volttech.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.bricked.volttech.VoltTech;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.text.DecimalFormat;

public class StringifyUtil {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final DecimalFormat singlePointFormat = new DecimalFormat("0.#");
    private static final DecimalFormat threePointFormat = new DecimalFormat("0.###");

    public static MutableComponent translate(String key) {
        return Component.translatable(VoltTech.MODID + "." + key);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable(VoltTech.MODID + "." + key, args);
    }

    public static String stringifyFluid(int fluid) {
        if (fluid > 1000) {
            float buckets = (float) fluid / 1000.f;
            return singlePointFormat.format(buckets) + " B";
        }
        return "%d mB".formatted(fluid);
    }

    public static String stringifyEnergy(int energy) {
        int thousand = 1000;
        int million = thousand * thousand;
        int billion = million * thousand;
        String energyType = "FE";
        if (energy > 10000) {
            if (energy >= billion)
                return threePointFormat.format((float) energy / billion) + " G" + energyType;
            if (energy >= million)
                return threePointFormat.format((float) energy / million) + " M" + energyType;
            return threePointFormat.format((float) energy / thousand) + " k" + energyType;
        }
        return "%d ".formatted(energy) + energyType;
    }

}
