package vectorwing.farmersdelight.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.IHopper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IBasket extends IHopper {
	VoxelShape[] COLLECTION_AREA_SHAPES = {
			Block.makeCuboidShape(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // down
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D),        // up
			Block.makeCuboidShape(0.0D, 0.0D, -16.0D, 16.0D, 16.0D, 16.0D),    // north
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 32.0D),        // south
			Block.makeCuboidShape(-16.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // west
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 32.0D, 16.0D, 16.0D)        // east
	};

	default VoxelShape getFacingCollectionArea(int facingIndex) {
		return COLLECTION_AREA_SHAPES[facingIndex];
	}

	/**
	 * Returns the worldObj for this tileEntity.
	 */
	@Nullable
	World getWorld();

	/**
	 * Gets the world X position for this hopper entity.
	 */
	double getXPos();

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	double getYPos();

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	double getZPos();
}