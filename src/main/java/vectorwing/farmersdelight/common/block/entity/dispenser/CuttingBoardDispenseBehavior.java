package vectorwing.farmersdelight.common.block.entity.dispenser;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

/**
 * Uses the given item as a tool when facing a Cutting Board.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CuttingBoardDispenseBehavior extends OptionalDispenseItemBehavior
{
	private static final DispenserLookup BEHAVIOUR_LOOKUP = new DispenserLookup();
	private static final HashMap<Item, DispenseItemBehavior> DISPENSE_ITEM_BEHAVIOR_HASH_MAP = new HashMap<>();

	public static void registerBehaviour(Item item, CuttingBoardDispenseBehavior behavior) {
		DISPENSE_ITEM_BEHAVIOR_HASH_MAP.put(item, BEHAVIOUR_LOOKUP.getDispenseMethod(new ItemStack(item))); // Save the old behaviours so they can be used later
		DispenserBlock.registerBehavior(item, behavior);
	}

	@Override
	public final ItemStack dispense(BlockSource source, ItemStack stack) {
		if (tryDispenseStackOnCuttingBoard(source, stack)) {
			this.playSound(source); // I added this because i completely overrode the super implementation which had the sounds.
			this.playAnimation(source, source.getBlockState().getValue(DispenserBlock.FACING)); // see above, same reasoning
			return stack;
		}
		return DISPENSE_ITEM_BEHAVIOR_HASH_MAP.get(stack.getItem()).dispense(source, stack); // Not targetted on cutting board, use vanilla/other mods behaviour
	}

	public boolean tryDispenseStackOnCuttingBoard(BlockSource source, ItemStack stack) {
		setSuccess(false);
		Level world = source.getLevel();
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = blockstate.getBlock();
		BlockEntity te = world.getBlockEntity(blockpos);
		if (block instanceof CuttingBoardBlock && te instanceof CuttingBoardBlockEntity) {
			CuttingBoardBlockEntity tileEntity = (CuttingBoardBlockEntity) te;
			ItemStack boardItem = tileEntity.getStoredItem().copy();
			if (!boardItem.isEmpty() && tileEntity.processStoredItemUsingTool(stack, null)) {
				CuttingBoardBlock.spawnCuttingParticles(world, blockpos, boardItem, 5);
				setSuccess(true);
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
			super(Block.Properties.copy(Blocks.DISPENSER));
		}

		@Override
		public DispenseItemBehavior getDispenseMethod(ItemStack itemStack) {
			return super.getDispenseMethod(itemStack);
		}
	}
}
