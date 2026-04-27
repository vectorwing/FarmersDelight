package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.utility.ShapeUtils;

import java.util.function.Supplier;

public class RotatedFeastBlock extends FeastBlock
{
	private final VoxelShape[][] combinedShapes;

	/**
	 * An extension of {@link FeastBlock} with block and plate collision shape rotation.
	 *
	 * @param properties		block properties
	 * @param servingItem		the meal to be served
	 * @param hasLeftovers		whether the block remains when out of servings. If false, the block vanishes once it runs out
	 * @param feastShapes		an array of the feast's shapes per stage. This does not include the container shape. The index must match the {@code SERVINGS} index, ordered the same way.
	 * @param containerShape	the container shape. This is optional, set it to {@code null} if the block has no plate.
	 */
	public RotatedFeastBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers, VoxelShape[] feastShapes, @Nullable VoxelShape containerShape) {
		super(properties, servingItem, hasLeftovers, true);
		this.combinedShapes = containerShape == null ? ShapeUtils.buildRotatedFoodShapes(feastShapes) : ShapeUtils.buildPlatedFoodShapes(feastShapes, containerShape);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return combinedShapes[state.getValue(SERVINGS)][state.getValue(FACING).get2DDataValue()];
	}
}
