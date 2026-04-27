package vectorwing.farmersdelight.common.block.entity.container;


import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;

public class CookingPotMenu extends RecipeBookMenu<RecipeWrapper, CookingPotRecipe>
{
	public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "item/empty_container_slot_bowl");

	public static final int INDEX_MEAL = 6;
	public static final int INDEX_CONTAINER = 7;
	public static final int INDEX_OUTPUT = 8;

	public final CookingPotBlockEntity blockEntity;
	public final ItemStackHandler inventory;
	private final ContainerData cookingPotData;
	private final ContainerLevelAccess canInteractWithCallable;
	protected final Level level;

	public CookingPotMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, getBlockEntity(playerInventory, data), new SimpleContainerData(4));
	}

	public CookingPotMenu(final int windowId, final Inventory playerInventory, final CookingPotBlockEntity blockEntity, ContainerData cookingPotData) {
		super(ModMenuTypes.COOKING_POT.get(), windowId);
		this.blockEntity = blockEntity;
		this.inventory = blockEntity.getInventory();
		this.cookingPotData = cookingPotData;
		this.level = playerInventory.player.level();
		this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

		// Ingredient Slots - 2 Rows x 3 Columns
		int startX = 8;
		int startY = 18;
		int inputStartX = 30;
		int inputStartY = 17;
		int borderSlotSize = 18;
		for (int row = 0; row < 2; ++row) {
			for (int column = 0; column < 3; ++column) {
				this.addSlot(new SlotItemHandler(inventory, (row * 3) + column,
						inputStartX + (column * borderSlotSize),
						inputStartY + (row * borderSlotSize)));
			}
		}

		// Meal Display
		this.addSlot(new CookingPotMealSlot(inventory, 6, 124, 26));

		// Bowl Input
		this.addSlot(new SlotItemHandler(inventory, 7, 92, 55)
		{
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_CONTAINER_SLOT_BOWL);
			}
		});

		// Bowl Output
		this.addSlot(new CookingPotResultSlot(playerInventory.player, blockEntity, inventory, 8, 124, 55));

		// Main Player Inventory
		int startPlayerInvY = startY * 4 + 12;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * borderSlotSize),
						startPlayerInvY + (row * borderSlotSize)));
			}
		}

		// Hotbar
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), 142));
		}

		this.addDataSlots(cookingPotData);
	}

	private static CookingPotBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(data.readBlockPos());
		if (blockEntity instanceof CookingPotBlockEntity cookingPot) {
			return cookingPot;
		}
		throw new IllegalStateException("Block entity is not correct! " + blockEntity);
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(canInteractWithCallable, playerIn, ModBlocks.COOKING_POT.get());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		int indexInventoryStart = INDEX_OUTPUT + 1;
		int indexInventoryEnd = indexInventoryStart + 36;
		ItemStack slotStackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			slotStackCopy = slotStack.copy();
			if (index == INDEX_OUTPUT) {
				if (!this.moveItemStackTo(slotStack, indexInventoryStart, indexInventoryEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index > INDEX_OUTPUT) {
				if (slotStack.is(ModTags.Items.SERVING_CONTAINERS) || slotStack.is(blockEntity.getContainer().getItem())) {
					if (!this.moveItemStackTo(slotStack, INDEX_CONTAINER, INDEX_OUTPUT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(slotStack, 0, INDEX_MEAL, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(slotStack, indexInventoryStart, indexInventoryEnd, false)) {
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (slotStack.getCount() == slotStackCopy.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, slotStack);
		}
		return slotStackCopy;
	}

	public int getCookProgressionScaled() {
		int i = this.cookingPotData.get(0);
		int j = this.cookingPotData.get(1);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	public boolean isHeated() {
		return blockEntity.isHeated();
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents helper) {
		for (int i = 0; i < inventory.getSlots(); i++) {
			helper.accountSimpleStack(inventory.getStackInSlot(i));
		}
	}

	@Override
	public void clearCraftingContent() {
		for (int i = 0; i < 6; i++) {
			this.inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean recipeMatches(RecipeHolder<CookingPotRecipe> recipe) {
		return recipe.value().matches(new RecipeWrapper(inventory), level);
	}

	@Override
	public int getResultSlotIndex() {
		return 7;
	}

	@Override
	public int getGridWidth() {
		return 3;
	}

	@Override
	public int getGridHeight() {
		return 2;
	}

	@Override
	public int getSize() {
		return 7;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.valueOf("FARMERSDELIGHT_COOKING");
	}

	@Override
	public boolean shouldMoveToInventory(int slot) {
		return slot < (getGridWidth() * getGridHeight());
	}
}
