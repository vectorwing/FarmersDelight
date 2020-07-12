package com.github.vectorwing.farmersdelight.blocks;

import com.github.vectorwing.farmersdelight.init.TileEntityInit;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class StoveBlockTile extends TileEntity implements IClearable, ITickableTileEntity
{
	private final int MAX_STACK_SIZE = 6;
	protected final NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_STACK_SIZE, ItemStack.EMPTY);
	private final int[] cookingTimes = new int[MAX_STACK_SIZE];
	private final int[] cookingTotalTimes = new int[MAX_STACK_SIZE];

	public StoveBlockTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public StoveBlockTile()	{ this(TileEntityInit.STOVE_TILE.get()); }

	@Override
	public void tick() {
		boolean isStoveLit = this.getBlockState().get(StoveBlock.LIT);
		if (this.world.isRemote) {
			if (isStoveLit) {
				this.addParticles();
			}

		} else {
			if (isStoveLit) {
				this.cookAndDrop();
			} else {
				for(int i = 0; i < this.inventory.size(); ++i) {
					if (this.cookingTimes[i] > 0) {
						this.cookingTimes[i] = MathHelper.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
					}
				}
			}
		}
	}

	private void cookAndDrop() {
		for(int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.get(i);
			if (!itemstack.isEmpty()) {
				++this.cookingTimes[i];
				if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
					IInventory iinventory = new Inventory(itemstack);
					ItemStack itemstack1 = this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, iinventory, this.world).map((p_213979_1_) -> {
						return p_213979_1_.getCraftingResult(iinventory);
					}).orElse(itemstack);
					if (world != null && !itemstack1.isEmpty()) {
						ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, itemstack1.copy());
						entity.setMotion(0, 0, 0);
						world.addEntity(entity);
					}
					this.inventory.set(i, ItemStack.EMPTY);
					this.inventoryChanged();
				}
			}
		}
	}

	public Vec2d getStoveItemOffset(int index) {
		final double X_OFFSET = 0.3D;
		final double Y_OFFSET = 0.2D;
		final Vec2d[] OFFSETS = {
			new Vec2d(X_OFFSET, Y_OFFSET),
			new Vec2d(0.0D, Y_OFFSET),
			new Vec2d(-X_OFFSET, Y_OFFSET),
			new Vec2d(X_OFFSET, -Y_OFFSET),
			new Vec2d(0.0D, -Y_OFFSET),
			new Vec2d(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}

	private void addParticles() {
		World world = this.getWorld();
		if (world != null) {
			BlockPos blockpos = this.getPos();
			Random random = world.rand;

			for(int j = 0; j < this.inventory.size(); ++j) {
				if (!this.inventory.get(j).isEmpty() && random.nextFloat() < 0.2F) {
					double d0 = (double)blockpos.getX() + 0.5D;
					double d1 = (double)blockpos.getY() + 1.0D;
					double d2 = (double)blockpos.getZ() + 0.5D;
					Vec2d v1 = this.getStoveItemOffset(j);

					Direction direction = this.getBlockState().get(StoveBlock.FACING);
					int directionIndex = direction.getHorizontalIndex();
					Vec2d offset = directionIndex % 2 == 0 ? v1 : new Vec2d(v1.y, v1.x);

					double d5 = d0 - (direction.getXOffset() * offset.x) + (direction.rotateY().getXOffset() * offset.x);
					double d7 = d2 - (direction.getZOffset() * offset.y) + (direction.rotateY().getZOffset() * offset.y);

					for(int k = 0; k < 3; ++k) {
						world.addParticle(ParticleTypes.SMOKE, d5, d1, d7, 0.0D, 5.0E-4D, 0.0D);
					}
				}
			}

		}
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.inventory.clear();
		ItemStackHelper.loadAllItems(compound, this.inventory);
		if (compound.contains("CookingTimes", 11)) {
			int[] aint = compound.getIntArray("CookingTimes");
			System.arraycopy(aint, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
		}

		if (compound.contains("CookingTotalTimes", 11)) {
			int[] aint1 = compound.getIntArray("CookingTotalTimes");
			System.arraycopy(aint1, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, aint1.length));
		}

	}

	public CompoundNBT write(CompoundNBT compound) {
		this.writeItems(compound);
		compound.putIntArray("CookingTimes", this.cookingTimes);
		compound.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory, true);
		return compound;
	}

	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	public CompoundNBT getUpdateTag() {
		return this.writeItems(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(pkt.getNbtCompound());
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
		return this.inventory.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn), this.world);
	}

	public boolean addItem(ItemStack itemStackIn, int cookTime) {
		for(int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.get(i);
			if (itemstack.isEmpty()) {
				this.cookingTotalTimes[i] = cookTime;
				this.cookingTimes[i] = 0;
				this.inventory.set(i, itemStackIn.split(1));
				this.inventoryChanged();
				return true;
			}
		}

		return false;
	}

	private void inventoryChanged() {
		super.markDirty();
		this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	public void clear() {
		this.inventory.clear();
	}
}
