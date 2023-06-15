package vectorwing.farmersdelight.common.block.entity.container;


import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

import java.util.Objects;

public class CookingPotMenu extends RecipeBookMenu<RecipeWrapper>
{
	public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = new ResourceLocation(FarmersDelight.MODID, "item/empty_container_slot_bowl");

	public final CookingPotBlockEntity tileEntity;
	public final ItemStackHandler inventory;
	private final ContainerData cookingPotData;
	private final ContainerLevelAccess canInteractWithCallable;
	protected final Level level;

	public CookingPotMenu(final int windowId, final Inventory playerInventory, final CookingPotBlockEntity tileEntity, ContainerData cookingPotDataIn) {
		super(ModMenuTypes.COOKING_POT.get(), windowId);
		this.tileEntity = tileEntity;
		this.inventory = tileEntity.getInventory();
		this.cookingPotData = cookingPotDataIn;
		this.level = playerInventory.player.level();
		this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());

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
			@OnlyIn(Dist.CLIENT)
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_CONTAINER_SLOT_BOWL);
			}
		});

		// Bowl Output
		this.addSlot(new CookingPotResultSlot(playerInventory.player, tileEntity, inventory, 8, 124, 55));

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

		this.addDataSlots(cookingPotDataIn);
	}

	private static CookingPotBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
		if (tileAtPos instanceof CookingPotBlockEntity) {
			return (CookingPotBlockEntity) tileAtPos;
		}
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public CookingPotMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data), new SimpleContainerData(4));
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(canInteractWithCallable, playerIn, ModBlocks.COOKING_POT.get());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		int indexMealDisplay = 6;
		int indexContainerInput = 7;
		int indexOutput = 8;
		int startPlayerInv = indexOutput + 1;
		int endPlayerInv = startPlayerInv + 36;
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == indexOutput) {
				if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index > indexOutput) {
				if (itemstack1.getItem() == Items.BOWL && !this.moveItemStackTo(itemstack1, indexContainerInput, indexContainerInput + 1, false)) {
					return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(itemstack1, 0, indexMealDisplay, false)) {
					return ItemStack.EMPTY;
				} else if (!this.moveItemStackTo(itemstack1, indexContainerInput, indexOutput, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@OnlyIn(Dist.CLIENT)
	public int getCookProgressionScaled() {
		int i = this.cookingPotData.get(0);
		int j = this.cookingPotData.get(1);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isHeated() {
		return tileEntity.isHeated();
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
	public boolean recipeMatches(Recipe<? super RecipeWrapper> recipe) {
		return recipe.matches(new RecipeWrapper(inventory), level);
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
		return FarmersDelight.RECIPE_TYPE_COOKING;
	}

	@Override
	public boolean shouldMoveToInventory(int slot) {
		return slot < (getGridWidth() * getGridHeight());
	}
}
