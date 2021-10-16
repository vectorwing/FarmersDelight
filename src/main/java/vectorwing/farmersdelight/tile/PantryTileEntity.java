package vectorwing.farmersdelight.tile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import vectorwing.farmersdelight.blocks.PantryBlock;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.TextUtils;

public class PantryTileEntity extends RandomizableContainerBlockEntity
{
	private NonNullList<ItemStack> pantryContents = NonNullList.withSize(27, ItemStack.EMPTY);
	private int numPlayersUsing;

	public PantryTileEntity() {
		super(ModTileEntityTypes.PANTRY_TILE);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		if (!trySaveLootTable(compound)) {
			ContainerHelper.saveAllItems(compound, pantryContents);
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		pantryContents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!tryLoadLootTable(compound)) {
			ContainerHelper.loadAllItems(compound, pantryContents);
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
	protected Component getDefaultName() {
		return TextUtils.getTranslation("container.pantry");
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return ChestMenu.threeRows(id, player, this);
	}

	@Override
	public void startOpen(Player player) {
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
		numPlayersUsing = ChestBlockEntity.getOpenCount(level, this, x, y, z);
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
	public void stopOpen(Player player) {
		if (!player.isSpectator()) {
			--numPlayersUsing;
		}

	}

	private void setOpenProperty(BlockState state, boolean open) {
		if (level != null) level.setBlock(getBlockPos(), state.setValue(PantryBlock.OPEN, open), 3);
	}

	private void playSound(BlockState state, SoundEvent sound) {
		if (level == null) return;

		Vec3i pantryFacingVector = state.getValue(PantryBlock.FACING).getNormal();
		double x = (double) worldPosition.getX() + 0.5D + (double) pantryFacingVector.getX() / 2.0D;
		double y = (double) worldPosition.getY() + 0.5D + (double) pantryFacingVector.getY() / 2.0D;
		double z = (double) worldPosition.getZ() + 0.5D + (double) pantryFacingVector.getZ() / 2.0D;
		level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
	}
}
