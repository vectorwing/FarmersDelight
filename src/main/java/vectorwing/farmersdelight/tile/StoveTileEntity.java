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
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.MathUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StoveTileEntity extends TileEntity implements IClearable, ITickableTileEntity
{
	private static final VoxelShape GRILLING_AREA = Block.makeCuboidShape(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);
	private final int MAX_STACK_SIZE = 6;
	private final int[] cookingTimes = new int[MAX_STACK_SIZE];
	private final int[] cookingTotalTimes = new int[MAX_STACK_SIZE];
	protected final NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_STACK_SIZE, ItemStack.EMPTY);

	public StoveTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public StoveTileEntity() {
		this(ModTileEntityTypes.STOVE_TILE.get());
	}

	@Override
	public void tick() {
		boolean isStoveLit = this.getBlockState().get(StoveBlock.LIT);
		boolean isStoveBlocked = this.isStoveBlockedAbove();
		if (world != null && this.world.isRemote) {
			if (isStoveLit) {
				this.addParticles();
			}
		} else {
			if (world != null && isStoveBlocked) {
				if (!this.inventory.isEmpty()) {
					InventoryHelper.dropItems(world, pos, this.getInventory());
					this.inventoryChanged();
				}
			}
			if (isStoveLit && !isStoveBlocked) {
				this.cookAndDrop();
			} else {
				for (int i = 0; i < this.inventory.size(); ++i) {
					if (this.cookingTimes[i] > 0) {
						this.cookingTimes[i] = MathHelper.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
					}
				}
			}
		}
	}

	private void cookAndDrop() {
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.get(i);
			if (!itemstack.isEmpty()) {
				++this.cookingTimes[i];
				if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
					if (world != null) {
						IInventory inventory = new Inventory(itemstack);
						ItemStack result = this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, inventory, this.world).map((recipe) -> recipe.getCraftingResult(inventory)).orElse(itemstack);
						if (!result.isEmpty()) {
							ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, result.copy());
							entity.setMotion(MathUtils.RAND.nextGaussian() * (double) 0.01F, 0.1F, MathUtils.RAND.nextGaussian() * (double) 0.01F);
							world.addEntity(entity);
						}
					}
					this.inventory.set(i, ItemStack.EMPTY);
					this.inventoryChanged();
				}
			}
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
		World world = this.getWorld();
		if (world != null) {
			BlockPos blockpos = this.getPos();
			Random random = world.rand;

			for (int j = 0; j < this.inventory.size(); ++j) {
				if (!this.inventory.get(j).isEmpty() && random.nextFloat() < 0.2F) {
					double d0 = (double) blockpos.getX() + 0.5D;
					double d1 = (double) blockpos.getY() + 1.0D;
					double d2 = (double) blockpos.getZ() + 0.5D;
					Vector2f v1 = this.getStoveItemOffset(j);

					Direction direction = this.getBlockState().get(StoveBlock.FACING);
					int directionIndex = direction.getHorizontalIndex();
					Vector2f offset = directionIndex % 2 == 0 ? v1 : new Vector2f(v1.y, v1.x);

					double d5 = d0 - (direction.getXOffset() * offset.x) + (direction.rotateY().getXOffset() * offset.x);
					double d7 = d2 - (direction.getZOffset() * offset.y) + (direction.rotateY().getZOffset() * offset.y);

					for (int k = 0; k < 3; ++k) {
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
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
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

	@Override
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
		return world == null || this.inventory.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn), this.world);
	}

	public boolean addItem(ItemStack itemStackIn, int cookTime) {
		for (int i = 0; i < this.inventory.size(); ++i) {
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
		if (world != null)
			this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}
}
