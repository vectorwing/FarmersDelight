package vectorwing.farmersdelight.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.ItemUtils;
import vectorwing.farmersdelight.utils.MathUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StoveTileEntity extends TileEntity implements ITickableTileEntity
{
	private static final VoxelShape GRILLING_AREA = Block.makeCuboidShape(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);
	private static final int INVENTORY_SLOT_COUNT = 6;
	private final int[] cookingTimes = new int[INVENTORY_SLOT_COUNT];
	private final int[] cookingTotalTimes = new int[INVENTORY_SLOT_COUNT];

	private ItemStackHandler inventoryNew;
//	private CampfireCookingRecipe lastRecipe;

	public StoveTileEntity(TileEntityType<?> tileEntityType) {
		super(tileEntityType);
		inventoryNew = createHandler();
	}

	public StoveTileEntity() {
		this(ModTileEntityTypes.STOVE_TILE.get());
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SLOT_COUNT)
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}
		};
	}

	@Override
	public void tick() {
		if (world == null) {
			return;
		}

		boolean isStoveLit = this.getBlockState().get(StoveBlock.LIT);

		if (this.world.isRemote) {
			if (isStoveLit) {
				this.addParticles();
			}
		} else {
			boolean isStoveBlocked = this.isStoveBlockedAbove();
			if (isStoveBlocked) {
				if (!ItemUtils.isInventoryEmpty(this.inventoryNew)) {
					ItemUtils.dropItems(world, pos, this.inventoryNew);
					this.inventoryChanged();
				}
			} else if (isStoveLit) {
				this.cookAndOutputItems();
			} else {
				for (int i = 0; i < this.inventoryNew.getSlots(); ++i) {
					if (this.cookingTimes[i] > 0) {
						this.cookingTimes[i] = MathHelper.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
					}
				}
			}
		}
	}

	private void cookAndOutputItems() {
		if (world == null) {
			return;
		}

		boolean didInventoryChange = false;
		for (int i = 0; i < this.inventoryNew.getSlots(); ++i) {
			ItemStack stoveStack = this.inventoryNew.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				++this.cookingTimes[i];
				if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
					IInventory inventory = new Inventory(stoveStack);
					ItemStack result = this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, inventory, this.world).map((recipe) -> recipe.getCraftingResult(inventory)).orElse(stoveStack);
					if (!result.isEmpty()) {
						ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, result.copy());
						entity.setMotion(MathUtils.RAND.nextGaussian() * (double) 0.01F, 0.1F, MathUtils.RAND.nextGaussian() * (double) 0.01F);
						world.addEntity(entity);
					}
					this.inventoryNew.setStackInSlot(i, ItemStack.EMPTY);
					didInventoryChange = true;
				}
			}
		}

		if (didInventoryChange) {
			this.inventoryChanged();
		}
	}

	public boolean isStoveBlockedAbove() {
		if (world != null) {
			BlockState above = world.getBlockState(pos.up());
			return VoxelShapes.compare(GRILLING_AREA, above.getShape(world, pos.up()), IBooleanFunction.AND);
		}
		return false;
	}

	public Vector2f getStoveItemOffset(int index) {
		final float X_OFFSET = 0.3F;
		final float Y_OFFSET = 0.2F;
		final Vector2f[] OFFSETS = {
				new Vector2f(X_OFFSET, Y_OFFSET),
				new Vector2f(0.0F, Y_OFFSET),
				new Vector2f(-X_OFFSET, Y_OFFSET),
				new Vector2f(X_OFFSET, -Y_OFFSET),
				new Vector2f(0.0F, -Y_OFFSET),
				new Vector2f(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}

	private void addParticles() {
		if (this.world == null) {
			return;
		}

		BlockPos pos = this.getPos();
		for (int i = 0; i < this.inventoryNew.getSlots(); ++i) {
			if (!this.inventoryNew.getStackInSlot(i).isEmpty() && this.world.rand.nextFloat() < 0.2F) {
				Vector2f stoveItemVector = this.getStoveItemOffset(i);
				Direction direction = this.getBlockState().get(StoveBlock.HORIZONTAL_FACING);
				int directionIndex = direction.getHorizontalIndex();
				Vector2f offset = directionIndex % 2 == 0 ? stoveItemVector : new Vector2f(stoveItemVector.y, stoveItemVector.x);

				double x = ((double) pos.getX() + 0.5D) - (direction.getXOffset() * offset.x) + (direction.rotateY().getXOffset() * offset.x);
				double y = (double) pos.getY() + 1.0D;
				double z = ((double) pos.getZ() + 0.5D) - (direction.getZOffset() * offset.y) + (direction.rotateY().getZOffset() * offset.y);

				for (int k = 0; k < 3; ++k) {
					world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
				}
			}
		}
	}

	public ItemStackHandler getInventory() {
		return this.inventoryNew;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains("Inventory")) {
			this.inventoryNew.deserializeNBT(compound.getCompound("Inventory"));
		} else {
			this.inventoryNew.deserializeNBT(compound);
		}
		if (compound.contains("CookingTimes", 11)) {
			int[] aint = compound.getIntArray("CookingTimes");
			System.arraycopy(aint, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
		}

		if (compound.contains("CookingTotalTimes", 11)) {
			int[] aint1 = compound.getIntArray("CookingTotalTimes");
			System.arraycopy(aint1, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, aint1.length));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		this.writeItems(compound);
		compound.putIntArray("CookingTimes", this.cookingTimes);
		compound.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", inventoryNew.serializeNBT());
		return compound;
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.writeItems(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.read(state, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(this.getBlockState(), pkt.getNbtCompound());
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
		return world == null /*|| this.inventory.stream().noneMatch(ItemStack::isEmpty)*/ ? Optional.empty() : this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn), this.world);
	}

	public boolean addItem(ItemStack itemStackIn, int cookTime) {
		for (int i = 0; i < this.inventoryNew.getSlots(); ++i) {
			ItemStack itemstack = this.inventoryNew.getStackInSlot(i);
			if (itemstack.isEmpty()) {
				this.cookingTotalTimes[i] = cookTime;
				this.cookingTimes[i] = 0;
				this.inventoryNew.setStackInSlot(i, itemStackIn.split(1));
				this.inventoryChanged();
				return true;
			}
		}

		return false;
	}

	private void inventoryChanged() {
		super.markDirty();
		if (world != null)
			this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}
}
