package me.bricked.volttech.blockentity;

import com.mojang.authlib.GameProfile;
import me.bricked.volttech.Config;
import me.bricked.volttech.capability.forgeenergy.SimpleEnergyStorage;
import me.bricked.volttech.container.ImplementedWorldlyContainer;
import me.bricked.volttech.menu.BlockBreakerMenu;
import me.bricked.volttech.register.BlockEntityRegistry;
import me.bricked.volttech.register.TagRegistry;
import me.bricked.volttech.util.Constraints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BlockBreakerBlockEntity extends BlockEntity implements MenuProvider, ImplementedWorldlyContainer, IEnergyBlockEntity {
    private final Constraints constraints;
    public BlockBreakerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.BLOCK_BREAKER_BLOCK_ENTITY.get(), pos, blockState);
        this.constraints = Constraints.get(blockState.getBlock());
        this.energyStorage = new SimpleEnergyStorage(constraints);
        this.dataSlot = new DataSlot() {
            @Override
            public int get() {
                return energyStorage.getEnergyStored();
            }

            @Override
            public void set(int value) {

            }
        };
    }
    private static final UUID fakePlayerUUID = UUID.nameUUIDFromBytes("block_breaker".getBytes());
    private static final GameProfile GAME_PROFILE = new GameProfile(fakePlayerUUID, "BlockBreaker");
    private static final int[] SLOTS_FOR_SIDE = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    private final SimpleEnergyStorage energyStorage;
    private final DataSlot dataSlot;
    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);
    private float breakProgress = 0.f;
    private int checkDelay = 0;

    public static void tick(Level level, BlockPos pos, BlockState state, BlockBreakerBlockEntity blockEntity) {
        if (level.isClientSide())
            return;
        if (blockEntity.checkDelay > 0) {
            blockEntity.checkDelay--;
            return;
        }
        if (blockEntity.energyStorage.getEnergyStored() < blockEntity.constraints.usageOrGeneration())
            return;
        Direction facing = state.getValue(BlockStateProperties.FACING);
        BlockPos breakPos = pos.relative(facing);
        BlockState breakState = level.getBlockState(breakPos);
        ItemStack toolStack = blockEntity.getItem(0);
        if (breakState.getDestroySpeed(level, pos) < 0 || !isValidToBreak(breakState))
            return;
        if (toolStack.isEmpty() || toolStack.is(TagRegistry.Items.BLOCK_BREAKER_BANNED) || toolStack.getDamageValue() == toolStack.getMaxDamage() - 1)
            return;

        ServerLevel serverLevel = (ServerLevel)level;
        FakePlayer fakePlayer = FakePlayerFactory.get(serverLevel, GAME_PROFILE);
        fakePlayer.setOnGround(true);
        fakePlayer.getInventory().setSelectedItem(toolStack);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, toolStack);

        blockEntity.breakProgress += breakState.getDestroyProgress(fakePlayer, serverLevel, breakPos);
        if (blockEntity.breakProgress > 0.f) {
            serverLevel.destroyBlockProgress(fakePlayer.getId(), breakPos, blockEntity.getDestroyStage());
        }
        if (blockEntity.breakProgress >= 1.0f) {
            IItemHandler handler = serverLevel.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, blockEntity, null);
            boolean canHarvest = breakState.canHarvestBlock(serverLevel, breakPos, fakePlayer);
            state.onDestroyedByPlayer(serverLevel, breakPos, fakePlayer, canHarvest, level.getFluidState(breakPos));
            SoundType soundtype = breakState.getSoundType(serverLevel, breakPos, fakePlayer);
            serverLevel.playSound(fakePlayer, breakPos, soundtype.getBreakSound(), SoundSource.BLOCKS);
            List<ItemStack> drops = breakState.getDrops(new LootParams.Builder(serverLevel).
                    withParameter(LootContextParams.TOOL, toolStack).
                    withParameter(LootContextParams.BLOCK_STATE, breakState).
                    withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(breakPos)));
            drops.forEach(itemStack -> {
                ItemStack remaining = itemStack;
                if (handler != null) {
                    for (int slot = 1; slot < handler.getSlots(); slot++) {
                        remaining = handler.insertItem(slot, remaining, false);
                        if (remaining.isEmpty())
                            break;
                    }
                }
                if (!remaining.isEmpty())
                    Block.popResource(serverLevel, breakPos, remaining);
            });
            toolStack.hurtAndBreak(1, fakePlayer, InteractionHand.MAIN_HAND);
            blockEntity.checkDelay = Config.BLOCK_BREAKER_TICK_DELAY.get();
            blockEntity.breakProgress = 0.f;
            blockEntity.energyStorage.removeEnergy(blockEntity.constraints.usageOrGeneration(), false);
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

    private int getDestroyStage() {
        return this.breakProgress > 0.0F ? (int)(this.breakProgress * 10.0F) : -1;
    }

    private static boolean isValidToBreak(BlockState blockState) {
        if (blockState.is(Blocks.AIR))
            return false;
        if (blockState.is(TagRegistry.Blocks.BLOCK_BREAKER_BANNED))
            return false;
        return true;
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
        return new BlockBreakerMenu(i, inventory, this, dataSlot);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_FOR_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return i > 0; // Everything but the pickaxe slot
    }

    public float getBreakProgress() {
        return breakProgress;
    }

    public void setBreakProgress(float breakProgress) {
        this.breakProgress = breakProgress;
    }
}
