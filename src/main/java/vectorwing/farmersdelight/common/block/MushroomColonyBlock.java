package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.tag.ModTags;

@SuppressWarnings("deprecation")
public class MushroomColonyBlock extends BushBlock implements BonemealableBlock
{
	public static final MapCodec<MushroomColonyBlock> CODEC = RecordCodecBuilder.mapCodec(
			builder -> builder.group(BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("mushroom").forGetter(block -> block.mushroomType), propertiesCodec())
					.apply(builder, MushroomColonyBlock::new)
	);

	public static final int PLACING_LIGHT_LEVEL = 13;
	public final Holder<Item> mushroomType;

	public static final IntegerProperty COLONY_AGE = BlockStateProperties.AGE_3;
	protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
	};

	public MushroomColonyBlock(Holder<Item> mushroomType, Properties properties) {
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
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
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
			return level.getRawBrightness(pos, 0) < PLACING_LIGHT_LEVEL && floorState.canSustainPlant(level, floorPos, net.minecraft.core.Direction.UP, state).isDefault();
		}
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int age = state.getValue(COLONY_AGE);

		if (age > 0 && heldStack.is(Tags.Items.TOOLS_SHEAR)) {
			popResource(level, pos, getCloneItemStack(level, pos, state));
			level.playSound(null, pos, SoundEvents.MOOSHROOM_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, state.setValue(COLONY_AGE, age - 1), 2);
			if (!level.isClientSide) {
				heldStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			}
			return ItemInteractionResult.SUCCESS;
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int age = state.getValue(COLONY_AGE);
		BlockState groundState = level.getBlockState(pos.below());
		if (age < getMaxAge() && groundState.is(ModTags.MUSHROOM_COLONY_GROWABLE_ON) && CommonHooks.canCropGrow(level, pos, state, random.nextInt(4) == 0)) {
			level.setBlock(pos, state.setValue(COLONY_AGE, age + 1), 2);
			CommonHooks.fireCropGrowPost(level, pos, state);
		}
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return new ItemStack(this.mushroomType.value());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COLONY_AGE);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
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
