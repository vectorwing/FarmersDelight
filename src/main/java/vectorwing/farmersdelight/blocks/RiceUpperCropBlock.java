package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;

public class RiceUpperCropBlock extends CropsBlock {
	public static final IntegerProperty RICE_AGE = BlockStateProperties.AGE_0_3;

	public RiceUpperCropBlock(Properties builder) {
		super(builder);
	}

	public IntegerProperty getAgeProperty() {
		return RICE_AGE;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected IItemProvider getSeedsItem() {
		return ModItems.RICE.get();
	}

//	@Override
//	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
//		if (rand.nextInt(3) != 0) {
//			super.tick(state, worldIn, pos, rand);
//		}
//	}
//
	@Override
	protected int getBonemealAgeIncrease(World worldIn) {
		return super.getBonemealAgeIncrease(worldIn);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(RICE_AGE);
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() == ModBlocks.RICE_CROP.get();
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.RICE_CROP.get();
	}
}
