package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import vectorwing.farmersdelight.core.tag.ModTags;

/**
 * Blocks that can be heated by the block below them.
 * This checks for the HEAT_SOURCES and HEAT_CONDUCTORS tag to determine heat state.
 * If the heat source has a LIT state, it must be "true" in order to give heat.
 */
public interface HeatableBlockEntity
{
	/**
	 * Checks for heat sources below the block. If it can, it also checks for conducted heat.
	 */
	default boolean isHeated(Level world, BlockPos pos) {
		BlockState stateBelow = world.getBlockState(pos.below());

		if (ModTags.HEAT_SOURCES.contains(stateBelow.getBlock())) {
			if (stateBelow.hasProperty(BlockStateProperties.LIT))
				return stateBelow.getValue(BlockStateProperties.LIT);
			return true;
		}

		if (!this.requiresDirectHeat() && ModTags.HEAT_CONDUCTORS.contains(stateBelow.getBlock())) {
			BlockState stateFurtherBelow = world.getBlockState(pos.below(2));
			if (ModTags.HEAT_SOURCES.contains(stateFurtherBelow.getBlock())) {
				if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT))
					return stateFurtherBelow.getValue(BlockStateProperties.LIT);
				return true;
			}
		}

		return false;
	}

	/**
	 * Determines if this block can only be heated directly, excluding conductors.
	 */
	default boolean requiresDirectHeat() {
		return false;
	}
}