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

	public PantryTileEntity() {
		super(ModTileEntityTypes.PANTRY_TILE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (!checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, pantryContents);
		}
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		pantryContents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		if (!checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, pantryContents);
		}
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return 27;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return pantryContents;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		pantryContents = itemsIn;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return TextUtils.getTranslation("container.pantry");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return ChestContainer.createGeneric9X3(id, player, this);
	}

	@Override
	public void openInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (numPlayersUsing < 0) {
				numPlayersUsing = 0;
			}

			++numPlayersUsing;
			BlockState state = getBlockState();
			boolean isOpen = state.get(PantryBlock.OPEN);
			if (!isOpen) {
				playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
				setOpenProperty(state, true);
			}

			scheduleTick();
		}
	}

	private void scheduleTick() {
		if (world != null) world.getPendingBlockTicks().scheduleTick(getPos(), getBlockState().getBlock(), 5);
	}

	public void tick() {
		if (world == null) return;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		numPlayersUsing = ChestTileEntity.calculatePlayersUsing(world, this, x, y, z);
		if (numPlayersUsing > 0) {
			scheduleTick();
		} else {
			BlockState state = getBlockState();
			if (!(state.getBlock() instanceof PantryBlock)) {
				remove();
				return;
			}

			boolean isOpen = state.get(PantryBlock.OPEN);
			if (isOpen) {
				playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
				setOpenProperty(state, false);
			}
		}

	}

	@Override
	public void closeInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			--numPlayersUsing;
		}

	}

	private void setOpenProperty(BlockState state, boolean open) {
		if (world != null) world.setBlockState(getPos(), state.with(PantryBlock.OPEN, open), 3);
	}

	private void playSound(BlockState state, SoundEvent sound) {
		if (world == null) return;

		Vector3i pantryFacingVector = state.get(PantryBlock.FACING).getDirectionVec();
		double x = (double) pos.getX() + 0.5D + (double) pantryFacingVector.getX() / 2.0D;
		double y = (double) pos.getY() + 0.5D + (double) pantryFacingVector.getY() / 2.0D;
		double z = (double) pos.getZ() + 0.5D + (double) pantryFacingVector.getZ() / 2.0D;
		world.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
