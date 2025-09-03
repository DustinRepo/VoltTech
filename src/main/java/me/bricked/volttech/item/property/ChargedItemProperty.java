package me.bricked.volttech.item.property;

import com.mojang.serialization.MapCodec;
import me.bricked.volttech.util.EnergyUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ChargedItemProperty implements ConditionalItemModelProperty {
    public static final MapCodec<ChargedItemProperty> MAP_CODEC = MapCodec.unit(new ChargedItemProperty());
    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return EnergyUtil.getStoredEnergy(itemStack) > 0;
    }
}
