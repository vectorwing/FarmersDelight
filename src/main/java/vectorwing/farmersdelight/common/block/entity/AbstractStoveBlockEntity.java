package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.StoveBlock;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractStoveBlockEntity<R extends AbstractCookingRecipe, RT extends RecipeType<R>> extends BlockEntity {
    private static final VoxelShape GRILLING_AREA = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
    private final NonNullList<ItemStack> items;
    private final int[] cookingProgress;
    private final int[] cookingTime;
    private final RecipeManager.CachedCheck<Container, R> recipeLookup;
    private final Vec2[] itemRenderOffsets;

    protected AbstractStoveBlockEntity(BlockEntityType<?> blockEntityType, int inventorySlotCount, RT recipeType, BlockPos pos, BlockState state, Vec2[] offsets) {
        super(blockEntityType, pos, state);

        items = NonNullList.withSize(inventorySlotCount, ItemStack.EMPTY);
        cookingProgress = new int[inventorySlotCount];
        cookingTime = new int[inventorySlotCount];
        recipeLookup = RecipeManager.createCheck(recipeType);
        itemRenderOffsets = new Vec2[inventorySlotCount];
        System.arraycopy(offsets, 0, itemRenderOffsets, 0, Math.min(inventorySlotCount, offsets.length));
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void load(CompoundTag nbtTag) {
        super.load(nbtTag);

        this.items.clear();
        ContainerHelper.loadAllItems(nbtTag, this.items);

        if (nbtTag.contains("CookingTimes", 11)) {
            int[] arrayCookingTimes = nbtTag.getIntArray("CookingTimes");
            System.arraycopy(arrayCookingTimes, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, arrayCookingTimes.length));
        }

        if (nbtTag.contains("CookingTotalTimes", 11)) {
            int[] arrayCookingTimesTotal = nbtTag.getIntArray("CookingTotalTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, arrayCookingTimesTotal.length));
        }
    }

    public void saveAdditional(CompoundTag nbtTag) {
        super.saveAdditional(nbtTag);
        ContainerHelper.saveAllItems(nbtTag, this.items, true);
        nbtTag.putIntArray("CookingTimes", this.cookingProgress);
        nbtTag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag nbtTag = super.getUpdateTag();
        ContainerHelper.saveAllItems(nbtTag, this.items, true);
        return nbtTag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity<?,?> stove) {
        for(int i = 0; i < stove.items.size(); ++i) {
            if (stove.items.get(i).isEmpty() || level.random.nextFloat() >= 0.2F) continue;

            Vec2 stoveItemVector = stove.getStoveItemOffset(i);
            Direction direction = state.getValue(StoveBlock.FACING);
            int directionIndex = direction.get2DDataValue();
            Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);
            double x = pos.getX() + 0.5 - (direction.getStepX() * offset.x) + (direction.getClockWise().getStepX() * offset.x);
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5 - (direction.getStepZ() * offset.y) + (direction.getClockWise().getStepZ() * offset.y);

            for(int k = 0; k < 3; ++k) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity<?,?> stove) {
        boolean didChange = false;

        for(int i = 0; i < stove.items.size(); ++i) {
            if (stove.cookingProgress[i] <= 0) continue;
            didChange = true;
            stove.cookingProgress[i] = Mth.clamp(stove.cookingProgress[i] - 2, 0, stove.cookingTime[i]);
        }

        if (!didChange) return;
        setChanged(level, pos, state);
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity<?,?> stove) {
        if (level == null) return;

        boolean didChange = false;

        if (stove.isBlockedAbove()) {
            stove.dropAllItems(level, pos, state);
            return;
        }

        for(int i = 0; i < stove.items.size(); ++i) {
            ItemStack stoveStack = stove.items.get(i);
            if (stoveStack.isEmpty()) continue;

            didChange = true;

            int var10002 = stove.cookingProgress[i]++;
            if (stove.cookingProgress[i] < stove.cookingTime[i]) continue;

            Container inventoryWrapper = new SimpleContainer(stoveStack);
            ItemStack result = stove.recipeLookup.getRecipeFor(inventoryWrapper, level).map((recipe) -> {
                return recipe.assemble(inventoryWrapper, level.registryAccess());
            }).orElse(stoveStack);
            if (!result.isItemEnabled(level.enabledFeatures())) continue;

            Containers.dropItemStack(
                    level,
                    stove.worldPosition.getX() + 0.5,
                    stove.worldPosition.getY() + 1.0,
                    stove.worldPosition.getZ() + 0.5,
                    result
            );
            stove.items.set(i, ItemStack.EMPTY);
            level.sendBlockUpdated(pos, state, state, 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        }

        if (!didChange) return;
        setChanged(level, pos, state);
    }

    public Optional<R> getCookableRecipe(ItemStack item) {
        if (isBlockedAbove()) return Optional.empty();
        if (items.stream().noneMatch(ItemStack::isEmpty)) return Optional.empty();
        return this.recipeLookup.getRecipeFor(new SimpleContainer(item), this.level);
    }

    public boolean placeFood(@Nullable Entity placer, ItemStack itemStackToPlace, int cookingTime) {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack itemStack = this.items.get(i);
            if (!itemStack.isEmpty()) continue;

            this.cookingTime[i] = cookingTime;
            this.cookingProgress[i] = 0;
            this.items.set(i, itemStackToPlace.split(1));
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(placer, this.getBlockState()));
            this.markUpdated();
            return true;
        }

        return false;
    }

    public boolean isBlockedAbove() {
        if (this.level == null) return false;

        BlockState above = this.level.getBlockState(this.worldPosition.above());
        return Shapes.joinIsNotEmpty(GRILLING_AREA, above.getShape(this.level, this.worldPosition.above()), BooleanOp.AND);
    }

    public void dropAllItems(Level level, BlockPos pos, BlockState state) {
        if (level == null) return;

        Containers.dropContents(level, pos, items);
    }

    public Vec2 getStoveItemOffset(int index) {
        return itemRenderOffsets[index];
    }

    protected void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void onExtinguish() {}

    public void extinguish() {
        if (level == null) return;
        onExtinguish();
        markUpdated();
    }
}
