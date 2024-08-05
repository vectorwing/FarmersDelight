package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class MushroomColonyBlock extends BushBlock implements BonemealableBlock
{
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
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(getAgeProperty())];
	}

	public IntegerProperty getAgeProperty() {
		return COLONY_AGE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isSolidRender(level, pos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos floorPos = pos.below();
		BlockState floorState = level.getBlockState(floorPos);
		if (floorState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
			return true;
		} else {
			return level.getRawBrightness(pos, 0) < PLACING_LIGHT_LEVEL && floorState.canSustainPlant(level, floorPos, net.minecraft.core.Direction.UP, this);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int age = state.getValue(COLONY_AGE);
		ItemStack heldStack = player.getItemInHand(hand);

		if (age > 0 && heldStack.is(Tags.Items.SHEARS)) {
			popResource(level, pos, getCloneItemStack(level, pos, state));
			level.playSound(null, pos, SoundEvents.MOOSHROOM_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, state.setValue(COLONY_AGE, age - 1), 2);
			if (!level.isClientSide) {
				heldStack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(hand));
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int age = state.getValue(COLONY_AGE);
		BlockState groundState = level.getBlockState(pos.below());
		if (age < getMaxAge() && groundState.is(ModTags.MUSHROOM_COLONY_GROWABLE_ON) && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(4) == 0)) {
			level.setBlock(pos, state.setValue(COLONY_AGE, age + 1), 2);
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return new ItemStack(this.mushroomType.get());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COLONY_AGE);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
		return state.getValue(getAgeProperty()) < getMaxAge();
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(Level level) {
		return Mth.nextInt(level.random, 1, 2);
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int age = Math.min(getMaxAge(), state.getValue(COLONY_AGE) + getBonemealAgeIncrease(level));
		level.setBlock(pos, state.setValue(COLONY_AGE, age), 2);
	}
}
