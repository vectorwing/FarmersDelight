package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
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
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Clearable
{
	private final ItemStackHandler items;
	private final int[] cookingProgress;
	private final int[] cookingTime;
	private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickRecipeLookup;

	protected AbstractStoveBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<? extends AbstractCookingRecipe> recipeType) {
		super(blockEntityType, blockPos, blockState);

		int inventorySlotCount = this.getInventorySlotCount();
		items = createHandler(inventorySlotCount);
		cookingProgress = new int[inventorySlotCount];
		cookingTime = new int[inventorySlotCount];
		quickRecipeLookup = RecipeManager.createCheck(recipeType);
	}

	protected abstract int getInventorySlotCount();

	public abstract Vec2 getStoveItemOffset(int index);

	public ItemStackHandler getItems() {
		return this.items;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);

		CompoundTag inventoryTag;
		if (tag.contains("Inventory")) inventoryTag = tag.getCompound("Inventory");
		else inventoryTag = tag;
		items.deserializeNBT(inventoryTag);

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
		tag.put("Inventory", items.serializeNBT());
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
		tag.put("Inventory", items.serializeNBT());
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
		for (int i = 0; i < items.getSlots(); ++i) {
			ItemStack ingredient = this.items.getStackInSlot(i);
			if (ingredient.isEmpty()) continue;
			didChange = true;

			++cookingProgress[i];
			if (cookingProgress[i] < cookingTime[i]) continue;
			Container container = new SimpleContainer(ingredient);
			ItemStack result = this.quickRecipeLookup.getRecipeFor(container, this.level).map(
				(recipe) -> recipe.assemble(container, this.level.registryAccess())
			).orElse(ingredient);

			if (!result.isItemEnabled(this.level.enabledFeatures())) continue;
			ItemUtils.spawnItemEntity(level, result.copy(),
				worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5,
				level.random.nextGaussian() * (double) 0.01F, 0.1F, level.random.nextGaussian() * (double) 0.01F);
			this.items.setStackInSlot(i, ItemStack.EMPTY);
			var state = this.getBlockState();
			this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
			this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(state));
		}
		if (didChange) this.setChanged();
	}

	private void coolItems() {
		assert this.level != null;

		boolean didChange = false;
		for (int i = 0; i < this.items.getSlots(); ++i) {
			int thisItemCookingProgress = this.cookingProgress[i];
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
		return IntStream.range(0, this.items.getSlots())
			.filter((i) -> this.items.getStackInSlot(i).isEmpty())
			.findFirst()
			.orElse(-1);
	}

	public boolean placeFood(@Nullable Entity entity, ItemStack foodStackToPlace, int foodCookingTime) {
		assert this.level != null;

		int emptySlotIndex = getNextEmptySlot();
		if (emptySlotIndex < 0) return false;
		assert this.items.getStackInSlot(emptySlotIndex).isEmpty();

		this.cookingTime[emptySlotIndex] = foodCookingTime;
		this.cookingProgress[emptySlotIndex] = 0;
		this.items.setStackInSlot(emptySlotIndex, foodStackToPlace.split(1));
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

	public Stream<ItemStack> streamItems() {
		return IntStream.range(0, this.items.getSlots())
			.mapToObj(this.items::getStackInSlot);
	}

	public boolean isEmpty() {
		return streamItems().allMatch(ItemStack::isEmpty);
	}

	public boolean isFull() {
		return streamItems().noneMatch(ItemStack::isEmpty);
	}

	public void dropAllItems() {
		if (this.level == null) return;
		ItemUtils.dropItems(this.level, this.worldPosition, this.items);
		var state = this.getBlockState();
		this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
		this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(state));
	}

	public void extinguish() {
		if (this.level == null) return;
		this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
		this.setChanged();
	}

	public void ignite() {
		if (this.level == null) return;
		this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
		this.setChanged();
	}

	public void clearContent() {
		streamItems().forEach((stack) -> stack.setCount(0));
	}

	private static ItemStackHandler createHandler(int slotCount) {
		return new ItemStackHandler(slotCount)
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}
		};
	}
}
