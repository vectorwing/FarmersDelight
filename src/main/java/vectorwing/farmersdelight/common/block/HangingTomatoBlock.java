package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@SuppressWarnings("deprecation")
public class HangingTomatoBlock extends TomatoBlock
{
	public HangingTomatoBlock(Properties properties) {
		super(properties, false);
		registerDefaultState(stateDefinition.any().setValue(getAgeProperty(), 0));
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		this.playerWillDestroy(level, pos, state, player);
		return placeRope(level, pos);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		super.onRemove(state, level, pos, newState, movedByPiston);
		if (Configuration.ENABLE_TOMATO_ROPE_PERMANENCE.get() && !movedByPiston && !state.is(newState.getBlock())) {
			placeRope(level, pos);
		}
	}

	public boolean placeRope(Level level, BlockPos pos) {
		Block configuredRopeBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(Configuration.DEFAULT_TOMATO_VINE_ROPE.get()));
		if (configuredRopeBlock == null) {
			configuredRopeBlock = ModBlocks.ROPE.get();
		}
		BlockState finalRopeState = configuredRopeBlock.equals(ModBlocks.ROPE.get())
				? RopeBlock.getStateWithConnections(ModBlocks.ROPE.get().defaultBlockState(), level, pos, Direction.UP)
				: configuredRopeBlock.defaultBlockState();

		return level.setBlock(pos, finalRopeState, level.isClientSide ? 11 : 3);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(VINE_AGE);
	}
}
