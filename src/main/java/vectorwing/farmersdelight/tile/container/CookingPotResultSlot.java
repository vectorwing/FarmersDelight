package vectorwing.farmersdelight.tile.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import vectorwing.farmersdelight.tile.CookingPotTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CookingPotResultSlot extends SlotItemHandler
{
	public final CookingPotTileEntity tileEntity;
	private final PlayerEntity player;
	private int removeCount;

	public CookingPotResultSlot(PlayerEntity player, CookingPotTileEntity tile, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.tileEntity = tile;
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	@Nonnull
	public ItemStack decrStackSize(int amount) {
		if (this.getHasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		super.onTake(thePlayer, stack);
		return stack;
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.removeCount);

		if (!this.player.world.isRemote) {
			tileEntity.clearUsedRecipes(this.player);
		}

		this.removeCount = 0;
	}
}
