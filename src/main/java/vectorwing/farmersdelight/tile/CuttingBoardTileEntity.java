package vectorwing.farmersdelight.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.blocks.CuttingBoardBlock;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.mixin.accessors.RecipeManagerAccessor;
import vectorwing.farmersdelight.registry.ModAdvancements;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.ItemUtils;
import vectorwing.farmersdelight.utils.TextUtils;
import vectorwing.farmersdelight.utils.tags.ForgeTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CuttingBoardTileEntity extends FDSyncedTileEntity
{
	private final ItemStackHandler inventory;
	private final LazyOptional<IItemHandler> inputHandler;
	private ResourceLocation lastRecipeID;

	private boolean isItemCarvingBoard;

	public CuttingBoardTileEntity() {
		super(ModTileEntityTypes.CUTTING_BOARD_TILE.get());
		inventory = createHandler();
		inputHandler = LazyOptional.of(() -> inventory);
		isItemCarvingBoard = false;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		isItemCarvingBoard = compound.getBoolean("IsItemCarved");
		inventory.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", inventory.serializeNBT());
		compound.putBoolean("IsItemCarved", isItemCarvingBoard);
		return compound;
	}

	public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable PlayerEntity player) {
		if (world == null) return false;

		Optional<CuttingBoardRecipe> matchingRecipe = getMatchingRecipe(new RecipeWrapper(inventory), toolStack, player);

		matchingRecipe.ifPresent(recipe -> {
			NonNullList<ItemStack> results = recipe.getResults();
			for (ItemStack resultStack : results) {
				Direction direction = getBlockState().get(CuttingBoardBlock.HORIZONTAL_FACING).rotateYCCW();
				ItemUtils.spawnItemEntity(world, resultStack.copy(),
						pos.getX() + 0.5 + (direction.getXOffset() * 0.2), pos.getY() + 0.2, pos.getZ() + 0.5 + (direction.getZOffset() * 0.2),
						direction.getXOffset() * 0.2F, 0.0F, direction.getZOffset() * 0.2F);
			}
			if (player != null) {
				toolStack.damageItem(1, player, (user) -> user.sendBreakAnimation(EquipmentSlotType.MAINHAND));
			} else {
				if (toolStack.attemptDamageItem(1, world.rand, null)) {
					toolStack.setCount(0);
				}
			}
			playProcessingSound(recipe.getSoundEventID(), toolStack.getItem(), getStoredItem().getItem());
			removeItem();
			if (player instanceof ServerPlayerEntity) {
				ModAdvancements.CUTTING_BOARD.trigger((ServerPlayerEntity) player);
			}
		});

		return matchingRecipe.isPresent();
	}

	private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack toolStack, @Nullable PlayerEntity player) {
		if (world == null) return Optional.empty();
		if (lastRecipeID != null) {
			IRecipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) world.getRecipeManager())
					.getRecipeMap(CuttingBoardRecipe.TYPE)
					.get(lastRecipeID);
			if (recipe instanceof CuttingBoardRecipe && recipe.matches(recipeWrapper, world) && ((CuttingBoardRecipe) recipe).getTool().test(toolStack)) {
				return Optional.of((CuttingBoardRecipe) recipe);
			}
		}
		List<CuttingBoardRecipe> recipeList = world.getRecipeManager().getRecipes(CuttingBoardRecipe.TYPE, recipeWrapper, world);
		if (recipeList.isEmpty()) {
			if (player != null)
				player.sendStatusMessage(TextUtils.getTranslation("block.cutting_board.invalid_item"), true);
			return Optional.empty();
		}
		Optional<CuttingBoardRecipe> recipe = recipeList.stream().filter(cuttingRecipe -> cuttingRecipe.getTool().test(toolStack)).findFirst();
		if (!recipe.isPresent()) {
			if (player != null)
				player.sendStatusMessage(TextUtils.getTranslation("block.cutting_board.invalid_tool"), true);
			return Optional.empty();
		}
		lastRecipeID = recipe.get().getId();
		return recipe;
	}

	public void playProcessingSound(String soundEventID, Item tool, Item boardItem) {
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundEventID));

		if (sound != null) {
			playSound(sound, 1.0F, 1.0F);
		} else if (tool.isIn(Tags.Items.SHEARS)) {
			playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
		} else if (tool.isIn(ForgeTags.TOOLS_KNIVES)) {
			playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
		} else if (boardItem instanceof BlockItem) {
			Block block = ((BlockItem) boardItem).getBlock();
			SoundType soundType = block.getDefaultState().getSoundType();
			playSound(soundType.getBreakSound(), 1.0F, 0.8F);
		} else {
			playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 0.8F);
		}
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		if (world != null)
			world.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, sound, SoundCategory.BLOCKS, volume, pitch);
	}

	public boolean addItem(ItemStack itemStack) {
		if (isEmpty() && !itemStack.isEmpty()) {
			inventory.setStackInSlot(0, itemStack.split(1));
			isItemCarvingBoard = false;
			inventoryChanged();
			return true;
		}
		return false;
	}

	public boolean carveToolOnBoard(ItemStack tool) {
		if (addItem(tool)) {
			isItemCarvingBoard = true;
			return true;
		}
		return false;
	}

	public ItemStack removeItem() {
		if (!isEmpty()) {
			isItemCarvingBoard = false;
			ItemStack item = getStoredItem().split(1);
			inventoryChanged();
			return item;
		}
		return ItemStack.EMPTY;
	}

	public IItemHandler getInventory() {
		return inventory;
	}

	public ItemStack getStoredItem() {
		return inventory.getStackInSlot(0);
	}

	public boolean isEmpty() {
		return inventory.getStackInSlot(0).isEmpty();
	}

	public boolean isItemCarvingBoard() {
		return isItemCarvingBoard;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
			return inputHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void remove() {
		super.remove();
		inputHandler.invalidate();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}
		};
	}
}
