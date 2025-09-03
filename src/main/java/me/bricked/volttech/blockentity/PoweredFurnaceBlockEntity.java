package me.bricked.volttech.blockentity;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedWorldlyContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.menu.PoweredFurnaceMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PoweredFurnaceBlockEntity extends BlockEntity implements ImplementedWorldlyContainer, IEnergyBlockEntity, MenuProvider {
    public PoweredFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.POWERED_FURNACE_BLOCK_ENTITY.get(), pos, blockState);
        this.quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleEnergyStorage(constraints);
        this.containerData = new ContainerData() {
            @Override
            public int get(int i) {
                switch (i){
                    case 0 -> {
                        return energyStorage.getEnergyStored();
                    }
                    case 1 -> {
                        return energyStorage.getMaxEnergyStored();
                    }
                };
                if (i < 6) {
                    return cookTime[i - 2];
                }
                return maxCookTime[i - 6];
            }

            @Override
            public void set(int i, int i1) {

            }

            @Override
            public int getCount() {
                return 10;
            }
        };
    }
    private final Constraints constraints;
    private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
    private static final int[] SLOTS_FOR_SIDE = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;
    private final SimpleEnergyStorage energyStorage;
    private final ContainerData containerData;
    private final NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);
    private final Map<ResourceKey<Recipe<?>>, Integer> recipesHoldingXP = Maps.newHashMap();
    private int[] cookTime = new int[]{0,0,0,0};
    private int[] maxCookTime = new int[]{0,0,0,0};

    public static void tick(Level level, BlockPos pos, BlockState state, PoweredFurnaceBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        ServerLevel serverLevel = (ServerLevel) level;
        boolean lit = false;
        for (int i = 0; i < 4; i++) {
            ItemStack inputItemStack = blockEntity.getItem(i);
            ItemStack outputStack = blockEntity.getItem(4 + i);
            if (inputItemStack.isEmpty() || blockEntity.energyStorage.getEnergyStored() <= blockEntity.constraints.usageOrGeneration()) {
                if (blockEntity.cookTime[i] > 0)
                    blockEntity.cookTime[i] = Math.clamp(blockEntity.cookTime[i] - 2, 0, blockEntity.maxCookTime[i]);
                continue;
            }

            ItemStack recipeResultStack = ItemUtil.getCookingRecipeStack(inputItemStack, serverLevel, blockEntity.quickCheck);
            boolean canOutput = outputStack.isEmpty() ||
                    (ItemStack.isSameItemSameComponents(recipeResultStack, outputStack) &&
                            ItemUtil.canFitIntoItemStack(recipeResultStack, outputStack));
            if (!recipeResultStack.isEmpty() && canOutput) {
                blockEntity.cookTime[i]++;
                lit = true;
                if (blockEntity.cookTime[i] >= blockEntity.maxCookTime[i]) {
                    int cookTime = getTotalCookTime(serverLevel, inputItemStack, blockEntity);
                    blockEntity.maxCookTime[i] = cookTime;
                    blockEntity.cookTime[i] = 0;
                    RecipeHolder<? extends AbstractCookingRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(new SingleRecipeInput(inputItemStack), serverLevel).orElse(null);
                    if (convertItem(level.registryAccess(), recipeHolder, blockEntity, i)) {
                        blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration(), false);
                        holdRecipe(blockEntity, recipeHolder.id(), 1);
                    }
                }
            }
        }
        boolean isAlreadyLit = state.getValue(BlockStateProperties.LIT);
        if (isAlreadyLit != lit) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, lit), Block.UPDATE_ALL);
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ImplementedWorldlyContainer.super.setItem(slot, stack);
        if (slot < 4) {
            maxCookTime[slot] = getTotalCookTime((ServerLevel) getLevel(), stack, this);
            cookTime[slot] = 0;
        }
    }

    private static void holdRecipe(PoweredFurnaceBlockEntity furnace, ResourceKey<Recipe<?>> key, int increment) {
        if (furnace.recipesHoldingXP.containsKey(key)) {
            int current = furnace.recipesHoldingXP.get(key);
            furnace.recipesHoldingXP.replace(key, current + increment);
        } else
            furnace.recipesHoldingXP.put(key, increment);
    }

    public static void givePlayerXP(Player player, ServerLevel level, PoweredFurnaceBlockEntity furnace) {
        furnace.recipesHoldingXP.forEach((resourceLocation, count) -> {
            level.recipeAccess().byKey(resourceLocation).ifPresent(recipeHolder -> {
                AbstractCookingRecipe recipe = ((AbstractCookingRecipe)recipeHolder.value());
                int xp = convertXP(recipe.experience(), count);
                player.giveExperiencePoints(xp);
            });
        });
        furnace.recipesHoldingXP.clear();
    }

    private static int convertXP(float xp, int count) {
        float combined = xp * count;
        int floor = Mth.floor(combined);
        float fraction = Mth.frac(combined);
        if (fraction != 0 && Math.random() < fraction)
            floor++;
        return floor;
    }

    private static boolean convertItem(RegistryAccess registryAccess, RecipeHolder<?> recipe, PoweredFurnaceBlockEntity furnace, int slot) {
        if (recipe != null) {
            ItemStack inputStack = furnace.getItem(slot);
            ItemStack recipeStack = ((AbstractCookingRecipe)recipe.value()).assemble(new SingleRecipeInput(inputStack), registryAccess);
            ItemStack outputStack = furnace.getItem(slot + 4);
            if (outputStack.isEmpty()) {
                furnace.setItem(slot + 4, recipeStack.copy());
            } else if (ItemStack.isSameItemSameComponents(outputStack, recipeStack)) {
                outputStack.grow(recipeStack.getCount());
            }
            inputStack.shrink(1);
            return true;
        }
        return false;
    }

    private static int getTotalCookTime(ServerLevel level, ItemStack itemStack, PoweredFurnaceBlockEntity blockEntity) {
        return blockEntity.quickCheck.
                getRecipeFor(new SingleRecipeInput(itemStack), level).
                map((recipeHolder) -> recipeHolder.value().cookingTime()).
                orElse(200);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        energyStorage.serialize(output);
        output.putIntArray("cookTime", cookTime);
        output.putIntArray("maxCookTime", maxCookTime);
        CompoundTag recipesTag = new CompoundTag();
        recipesHoldingXP.forEach((resourceLocation, integer) -> {
            recipesTag.putInt(resourceLocation.toString(), integer);
        });
        output.store("recipes", RECIPES_CODEC, recipesHoldingXP);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        ContainerHelper.loadAllItems(valueInput, items);
        energyStorage.deserialize(valueInput);
        valueInput.getIntArray("cookTime").ifPresent(ints -> cookTime = ints);
        valueInput.getIntArray("maxCookTime").ifPresent(ints -> maxCookTime = ints);
        this.recipesHoldingXP.clear();
        valueInput.read("recipes", RECIPES_CODEC).ifPresent(this.recipesHoldingXP::putAll);
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PoweredFurnaceMenu(i, inventory, this, containerData);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_FOR_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        if (slot >= 4)
            return false;
        return !ItemUtil.getCookingRecipeStack(itemStack, (ServerLevel) level, this.quickCheck).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot > 3;
    }
}
