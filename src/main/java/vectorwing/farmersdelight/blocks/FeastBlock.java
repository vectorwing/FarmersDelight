package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class FeastBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);
	public final Supplier<Item> servingItem;
	public final Supplier<Item> leftoverItem;

	protected static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
	};

	/**
	 * This block provides up to 4 servings of food to players who interact with it.
	 * If a leftover item is specified, the block lingers at 0 servings, and is destroyed on right-click.
	 * @param properties Block properties.
	 * @param servingItem The meal to be served.
	 * @param leftoverItem An item to be given back when out of servings, such as a dirty dish.
	 */
	public FeastBlock(Properties properties, Supplier<Item> servingItem, @Nullable Supplier<Item> leftoverItem) {
		super(properties);
		this.servingItem = servingItem;
		this.leftoverItem = leftoverItem;
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(SERVINGS, 4));
	}

	public ItemStack getServingItem() {
		return new ItemStack(this.servingItem.get());
	}

	public ItemStack getLeftoverItem() {
		return leftoverItem == null ? ItemStack.EMPTY : new ItemStack(this.leftoverItem.get());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.get(SERVINGS)];
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			if (this.takeServing(worldIn, pos, state, player, handIn).isSuccessOrConsume()) {
				return ActionResultType.SUCCESS;
			}
		}

		return this.takeServing(worldIn, pos, state, player, handIn);
	}

	private ActionResultType takeServing(IWorld worldIn, BlockPos pos, BlockState state, PlayerEntity player, Hand handIn) {
		int servings = state.get(SERVINGS);

		if (servings == 0) {
			InventoryHelper.spawnItemStack((World) worldIn, pos.getX(), pos.getY(), pos.getZ(), this.getLeftoverItem());
			worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
			worldIn.removeBlock(pos, false);
			return ActionResultType.SUCCESS;
		}

		ItemStack serving = this.getServingItem();
		ItemStack heldItem = player.getHeldItem(handIn);

		if (servings > 0 && heldItem.isItemEqual(serving.getContainerItem())) {
			worldIn.setBlockState(pos, state.with(SERVINGS, servings - 1), 3);
			if (!player.abilities.isCreativeMode) {
				heldItem.shrink(1);
			}
			if (!player.inventory.addItemStackToInventory(serving)) {
				player.dropItem(serving, false);
			}
			if (worldIn.getBlockState(pos).get(SERVINGS) == 0 && getLeftoverItem().isEmpty()) {
				worldIn.removeBlock(pos, false);
			}
			worldIn.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return blockState.get(SERVINGS);
	}

	public int getMaxServings() {
		return 4;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}
