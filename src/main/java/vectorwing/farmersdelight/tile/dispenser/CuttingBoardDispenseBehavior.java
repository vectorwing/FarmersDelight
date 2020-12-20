package vectorwing.farmersdelight.tile.dispenser;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.blocks.CuttingBoardBlock;
import vectorwing.farmersdelight.tile.CuttingBoardTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

/**
 * Uses the given item as a tool when facing a Cutting Board.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CuttingBoardDispenseBehavior extends OptionalDispenseBehavior
{
	private static final DispenserLookup BEHAVIOUR_LOOKUP = new DispenserLookup();
	private static final HashMap<Item, IDispenseItemBehavior> DISPENSE_ITEM_BEHAVIOR_HASH_MAP = new HashMap<>();

	public static void registerBehaviour(Item item, CuttingBoardDispenseBehavior behavior) {
		DISPENSE_ITEM_BEHAVIOR_HASH_MAP.put(item, BEHAVIOUR_LOOKUP.getBehavior(new ItemStack(item))); // Save the old behaviours so they can be used later
		DispenserBlock.registerDispenseBehavior(item, behavior);
	}

	@Override
	public final ItemStack dispense(IBlockSource source, ItemStack stack) {
		if (tryDispenseStackOnCuttingBoard(source, stack)) {
			this.playDispenseSound(source); // I added this because i completely overrode the super implementation which had the sounds.
			this.spawnDispenseParticles(source, source.getBlockState().get(DispenserBlock.FACING)); // see above, same reasoning
			return stack;
		}
		return DISPENSE_ITEM_BEHAVIOR_HASH_MAP.get(stack.getItem()).dispense(source, stack); // Not targetted on cutting board, use vanilla/other mods behaviour
	}

	public boolean tryDispenseStackOnCuttingBoard(IBlockSource source, ItemStack stack) {
		setSuccessful(false);
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = blockstate.getBlock();
		TileEntity te = world.getTileEntity(blockpos);
		if (block instanceof CuttingBoardBlock && te instanceof CuttingBoardTileEntity) {
			CuttingBoardTileEntity tileEntity = (CuttingBoardTileEntity) te;
			ItemStack boardItem = tileEntity.getStoredItem().copy();
			if (!boardItem.isEmpty() && tileEntity.processItemUsingTool(stack, null)) {
				CuttingBoardBlock.spawnCuttingParticles(world, blockpos, boardItem, 5);
				setSuccessful(true);
			}
			return true;
		}
		return false;
	}

	@ParametersAreNonnullByDefault
	@MethodsReturnNonnullByDefault
	private static class DispenserLookup extends DispenserBlock
	{
		protected DispenserLookup() {
			super(Block.Properties.from(Blocks.DISPENSER));
		}

		public IDispenseItemBehavior getBehavior(ItemStack itemStack) {
			return super.getBehavior(itemStack);
		}
	}
}
