package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
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
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.Random;

@SuppressWarnings("deprecation")
public class TomatoVineBlock extends CropBlock
{
	public static final IntegerProperty VINE_AGE = BlockStateProperties.AGE_3;
	public static final BooleanProperty ROPE = BooleanProperty.create("rope");
	private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public TomatoVineBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(getAgeProperty(), 0).setValue(ROPE, false));
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
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		if (level.getRawBrightness(pos, 0) >= 9) {
			int age = this.getAge(state);
			if (age < this.getMaxAge()) {
				float speed = getGrowthSpeed(this, level, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / speed) + 1) == 0)) {
					level.setBlock(pos, state.setValue(getAgeProperty(), age + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
				}
			}
			attemptRopeClimb(level, pos, random);
		}
	}

	public void attemptRopeClimb(ServerLevel level, BlockPos pos, Random random) {
		if (random.nextFloat() < 0.2F) {
			BlockPos posAbove = pos.above();
			if (level.getBlockState(posAbove).is(ModBlocks.ROPE.get())) {
				level.setBlockAndUpdate(posAbove, defaultBlockState().setValue(ROPE, true));
			}
		}

	}

	@Override
	public BlockState getStateForAge(int age) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), age);
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(ModBlocks.TOMATO_CROP.get()) || super.mayPlaceOn(state, level, pos);
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
		builder.add(VINE_AGE, ROPE);
	}

	@Override
	protected int getBonemealAgeIncrease(Level level) {
		return super.getBonemealAgeIncrease(level) / 2;
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		int newAge = this.getAge(state) + this.getBonemealAgeIncrease(level);
		int maxAge = this.getMaxAge();
		if (newAge > maxAge) {
			newAge = maxAge;
		}

		level.setBlockAndUpdate(pos, state.setValue(getAgeProperty(), newAge));
		attemptRopeClimb(level, pos, random);
	}
}
