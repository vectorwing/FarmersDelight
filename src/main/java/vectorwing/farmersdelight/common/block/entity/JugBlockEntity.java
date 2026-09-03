package vectorwing.farmersdelight.common.block.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.JugMenu;
import vectorwing.farmersdelight.common.block.entity.inventory.SingleSlotItemHandler;
import vectorwing.farmersdelight.common.crafting.FluidEmptyingRecipe;
import vectorwing.farmersdelight.common.crafting.FluidFillingRecipe;
import vectorwing.farmersdelight.common.crafting.SoakingRecipe;
import vectorwing.farmersdelight.common.crafting.input.FluidHandlingInput;
import vectorwing.farmersdelight.common.crafting.input.SoakingRecipeInput;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.FluidHandlingUtils;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Optional;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class JugBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, Clearable
{
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int JUG_CAPACITY = 16000;    // mB

	private final ItemStackHandler inventory;
	private final IItemHandler inputHandler;
	private final IItemHandler outputHandler;
	private final FluidTank fluidTank;

	private int processingTime;
	private int processingTimeTotal;
	protected final ContainerData containerData;

	private Component customName;

	private final RecipeManager.CachedCheck<SoakingRecipeInput, SoakingRecipe> soakingCache;
	private final RecipeManager.CachedCheck<FluidHandlingInput, FluidFillingRecipe> fillingCache;
	private final RecipeManager.CachedCheck<FluidHandlingInput, FluidEmptyingRecipe> emptyingCache;

	public JugBlockEntity(BlockPos pos, BlockState state) {
		this(ModBlockEntityTypes.JUG.get(), pos, state);
	}

	public JugBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.inventory = createItemHandler();
		this.inputHandler = new SingleSlotItemHandler(inventory, INPUT_SLOT);
		this.outputHandler = new SingleSlotItemHandler(inventory, OUTPUT_SLOT);
		this.fluidTank = createFluidHandler();
		this.containerData = createIntArray();

		this.soakingCache = RecipeManager.createCheck(ModRecipeTypes.SOAKING.get());
		this.fillingCache = RecipeManager.createCheck(ModRecipeTypes.FLUID_FILLING.get());
		this.emptyingCache = RecipeManager.createCheck(ModRecipeTypes.FLUID_EMPTYING.get());
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.JUG.get(), (jug, context) -> context == Direction.UP ? jug.inputHandler : jug.outputHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.GLASS_JUG.get(), (jug, context) -> context == Direction.UP ? jug.inputHandler : jug.outputHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntityTypes.JUG.get(), (jug, context) -> jug.fluidTank);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntityTypes.GLASS_JUG.get(), (jug, context) -> jug.fluidTank);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		inventory.deserializeNBT(registries, tag);
		fluidTank.readFromNBT(registries, tag);
		if (tag.contains("CustomName", 8)) {
			this.customName = parseCustomNameSafe(tag.getString("CustomName"), registries);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (ItemUtils.doesInventoryHaveItems(inventory)) {
			tag.merge(inventory.serializeNBT(registries));
		}
		fluidTank.writeToNBT(registries, tag);
		if (this.customName != null) {
			tag.putString("CustomName", Component.Serializer.toJson(this.customName, registries));
		}
	}

	private Optional<RecipeHolder<SoakingRecipe>> getSoakingRecipe(SoakingRecipeInput recipeInput) {
		if (level == null) return Optional.empty();
		return !getInput().isEmpty() ? soakingCache.getRecipeFor(recipeInput, this.level) : Optional.empty();
	}

	private Optional<RecipeHolder<FluidFillingRecipe>> getFillingRecipe(FluidHandlingInput recipeInput) {
		if (level == null) return Optional.empty();
		return !getInput().isEmpty() ? fillingCache.getRecipeFor(recipeInput, this.level) : Optional.empty();
	}

	private Optional<RecipeHolder<FluidEmptyingRecipe>> getEmptyingRecipe(FluidHandlingInput recipeInput) {
		if (level == null) return Optional.empty();
		return !getInput().isEmpty() ? emptyingCache.getRecipeFor(recipeInput, this.level) : Optional.empty();
	}

	public static void jugTick(Level level, BlockPos pos, BlockState state, JugBlockEntity jug) {
		ItemStack input = jug.getInput();

		if (!input.isEmpty()) {
			if (jug.emptyInputSlot()) return;
			if (jug.fillInputSlot()) return;

			Optional<RecipeHolder<SoakingRecipe>> recipe = jug.getSoakingRecipe(new SoakingRecipeInput(jug.getInput(), jug.getFluidTank().getFluid()));
			if (recipe.isPresent() && jug.canSoakInput(recipe.get().value())) {
				jug.processSoaking(recipe.get(), jug);
			} else {
				jug.processingTime = 0;
			}
		} else {
			jug.processingTime = 0;
		}
	}

	public boolean emptyInputSlot() {
		if (level == null) return false;

		Pair<FluidStack, ItemStack> testResult = FluidHandlingUtils.testEmptyingInput(getInput(), fluidTank, level, emptyingCache);
		FluidStack resultFluid = testResult.getFirst();
		ItemStack resultStack = testResult.getSecond();
		if (!resultFluid.isEmpty() && canMoveItemToOutput(resultStack)) {
			processSlots(resultStack);
			fluidTank.fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
			return true;
		}

		return false;
	}

	public boolean fillInputSlot() {
		if (level == null) return false;

		Pair<Integer, ItemStack> testResult = FluidHandlingUtils.testFillingInput(getInput(), fluidTank, level, fillingCache);
		int resultFluidAmount = testResult.getFirst();
		ItemStack resultStack = testResult.getSecond();
		if (resultFluidAmount > 0 && canMoveItemToOutput(resultStack)) {
			processSlots(resultStack);
			fluidTank.drain(resultFluidAmount, IFluidHandler.FluidAction.EXECUTE);
			return true;
		}

		return false;
	}

	public boolean canMoveItemToOutput(ItemStack stack) {
		return inventory.insertItem(OUTPUT_SLOT, stack, true).isEmpty();
	}

	public void processSlots(ItemStack stackToOutput) {
		inventory.extractItem(INPUT_SLOT, 1, false);
		inventory.insertItem(OUTPUT_SLOT, stackToOutput, false);
	}

	public FluidActionResult useItemOnJug(ItemStack stack, Player player) {
		Pair<FluidStack, ItemStack> testEmptying = FluidHandlingUtils.testEmptyingInput(stack, fluidTank, level, emptyingCache);
		FluidStack resultFluid = testEmptying.getFirst();
		ItemStack resultStack = testEmptying.getSecond();

		if (!resultFluid.isEmpty()) {
			fluidTank.fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
			if (level != null) {
				playFluidEmptySound(level, resultFluid.getFluidType(), resultStack.getItem());
			}
			return getFluidResultAndStoreRemainder(stack, resultStack, player);
		}

		Pair<Integer, ItemStack> testFilling = FluidHandlingUtils.testFillingInput(stack, fluidTank, level, fillingCache);
		int resultAmount = testFilling.getFirst();
		resultStack = testFilling.getSecond();

		if (resultAmount > 0) {
			if (level != null) {
				playFluidFillSound(level, fluidTank.getFluid().getFluidType(), stack.getItem());
			}
			fluidTank.drain(resultAmount, IFluidHandler.FluidAction.EXECUTE);
			return getFluidResultAndStoreRemainder(stack, resultStack, player);
		}

		return FluidActionResult.FAILURE;
	}

	public void playFluidFillSound(Level level, FluidType fluid, ItemLike item) {
		if (item.equals(Items.GLASS_BOTTLE)) {
			level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1, 1);
		} else {
			SoundEvent sound = fluid.getSound(SoundActions.BUCKET_FILL);
			if (sound != null) {
				level.playSound(null, getBlockPos(), sound, SoundSource.BLOCKS, 1, 1);
			}
		}
	}

	public void playFluidEmptySound(Level level, FluidType fluid, ItemLike item) {
		if (item.equals(Items.GLASS_BOTTLE)) {
			level.playSound(null, getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1, 1);
		} else {
			SoundEvent sound = fluid.getSound(SoundActions.BUCKET_EMPTY);
			if (sound != null) {
				level.playSound(null, getBlockPos(), sound, SoundSource.BLOCKS, 1, 1);
			}
		}
	}

	public FluidActionResult getFluidResultAndStoreRemainder(ItemStack inputStack, ItemStack resultStack, Player player) {
		if (player.getAbilities().instabuild) {
			return new FluidActionResult(inputStack);
		}
		if (inputStack.getCount() == 1) {
			return new FluidActionResult(resultStack);
		} else {
			ItemStack remainderStack = ItemHandlerHelper.insertItemStacked(new InvWrapper(player.getInventory()), resultStack, false);

			if (!remainderStack.isEmpty()) {
				ItemHandlerHelper.giveItemToPlayer(player, remainderStack);
			}

			ItemStack stackCopy = inputStack.copy();
			stackCopy.shrink(1);
			return new FluidActionResult(stackCopy);
		}
	}

	public boolean canSoakInput(SoakingRecipe recipe) {
		if (level == null || getInput().isEmpty()) return false;

		ItemStack resultStack = recipe.assemble(new SoakingRecipeInput(getInput(), fluidTank.getFluid()), this.level.registryAccess());
		if (resultStack.isEmpty()) return false;
		return inventory.insertItem(OUTPUT_SLOT, resultStack, true).isEmpty();
	}

	private void processSoaking(RecipeHolder<SoakingRecipe> recipe, JugBlockEntity jug) {
		if (level == null) return;

		SoakingRecipe soakingRecipe = recipe.value();

		++processingTime;
		processingTimeTotal = soakingRecipe.getProcessingTime();
		if (processingTime < processingTimeTotal) {
			return;
		}

		processingTime = 0;
		ItemStack resultStack = soakingRecipe.assemble(new SoakingRecipeInput(jug.getInput(), jug.getFluidTank().getFluid()), this.level.registryAccess());
		if (!resultStack.isEmpty()) {
			inventory.insertItem(OUTPUT_SLOT, resultStack, false);
			inventory.extractItem(INPUT_SLOT, 1, false);
			if (soakingRecipe.doesConsumeFluid()) {
				fluidTank.drain(soakingRecipe.getFluid().amount(), IFluidHandler.FluidAction.EXECUTE);
			}
		}
	}

	public FluidTank getFluidTank() {
		return fluidTank;
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getInput() {
		return inventory.getStackInSlot(INPUT_SLOT);
	}

	public ItemStack getOutput() {
		return inventory.getStackInSlot(OUTPUT_SLOT);
	}

	@Override
	public Component getName() {
		return this.customName != null ? this.customName : TextUtils.container("jug");
	}

	@Override
	public Component getDisplayName() {
		return this.getName();
	}

	@Override
	@javax.annotation.Nullable
	public Component getCustomName() {
		return customName;
	}

	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);
		this.customName = componentInput.get(DataComponents.CUSTOM_NAME);
		this.fluidTank.setFluid(componentInput.getOrDefault(ModDataComponents.FLUID_TANK.get(), SimpleFluidContent.EMPTY).copy());
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CUSTOM_NAME, this.customName);
		components.set(ModDataComponents.FLUID_TANK.get(), SimpleFluidContent.copyOf(this.fluidTank.getFluid()));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("CustomName");
		tag.remove("Items");
		tag.remove("Size");
		tag.remove("Fluid");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new JugMenu(containerId, playerInventory, this, containerData);
	}

	private ItemStackHandler createItemHandler() {
		return new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	private FluidTank createFluidHandler() {
		return new FluidTank(JUG_CAPACITY)
		{
			@Override
			protected void onContentsChanged() {
				inventoryChanged();
			}
		};
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
		this.loadWithComponents(pkt.getTag(), lookupProvider);
	}

	private ContainerData createIntArray() {
		return new ContainerData()
		{
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> JugBlockEntity.this.processingTime;
					case 1 -> JugBlockEntity.this.processingTimeTotal;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> JugBlockEntity.this.processingTime = value;
					case 1 -> JugBlockEntity.this.processingTimeTotal = value;
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};
	}

	@Override
	public void clearContent() {
		ItemUtils.clearItems(inventory);
		fluidTank.setFluid(FluidStack.EMPTY);
	}
}
