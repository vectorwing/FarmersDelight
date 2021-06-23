package vectorwing.farmersdelight.blocks;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PieBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);

	public final Supplier<Item> pieSlice;

	public PieBlock(Properties properties, Supplier<Item> pieSlice) {
		super(properties);
		this.pieSlice = pieSlice;
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(BITES, 0));
	}

	public ItemStack getPieSliceItem() {
		return new ItemStack(this.pieSlice.get());
	}

	public int getMaxBites() {
		return 4;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemStack itemstack = player.getHeldItem(handIn);
		if (worldIn.isRemote) {
			if (ModTags.KNIVES.contains(itemstack.getItem())) {
				return cutSlice(worldIn, pos, state);
			}

			if (this.consumeBite(worldIn, pos, state, player) == ActionResultType.SUCCESS) {
				return ActionResultType.SUCCESS;
			}

			if (itemstack.isEmpty()) {
				return ActionResultType.CONSUME;
			}
		}

		if (ModTags.KNIVES.contains(itemstack.getItem())) {
			return cutSlice(worldIn, pos, state);
		}
		return this.consumeBite(worldIn, pos, state, player);
	}

	/**
	 * Eats a slice from the pie, feeding the player.
	 */
	protected ActionResultType consumeBite(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn) {
		if (!playerIn.canEat(false)) {
			return ActionResultType.PASS;
		} else {
			ItemStack slice = this.getPieSliceItem();
			Food sliceFood = slice.getItem().getFood();

			playerIn.getFoodStats().consume(slice.getItem(), slice);
			if (this.getPieSliceItem().getItem().isFood() && sliceFood != null) {
				for (Pair<EffectInstance, Float> pair : sliceFood.getEffects()) {
					if (!worldIn.isRemote && pair.getFirst() != null && worldIn.rand.nextFloat() < pair.getSecond()) {
						playerIn.addPotionEffect(new EffectInstance(pair.getFirst()));
					}
				}
			}

			int bites = state.get(BITES);
			if (bites < getMaxBites() - 1) {
				worldIn.setBlockState(pos, state.with(BITES, bites + 1), 3);
			} else {
				worldIn.removeBlock(pos, false);
			}
			worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);
			return ActionResultType.SUCCESS;
		}
	}

	/**
	 * Cuts off a bite and drops a slice item, without feeding the player.
	 */
	protected ActionResultType cutSlice(World worldIn, BlockPos pos, BlockState state) {
		int bites = state.get(BITES);
		if (bites < getMaxBites() - 1) {
			worldIn.setBlockState(pos, state.with(BITES, bites + 1), 3);
		} else {
			worldIn.removeBlock(pos, false);
		}
		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), this.getPieSliceItem());
		worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
		return ActionResultType.SUCCESS;
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
		builder.add(FACING, BITES);
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return getMaxBites() - blockState.get(BITES);
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
