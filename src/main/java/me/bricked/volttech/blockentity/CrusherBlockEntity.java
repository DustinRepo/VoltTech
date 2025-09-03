package me.bricked.volttech.blockentity;

import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedWorldlyContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.menu.CrusherMenu;
import me.bricked.volttech.recipe.crusher.CrusherRecipe;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.RecipeRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.ItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class CrusherBlockEntity extends BlockEntity implements MenuProvider, ImplementedWorldlyContainer, IEnergyBlockEntity {
    private final Constraints constraints;
    public CrusherBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.CRUSHER_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleEnergyStorage(constraints);
        this.containerData = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> crushTicks;
                    case 2 -> maxCrushTicks;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {

            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        this.quickCheck = RecipeManager.createCheck(RecipeRegistry.CRUSHER_RECIPE.get());
    }
    private static final int[] SLOTS_FOR_SIDE = new int[]{0, 1, 2};
    private final SimpleEnergyStorage energyStorage;
    private final ContainerData containerData;
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends CrusherRecipe> quickCheck;
    private int crushTicks;
    private int maxCrushTicks;

    public static void tick(Level level, BlockPos pos, BlockState state, CrusherBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        ItemStack inputStack = blockEntity.getItem(0);
        ItemStack outputStack = blockEntity.getItem(1);
        ItemStack chanceOutputStack = blockEntity.getItem(2);

        if (inputStack.isEmpty() || blockEntity.energyStorage.getEnergyStored() < blockEntity.constraints.usageOrGeneration()) {
            if (blockEntity.crushTicks > 0)
                blockEntity.crushTicks = Math.clamp(blockEntity.crushTicks - 2, 0, blockEntity.maxCrushTicks);
            return;
        }
        RecipeHolder<? extends CrusherRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(new SingleRecipeInput(inputStack), (ServerLevel) level).orElse(null);
        if (recipeHolder == null)
            return;
        ItemStack resultStack = recipeHolder.value().result();
        ItemStack chanceResultStack = recipeHolder.value().chanceResult();
        boolean canOutputResult = outputStack.isEmpty() ||
                (ItemStack.isSameItemSameComponents(resultStack, outputStack) &&
                        ItemUtil.canFitIntoItemStack(resultStack, outputStack));
        boolean canOutputChanceResult = chanceOutputStack.isEmpty() ||
                (ItemStack.isSameItemSameComponents(chanceResultStack, chanceOutputStack) &&
                        ItemUtil.canFitIntoItemStack(chanceResultStack, chanceOutputStack));
        if (!resultStack.isEmpty() && canOutputResult && canOutputChanceResult) {
            blockEntity.crushTicks++;
            if (blockEntity.crushTicks >= blockEntity.maxCrushTicks) {
                blockEntity.maxCrushTicks = blockEntity.getCrushTicks(inputStack);
                blockEntity.crushTicks = 0;
                if (convertItems(recipeHolder, blockEntity))
                    blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration(), false);
            }
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ImplementedWorldlyContainer.super.setItem(slot, stack);
        if (slot == 0) {
            maxCrushTicks = getCrushTicks(stack);
            crushTicks = 0;
        }
    }

    private static boolean convertItems(RecipeHolder<? extends CrusherRecipe> recipeHolder, CrusherBlockEntity crusher) {
        if (recipeHolder != null) {
            ItemStack inputStack = crusher.getItem(0);
            ItemStack resultStack = recipeHolder.value().result();
            ItemStack chanceResultStack = recipeHolder.value().chanceResult();
            ItemStack outputStack = crusher.getItem(1);
            ItemStack chanceOutputStack = crusher.getItem(2);
            float chance = recipeHolder.value().chance();
            // Whether both are empty, both have items, or one is empty while other has items
            if (outputStack.isEmpty() && chanceOutputStack.isEmpty()) {
                crusher.setItem(1, resultStack.copy());
                if (Math.random() <= chance)
                    crusher.setItem(2, chanceResultStack.copy());
            } else if (ItemStack.isSameItemSameComponents(outputStack, resultStack) && ItemStack.isSameItemSameComponents(chanceOutputStack, chanceResultStack)) {
                outputStack.grow(resultStack.getCount());
                if (Math.random() <= chance)
                    chanceOutputStack.grow(chanceResultStack.getCount());
            } else if (outputStack.isEmpty() && ItemStack.isSameItemSameComponents(chanceOutputStack, chanceResultStack)) {
                crusher.setItem(1, resultStack.copy());
                if (Math.random() <= chance)
                    chanceOutputStack.grow(chanceResultStack.getCount());
            } else if (chanceOutputStack.isEmpty() && ItemStack.isSameItemSameComponents(outputStack, resultStack)) {
                outputStack.grow(resultStack.getCount());
                if (Math.random() <= chance)
                    crusher.setItem(2, chanceResultStack.copy());
            }
            inputStack.shrink(1);
            return true;
        }
        return false;
    }

    private int getCrushTicks(ItemStack itemStack) {
        return quickCheck.
                getRecipeFor(new SingleRecipeInput(itemStack), (ServerLevel)getLevel()).
                map(recipeHolder -> recipeHolder.value().ticks()).
                orElse(100);
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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        energyStorage.serialize(output);
        ContainerHelper.saveAllItems(output, items);
        output.putInt("crushTicks", crushTicks);
        output.putInt("maxCrushTicks", maxCrushTicks);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.deserialize(input);
        ContainerHelper.loadAllItems(input, items);
        this.crushTicks = input.getIntOr("crushTicks", 0);
        this.maxCrushTicks = input.getIntOr("maxCrushTicks", 0);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_FOR_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        SingleRecipeInput recipeInput = new SingleRecipeInput(itemStack);
        RecipeHolder<? extends CrusherRecipe> recipeHolder = quickCheck.getRecipeFor(recipeInput, (ServerLevel)getLevel()).orElse(null);
        return i == 0 && recipeHolder != null && !recipeHolder.value().assemble(recipeInput, getLevel().registryAccess()).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot > 0;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CrusherMenu(i, inventory, this, containerData);
    }
}
