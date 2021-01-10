package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.Random;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class MushroomColonyBlock extends BushBlock implements IGrowable
{
	public static final int GROWING_LIGHT_LEVEL = 12;
	public static final int PLACING_LIGHT_LEVEL = 13;
	public final Supplier<Item> mushroomType;

	public static final IntegerProperty COLONY_AGE = BlockStateProperties.AGE_0_3;
	protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
			Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
	};

	public MushroomColonyBlock(Properties properties, Supplier<Item> mushroomType) {
		super(properties);
		this.mushroomType = mushroomType;
		this.setDefaultState(this.stateContainer.getBaseState().with(COLONY_AGE, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}

	public IntegerProperty getAgeProperty() {
		return COLONY_AGE;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.isOpaqueCube(worldIn, pos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.isIn(BlockTags.MUSHROOM_GROW_BLOCK)) {
			return true;
		} else {
			return worldIn.getLightSubtracted(pos, 0) < PLACING_LIGHT_LEVEL && blockstate.canSustainPlant(worldIn, blockpos, net.minecraft.util.Direction.UP, this);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		int age = state.get(COLONY_AGE);
		ItemStack heldItem = player.getHeldItem(handIn);

		if (age > 0 && heldItem.getItem().isIn(Tags.Items.SHEARS)) {
			spawnAsEntity(worldIn, pos, this.getItem(worldIn, pos, state));
			worldIn.playSound(null, pos, SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
			worldIn.setBlockState(pos, state.with(COLONY_AGE, age - 1), 2);
			if (!worldIn.isRemote) {
				heldItem.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(handIn));
			}
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(COLONY_AGE) < 3;
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return false;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		int age = state.get(COLONY_AGE);
		BlockState groundState = worldIn.getBlockState(pos.down());
		if (age < this.getMaxAge() && groundState.getBlock() == ModBlocks.RICH_SOIL.get() && worldIn.getLightSubtracted(pos.up(), 0) <= GROWING_LIGHT_LEVEL && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0)) {
			worldIn.setBlockState(pos, state.with(COLONY_AGE, age + 1), 2);
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
		}
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(this.mushroomType.get());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(COLONY_AGE);
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		int age = Math.min(3, state.get(COLONY_AGE) + 1);
		worldIn.setBlockState(pos, state.with(COLONY_AGE, age), 2);
	}
}
