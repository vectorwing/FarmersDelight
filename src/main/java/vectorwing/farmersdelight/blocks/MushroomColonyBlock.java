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
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class MushroomColonyBlock extends BushBlock implements IGrowable
{
	public static final int GROWING_LIGHT_LEVEL = 12;
	public static final int PLACING_LIGHT_LEVEL = 13;
	public final Supplier<Item> mushroomType;

	public static final IntegerProperty COLONY_AGE = BlockStateProperties.AGE_3;
	protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
	};

	public MushroomColonyBlock(Properties properties, Supplier<Item> mushroomType) {
		super(properties);
		this.mushroomType = mushroomType;
		this.registerDefaultState(this.stateDefinition.any().setValue(COLONY_AGE, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}

	public IntegerProperty getAgeProperty() {
		return COLONY_AGE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.isSolidRender(worldIn, pos);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos floorPos = pos.below();
		BlockState floorState = worldIn.getBlockState(floorPos);
		if (floorState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
			return true;
		} else {
			return worldIn.getRawBrightness(pos, 0) < PLACING_LIGHT_LEVEL && floorState.canSustainPlant(worldIn, floorPos, net.minecraft.util.Direction.UP, this);
		}
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		int age = state.getValue(COLONY_AGE);
		ItemStack heldStack = player.getItemInHand(handIn);

		if (age > 0 && heldStack.getItem().is(Tags.Items.SHEARS)) {
			popResource(worldIn, pos, this.getCloneItemStack(worldIn, pos, state));
			worldIn.playSound(null, pos, SoundEvents.MOOSHROOM_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
			worldIn.setBlock(pos, state.setValue(COLONY_AGE, age - 1), 2);
			if (!worldIn.isClientSide) {
				heldStack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(handIn));
			}
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return false;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		int age = state.getValue(COLONY_AGE);
		Block groundState = worldIn.getBlockState(pos.below()).getBlock();
		if (age < this.getMaxAge() && ModTags.MUSHROOM_COLONY_GROWABLE_ON.contains(groundState) && worldIn.getRawBrightness(pos.above(), 0) <= GROWING_LIGHT_LEVEL && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0)) {
			worldIn.setBlock(pos, state.setValue(COLONY_AGE, age + 1), 2);
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
		}
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(this.mushroomType.get());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(COLONY_AGE);
	}

	@Override
	public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		int age = Math.min(3, state.getValue(COLONY_AGE) + 1);
		worldIn.setBlock(pos, state.setValue(COLONY_AGE, age), 2);
	}
}
