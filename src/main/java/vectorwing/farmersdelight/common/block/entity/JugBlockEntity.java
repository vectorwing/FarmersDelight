package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.container.JugMenu;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class JugBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, Clearable
{
	private final ItemStackHandler inventory;
	private Component customName;

	public JugBlockEntity(BlockPos pos, BlockState state) {
		this(ModBlockEntityTypes.JUG.get(), pos, state);
	}

	public JugBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.inventory = createHandler();
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		inventory.deserializeNBT(registries, tag);
		if (tag.contains("CustomName", 8)) {
			this.customName = parseCustomNameSafe(tag.getString("CustomName"), registries);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.merge(inventory.serializeNBT(registries));
		if (this.customName != null) {
			tag.putString("CustomName", Component.Serializer.toJson(this.customName, registries));
		}
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getInput() {
		return inventory.getStackInSlot(0);
	}

	public ItemStack getOutput() {
		return inventory.getStackInSlot(1);
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
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(DataComponents.CUSTOM_NAME, this.customName);
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("CustomName");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new JugMenu(containerId, playerInventory, this);
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	@Override
	public void clearContent() {
		ItemUtils.clearItems(inventory);
	}
}
