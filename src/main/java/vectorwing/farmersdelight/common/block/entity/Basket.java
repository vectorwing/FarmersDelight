package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface Basket extends Container
{
	VoxelShape[] COLLECTION_AREA_SHAPES = {
			Block.box(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // down
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D),        // up
			Block.box(0.0D, 0.0D, -16.0D, 16.0D, 16.0D, 16.0D),    // north
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 32.0D),        // south
			Block.box(-16.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // west
			Block.box(0.0D, 0.0D, 0.0D, 32.0D, 16.0D, 16.0D)        // east
	};

	default VoxelShape getFacingCollectionArea(int facingIndex) {
		return COLLECTION_AREA_SHAPES[facingIndex];
	}

	/**
	 * Gets the world X position for this hopper entity.
	 */
	double getLevelX();

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	double getLevelY();

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	double getLevelZ();
}