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
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.network.PacketDistributor;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.network.payload.RichSoilBoostParticlesPayload;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

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

		tryBoostingPlantsAboveAndBelow(level, pos, random);
	}

	public static void tryBoostingPlantsAboveAndBelow(ServerLevel level, BlockPos pos, RandomSource random) {
		if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0 || random.nextFloat() > Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
			return;
		}

		BlockPos abovePos = pos.above();
		BlockState aboveState = level.getBlockState(abovePos);
		if (!aboveState.is(ModTags.Blocks.PLANTED_FROM_BELOW) && boostPlant(aboveState, abovePos, level)) {
			return;
		}

		BlockPos belowPos = pos.below();
		BlockState belowState = level.getBlockState(belowPos);
		if (belowState.is(ModTags.Blocks.PLANTED_FROM_BELOW)) {
			boostPlant(belowState, belowPos, level);
		}
	}

	public static boolean boostPlant(BlockState plantState, BlockPos plantPos, ServerLevel level) {
		if (plantState.is(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL)) {
			return false;
		}
		if (plantState.getBlock() instanceof BonemealableBlock growable) {
			if (growable.isValidBonemealTarget(level, plantPos, plantState) && CommonHooks.canCropGrow(level, plantPos, plantState, true)) {
				growable.performBonemeal(level, level.random, plantPos, plantState);
				PacketDistributor.sendToPlayersTrackingChunk(level, level.getChunkAt(plantPos).getPos(), new RichSoilBoostParticlesPayload(plantPos));
				CommonHooks.fireCropGrowPost(level, plantPos, plantState);
				return true;
			}
		}
		return false;
	}

	public boolean convertMushroomToColony(BlockState targetState, BlockPos targetPos, ServerLevel level) {
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
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate) {
		if (toolAction.equals(ItemAbilities.HOE_TILL) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
			return ModBlocks.RICH_SOIL_FARMLAND.get().defaultBlockState();
		}
		return null;
	}


	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, BlockState plantState) {
		return TriState.DEFAULT;

		// TODO: Figure out how to correctly configure Rich Soil's plant compatibility, since PlantType was removed
//		PlantType plantType = plantState.getPlantType(level, pos.relative(facing));
//		return plantType != PlantType.CROP && plantType != PlantType.NETHER && plantType != PlantType.WATER;
	}
}
