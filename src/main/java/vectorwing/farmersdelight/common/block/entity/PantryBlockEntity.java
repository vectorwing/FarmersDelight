package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.PantryBlock;
import vectorwing.farmersdelight.core.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.core.util.TextUtils;

public class PantryBlockEntity extends RandomizableContainerBlockEntity
{
	private NonNullList<ItemStack> pantryContents = NonNullList.withSize(27, ItemStack.EMPTY);
	private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter()
	{
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			PantryBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
			PantryBlockEntity.this.updateBlockState(state, true);
		}

		protected void onClose(Level level, BlockPos pos, BlockState state) {
			PantryBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
			PantryBlockEntity.this.updateBlockState(state, false);
		}

		protected void openerCountChanged(Level level, BlockPos pos, BlockState sta, int arg1, int arg2) {
		}

		protected boolean isOwnContainer(Player p_155060_) {
			if (p_155060_.containerMenu instanceof ChestMenu) {
				Container container = ((ChestMenu) p_155060_.containerMenu).getContainer();
				return container == PantryBlockEntity.this;
			} else {
				return false;
			}
		}
	};

	public PantryBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.PANTRY.get(), pos, state);
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
	public void load(CompoundTag compound) {
		super.load(compound);
		pantryContents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!tryLoadLootTable(compound)) {
			ContainerHelper.loadAllItems(compound, pantryContents);
		}
	}

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

	public void startOpen(Player pPlayer) {
		if (level != null && !this.remove && !pPlayer.isSpectator()) {
			this.openersCounter.incrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
		}
	}

	public void stopOpen(Player pPlayer) {
		if (level != null && !this.remove && !pPlayer.isSpectator()) {
			this.openersCounter.decrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
		}
	}

	public void recheckOpen() {
		if (level != null && !this.remove) {
			this.openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
		}
	}

	void updateBlockState(BlockState state, boolean open) {
		if (level != null) {
			this.level.setBlock(this.getBlockPos(), state.setValue(PantryBlock.OPEN, open), 3);
		}
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
