package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SafetyNetBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 9.0D, 16.0D);

	public SafetyNetBlock()
	{
		super(Block.Properties.create(Material.CARPET).hardnessAndResistance(0.2F).sound(SoundType.CLOTH));
	}

	public SafetyNetBlock(net.minecraft.block.Block.Properties properties) {
		super(properties);
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.isSuppressingBounce()) {
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		} else {
			entityIn.onLivingFall(fallDistance, 0.0F);
		}

	}

	public void onLanded(IBlockReader worldIn, Entity entityIn) {
		if (entityIn.isSuppressingBounce()) {
			super.onLanded(worldIn, entityIn);
		} else {
			this.bounceEntity(entityIn);
		}
	}

	private void bounceEntity(Entity entityIn) {
		Vec3d vec3d = entityIn.getMotion();
		if (vec3d.y < 0.0D) {
			double d0 = entityIn instanceof LivingEntity ? 0.6D : 0.8D;
			entityIn.setMotion(vec3d.x, -vec3d.y * d0, vec3d.z);
		}
	}
}
