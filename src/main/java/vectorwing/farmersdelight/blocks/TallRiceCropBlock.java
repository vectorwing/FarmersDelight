package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.init.ModItems;

import javax.annotation.Nullable;
import java.util.Random;

public class TallRiceCropBlock extends BushBlock implements IWaterLoggable, IGrowable
{
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty AGE = IntegerProperty.create("age", 5, 7);

	public TallRiceCropBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER).with(AGE, 5));
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}
	public int getMaxAge() {
		return 7;
	}
	protected int getAge(BlockState state) {
		return state.get(this.getAgeProperty());
	}
	public boolean isMaxAge(BlockState state) {
		return state.get(this.getAgeProperty()) >= this.getMaxAge();
	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.nextInt(worldIn.rand, 2, 5);
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1)) return;
		if (state.get(HALF) == DoubleBlockHalf.UPPER && worldIn.getLightSubtracted(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (i < this.getMaxAge()) {
				float f = 10;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
					worldIn.setBlockState(pos, state.with(AGE, i + 1), 2);
					if (worldIn.getBlockState(pos.down()).getBlock() == this && worldIn.getBlockState(pos.down()).get(AGE) < this.getMaxAge()) {
						worldIn.setBlockState(pos.down(), worldIn.getBlockState(pos.down()).with(AGE,i + 1), 2);
					}
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}

	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return !this.isMaxAge(state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return true;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{
		this.grow(worldIn, pos, state);
	}

	protected IItemProvider getSeedsItem() {
		return ModItems.RICE.get();
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(this.getSeedsItem());
	}

	public BlockState withAge(int age) {
		return this.getDefaultState().with(this.getAgeProperty(), Integer.valueOf(age));
	}

	public void grow(World worldIn, BlockPos pos, BlockState state) {
		int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
		int j = this.getMaxAge();
		if (i > j) i = j;

		worldIn.setBlockState(pos, state.with(AGE, i));
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			worldIn.setBlockState(pos.down(), worldIn.getBlockState(pos.down()).with(AGE, i));
		} else {
			worldIn.setBlockState(pos.up(), worldIn.getBlockState(pos.up()).with(AGE, i));
		}
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		DoubleBlockHalf half = stateIn.get(HALF);
		if (!blockstate.isAir()) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.getBlock() == this && facingState.get(HALF) != half) {
			return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return blockpos.getY() < context.getWorld().getDimension().getHeight() - 1
				&& ifluidstate.isTagged(FluidTags.WATER)
				&& ifluidstate.getLevel() == 8
				&& context.getWorld().getBlockState(blockpos.up()).isReplaceable(context)
				? super.getStateForPlacement(context) : null;
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public void placeAt(IWorld worldIn, BlockPos pos, int flags, int age) {
		worldIn.setBlockState(pos, this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), flags);
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
			if (blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
				worldIn.setBlockState(blockpos, Blocks.WATER.getDefaultState(), 35);
			} else {
				worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			}
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			if (!worldIn.isRemote && !player.isCreative()) {
				spawnDrops(state, worldIn, pos, (TileEntity)null, player, player.getHeldItemMainhand());
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, AGE, HALF);
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.isSolidSide(worldIn, pos, Direction.UP) && state.getBlock() == Blocks.DIRT;
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		if (state.get(HALF) != DoubleBlockHalf.UPPER) {
			IFluidState ifluidstate = worldIn.getFluidState(pos);
			return this.isValidGround(worldIn.getBlockState(pos.down()), worldIn, pos) && ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8;
		} else {
			BlockState blockstate = worldIn.getBlockState(pos.down());
			if (state.getBlock() != this) return super.isValidPosition(state, worldIn, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return blockstate.getBlock() == this && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos) {
		return ModBlocks.TALL_RICE_CROP.get().getDefaultState();
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)	{
		return state.get(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
		//return state.get(HALF) == DoubleBlockHalf.LOWER;
		return true;
	}

	public IFluidState getFluidState(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER
				? Fluids.WATER.getStillFluidState(false)
				: Fluids.EMPTY.getDefaultState();
	}
}
