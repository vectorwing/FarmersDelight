package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CookingPotResultSlot extends ResourceHandlerSlot
{
	public final CookingPotBlockEntity cookingPot;
	private final Player player;

	public CookingPotResultSlot(Player player, CookingPotBlockEntity blockEntity, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> modifier, int index, int xPosition, int yPosition) {
		super(handler, modifier, index, xPosition, yPosition);
		this.cookingPot = blockEntity;
		this.player = player;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		super.onTake(player, stack);
	}

	// 26.1: StackCopySlot#remove is final (extraction is handled by the resource handler), so the crafted
	// count is taken from the stack pulled out of the slot rather than accumulated via a remove() override.
	@Override
	protected void checkTakeAchievements(ItemStack stack) {
		stack.onCraftedBy(this.player, stack.getCount());

		if (!this.player.level().isClientSide()) {
			cookingPot.awardUsedRecipes(this.player, cookingPot.getDroppableInventory());
		}
	}
}
