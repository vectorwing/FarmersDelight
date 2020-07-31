package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
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
import org.apache.logging.log4j.LogManager;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.init.ModItems;

import javax.annotation.Nullable;
import java.util.Random;
import org.apache.logging.log4j.Logger;

public class RiceCropBlock extends BushBlock implements IWaterLoggable, IGrowable
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 9.0D, 11.0D),
			Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 9.0D, 11.0D),
			Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)};

	public RiceCropBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, true).with(AGE, 0));
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1)) return;
		if (worldIn.getLightSubtracted(pos.up(), 0) >= 6) {
			int i = this.getAge(state);
			if (i <= this.getMaxAge()) {
				float f = 10;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
					if (i == 4) {
						TallRiceCropBlock tallRice = (TallRiceCropBlock) ModBlocks.TALL_RICE_CROP.get();
						if (tallRice.getDefaultState().isValidPosition(worldIn, pos) && worldIn.isAirBlock(pos.up())) {
							tallRice.placeAt(worldIn, pos, 2, 6);
							net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
						}
					} else {
						worldIn.setBlockState(pos, this.withAge(i + 1), 2);
						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
					}
				}
			}
		}

	}

	public void grow(World worldIn, BlockPos pos, BlockState state) {
		int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
		int j = 7;
		if (i > j) i = j;
		if (i <= 4) {
			worldIn.setBlockState(pos, state.with(AGE, i));
		} else {
			TallRiceCropBlock tallRice = (TallRiceCropBlock)ModBlocks.TALL_RICE_CROP.get();
			if (tallRice.getDefaultState().isValidPosition(worldIn, pos) && worldIn.isAirBlock(pos.up())) {
				tallRice.placeAt(worldIn, pos, 2, i);
			}
		}
	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.nextInt(worldIn.rand, 2, 5);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		IFluidState ifluidstate = worldIn.getFluidState(pos);
		return super.isValidPosition(state, worldIn, pos) && ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.isSolidSide(worldIn, pos, Direction.UP) && (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_BLOCK);
	}

	public IntegerProperty getAgeProperty() { return AGE; }

	protected int getAge(BlockState state) {
		return state.get(this.getAgeProperty());
	}

	public int getMaxAge() {
		return 4;
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.RICE.get());
	}

	public BlockState withAge(int age) {
		return this.getDefaultState().with(this.getAgeProperty(), Integer.valueOf(age));
	}

	public boolean isMaxAge(BlockState state) {
		return state.get(this.getAgeProperty()) >= this.getMaxAge();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE, WATERLOGGED);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		if (!blockstate.isAir()) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return blockstate;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8 ? super.getStateForPlacement(context) : null;
	}

	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{
		this.grow(worldIn, pos, state);
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)	{ return false;	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {	return false; }

	public IFluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStillFluidState(false);
	}
}
