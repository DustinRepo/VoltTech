package me.bricked.volttech.blockentity;

import com.mojang.authlib.GameProfile;
import me.bricked.volttech.Config;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.container.ImplementedContainer;
import me.bricked.volttech.menu.BlockPlacerMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BlockPlacerBlockEntity extends BlockEntity implements MenuProvider, ImplementedContainer, IEnergyBlockEntity {
    private final Constraints constraints;
    public BlockPlacerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.BLOCK_PLACER_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleEnergyStorage(constraints);
        this.dataSlot = new DataSlot() {
            @Override
            public int get() {
                return energyStorage.getEnergyStored();
            }

            @Override
            public void set(int value) {}
        };
    }
    private static final UUID fakePlayerUUID = UUID.nameUUIDFromBytes("block_placer".getBytes());
    private static final GameProfile GAME_PROFILE = new GameProfile(fakePlayerUUID, "BlockPlacer");
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final SimpleEnergyStorage energyStorage;
    private final DataSlot dataSlot;
    private int placeDelay = 0;

    public static void tick(Level level, BlockPos pos, BlockState state, BlockPlacerBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        if (blockEntity.placeDelay > 0) {
            blockEntity.placeDelay--;
            return;
        }
        if (blockEntity.energyStorage.getEnergyStored() < blockEntity.constraints.usageOrGeneration())
            return;

        ItemStack placeStack = blockEntity.getItem(0);
        if (!(placeStack.getItem() instanceof BlockItem blockItem) || placeStack.is(TagRegistry.Items.BLOCK_PLACER_BANNED))
            return;

        FakePlayer fakePlayer = FakePlayerFactory.get((ServerLevel) level, GAME_PROFILE);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, placeStack);
        fakePlayer.getInventory().setSelectedItem(placeStack);

        Direction facing = state.getValue(BlockStateProperties.FACING);
        BlockPos placePos = pos.relative(facing);
        BlockState placeState = level.getBlockState(placePos);
        if (!placeState.canBeReplaced())
            return;

        UseOnContext ctx = new UseOnContext(fakePlayer, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atLowerCornerOf(placePos), Direction.UP, placePos, false));
        InteractionResult placeResult = blockItem.place(new BlockPlaceContext(ctx));

        if (placeResult.consumesAction()) {
            blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration(), false);
            blockEntity.placeDelay = Config.BLOCK_PLACER_TICK_DELAY.get();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
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
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BlockPlacerMenu(i, inventory, this, dataSlot);
    }
}
