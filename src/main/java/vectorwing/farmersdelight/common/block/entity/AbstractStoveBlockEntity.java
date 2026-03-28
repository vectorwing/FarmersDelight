package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> items;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final int[] cookingProgress;
    private final int[] cookingTime;
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickRecipeLookup;

    protected AbstractStoveBlockEntity(
            BlockEntityType<?> blockEntityType,
            BlockPos blockPos,
            BlockState blockState,
            RecipeType<? extends AbstractCookingRecipe> recipeType
    ) {
        super(blockEntityType, blockPos, blockState);

        int inventorySlotCount = this.getInventorySlotCount();
        items = NonNullList.withSize(inventorySlotCount, ItemStack.EMPTY);
        cookingProgress = new int[inventorySlotCount];
        cookingTime = new int[inventorySlotCount];
        this.recipeType = recipeType;
        quickRecipeLookup = RecipeManager.createCheck(recipeType);
    }

    protected abstract int getInventorySlotCount();

    public abstract Vec2 getStoveItemOffset(int index);

    public abstract VoxelShape getGrillingArea();

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items);

        if (tag.contains("CookingTimes", 11)) {
            int[] arrayCookingTimes = tag.getIntArray("CookingTimes");
            System.arraycopy(arrayCookingTimes, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, arrayCookingTimes.length));
        }

        if (tag.contains("CookingTotalTimes", 11)) {
            int[] arrayCookingTimesTotal = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, arrayCookingTimesTotal.length));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items, true);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        ContainerHelper.saveAllItems(tag, this.items, true);
        return tag;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity stoveEntity) {
        if (stoveEntity.isEmpty()) return;
        if (stoveEntity.shouldDropItems()) {
            stoveEntity.dropAllItems();
            stoveEntity.setChanged();
            return;
        }

        if (state.getValue(AbstractStoveBlock.LIT)) {
            stoveEntity.cookAndOutputItems();
        } else {
            stoveEntity.coolItems();
        }
    }

    private void cookAndOutputItems() {
        assert this.level != null;

        boolean didChange = false;
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack ingredient = this.items.get(i);
            if (ingredient.isEmpty()) continue;
            didChange = true;

            ++cookingProgress[i];
            if (cookingProgress[i] < cookingTime[i]) continue;
            Container container = new SimpleContainer(ingredient);
            ItemStack result = this.quickRecipeLookup.getRecipeFor(container, this.level).map(
                (recipe) -> recipe.assemble(container, this.level.registryAccess())
            ).orElse(ingredient);

            if (!result.isItemEnabled(this.level.enabledFeatures())) continue;
            Containers.dropItemStack(
                this.level,
                this.worldPosition.getX() + 0.5,
                this.worldPosition.getY() + 1.0,
                this.worldPosition.getZ() + 0.5,
                result
            );
            this.items.set(i, ItemStack.EMPTY);
            var state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(state));
        }
        if (didChange) this.setChanged();
    }

    private void coolItems() {
        assert this.level != null;

        boolean didChange = false;
        for (int i = 0; i < this.items.size(); ++i) {
            int thisItemCookingProgress =  this.cookingProgress[i];
            if (thisItemCookingProgress <= 0) continue;
            didChange = true;
            this.cookingProgress[i] = Mth.clamp(thisItemCookingProgress - 2, 0, this.cookingTime[i]);
        }
        if (didChange) this.setChanged();
    }

    public Optional<? extends AbstractCookingRecipe> getCookingRecipe(ItemStack itemStack) {
        assert this.level != null;
        return this.quickRecipeLookup.getRecipeFor(new SimpleContainer(itemStack), this.level);
    }

    public int getNextEmptySlot() {
        return IntStream.range(0, this.items.size())
                .filter((i) -> this.items.get(i).isEmpty())
                .findFirst()
                .orElse(-1);
    }

    public boolean placeFood(@Nullable Entity entity, ItemStack foodStackToPlace, int foodCookingTime) {
        assert this.level != null;

        int emptySlotIndex = getNextEmptySlot();
        if (emptySlotIndex < 0) return false;
        assert this.items.get(emptySlotIndex).isEmpty();

        this.cookingTime[emptySlotIndex] = foodCookingTime;
        this.cookingProgress[emptySlotIndex] = 0;
        this.items.set(emptySlotIndex, foodStackToPlace.split(1));
        var state = this.getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
        this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(entity, state));
        this.setChanged();
        return true;
    }

    public boolean shouldDropItems() {
        if (this.level == null) return false;
        return AbstractStoveBlock.isStoveTopCovered(this.level, this.worldPosition, this.getBlockState());
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    public boolean isFull() {
        return this.items.stream().noneMatch(ItemStack::isEmpty);
    }

    public void dropAllItems() {
        if (this.level == null) return;
        Containers.dropContents(this.level, this.worldPosition, items);
        this.items.clear();
        var state = this.getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
        this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(state));
    }

    public void extinguish() {
        if (this.level == null) return;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        this.setChanged();
        // level.gameEvent is called in AbstractStoveBlock::extinguish, so it is not called here
    }
}
