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
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (!trySaveLootTable(compound)) {
			ItemStackHelper.saveAllItems(compound, pantryContents);
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		pantryContents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!tryLoadLootTable(compound)) {
			ItemStackHelper.loadAllItems(compound, pantryContents);
		}
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getContainerSize() {
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
		return ChestContainer.threeRows(id, player, this);
	}

	@Override
	public void startOpen(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (numPlayersUsing < 0) {
				numPlayersUsing = 0;
			}

			++numPlayersUsing;
			BlockState state = getBlockState();
			boolean isOpen = state.getValue(PantryBlock.OPEN);
			if (!isOpen) {
				playSound(state, SoundEvents.BARREL_OPEN);
				setOpenProperty(state, true);
			}

			scheduleTick();
		}
	}

	private void scheduleTick() {
		if (level != null) level.getBlockTicks().scheduleTick(getBlockPos(), getBlockState().getBlock(), 5);
	}

	public void tick() {
		if (level == null) return;

		int x = worldPosition.getX();
		int y = worldPosition.getY();
		int z = worldPosition.getZ();
		numPlayersUsing = ChestTileEntity.getOpenCount(level, this, x, y, z);
		if (numPlayersUsing > 0) {
			scheduleTick();
		} else {
			BlockState state = getBlockState();
			if (!(state.getBlock() instanceof PantryBlock)) {
				setRemoved();
				return;
			}

			boolean isOpen = state.getValue(PantryBlock.OPEN);
			if (isOpen) {
				playSound(state, SoundEvents.BARREL_CLOSE);
				setOpenProperty(state, false);
			}
		}

	}

	@Override
	public void stopOpen(PlayerEntity player) {
		if (!player.isSpectator()) {
			--numPlayersUsing;
		}

	}

	private void setOpenProperty(BlockState state, boolean open) {
		if (level != null) level.setBlock(getBlockPos(), state.setValue(PantryBlock.OPEN, open), 3);
	}

	private void playSound(BlockState state, SoundEvent sound) {
		if (level == null) return;

		Vector3i pantryFacingVector = state.getValue(PantryBlock.FACING).getNormal();
		double x = (double) worldPosition.getX() + 0.5D + (double) pantryFacingVector.getX() / 2.0D;
		double y = (double) worldPosition.getY() + 0.5D + (double) pantryFacingVector.getY() / 2.0D;
		double z = (double) worldPosition.getZ() + 0.5D + (double) pantryFacingVector.getZ() / 2.0D;
		level.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}
}
