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
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
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

public class CuttingBoardTileEntity extends TileEntity
{
	private boolean isItemCarvingBoard;
	private final ItemStackHandler inventory = createHandler();
	private LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> inventory);
	protected final IRecipeType<? extends CuttingBoardRecipe> recipeType;

	private ResourceLocation lastRecipeID;

	public CuttingBoardTileEntity(TileEntityType<?> tileEntityTypeIn, IRecipeType<? extends CuttingBoardRecipe> recipeTypeIn) {
		super(tileEntityTypeIn);
		this.recipeType = recipeTypeIn;
		this.isItemCarvingBoard = false;
	}

	public CuttingBoardTileEntity() {
		this(ModTileEntityTypes.CUTTING_BOARD_TILE.get(), CuttingBoardRecipe.TYPE);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.isItemCarvingBoard = compound.getBoolean("IsItemCarved");
		this.inventory.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", inventory.serializeNBT());
		compound.putBoolean("IsItemCarved", this.isItemCarvingBoard);
		return compound;
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(this.getBlockState(), pkt.getNbtCompound());
	}

	private void inventoryChanged() {
		super.markDirty();
		this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	// ======== RECIPE PROCESSING ========

	/**
	 * Checks for a recipe that matches the given inventory and tool.
	 * The last recipe is cached to optimize repeating processes.
	 * If a player is passed, they will be notified of invalid items or tools with a status message.
	 */
	private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper inventory, ItemStack toolStack, @Nullable PlayerEntity player) {
		if (this.world == null) return Optional.empty();
		if (lastRecipeID != null) {
			IRecipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) this.world.getRecipeManager())
					.getRecipeMap(CuttingBoardRecipe.TYPE)
					.get(lastRecipeID);
			if (recipe instanceof CuttingBoardRecipe && recipe.matches(inventory, world) && ((CuttingBoardRecipe) recipe).getTool().test(toolStack)) {
				return Optional.of((CuttingBoardRecipe) recipe);
			}
		}
		List<CuttingBoardRecipe> recipeList = this.world.getRecipeManager().getRecipes(CuttingBoardRecipe.TYPE, inventory, this.world);
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

	/**
	 * Attempts to apply a recipe to the Cutting Board's stored item, using the given tool.
	 *
	 * @param toolStack The item stack used to process the item.
	 * @return Whether the process succeeded or failed.
	 */
	public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable PlayerEntity player) {
		if (this.world == null) return false;

		Optional<CuttingBoardRecipe> matchingRecipe = this.getMatchingRecipe(new RecipeWrapper(this.inventory), toolStack, player);

		matchingRecipe.ifPresent(recipe -> {
			NonNullList<ItemStack> results = recipe.getResults();
			for (ItemStack result : results) {
				Direction direction = this.getBlockState().get(CuttingBoardBlock.HORIZONTAL_FACING).rotateYCCW();
				ItemUtils.spawnItemEntity(world, result.copy(),
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
			this.playProcessingSound(recipe.getSoundEventID(), toolStack.getItem(), this.getStoredItem().getItem());
			this.removeItem();
			this.inventoryChanged();
			if (player instanceof ServerPlayerEntity) {
				ModAdvancements.CUTTING_BOARD.trigger((ServerPlayerEntity) player);
			}
		});

		return matchingRecipe.isPresent();
	}

	public void playProcessingSound(String soundEventID, Item tool, Item boardItem) {
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundEventID));

		if (sound != null) {
			this.playSound(sound, 1.0F, 1.0F);
		} else if (tool.isIn(Tags.Items.SHEARS)) {
			this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
		} else if (tool.isIn(ForgeTags.TOOLS_KNIVES)) {
			this.playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
		} else if (boardItem instanceof BlockItem) {
			Block block = ((BlockItem) boardItem).getBlock();
			SoundType soundType = block.getDefaultState().getSoundType();
			this.playSound(soundType.getBreakSound(), 1.0F, 0.8F);
		} else {
			this.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 0.8F);
		}
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		this.world.playSound(null, this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, sound, SoundCategory.BLOCKS, volume, pitch);
	}

	// ======== ITEM HANDLING ========

	/**
	 * Places the given stack on the board, but carved into it instead of laying on top.
	 * This is purely for decoration purposes; the item can still be processed.
	 * Ideally, the caller checks if the item is a damageable tool first.
	 */
	public boolean carveToolOnBoard(ItemStack tool) {
		if (this.addItem(tool)) {
			this.isItemCarvingBoard = true;
			return true;
		}
		return false;
	}

	public boolean getIsItemCarvingBoard() {
		return this.isItemCarvingBoard;
	}

	public IItemHandler getInventory() {
		return this.inventory;
	}

	public boolean isEmpty() {
		return this.inventory.getStackInSlot(0).isEmpty();
	}

	public ItemStack getStoredItem() {
		return this.inventory.getStackInSlot(0);
	}

	public boolean addItem(ItemStack itemStack) {
		if (this.isEmpty() && !itemStack.isEmpty()) {
			this.inventory.setStackInSlot(0, itemStack.split(1));
			this.isItemCarvingBoard = false;
			this.inventoryChanged();
			return true;
		}
		return false;
	}

	public ItemStack removeItem() {
		if (!this.isEmpty()) {
			this.isItemCarvingBoard = false;
			ItemStack item = this.getStoredItem().split(1);
			this.inventoryChanged();
			return item;
		}
		return ItemStack.EMPTY;
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
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
}
