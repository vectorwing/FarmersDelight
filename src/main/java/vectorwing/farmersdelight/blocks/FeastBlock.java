package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
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
import vectorwing.farmersdelight.utils.TextUtils;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class FeastBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);
	public final Supplier<Item> servingItem;
	public final boolean hasLeftovers;

	protected static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
	};

	/**
	 * This block provides up to 4 servings of food to players who interact with it.
	 * If a leftover item is specified, the block lingers at 0 servings, and is destroyed on right-click.
	 *
	 * @param properties   Block properties.
	 * @param servingItem  The meal to be served.
	 * @param hasLeftovers Whether the block remains when out of servings. If false, the block vanishes once it runs out.
	 */
	public FeastBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties);
		this.servingItem = servingItem;
		this.hasLeftovers = hasLeftovers;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SERVINGS, 4));
	}

	public ItemStack getServingItem() {
		return new ItemStack(this.servingItem.get());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.getValue(SERVINGS)];
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			if (this.takeServing(worldIn, pos, state, player, handIn).consumesAction()) {
				return ActionResultType.SUCCESS;
			}
		}

		return this.takeServing(worldIn, pos, state, player, handIn);
	}

	private ActionResultType takeServing(IWorld worldIn, BlockPos pos, BlockState state, PlayerEntity player, Hand handIn) {
		int servings = state.getValue(SERVINGS);

		if (servings == 0) {
			worldIn.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
			worldIn.destroyBlock(pos, true);
			return ActionResultType.SUCCESS;
		}

		ItemStack serving = this.getServingItem();
		ItemStack heldStack = player.getItemInHand(handIn);

		if (servings > 0) {
			if (heldStack.sameItem(serving.getContainerItem())) {
				worldIn.setBlock(pos, state.setValue(SERVINGS, servings - 1), 3);
				if (!player.abilities.instabuild) {
					heldStack.shrink(1);
				}
				if (!player.inventory.add(serving)) {
					player.drop(serving, false);
				}
				if (worldIn.getBlockState(pos).getValue(SERVINGS) == 0 && !this.hasLeftovers) {
					worldIn.removeBlock(pos, false);
				}
				worldIn.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return ActionResultType.SUCCESS;
			} else {
				player.displayClientMessage(TextUtils.getTranslation("block.feast.use_container", serving.getContainerItem().getHoverName()), true);
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.below()).getMaterial().isSolid();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
		return blockState.getValue(SERVINGS);
	}

	public int getMaxServings() {
		return 4;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}
