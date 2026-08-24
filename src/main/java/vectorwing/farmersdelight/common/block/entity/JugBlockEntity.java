package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.container.JugMenu;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class JugBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, Clearable
{
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int JUG_CAPACITY = 16000;    // mB

	private final ItemStackHandler inventory;
	private final FluidTank fluidTank;
	private Component customName;

	public JugBlockEntity(BlockPos pos, BlockState state) {
		this(ModBlockEntityTypes.JUG.get(), pos, state);
	}

	public JugBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.inventory = createItemHandler();
		this.fluidTank = createFluidHandler();
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

	public static void jugTick(Level level, BlockPos pos, BlockState state, JugBlockEntity jug) {
//		boolean didInventoryChange = false;

		ItemStack input = jug.getInput();
		if (!input.isEmpty()) {
			jug.transferFluidWithInputSlot();
		}
	}

	public void transferFluidWithInputSlot() {
		ItemStack inputStack = getInput();

		// Try to transfer fluid using capabilities
		IFluidHandlerItem fluidHandler = inputStack.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler == null) return;

		// We have an input which has a fluid handler (empty or filled).
		// If the item contains fluid:
		// Does the fluid match the Jug's stored fluid?
		// Can the Jug fit the input's fluid?
		// Can the output slot fit what will be left behind after transfer?
		// If so, we empty the input into the Jug, and move the remainder to the output.
		// If the item has no fluid:
		// Can we fill the item with the Jug's stored fluid?
		// Can the result be moved to the output?
		// If so, we fill the input from the Jug, and move the remainder to the output.

		if (canDrainInput(inputStack)) {
			FluidActionResult result = FluidUtil.tryEmptyContainer(inputStack, fluidTank, fluidTank.getCapacity(), null, true);
			if (result.isSuccess()) {
				inventory.extractItem(INPUT_SLOT, 1, false);
				inventory.insertItem(OUTPUT_SLOT, result.getResult(), false);
			}
		} else if (canFillInput(inputStack)) {
			FluidActionResult result = FluidUtil.tryFillContainer(inputStack, fluidTank, fluidTank.getCapacity(), null, true);
			if (result.isSuccess()) {
				inventory.extractItem(INPUT_SLOT, 1, false);
				inventory.insertItem(OUTPUT_SLOT, result.getResult(), false);
			}
		}
	}

	public boolean canDrainInput(ItemStack stack) {
		FluidActionResult result = FluidUtil.tryEmptyContainer(stack, fluidTank, fluidTank.getCapacity(), null, false);
		return result.isSuccess() && inventory.insertItem(OUTPUT_SLOT, result.getResult(), true).isEmpty();
	}

	public boolean canFillInput(ItemStack stack) {
		FluidActionResult result = FluidUtil.tryFillContainer(stack, fluidTank, fluidTank.getCapacity(), null, false);
		return result.isSuccess() && inventory.insertItem(OUTPUT_SLOT, result.getResult(), true).isEmpty();
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
		return new JugMenu(containerId, playerInventory, this);
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

	@Override
	public void clearContent() {
		ItemUtils.clearItems(inventory);
		fluidTank.setFluid(FluidStack.EMPTY);
	}
}
