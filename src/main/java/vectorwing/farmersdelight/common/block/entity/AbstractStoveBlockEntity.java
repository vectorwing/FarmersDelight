package vectorwing.farmersdelight.common.block.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Clearable
{
	private final ItemStacksResourceHandler items;
	private final int[] cookingProgress;
	private final int[] cookingTime;
	private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickRecipeLookup;

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

	public ItemStacksResourceHandler getItems() {
		return this.items;
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);

		items.deserialize(input);

		Optional<int[]> maybeCookingTimes = input.getIntArray("CookingTimes");
		if (maybeCookingTimes.isPresent()) {
			int[] arrayCookingTimes = maybeCookingTimes.get();
			System.arraycopy(arrayCookingTimes, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, arrayCookingTimes.length));
		}

		Optional<int[]> maybeCookingTimesTotal = input.getIntArray("CookingTotalTimes");
		if (maybeCookingTimesTotal.isPresent()) {
			int[] arrayCookingTimesTotal = maybeCookingTimesTotal.get();
			System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, arrayCookingTimesTotal.length));
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		items.serialize(output);
		output.putIntArray("CookingTimes", this.cookingProgress);
		output.putIntArray("CookingTotalTimes", this.cookingTime);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);

		ProblemReporter.Collector reporter = new ProblemReporter.Collector(problemPath());
		TagValueOutput valueOutput = TagValueOutput.createWithContext(reporter, registries);
		items.serialize(valueOutput);
		tag.put("inventory", valueOutput.buildResult());

		if (!reporter.isEmpty()) {
			FarmersDelight.LOGGER.warn("[{}] Serialization errors:\n{}", FarmersDelight.LOGGER.getName(), reporter.getTreeReport());
		}

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
		assert this.level instanceof ServerLevel;

		boolean didChange = false;
		for (int i = 0; i < this.items.size(); ++i) {
			ItemStack ingredient = this.items.getResource(i).toStack(this.items.getAmountAsInt(i));
			if (ingredient.isEmpty()) continue;
			didChange = true;

			++cookingProgress[i];
			if (cookingProgress[i] < cookingTime[i]) continue;

			var input = new SingleRecipeInput(ingredient);
			ItemStack result = this.quickRecipeLookup.getRecipeFor(input, (ServerLevel) this.level)
				.map((recipe) -> recipe.value().assemble(input))
				.orElse(ingredient);

			if (!result.isItemEnabled(this.level.enabledFeatures())) continue;
			ItemUtils.spawnItemEntity(level, result.copy(),
				worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5,
				level.getRandom().nextGaussian() * (double) 0.01F, 0.1F, level.getRandom().nextGaussian() * (double) 0.01F);
			this.items.set(i, ItemResource.EMPTY, 0);
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
			int thisItemCookingProgress = this.cookingProgress[i];
			if (thisItemCookingProgress <= 0) continue;
			didChange = true;
			this.cookingProgress[i] = Mth.clamp(thisItemCookingProgress - 2, 0, this.cookingTime[i]);
		}
		if (didChange) this.setChanged();
	}

	public Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> getCookingRecipe(ItemStack itemStack) {
		assert this.level != null;
		if (!(level instanceof ServerLevel)) {
			return Optional.empty();
		}
		return this.quickRecipeLookup.getRecipeFor(new SingleRecipeInput(itemStack), (ServerLevel) this.level);
	}

	public int getNextEmptySlot() {
		return IntStream.range(0, this.items.size())
			.filter((i) -> this.items.getAmountAsInt(i) == 0)
			.findFirst()
			.orElse(-1);
	}

	public boolean placeFood(@Nullable Entity entity, ItemStack foodStackToPlace, RecipeHolder<? extends AbstractCookingRecipe> recipe) {
		assert this.level != null;

		int emptySlotIndex = getNextEmptySlot();
		if (emptySlotIndex < 0) return false;
		assert this.items.getResource(emptySlotIndex).isEmpty();

		this.cookingTime[emptySlotIndex] = recipe.value().cookingTime();
		this.cookingProgress[emptySlotIndex] = 0;
		ItemStack split = foodStackToPlace.split(1);
		this.items.set(emptySlotIndex, ItemResource.of(split), split.count());
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
		return this.items.copyToList().stream();
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

	private static ItemStacksResourceHandler createHandler(int slotCount) {
		return new ItemStacksResourceHandler(slotCount)
		{
			@Override
			public int getCapacity(int slot, ItemResource resource) {
				return 1;
			}
		};
	}
}
