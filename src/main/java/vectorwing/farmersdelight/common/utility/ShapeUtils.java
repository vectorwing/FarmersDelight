package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Credits to TheGridExpert for the implementation
 */
public class ShapeUtils
{
    private static final Map<VoxelShape[], VoxelShape[][]> PLATED_SHAPE_CACHE = new IdentityHashMap<>();
    private static final Map<VoxelShape[], VoxelShape[][]> ROTATED_SHAPE_CACHE = new IdentityHashMap<>();

    /**
     * Rotates a VoxelShape around the Y-axis (vertical axis).<br>
     * <b>The result must be cached.</b>
     * <p>
     * Credits to JTK222 for the algorithm.
     */
    public static @NotNull VoxelShape rotateY(@NotNull VoxelShape shape, @NotNull RotationAmount rotation) {
        List<VoxelShape> rotatedShapes = new ArrayList<>();

        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            x1 = (x1 * 16) - 8;
            x2 = (x2 * 16) - 8;
            y1 *= 16;
            y2 *= 16;
            z1 = (z1 * 16) - 8;
            z2 = (z2 * 16) - 8;

            double nx1, nz1, nx2, nz2;

            switch (rotation) {
                case NINETY -> {
                    nx1 = 8 - z1;
                    nz1 = 8 + x1;
                    nx2 = 8 - z2;
                    nz2 = 8 + x2;
                }
                case HUNDRED_EIGHTY -> {
                    nx1 = 8 - x1;
                    nz1 = 8 - z1;
                    nx2 = 8 - x2;
                    nz2 = 8 - z2;
                }
                case TWO_HUNDRED_SEVENTY -> {
                    nx1 = 8 + z1;
                    nz1 = 8 - x1;
                    nx2 = 8 + z2;
                    nz2 = 8 - x2;
                }
                default -> throw new IllegalArgumentException("Unexpected rotation: " + rotation);
            }

            rotatedShapes.add(blockBox(nx1, y1, nz1, nx2, y2, nz2));
        });

        return mergeShapes(rotatedShapes);
    }

    public static @NotNull VoxelShape blockBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }

    /**
     * Returns an unmodifiable map of the provided north-facing shape rotated to all four horizontal directions.
     * Does not include the vertical ones.
     */
    public static Map<Direction, VoxelShape> getShapesRotatedFromNorth(VoxelShape shapeOnNorth) {
        EnumMap<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
        map.put(Direction.NORTH, shapeOnNorth);
        map.put(Direction.EAST, rotateY(shapeOnNorth, RotationAmount.NINETY));
        map.put(Direction.SOUTH, rotateY(shapeOnNorth, RotationAmount.HUNDRED_EIGHTY));
        map.put(Direction.WEST, rotateY(shapeOnNorth, RotationAmount.TWO_HUNDRED_SEVENTY));
        return Collections.unmodifiableMap(map);
    }

    /**
     * Builds a 2D array of combined plate and dish shapes for all serving stages and horizontal directions.<br>
     * The result gets cached. If multiple blocks have the same shapes, it returns the result from the cache.
     * <p>
     * The returned array is indexed as {@code [servings][direction]}, where servings are numbered the same
     * way as in the block class (0 — no servings left, 1 — one serving, etc.), and the direction index
     * corresponds to {@link Direction#get2DDataValue()}.
     * <p>
     * {@code dishShapes} should be ordered the same way as {@code SERVINGS} in the block — from the least
     * amount of servings to the maximum, not oppositely.
     *
     * @param dishShapes the north-facing shapes for each serving stage, ordered from fewest to most servings
     * @param plateShape the plate shape, shown at all stages including when no servings remain
     */
    public static VoxelShape[][] buildPlatedFoodShapes(VoxelShape[] dishShapes, VoxelShape plateShape) {
        return PLATED_SHAPE_CACHE.computeIfAbsent(dishShapes, shapes -> {
            VoxelShape[][] result = new VoxelShape[shapes.length + 1][4];

            for (int j = 0; j < 4; j++) {
                result[0][j] = plateShape;
            }

            for (int i = 0; i < shapes.length; i++) {
                Map<Direction, VoxelShape> rotatedRoast = ShapeUtils.getShapesRotatedFromNorth(shapes[i]);
                for (Map.Entry<Direction, VoxelShape> entry : rotatedRoast.entrySet()) {
                    result[i + 1][entry.getKey().get2DDataValue()] = Shapes.join(plateShape, entry.getValue(), BooleanOp.OR);
                }
            }

            return result;
        });
    }

    /**
     * See {@link #buildPlatedFoodShapes(VoxelShape[], VoxelShape)} for full details.
     * Identical in behaviour, but without a plate shape, therefore the index 0 is the
     * least-served dish shape instead of a bare plate.
     */
    public static VoxelShape[][] buildRotatedFoodShapes(VoxelShape[] dishShapes) {
        return ROTATED_SHAPE_CACHE.computeIfAbsent(dishShapes, shapes -> {
            VoxelShape[][] result = new VoxelShape[shapes.length][4];

            for (int i = 0; i < shapes.length; i++) {
                Map<Direction, VoxelShape> rotated = getShapesRotatedFromNorth(shapes[i]);
                for (Map.Entry<Direction, VoxelShape> entry : rotated.entrySet()) {
                    result[i][entry.getKey().get2DDataValue()] = entry.getValue();
                }
            }

            return result;
        });
    }

    /**
     * Merges a list of shapes together.
     */
    private static VoxelShape mergeShapes(List<VoxelShape> shapes) {
        return shapes.stream().reduce((a, b) -> Shapes.join(a, b, BooleanOp.OR)).orElse(Block.box(0, 0, 0, 16, 16, 16));
    }

    public enum RotationAmount {
        NINETY,
        HUNDRED_EIGHTY,
        TWO_HUNDRED_SEVENTY
    }
}
