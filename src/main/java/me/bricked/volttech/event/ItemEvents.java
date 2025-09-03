package me.bricked.volttech.event;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.item.charge.IChargeableItem;
import me.bricked.volttech.item.UpgradeCardItem;
import me.bricked.volttech.register.AttributeRegistry;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@EventBusSubscriber(modid = VoltTech.MODID)
public class ItemEvents {
    static final ResourceLocation speedResourceLocation = VoltTech.resourceLocation("speed_mod");
    static final ResourceLocation jumpResourceLocation = VoltTech.resourceLocation("jump_mod");
    static final ResourceLocation healthResourceLocation = VoltTech.resourceLocation("health_mod");
    static final ResourceLocation phaseResourceLocation = VoltTech.resourceLocation("phase_mod");
    static final ResourceLocation stepResourceLocation = VoltTech.resourceLocation("step_mod");
    static final ResourceLocation reachResourceLocation = VoltTech.resourceLocation("reach_mod");
    static final ResourceLocation evasionResourceLocation = VoltTech.resourceLocation("evasion_mod");
    static final ResourceLocation damageResourceLocation = VoltTech.resourceLocation("damage_mod");
    static final ResourceLocation jetpackResourceLocation = VoltTech.resourceLocation("jetpack_mod");
    static final ResourceLocation armorResourceLocation = VoltTech.resourceLocation("armor_mod");

    @SubscribeEvent
    public static void runAttributes(ItemAttributeModifierEvent itemAttributeModifierEvent) {
        ItemStack itemStack = itemAttributeModifierEvent.getItemStack();
        boolean jetpackEnabled = ItemUtil.isJetpackEnabled(itemStack);
        if (!jetpackEnabled) {
            itemAttributeModifierEvent.removeModifier(AttributeRegistry.JETPACK_FLIGHT, jetpackResourceLocation);
            itemAttributeModifierEvent.removeModifier(AttributeRegistry.JETPACK_SPEED, jetpackResourceLocation);
        } else {
            itemAttributeModifierEvent.addModifier(AttributeRegistry.JETPACK_FLIGHT, new AttributeModifier(
                    jetpackResourceLocation,
                    1.0,
                    AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.ARMOR);
            int flyUpgrades = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.JETPACK_UPGRADE_CARD.get());
            if (flyUpgrades > 0) {
                itemAttributeModifierEvent.addModifier(AttributeRegistry.JETPACK_SPEED, new AttributeModifier(
                        jetpackResourceLocation,
                        flyUpgrades * 0.015,
                        AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.ARMOR);
            }
        }
        if (itemStack.getItem() instanceof IChargeableItem chargeableItem) {
            int energy = EnergyUtil.getStoredEnergy(itemStack);
            if (energy < chargeableItem.getLogisticalConstraints().usageOrGeneration())
                removeArmorEffects(itemAttributeModifierEvent, itemStack);
            else
                replaceArmorEffects(itemAttributeModifierEvent);
        } else if (UpgradeData.isUpgradeableItem(itemStack.getItem())) {
            if (ItemUtil.hasUpgradeApplied(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get())) {
                int energy = EnergyUtil.getStoredEnergy(itemStack);
                if (energy < ItemUtil.getEnergyUsage(itemStack))
                    removeArmorEffects(itemAttributeModifierEvent, itemStack);
                else
                    replaceArmorEffects(itemAttributeModifierEvent);
            }
            int phaseUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.PHASE_UPGRADE_CARD.get());
            if (phaseUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(AttributeRegistry.PHASE, new AttributeModifier(
                        phaseResourceLocation,
                        1,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.PHASE_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int stepHeightUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.STEP_HEIGHT_UPGRADE_CARD.get());
            if (stepHeightUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(Attributes.STEP_HEIGHT, new AttributeModifier(
                        stepResourceLocation,
                        stepHeightUpgradeCount,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.STEP_HEIGHT_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int speedUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.SPEED_UPGRADE_CARD.get());
            if (speedUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                        speedResourceLocation,
                        0.25 * speedUpgradeCount,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ), ItemRegistry.SPEED_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int jumpHeightUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.JUMP_BOOST_UPGRADE_CARD.get());
            if (jumpHeightUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(Attributes.JUMP_STRENGTH, new AttributeModifier(
                        jumpResourceLocation,
                        0.25 * jumpHeightUpgradeCount,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ), ItemRegistry.JUMP_BOOST_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int healthUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.HEALTH_UPGRADE_CARD.get());
            if (healthUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(
                        healthResourceLocation,
                        2 * healthUpgradeCount,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.HEALTH_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int evasionUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.EVASION_UPGRADE_CARD.get());
            if (evasionUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(AttributeRegistry.EVASION, new AttributeModifier(
                        evasionResourceLocation,
                        0.1 * evasionUpgradeCount,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.EVASION_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int damageUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.DAMAGE_UPGRADE_CARD.get());
            if (damageUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        damageResourceLocation,
                        damageUpgradeCount * 2,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.DAMAGE_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }

            int reachUpgradeCount = ItemUtil.getUpgradeCount(itemStack, ItemRegistry.REACH_UPGRADE_CARD.get());
            if (reachUpgradeCount > 0) {
                itemAttributeModifierEvent.addModifier(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(
                        reachResourceLocation,
                        reachUpgradeCount,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.REACH_UPGRADE_CARD.get().getEquipmentSlotGroup());
                itemAttributeModifierEvent.addModifier(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(
                        reachResourceLocation,
                        reachUpgradeCount,
                        AttributeModifier.Operation.ADD_VALUE
                ), ItemRegistry.REACH_UPGRADE_CARD.get().getEquipmentSlotGroup());
            }
        }
    }

    private static void removeArmorEffects(ItemAttributeModifierEvent event, ItemStack itemStack) {
        Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
        double armor = 0;
        double toughness = 0;
        if (equippable != null) {
            // Get armor values from attribute
            for (ItemAttributeModifiers.Entry modifier : event.getModifiers()) {
                if (modifier.attribute() == Attributes.ARMOR && modifier.modifier().id().getPath().startsWith("armor."))
                    armor = modifier.modifier().amount();
                else if (modifier.attribute() == Attributes.ARMOR_TOUGHNESS)
                    toughness = modifier.modifier().amount();
            }
            event.addModifier(Attributes.ARMOR, new AttributeModifier(
                    armorResourceLocation,
                    -armor,
                    AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.bySlot(equippable.slot()));
            event.addModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                    armorResourceLocation,
                    -toughness,
                    AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.bySlot(equippable.slot()));
        }
    }

    private static void replaceArmorEffects(ItemAttributeModifierEvent event) {
        event.removeModifier(Attributes.ARMOR, armorResourceLocation);
        event.removeModifier(Attributes.ARMOR_TOUGHNESS, armorResourceLocation);
    }

    @SubscribeEvent
    public static void attackEntityWithItem(AttackEntityEvent attackEntityEvent) {
        Player attackingPlayer = attackEntityEvent.getEntity();
        ItemStack itemStack = attackingPlayer.getMainHandItem();
        IEnergyStorage storage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage == null)
            return;
        if (itemStack.getItem() instanceof IChargeableItem chargeableSwordItem) {
            if (storage.getEnergyStored() < chargeableSwordItem.getLogisticalConstraints().usageOrGeneration()) {
                attackEntityEvent.setCanceled(true);
            }
        }
        if (UpgradeData.isUpgradeableItem(itemStack.getItem())) {
            int use = Math.min(storage.getEnergyStored(), ItemUtil.getEnergyUsage(itemStack));
            if (ItemUtil.hasUpgradeApplied(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get()) && storage.getEnergyStored() < use) {
                attackEntityEvent.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        String descriptionId = itemStack.getItem().getDescriptionId();
        UpgradeData upgradeData = UpgradeData.get(itemStack.getItem());
        boolean isUpgradeable = upgradeData != null;
        if (!id.getNamespace().equalsIgnoreCase(VoltTech.MODID) && !isUpgradeable)
            return;
        Component descriptionTooltip = null;
        ArrayList<Component> energyTooltips = new ArrayList<>();
        IEnergyStorage iEnergyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (iEnergyStorage != null) {
            boolean showEnergyTooltip = true;
            if ((isUpgradeable && !ItemUtil.hasUpgradeApplied(itemStack, ItemRegistry.ENERGY_UPGRADE_CARD.get())) ||
                    (itemStack.getItem() instanceof IChargeableItem chargeableItem && chargeableItem.isInfiniteEnergy()))
                showEnergyTooltip = false;
            if (showEnergyTooltip) {
                int energyTextColor = 0xffff6900;
                energyTooltips.add(Component.literal("%s / %s".formatted(StringifyUtil.stringifyEnergy(iEnergyStorage.getEnergyStored()), StringifyUtil.stringifyEnergy(iEnergyStorage.getMaxEnergyStored()))).copy().withColor(0xffaaff00));
                if (isUpgradeable) {
                    energyTooltips.add(StringifyUtil.translate("tooltip.charge_speed", StringifyUtil.stringifyEnergy(ItemUtil.getUpgradedEnergyMaxInput(itemStack))).withColor(energyTextColor));
                    energyTooltips.add(StringifyUtil.translate("tooltip.energy_usage", StringifyUtil.stringifyEnergy(ItemUtil.getEnergyUsage(itemStack))).withColor(energyTextColor));
                } else {
                    Constraints constraints = Constraints.get(itemStack.getItem());
                    if (constraints != null) {
                        energyTooltips.add(StringifyUtil.translate("tooltip.charge_speed", StringifyUtil.stringifyEnergy(constraints.maxInput())).withColor(energyTextColor));
                        energyTooltips.add(StringifyUtil.translate("tooltip.energy_usage", StringifyUtil.stringifyEnergy(constraints.usageOrGeneration())).withColor(energyTextColor));
                    }
                }
            }
        }
        String translate = Language.getInstance().getOrDefault("tooltip." + descriptionId, "");
        if (!translate.isEmpty())
            descriptionTooltip = Component.translatable(translate).withColor(0xff00aaff);
        int placement = 1;
        for (Component energyTooltip : energyTooltips) {
            event.getToolTip().add(placement++, energyTooltip);
        }
        if (isUpgradeable)
            placement = addUpgradeTooltip(placement, event);
        if (descriptionTooltip != null)
            event.getToolTip().add(placement++, descriptionTooltip);
    }

    private static int addUpgradeTooltip(int placement, ItemTooltipEvent event) {
        if (!event.getFlags().hasShiftDown()) {
            event.getToolTip().add(placement++, StringifyUtil.translate("tooltip.show_upgrades").withColor(0xffa0a0a0));
            return placement;
        }
        ItemStack itemStack = event.getItemStack();
        event.getToolTip().add(placement++, StringifyUtil.translate("tooltip.item_upgrades").withColor(0xffa0a0a0));
        for (Supplier<DataComponentType<Integer>> upgradeComponent : DataComponentRegistry.UPGRADE_COMPONENTS) {
            UpgradeCardItem cardItem = UpgradeCardItem.from(upgradeComponent.get());
            UpgradeData upgradeData = UpgradeData.get(itemStack.getItem());
            boolean isUpgradeBlocked = upgradeData != null && upgradeData.isUpgradeBlocked(cardItem);
            if (cardItem != null && cardItem.canApply(itemStack) && !isUpgradeBlocked) {
                String description = Component.translatable(cardItem.getDescriptionId()).getString();
                int max = cardItem.getMaxCount();
                int count = ItemUtil.getUpgradeCount(itemStack, cardItem);
                event.getToolTip().add(placement++,
                        Component.literal("%s%s§7:§r %d/%d".formatted(count > 0 ? ChatFormatting.GREEN : ChatFormatting.RED, description, count, max)).
                        withColor(0xffff7700));
            }
        }

        return placement;
    }
}
