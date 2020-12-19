package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vectorwing.farmersdelight.registry.ModItems;

import java.util.Random;

@SuppressWarnings("deprecation")
public class TomatoesBlock extends BushBlock implements IGrowable
{
	private static final int TOMATO_BEARING_AGE = 7;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	public TomatoesBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
	}

	public int getMaxAge() { return 7; }

	protected int getAge(BlockState state) { return state.get(AGE); }

	public BlockState withAge(int age) {
		return this.getDefaultState().with(AGE, age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.get(AGE) >= this.getMaxAge();
	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.nextInt(worldIn.rand, 2, 5);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(AGE)];
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.TOMATO_SEEDS.get());
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1))
			return;
		if (worldIn.getLightSubtracted(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (i < this.getMaxAge()) {
				float f = getGrowthChance(this, worldIn, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0)) {
					worldIn.setBlockState(pos, this.withAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		int newAge = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
		int maxAge = this.getMaxAge();
		if (newAge > maxAge) {
			newAge = maxAge;
		}

		worldIn.setBlockState(pos, this.withAge(newAge), 2);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		int i = state.get(AGE);
		boolean flag = i == TOMATO_BEARING_AGE;
		if (!flag && player.getHeldItem(handIn).getItem() == Items.BONE_MEAL) {
			return ActionResultType.PASS;
		} else if (flag) {
			int j = 1 + worldIn.rand.nextInt(2);
			spawnAsEntity(worldIn, pos, new ItemStack(ModItems.TOMATO.get(), j));
			worldIn.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
			worldIn.setBlockState(pos, state.with(AGE, TOMATO_BEARING_AGE - 2), 2);
			return ActionResultType.SUCCESS;
		} else {
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && super.isValidPosition(state, worldIn, pos);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof RavagerEntity && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entityIn)) {
			worldIn.destroyBlock(pos, true, entityIn);
		}

		super.onEntityCollision(state, worldIn, pos, entityIn);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	protected static float getGrowthChance(Block blockIn, IBlockReader worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos floorPos = pos.down();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				BlockState blockstate = worldIn.getBlockState(floorPos.add(i, 0, j));
				if (blockstate.canSustainPlant(worldIn, floorPos.add(i, 0, j), net.minecraft.util.Direction.UP, (net.minecraftforge.common.IPlantable) blockIn)) {
					f1 = 1.0F;
					if (blockstate.isFertile(worldIn, floorPos.add(i, 0, j))) {
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
		}
		else {
			boolean flag2 = blockIn == worldIn.getBlockState(westPos.north()).getBlock() || blockIn == worldIn.getBlockState(eastPos.north()).getBlock() || blockIn == worldIn.getBlockState(eastPos.south()).getBlock() || blockIn == worldIn.getBlockState(westPos.south()).getBlock();
			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}
}
