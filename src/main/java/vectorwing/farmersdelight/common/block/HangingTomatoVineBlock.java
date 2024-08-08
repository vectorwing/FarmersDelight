package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import javax.annotation.Nullable;

public class HangingTomatoVineBlock extends TomatoVineBlock
{
	public HangingTomatoVineBlock(Properties properties) {
		super(properties, false);
		registerDefaultState(stateDefinition.any().setValue(getAgeProperty(), 0));
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos belowPos = pos.below();
		BlockState belowState = level.getBlockState(belowPos);

		if (belowState.is(ModBlocks.TOMATO_CROP.get()) || belowState.is(ModBlocks.HANGING_TOMATO_CROP.get())) {
			return hasGoodCropConditions(level, pos);
		}

		return super.canSurvive(state, level, pos);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, blockEntity, stack);
		destroyAndPlaceRope(level, pos);
	}

//	@Override
//	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
//		if (state.getBlock() != newState.getBlock()) {
//			level.destroyBlock(pos, true);
////			destroyAndPlaceRope(level, pos);
//		}
//	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
			destroyAndPlaceRope(level, pos);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(VINE_AGE);
	}

	public static void destroyAndPlaceRope(Level level, BlockPos pos) {
		Block configuredRopeBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Configuration.DEFAULT_TOMATO_VINE_ROPE.get()));
		Block finalRopeBlock = configuredRopeBlock != null ? configuredRopeBlock : ModBlocks.ROPE.get();

		level.setBlockAndUpdate(pos, finalRopeBlock.defaultBlockState());
	}
}
