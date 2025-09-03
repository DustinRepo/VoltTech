package me.bricked.volttech.event;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.AttributeRegistry;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.EnergyUtil;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = VoltTech.MODID)
public class EntityEvents {

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, AttributeRegistry.PHASE);
        event.add(EntityType.PLAYER, AttributeRegistry.EVASION);
        event.add(EntityType.PLAYER, AttributeRegistry.JETPACK_FLIGHT);
        event.add(EntityType.PLAYER, AttributeRegistry.JETPACK_SPEED);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre livingDamageEvent) {
        LivingEntity livingEntity = livingDamageEvent.getEntity();
        AttributeInstance evasionInstance = livingEntity.getAttribute(AttributeRegistry.EVASION);
        if (evasionInstance != null) {
            double evadeChance = Math.clamp(evasionInstance.getValue(), 0, 0.5);
            if (Math.random() <= evadeChance) {
                livingDamageEvent.setNewDamage(0.f);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre entityTickEvent) {
        Entity entity = entityTickEvent.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            int hellforgedCount = 0;
            int nvUpgrades = 0;
            int autoFeedUpgrades = 0;
            int fireResistUpgrades = 0;
            int witherResistUpgrades = 0;
            int poisonResistUpgrades = 0;
            int magnetUpgrades = 0;
            int regenUpgrades = 0;
            for (EquipmentSlot value : EquipmentSlot.values()) {
                if (!livingEntity.hasItemInSlot(value))
                    continue;
                ItemStack equipmentStack = livingEntity.getItemBySlot(value);
                if (value.isArmor()) {
                    if (isHellforgedArmor(equipmentStack.getItem()))
                        hellforgedCount++;
                }
                if (ItemRegistry.AUTO_FEED_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value))
                    autoFeedUpgrades += ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.AUTO_FEED_UPGRADE_CARD.get());
                if (ItemRegistry.FIRE_RESIST_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value))
                    fireResistUpgrades += ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.FIRE_RESIST_UPGRADE_CARD.get());
                if (ItemRegistry.WITHER_RESIST_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value))
                    witherResistUpgrades += ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.WITHER_RESIST_UPGRADE_CARD.get());
                if (ItemRegistry.POISON_RESIST_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value))
                    poisonResistUpgrades += ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.POISON_RESIST_UPGRADE_CARD.get());
                if (ItemRegistry.MAGNET_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value))
                    magnetUpgrades += ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.MAGNET_UPGRADE_CARD.get());
                if (ItemRegistry.REGENERATION_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value))
                    regenUpgrades += ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.REGENERATION_UPGRADE_CARD.get());
                if (ItemRegistry.NIGHT_VISION_UPGRADE_CARD.get().getEquipmentSlotGroup().test(value) && nvUpgrades == 0)
                    nvUpgrades = ItemUtil.getUpgradeCount(equipmentStack, ItemRegistry.NIGHT_VISION_UPGRADE_CARD.get());
            }
            if (hellforgedCount == 4)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 310, 0, false, false, false));
            if (nvUpgrades > 0)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 310, 0, false, false, false));
            if (regenUpgrades > 0)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 310, regenUpgrades - 1, false, false, false));
            if (fireResistUpgrades > 0) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 310, 0, false, false, false));
                if (livingEntity.isOnFire())
                    livingEntity.extinguishFire();
            }
            if (witherResistUpgrades > 0) {
                if (livingEntity.hasEffect(MobEffects.WITHER))
                    livingEntity.removeEffect(MobEffects.WITHER);
            }
            if (poisonResistUpgrades > 0) {
                if (livingEntity.hasEffect(MobEffects.POISON))
                    livingEntity.removeEffect(MobEffects.POISON);
            }
            if (livingEntity instanceof Player player) {
                if (autoFeedUpgrades > 0) {
                    if (player.getFoodData().needsFood()) {
                        Inventory playerInventory = player.getInventory();
                        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
                            ItemStack itemStack = playerInventory.getItem(i);
                            // Only allow edible slop instead of all foods
                            // TODO: maybe find a good way to have settings for Auto Eat like selecting specific foods
                            // or disabling foods that give debuff effects
                            if (itemStack.isEmpty() || itemStack.getItem() != ItemRegistry.EDIBLE_SLOP.get())
                                continue;
                            FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
                            Consumable consumable = itemStack.get(DataComponents.CONSUMABLE);
                            if (foodProperties != null && consumable != null) {
                                player.getFoodData().eat(foodProperties);
                                ItemStack ret = consumable.onConsume(player.level(), player, itemStack);
                                playerInventory.setItem(i, ret);
                                break;
                            }
                        }
                    }
                }
                if (magnetUpgrades > 0) {
                    Level level = player.level();
                    float size = 5.f * magnetUpgrades;
                    level.getEntities(player, AABB.ofSize(player.position(), size, size, size), entity1 -> entity1 instanceof ItemEntity || entity1 instanceof ExperienceOrb).forEach(ent -> {
                        ent.playerTouch(player);
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTickPost(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.getAttributeValue(AttributeRegistry.JETPACK_FLIGHT) >= 1.0) {
                ItemStack dischargeStack = player.getItemBySlot(EquipmentSlot.CHEST);
                // Chest slot wasn't the jetpack, try to find it
                if (!ItemUtil.isJetpackEnabled(dischargeStack)) {
                    for (EquipmentSlot value : EquipmentSlot.values()) {
                        ItemStack testStack = player.getItemBySlot(value);
                        if (ItemUtil.isJetpackEnabled(testStack)) {
                            dischargeStack = testStack;
                            break;
                        }
                    }
                }
                if (!player.isCreative()) {
                    EnergyUtil.useStoredEnergy(dischargeStack, 1);
                    if (!ItemUtil.hasJetpackCharge(dischargeStack))
                        ItemUtil.setJetpackEnabled(dischargeStack, false);
                }
                player.resetFallDistance(); // Reset fall damage if flying
            }
        }
    }

    @SubscribeEvent
    public static void onLivingBreathe(LivingBreatheEvent event) {
        boolean hasWaterBreathing = false;
        LivingEntity livingEntity = event.getEntity();
        for (EquipmentSlot value : EquipmentSlot.values()) {
            if (!livingEntity.hasItemInSlot(value))
                continue;
            ItemStack armorStack = livingEntity.getItemBySlot(value);
            if (ItemUtil.getUpgradeCount(armorStack, ItemRegistry.WATER_BREATHING_UPGRADE_CARD.get()) > 0) {
                hasWaterBreathing = true;
                break;
            }
        }
        if (hasWaterBreathing)
            event.setCanBreathe(true);
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity livingEntity = event.getEntity();
        int featherFallingUpgrades = 0;
        for (EquipmentSlot value : EquipmentSlot.values()) {
            if (!livingEntity.hasItemInSlot(value))
                continue;
            ItemStack armorStack = livingEntity.getItemBySlot(value);
            featherFallingUpgrades += ItemUtil.getUpgradeCount(armorStack, ItemRegistry.FEATHER_FALLING_UPGRADE_CARD.get());
        }
        if (featherFallingUpgrades > 0) {
            float multiplier = event.getDamageMultiplier();
            float damageRemoved = multiplier * (0.1f * featherFallingUpgrades);
            event.setDamageMultiplier(multiplier - damageRemoved);
        }
    }

    private static boolean isHellforgedArmor(Item item) {
        return item == ItemRegistry.HELLFORGED_HELMET.get() ||
                item == ItemRegistry.HELLFORGED_CHESTPLATE.get() ||
                item == ItemRegistry.HELLFORGED_LEGGINGS.get() ||
                item == ItemRegistry.HELLFORGED_BOOTS.get();
    }
}
