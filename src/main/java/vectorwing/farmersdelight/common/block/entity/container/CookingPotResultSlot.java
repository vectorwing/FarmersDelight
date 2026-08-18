package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class CookingPotResultSlot extends ResourceHandlerSlot
{
	public final CookingPotBlockEntity cookingPot;
	private final Player player;
	private int removeCount;

	public CookingPotResultSlot(Player player, CookingPotBlockEntity blockEntity, ItemStacksResourceHandler inventory, int index, int xPosition, int yPosition) {
		super(inventory, inventory::set, index, xPosition, yPosition);
		this.cookingPot = blockEntity;
		this.player = player;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public Optional<ItemStack> tryRemove(int amount, int maxAmount, Player player) {
		if (this.hasItem()) {
			this.removeCount += Math.min(amount, this.getItem().getCount());
		}
		return super.tryRemove(amount, maxAmount, player);
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		super.onTake(player, stack);
	}

	@Override
	protected void onQuickCraft(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.checkTakeAchievements(stack);
	}

	@Override
	protected void checkTakeAchievements(ItemStack stack) {
		stack.onCraftedBy(this.player, this.removeCount);

		if (!this.player.level().isClientSide()) {
			cookingPot.awardUsedRecipes(this.player, cookingPot.getDroppableInventory());
		}

		this.removeCount = 0;
	}
}
