package me.bricked.volttech.blockentity;

import me.bricked.volttech.Config;
import me.bricked.volttech.container.ImplementedWorldlyContainer;
import me.bricked.volttech.capability.forgeenergy.SimpleDirectionalEnergyStorage;
import me.bricked.volttech.menu.FoodMasherMenu;
import me.bricked.volttech.recipe.food_masher.FoodMasherRecipe;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class FoodMasherBlockEntity extends BlockEntity implements IEnergyBlockEntity, MenuProvider, ImplementedWorldlyContainer {
    private final Constraints constraints;
    public FoodMasherBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.FOOD_MASHER_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleDirectionalEnergyStorage(constraints){
            @Override
            public int getMaxInsert(Direction direction) {
                if (direction == Direction.UP)
                    return 0;
                return constraints.maxInput();
            }

            @Override
            public int getMaxOutput(Direction direction) {
                if (direction != null)
                    return 0;
                return constraints.maxOutput();
            }
        };
        this.containerData = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> mashingTicks;
                    case 2 -> maxMashingTicks;
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
        this.quickCheck = RecipeManager.createCheck(RecipeRegistry.FOOD_MASHER_RECIPE.get());
    }
    private static final int[] SLOTS_FOR_SIDE = new int[]{0, 1};
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final SimpleDirectionalEnergyStorage energyStorage;
    private final ContainerData containerData;
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends FoodMasherRecipe> quickCheck;
    private int mashingTicks;
    private int maxMashingTicks;

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, FoodMasherBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        ItemStack inputStack = blockEntity.getItem(0);
        ItemStack outputStack = blockEntity.getItem(1);

        if (inputStack.isEmpty() || blockEntity.energyStorage.getEnergyStored() < blockEntity.constraints.usageOrGeneration()) {
            if (blockEntity.mashingTicks > 0)
                blockEntity.mashingTicks = Math.clamp(blockEntity.mashingTicks - 2, 0, blockEntity.maxMashingTicks);
            return;
        }
        RecipeHolder<? extends FoodMasherRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(new SingleRecipeInput(inputStack), (ServerLevel) level).orElse(null);
        if (recipeHolder == null)
            return;
        ItemStack resultStack = recipeHolder.value().result();
        boolean canOutputResult = outputStack.isEmpty() ||
                (ItemStack.isSameItemSameComponents(resultStack, outputStack) &&
                        ItemUtil.canFitIntoItemStack(resultStack, outputStack));
        if (!resultStack.isEmpty() && canOutputResult) {
            blockEntity.mashingTicks++;
            if (blockEntity.mashingTicks >= blockEntity.maxMashingTicks) {
                blockEntity.maxMashingTicks = blockEntity.getMashingTicks(inputStack);
                blockEntity.mashingTicks = 0;
                if (convertItems(recipeHolder, blockEntity))
                    blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration());
            }
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ImplementedWorldlyContainer.super.setItem(slot, stack);
        if (slot == 0) {
            maxMashingTicks = getMashingTicks(stack);
            mashingTicks = 0;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        energyStorage.serialize(output);
        output.putInt("mashingTicks", mashingTicks);
        output.putInt("maxMashingTicks", maxMashingTicks);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        energyStorage.deserialize(input);
        this.mashingTicks = input.getIntOr("mashingTicks", 0);
        this.maxMashingTicks = input.getIntOr("maxMashingTicks", 0);
    }

    private static boolean convertItems(RecipeHolder<? extends FoodMasherRecipe> recipeHolder, FoodMasherBlockEntity masher) {
        if (recipeHolder != null) {
            ItemStack inputStack = masher.getItem(0);
            ItemStack resultStack = recipeHolder.value().result();
            ItemStack outputStack = masher.getItem(1);
            if (outputStack.isEmpty())
                masher.setItem(1, resultStack.copy());
            else if (ItemStack.isSameItemSameComponents(outputStack, resultStack))
                outputStack.grow(resultStack.getCount());
            inputStack.shrink(1);
            return true;
        }
        return false;
    }

    private int getMashingTicks(ItemStack itemStack) {
        return quickCheck.
                getRecipeFor(new SingleRecipeInput(itemStack), (ServerLevel) getLevel()).
                map(recipeHolder -> recipeHolder.value().ticks()).
                orElse(50);
    }

    @Override
    public IEnergyStorage getEnergyStorage(BlockState state, Direction direction) {
        return energyStorage.getEnergyStorage(direction);
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
        return getBlockState().getBlock().getName();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_FOR_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        SingleRecipeInput recipeInput = new SingleRecipeInput(itemStack);
        RecipeHolder<? extends FoodMasherRecipe> recipeHolder = quickCheck.getRecipeFor(recipeInput, (ServerLevel) getLevel()).orElse(null);
        return i == 0 && recipeHolder != null && !recipeHolder.value().assemble(recipeInput, getLevel().registryAccess()).isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return i == 1;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FoodMasherMenu(i, inventory, this, containerData);
    }
}
