package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.IHopper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IBasket extends IHopper
{
	VoxelShape[] COLLECTION_AREA_SHAPES = {
		VoxelShapes.or(Block.makeCuboidShape(0.0D, -16.0D, 0.0D, 16.0D, 0.0D, 16.0D), Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)),	// down
		VoxelShapes.or(Block.makeCuboidShape(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D), Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D)),	// up
		VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, -16.0D, 16.0D, 16.0D, 0.0D), Block.makeCuboidShape(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)),	// north
		VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 16.0D, 16.0D, 16.0D, 32.0D), Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D)),	// south
		VoxelShapes.or(Block.makeCuboidShape(-16.0D, 0.0D, 0.0D, 0.0D, 16.0D, 16.0D), Block.makeCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D)),	// west
		VoxelShapes.or(Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 32.0D, 16.0D, 16.0D), Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D))		// east
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