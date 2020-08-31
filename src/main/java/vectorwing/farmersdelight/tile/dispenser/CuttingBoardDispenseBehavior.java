package vectorwing.farmersdelight.tile.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.blocks.CuttingBoardBlock;
import vectorwing.farmersdelight.tile.CuttingBoardTileEntity;

/**
 * Uses the given item as a tool when facing a Cutting Board.
 */
public class CuttingBoardDispenseBehavior extends OptionalDispenseBehavior
{
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = blockstate.getBlock();
		if (block instanceof CuttingBoardBlock && world.getTileEntity(blockpos) instanceof CuttingBoardTileEntity) {
			CuttingBoardTileEntity tileEntity = (CuttingBoardTileEntity) world.getTileEntity(blockpos);
			ItemStack boardItem = tileEntity.getStoredItem().copy();
			if (!boardItem.isEmpty() && tileEntity.processItemUsingTool(stack, null)) {
				CuttingBoardBlock.spawnCuttingParticles(world, blockpos, boardItem, 5);
			}
			return stack;
		}
		return super.dispenseStack(source, stack);
	}
}
