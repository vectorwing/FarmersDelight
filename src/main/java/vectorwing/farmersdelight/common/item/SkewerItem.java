package vectorwing.farmersdelight.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.GameplayUtils;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.Optional;

public class SkewerItem extends Item
{
	public SkewerItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack skewerStack = player.getItemInHand(hand);
		if (GameplayUtils.isPlayerNearHeatSource(player, level)) {
			Optional<RecipeHolder<CampfireCookingRecipe>> recipeHolder = RecipeUtils.getCampfireCookingRecipe(skewerStack, level);
			if (recipeHolder.isPresent()) {
				skewerStack.set(ModDataComponents.COOKING_TIME_LENGTH, 120);
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(skewerStack);
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
		stack.remove(ModDataComponents.COOKING_TIME_LENGTH);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!stack.has(ModDataComponents.COOKING_TIME_LENGTH)) {
			return super.finishUsingItem(stack, level, entity);
		}
		if (entity instanceof Player player) {
			Optional<RecipeHolder<CampfireCookingRecipe>> recipeHolder = RecipeUtils.getCampfireCookingRecipe(stack, level);
			recipeHolder.ifPresent((recipe) -> {
				ItemStack resultStack = recipe.value().assemble(new SingleRecipeInput(stack), level.registryAccess());
				if (!player.getInventory().add(resultStack)) {
					player.drop(resultStack, false);
				}
				stack.shrink(1);
				if (player instanceof ServerPlayer) {
					CriteriaTriggers.USING_ITEM.trigger((ServerPlayer) player, stack);
				}
			});
			stack.remove(ModDataComponents.COOKING_TIME_LENGTH);
		}
		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return stack.has(ModDataComponents.COOKING_TIME_LENGTH)
			? stack.getOrDefault(ModDataComponents.COOKING_TIME_LENGTH, 0)
			: super.getUseDuration(stack, entity);
	}

	public UseAnim getUseAnimation(ItemStack stack) {
		return stack.has(ModDataComponents.COOKING_TIME_LENGTH) ? UseAnim.NONE : super.getUseAnimation(stack);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		if (stack.has(ModDataComponents.COOKING_TIME_LENGTH.get())) {
			return Math.round(13.0F - (float) ClientRenderUtils.getClientPlayerHack().getUseItemRemainingTicks() * 13.0F / (float) this.getUseDuration(stack, ClientRenderUtils.getClientPlayerHack()));
		} else {
			return super.getBarWidth(stack);
		}
	}

	@Override
	public int getBarColor(ItemStack stack) {
		if (stack.has(ModDataComponents.COOKING_TIME_LENGTH.get())) {
			return 0xFF8B4F;
		} else return super.getBarColor(stack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return super.isBarVisible(stack) || stack.has(ModDataComponents.COOKING_TIME_LENGTH.get());
	}
}
