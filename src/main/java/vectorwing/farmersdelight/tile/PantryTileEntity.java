package vectorwing.farmersdelight.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import vectorwing.farmersdelight.blocks.PantryBlock;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.TextUtils;

public class PantryTileEntity extends LockableLootTileEntity
{
	private NonNullList<ItemStack> pantryContents = NonNullList.withSize(27, ItemStack.EMPTY);
	private int numPlayersUsing;

	private PantryTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public PantryTileEntity() {
		this(ModTileEntityTypes.PANTRY_TILE.get());
	}

	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.pantryContents);
		}

		return compound;
	}

	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.pantryContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if (!this.checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, this.pantryContents);
		}

	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return 27;
	}

	protected NonNullList<ItemStack> getItems() {
		return this.pantryContents;
	}

	protected void setItems(NonNullList<ItemStack> itemsIn) {
		this.pantryContents = itemsIn;
	}

	protected ITextComponent getDefaultName() {
		return TextUtils.getTranslation("container.pantry");
	}

	protected Container createMenu(int id, PlayerInventory player) {
		return ChestContainer.createGeneric9X3(id, player, this);
	}

	public void openInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			BlockState blockstate = this.getBlockState();
			boolean flag = blockstate.get(PantryBlock.OPEN);
			if (!flag) {
				this.playSound(blockstate, SoundEvents.BLOCK_BARREL_OPEN);
				this.setOpenProperty(blockstate, true);
			}

			this.scheduleTick();
		}

	}

	private void scheduleTick() {
		this.world.getPendingBlockTicks().scheduleTick(this.getPos(), this.getBlockState().getBlock(), 5);
	}

	public void pantryTick() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		this.numPlayersUsing = ChestTileEntity.calculatePlayersUsing(this.world, this, i, j, k);
		if (this.numPlayersUsing > 0) {
			this.scheduleTick();
		} else {
			BlockState blockstate = this.getBlockState();
			if (!(blockstate.getBlock() instanceof PantryBlock)) {
				this.remove();
				return;
			}

			boolean flag = blockstate.get(PantryBlock.OPEN);
			if (flag) {
				this.playSound(blockstate, SoundEvents.BLOCK_BARREL_CLOSE);
				this.setOpenProperty(blockstate, false);
			}
		}

	}

	public void closeInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			--this.numPlayersUsing;
		}

	}

	private void setOpenProperty(BlockState state, boolean open) {
		this.world.setBlockState(this.getPos(), state.with(PantryBlock.OPEN, open), 3);
	}

	private void playSound(BlockState state, SoundEvent sound) {
		Vector3i vec3i = state.get(PantryBlock.FACING).getDirectionVec();
		double d0 = (double) this.pos.getX() + 0.5D + (double) vec3i.getX() / 2.0D;
		double d1 = (double) this.pos.getY() + 0.5D + (double) vec3i.getY() / 2.0D;
		double d2 = (double) this.pos.getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
		this.world.playSound((PlayerEntity) null, d0, d1, d2, sound, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
