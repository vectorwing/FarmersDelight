package vectorwing.farmersdelight.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShapes
{
    public static final VoxelShape TRAY_OUTER_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    public static final VoxelShape TRAY_INNER_SHAPE = Block.box(2.0D, 1.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    public static final VoxelShape TRAY_SHAPE = Shapes.join(
            TRAY_OUTER_SHAPE,
            TRAY_INNER_SHAPE,
            BooleanOp.ONLY_FIRST);

    public static final VoxelShape[] ROAST_CHICKEN_SHAPES = new VoxelShape[] {
            Block.box(4.0D, 1.0D, 10.0D, 12.0D, 8.0D, 12.0D),
            Block.box(4.0D, 1.0D, 8.0D, 12.0D, 8.0D, 12.0D),
            Block.box(4.0D, 1.0D, 6.0D, 12.0D, 8.0D, 12.0D),
            Block.box(4.0D, 1.0D, 4.0D, 12.0D, 8.0D, 12.0D)
    };
    public static final VoxelShape[] HONEY_GLAZED_HAM_SHAPES = new VoxelShape[] {
            Block.box(4.0D, 1.0D, 3.0D, 12.0D, 3.0D, 11.0D),
            Block.box(4.0D, 1.0D, 6.0D, 12.0D, 9.0D, 10.0D),
            Block.box(4.0D, 2.0D, 4.0D, 12.0D, 10.0D, 10.0D),
            Block.box(4.0D, 2.0D, 2.0D, 12.0D, 10.0D, 10.0D)
    };
    public static final VoxelShape[] SHEPHERDS_PIE_SHAPES = new VoxelShape[] {
            Block.box(2.0D, 1.0D, 8.0D, 8.0D, 8.0D, 14.0D),
            Block.box(2.0D, 1.0D, 8.0D, 14.0D, 8.0D, 14.0D),
            Shapes.join(Block.box(8.0D, 1.0D, 2.0D, 14.0D, 8.0D, 8.0D), Block.box(2.0D, 1.0D, 8.0D, 14.0D, 8.0D, 14.0D), BooleanOp.OR),
            Block.box(2.0D, 1.0D, 2.0D, 14.0D, 8.0D, 14.0D)
    };
}
