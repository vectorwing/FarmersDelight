package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RichSoilBlock extends Block
{
	public RichSoilBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		BlockPos abovePos = pos.above();
		BlockState aboveState = level.getBlockState(abovePos);

		if (convertMushroomToColony(aboveState, abovePos, level)) {
			return;
		}

		if (Configuration.RICH_SOIL_BOOST_CHANCE.get() > 0.0 && random.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
			if (aboveState.is(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL)) {
				return;
			}
			if (aboveState.getBlock() instanceof BonemealableBlock growable) {
				if (growable.isValidBonemealTarget(level, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(level, pos.above(), aboveState, true)) {
					growable.performBonemeal(level, level.random, pos.above(), aboveState);
					level.levelEvent(2005, pos.above(), 0);
					ForgeHooks.onCropsGrowPost(level, pos.above(), aboveState);
				}
			}
		}
	}

	public boolean convertMushroomToColony(BlockState targetState, BlockPos targetPos, ServerLevel level) {
		// TODO: Make this dynamic in some fashion. Is it worth doing it on 1.20.1?
		if (targetState.is(Blocks.BROWN_MUSHROOM)) {
			level.setBlockAndUpdate(targetPos, ModBlocks.BROWN_MUSHROOM_COLONY.get().defaultBlockState());
			return true;
		}
		if (targetState.is(Blocks.RED_MUSHROOM)) {
			level.setBlockAndUpdate(targetPos, ModBlocks.RED_MUSHROOM_COLONY.get().defaultBlockState());
			return true;
		}

		return false;
	}

	@Override
	@Nullable
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
		if (toolAction.equals(ToolActions.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
			return ModBlocks.RICH_SOIL_FARMLAND.get().defaultBlockState();
		}
		return null;
	}


	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
		return plantType != PlantType.CROP && plantType != PlantType.NETHER && plantType != PlantType.WATER;
	}
}
