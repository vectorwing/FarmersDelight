package vectorwing.farmersdelight.tile;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.blocks.CuttingBoardBlock;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

import javax.annotation.Nullable;

public class CuttingBoardTileEntity extends TileEntity
{
	private boolean isItemCarvingBoard;
	private ItemStackHandler itemHandler = createHandler();
	protected final IRecipeType<? extends CuttingBoardRecipe> recipeType;

	public CuttingBoardTileEntity(TileEntityType<?> tileEntityTypeIn, IRecipeType<? extends CuttingBoardRecipe> recipeTypeIn) {
		super(tileEntityTypeIn);
		this.recipeType = recipeTypeIn;
		this.isItemCarvingBoard = false;
	}

	public CuttingBoardTileEntity() { this(ModTileEntityTypes.CUTTING_BOARD_TILE.get(), CuttingBoardRecipe.TYPE); }

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.isItemCarvingBoard = compound.getBoolean("IsItemCarved");
		this.itemHandler.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", itemHandler.serializeNBT());
		compound.putBoolean("IsItemCarved", this.isItemCarvingBoard);
		return compound;
	}

	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(pkt.getNbtCompound());
	}

	private void inventoryChanged() {
		super.markDirty();
		this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	// ======== RECIPE PROCESSING ========

	/**
	 * Attempts to apply a recipe to the Cutting Board's stored item, using the given tool.
	 * @param tool The item stack used to process the item.
	 * @return Whether the process succeeded or failed.
	 */
	public boolean processItemUsingTool(ItemStack tool, @Nullable PlayerEntity player) {
		CuttingBoardRecipe irecipe = this.world.getRecipeManager()
				.getRecipe(this.recipeType, new RecipeWrapper(itemHandler), this.world).orElse(null);

		if (irecipe != null && irecipe.getTool().test(tool)) {
			NonNullList<ItemStack> results = this.getResults();
			for (ItemStack result : results) {
				Direction direction = this.getBlockState().get(CuttingBoardBlock.FACING).rotateYCCW();
				ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5 + (direction.getXOffset() * 0.2), pos.getY() + 0.2, pos.getZ() + 0.5 + (direction.getZOffset() * 0.2), result.copy());
				entity.setMotion(direction.getXOffset() * 0.2F, 0.0F, direction.getZOffset() * 0.2F);
				world.addEntity(entity);
			}
			if (player != null) {
				tool.damageItem(1, player, (user) -> {
					user.sendBreakAnimation(EquipmentSlotType.MAINHAND);
				});
			} else {
				if (tool.attemptDamageItem(1, world.rand, (ServerPlayerEntity)null)) {
					tool.setCount(0);
				}
			}
			this.removeItem();
			this.inventoryChanged();
			return true;
		}

		return false;
	}

	protected NonNullList<ItemStack> getResults() {
		return this.world.getRecipeManager().getRecipe(this.recipeType, new RecipeWrapper(itemHandler), this.world).map(CuttingBoardRecipe::getResults).orElse(NonNullList.withSize(1, ItemStack.EMPTY));
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
		return this.itemHandler;
	}

	public boolean isEmpty() {
		return this.itemHandler.getStackInSlot(0).isEmpty();
	}

	public ItemStack getStoredItem() {
		return this.itemHandler.getStackInSlot(0);
	}

	public boolean addItem(ItemStack itemStack) {
		if (this.isEmpty() && !itemStack.isEmpty()) {
			this.itemHandler.setStackInSlot(0, itemStack.split(1));
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
		return new ItemStackHandler() {
			@Override
			public int getSlotLimit(int slot)
			{
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				// TODO: Do we do anything here?
			}
		};
	}
}
