package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
import java.util.Set;

public class ShapeUtils {
    /**
     * Rotate voxel. Credits to JTK222|Lukas
     * Cache the result
     */
    public static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        if (direction == Direction.NORTH) {
            return shape;
        }

        Set<VoxelShape> rotatedShapes = new HashSet<>();

        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            x1 = (x1 * 16) - 8;
            x2 = (x2 * 16) - 8;
            z1 = (z1 * 16) - 8;
            z2 = (z2 * 16) - 8;

            if (direction == Direction.EAST) {
                rotatedShapes.add(blockBox(8 - z1, y1 * 16, 8 + x1, 8 - z2, y2 * 16, 8 + x2));
            } else if (direction == Direction.SOUTH) {
                rotatedShapes.add(blockBox(8 - x1, y1 * 16, 8 - z1, 8 - x2, y2 * 16, 8 - z2));
            } else if (direction == Direction.WEST) {
                rotatedShapes.add(blockBox(8 + z1, y1 * 16, 8 - x1, 8 + z2, y2 * 16, 8 - x2));
            }
        });

        return rotatedShapes.stream().reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElseGet(() -> Block.box(0, 0, 0, 16, 16, 16));
    }

    public static VoxelShape blockBox(double pX1, double pY1, double pZ1, double pX2, double pY2, double pZ2) {
        return Block.box(Math.min(pX1, pX2), Math.min(pY1, pY2), Math.min(pZ1, pZ2), Math.max(pX1, pX2), Math.max(pY1, pY2), Math.max(pZ1, pZ2));
    }
}
