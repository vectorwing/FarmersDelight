package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.JugMenu;
import vectorwing.farmersdelight.common.block.entity.inventory.SingleSlotItemHandler;
import vectorwing.farmersdelight.common.crafting.FluidFillingRecipe;
import vectorwing.farmersdelight.common.crafting.SoakingRecipe;
import vectorwing.farmersdelight.common.crafting.input.FluidHandlingInput;
import vectorwing.farmersdelight.common.crafting.input.SoakingRecipeInput;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
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
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.JUG.get(), (be, context) -> context == Direction.UP ? be.inputHandler : be.outputHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.GLASS_JUG.get(), (be, context) -> context == Direction.UP ? be.inputHandler : be.outputHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntityTypes.JUG.get(), (be, context) -> be.fluidTank);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntityTypes.GLASS_JUG.get(), (be, context) -> be.fluidTank);
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

	public static void jugTick(Level level, BlockPos pos, BlockState state, JugBlockEntity jug) {
		ItemStack input = jug.getInput();

		if (!input.isEmpty()) {
			if (jug.emptyInput()) return;
			if (jug.fillInput()) return;

			// Second, if the above fails, check if the item can be soaked in the fluid.
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

	public boolean emptyInput() {
		if (level == null) return false;

		ItemStack inputStack = getInput();

		IFluidHandlerItem fluidHandler = inputStack.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler == null) return false;

		FluidActionResult result = FluidUtil.tryEmptyContainer(inputStack, fluidTank, fluidTank.getCapacity(), null, false);
		if (result.isSuccess() && canMoveItemToOutput(result.getResult())) {
			FluidUtil.tryEmptyContainer(inputStack, fluidTank, fluidTank.getCapacity(), null, true);
			inventory.extractItem(INPUT_SLOT, 1, false);
			inventory.insertItem(OUTPUT_SLOT, result.getResult(), false);
			return true;
		}

		return false;
	}

	public boolean fillInput() {
		if (level == null) return false;

		ItemStack inputStack = getInput();

		// Try using a recipe
		FluidHandlingInput fillingInput = new FluidHandlingInput(inputStack, fluidTank);
		Optional<RecipeHolder<FluidFillingRecipe>> recipe = getFillingRecipe(fillingInput);
		if (recipe.isPresent()) {
			FluidFillingRecipe fillingRecipe = recipe.get().value();
			ItemStack resultStack = fillingRecipe.assemble(fillingInput, this.level.registryAccess());
			if (canMoveItemToOutput(resultStack)) {
				inventory.extractItem(INPUT_SLOT, 1, false);
				inventory.insertItem(OUTPUT_SLOT, resultStack, false);
				fluidTank.drain(fillingRecipe.getFluid().amount(), IFluidHandler.FluidAction.EXECUTE);
				return true;
			}
		}

		// Try using a capability
		IFluidHandlerItem fluidHandler = inputStack.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler == null) return false;

		FluidActionResult result = FluidUtil.tryFillContainer(inputStack, fluidTank, fluidTank.getCapacity(), null, false);
		if (result.isSuccess() && canMoveItemToOutput(result.getResult())) {
			FluidUtil.tryFillContainer(inputStack, fluidTank, fluidTank.getCapacity(), null, true);
			inventory.extractItem(INPUT_SLOT, 1, false);
			inventory.insertItem(OUTPUT_SLOT, result.getResult(), false);
			return true;
		}

		return false;
	}

	public boolean canDrainInput(ItemStack stack) {
		if (level == null || stack.isEmpty()) return false;

		FluidActionResult result = FluidUtil.tryEmptyContainer(stack, fluidTank, fluidTank.getCapacity(), null, false);
		return result.isSuccess() && canMoveItemToOutput(result.getResult());
	}

	public boolean canFillInput(ItemStack stack) {
		if (level == null || stack.isEmpty()) return false;

		// Check for a fluid filling recipe
		FluidHandlingInput input = new FluidHandlingInput(getInput(), getFluidTank());
		Optional<RecipeHolder<FluidFillingRecipe>> recipe = this.getFillingRecipe(input);
		if (recipe.isPresent()) {
			ItemStack resultStack = recipe.get().value().assemble(input, this.level.registryAccess());
			return !resultStack.isEmpty() && canMoveItemToOutput(resultStack);
		}

		// Check if the input has a fluid capability
		FluidActionResult result = FluidUtil.tryFillContainer(stack, fluidTank, fluidTank.getCapacity(), null, false);
		return result.isSuccess() && canMoveItemToOutput(result.getResult());
	}

	public boolean canMoveItemToOutput(ItemStack stack) {
		return inventory.insertItem(OUTPUT_SLOT, stack, true).isEmpty();
	}

	public FluidActionResult useFluidContainerOnJug(ItemStack stack, Player player) {
		FluidActionResult emptyResult = FluidUtil.tryEmptyContainerAndStow(stack, fluidTank, new InvWrapper(player.getInventory()), fluidTank.getCapacity(), player, true);
		if (emptyResult.isSuccess()) {
			inventoryChanged();
			return emptyResult;
		}
		FluidActionResult fillResult = FluidUtil.tryFillContainerAndStow(stack, fluidTank, new InvWrapper(player.getInventory()), fluidTank.getCapacity(), player, true);
		if (fillResult.isSuccess()) {
			inventoryChanged();
		}
		return fillResult;
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
