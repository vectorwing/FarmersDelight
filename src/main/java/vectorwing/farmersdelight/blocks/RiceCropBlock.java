package vectorwing.farmersdelight.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class RiceCropBlock extends BushBlock implements IWaterLoggable, IGrowable {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;
	public static final BooleanProperty SUPPORTING = BooleanProperty.create("supporting");
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
			Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)};

	public RiceCropBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.getDefaultState().with(AGE, 0).with(SUPPORTING, false));
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1)) return;
		if (worldIn.getLightSubtracted(pos.up(), 0) >= 6) {
			int age = this.getAge(state);
			if (age <= this.getMaxAge()) {
				float chance = 10;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / chance) + 1) == 0)) {
					if (age == this.getMaxAge()) {
						RiceUpperCropBlock riceUpper = (RiceUpperCropBlock)ModBlocks.RICE_UPPER_CROP.get();
						if (riceUpper.getDefaultState().isValidPosition(worldIn, pos.up()) && worldIn.isAirBlock(pos.up())) {
							worldIn.setBlockState(pos.up(), riceUpper.getDefaultState());
							net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
						}
					} else {
						worldIn.setBlockState(pos, this.withAge(age + 1), 2);
						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
					}
				}
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		FluidState ifluidstate = worldIn.getFluidState(pos);
		return super.isValidPosition(state, worldIn, pos) && ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return super.isValidGround(state, worldIn, pos) || Tags.Blocks.DIRT.contains(state.getBlock());
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	protected int getAge(BlockState state) {
		return state.get(this.getAgeProperty());
	}

	public int getMaxAge() {
		return 3;
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.RICE.get());
	}

	public BlockState withAge(int age) {
		return this.getDefaultState().with(this.getAgeProperty(), age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.get(this.getAgeProperty()) >= this.getMaxAge();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE, SUPPORTING);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		if (!blockstate.isAir()) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		if (facing == Direction.UP) {
			return blockstate.with(SUPPORTING, isSupportingRiceUpper(facingState));
		}

		return blockstate;
	}

	public boolean isSupportingRiceUpper(BlockState topState) {
		return topState.getBlock() == ModBlocks.RICE_UPPER_CROP.get();
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8 ? super.getStateForPlacement(context) : null;
	}

	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		BlockState upperState = worldIn.getBlockState(pos.up());
		if (upperState.getBlock() instanceof RiceUpperCropBlock) {
			return !((RiceUpperCropBlock)upperState.getBlock()).isMaxAge(upperState);
		}
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.nextInt(worldIn.rand, 1, 4);
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state){
		int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(worldIn), 7);
		if (ageGrowth <= this.getMaxAge()) {
			worldIn.setBlockState(pos, state.with(AGE, ageGrowth));
		} else {
			BlockState top = worldIn.getBlockState(pos.up());
			if (top.getBlock() == ModBlocks.RICE_UPPER_CROP.get()) {
				IGrowable growable = (IGrowable) worldIn.getBlockState(pos.up()).getBlock();
				if (growable.canGrow(worldIn, pos.up(), top, false)) {
					growable.grow(worldIn, worldIn.rand, pos.up(), top);
				}
			} else {
				RiceUpperCropBlock riceUpper = (RiceUpperCropBlock)ModBlocks.RICE_UPPER_CROP.get();
				int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
				if (riceUpper.getDefaultState().isValidPosition(worldIn, pos.up()) && worldIn.isAirBlock(pos.up())) {
					worldIn.setBlockState(pos, state.with(AGE, this.getMaxAge()));
					worldIn.setBlockState(pos.up(), riceUpper.getDefaultState().with(RiceUpperCropBlock.RICE_AGE, remainingGrowth), 2);
				}
			}
		}
	}

	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStillFluidState(false);
	}
}
