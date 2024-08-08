package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

@SuppressWarnings("deprecation")
public class TomatoVineBlock extends CropBlock
{
	public static final IntegerProperty VINE_AGE = BlockStateProperties.AGE_3;
	public static final BooleanProperty ROPELOGGED = BooleanProperty.create("ropelogged");
	private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public TomatoVineBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(getAgeProperty(), 0).setValue(ROPELOGGED, false));
	}

	protected TomatoVineBlock(Properties properties, boolean dummy) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int age = state.getValue(getAgeProperty());
		boolean isMature = age == getMaxAge();
		if (!isMature && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		} else if (isMature) {
			int quantity = 1 + level.random.nextInt(2);
			popResource(level, pos, new ItemStack(ModItems.TOMATO.get(), quantity));

			if (level.random.nextFloat() < 0.05) {
				popResource(level, pos, new ItemStack(ModItems.ROTTEN_TOMATO.get()));
			}

			level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
			level.setBlock(pos, state.setValue(getAgeProperty(), 0), 2);
			return InteractionResult.SUCCESS;
		} else {
			return super.use(state, level, pos, player, hand, hit);
		}
	}

	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		if (level.getRawBrightness(pos, 0) >= 9) {
			int age = this.getAge(state);
			if (age < this.getMaxAge()) {
				float speed = 5;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / speed) + 1) == 0)) {
					level.setBlock(pos, state.setValue(getAgeProperty(), age + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
				}
			}
			climbRopeAbove(level, pos, random);
		}
	}

	@Override
	public BlockState getStateForAge(int age) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), age);
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return VINE_AGE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return ModItems.TOMATO_SEEDS.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(VINE_AGE, ROPELOGGED);
	}


	public boolean canClimbBlock(BlockState stateAbove) {
		return Configuration.ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES.get() ? stateAbove.is(ModTags.ROPES) : stateAbove.is(ModBlocks.ROPE.get());
	}

	public void climbRopeAbove(ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos posAbove = pos.above();
		BlockState stateAbove = level.getBlockState(posAbove);
		if (canClimbBlock(stateAbove)) {
			int vineHeight;
			for (vineHeight = 1; level.getBlockState(pos.below(vineHeight)).is(this); ++vineHeight) {
			}
			if (vineHeight < 3) {
				level.setBlockAndUpdate(posAbove, ModBlocks.HANGING_TOMATO_CROP.get().defaultBlockState());
			}
		}
	}

	@Override
	protected int getBonemealAgeIncrease(Level level) {
		return super.getBonemealAgeIncrease(level) / 2;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
		if (!this.isMaxAge(state)) {
			return true;
		}

		BlockPos.MutableBlockPos mutablePos = pos.mutable();
		for (int height = 0; height < 2; height++) {
			mutablePos.move(Direction.UP);
			BlockState nextState = level.getBlockState(mutablePos);
			if (canClimbBlock(nextState)) {
				return true;
			}
			if (nextState.is(ModBlocks.HANGING_TOMATO_CROP.get())) {
				if (!isMaxAge(nextState)) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int newAge = this.getAge(state) + this.getBonemealAgeIncrease(level);
		if (newAge <= this.getMaxAge()) {
			level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), newAge));
			if (random.nextFloat() < 0.3F) {
				climbRopeAbove(level, pos, random);
			}
		} else {
			BlockState aboveState = level.getBlockState(pos.above());
			if (canClimbBlock(level.getBlockState(pos.above()))) {
				climbRopeAbove(level, pos, random);
			} else if (aboveState.is(ModBlocks.HANGING_TOMATO_CROP.get()) && isValidBonemealTarget(level, pos, aboveState, false)) {
				performBonemeal(level, random, pos.above(), aboveState);
			}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos belowPos = pos.below();
		BlockState belowState = level.getBlockState(belowPos);

		return (belowState.is(ModBlocks.TOMATO_CROP.get()) && hasGoodCropConditions(level, pos)) || super.canSurvive(state, level, pos);
	}

	public boolean hasGoodCropConditions(LevelReader level, BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (!state.canSurvive(level, currentPos)) {
			level.scheduleTick(currentPos, this, 1);
		}

		return state;
	}

	public static void destroyAndPlaceRope(Level level, BlockPos pos) {
		Block configuredRopeBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Configuration.DEFAULT_TOMATO_VINE_ROPE.get()));
		Block finalRopeBlock = configuredRopeBlock != null ? configuredRopeBlock : ModBlocks.ROPE.get();

		level.setBlockAndUpdate(pos, finalRopeBlock.defaultBlockState());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
			if (state.getValue(TomatoVineBlock.ROPELOGGED)) {
				destroyAndPlaceRope(level, pos);
			}
		}
	}
}
