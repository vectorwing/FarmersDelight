package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.Random;

@SuppressWarnings("deprecation")
public class TomatoBlock extends BushBlock implements BonemealableBlock
{
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	private static final int TOMATO_BEARING_AGE = 7;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	public TomatoBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}

	protected static float getGrowthChance(Block blockIn, BlockGetter worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos floorPos = pos.below();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				BlockState blockstate = worldIn.getBlockState(floorPos.offset(i, 0, j));
				if (blockstate.canSustainPlant(worldIn, floorPos.offset(i, 0, j), net.minecraft.core.Direction.UP, (net.minecraftforge.common.IPlantable) blockIn)) {
					f1 = 1.0F;
					if (blockstate.isFertile(worldIn, floorPos.offset(i, 0, j))) {
						f1 = 3.0F;
					}
				}

				if (i != 0 || j != 0) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos northPos = pos.north();
		BlockPos southPos = pos.south();
		BlockPos westPos = pos.west();
		BlockPos eastPos = pos.east();
		boolean isMatchedWestEast = blockIn == worldIn.getBlockState(westPos).getBlock() || blockIn == worldIn.getBlockState(eastPos).getBlock();
		boolean isMatchedNorthSouth = blockIn == worldIn.getBlockState(northPos).getBlock() || blockIn == worldIn.getBlockState(southPos).getBlock();
		if (isMatchedWestEast && isMatchedNorthSouth) {
			f /= 2.0F;
		} else {
			boolean flag2 = blockIn == worldIn.getBlockState(westPos.north()).getBlock() || blockIn == worldIn.getBlockState(eastPos.north()).getBlock() || blockIn == worldIn.getBlockState(eastPos.south()).getBlock() || blockIn == worldIn.getBlockState(westPos.south()).getBlock();
			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	public int getMaxAge() {
		return 7;
	}

	protected int getAge(BlockState state) {
		return state.getValue(AGE);
	}

	public BlockState withAge(int age) {
		return this.defaultBlockState().setValue(AGE, age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.getValue(AGE) >= this.getMaxAge();
	}

	protected int getBonemealAgeIncrease(Level worldIn) {
		return Mth.nextInt(worldIn.random, 2, 5);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.TOMATO_SEEDS.get());
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1))
			return;
		if (worldIn.getRawBrightness(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (i < this.getMaxAge()) {
				float f = getGrowthChance(this, worldIn, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0)) {
					worldIn.setBlock(pos, this.withAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}
	}

	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos) {
		return PlantType.CROP;
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	@Override
	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
		int newAge = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
		int maxAge = this.getMaxAge();
		if (newAge > maxAge) {
			newAge = maxAge;
		}

		worldIn.setBlock(pos, this.withAge(newAge), 2);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		int age = state.getValue(AGE);
		boolean isMature = age == TOMATO_BEARING_AGE;
		if (!isMature && player.getItemInHand(handIn).getItem() == Items.BONE_MEAL) {
			return InteractionResult.PASS;
		} else if (isMature) {
			int j = 1 + worldIn.random.nextInt(2);
			popResource(worldIn, pos, new ItemStack(ModItems.TOMATO.get(), j));
			worldIn.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
			worldIn.setBlock(pos, state.setValue(AGE, TOMATO_BEARING_AGE - 2), 2);
			return InteractionResult.SUCCESS;
		} else {
			return super.use(state, worldIn, pos, player, handIn, hit);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return (worldIn.getRawBrightness(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && super.canSurvive(state, worldIn, pos);
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof Ravager && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entityIn)) {
			worldIn.destroyBlock(pos, true, entityIn);
		}

		super.entityInside(state, worldIn, pos, entityIn);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
