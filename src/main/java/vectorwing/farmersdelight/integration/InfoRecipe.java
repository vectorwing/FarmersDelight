package vectorwing.farmersdelight.integration;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

/**
 * A generic representation of an info recipe, can be used with EMI/JEI/etc.
 */
public record InfoRecipe(List<ItemStack> stacks, MutableComponent info) {
	public InfoRecipe(ItemStack stack, MutableComponent info) {
		this(List.of(stack), info);
	}

	public static List<InfoRecipe> infoRecipes() {
		return List.of(
			new InfoRecipe(new ItemStack(ModItems.WHEAT_DOUGH.get()), TextUtils.JEI("info.dough")),
			new InfoRecipe(new ItemStack(ModItems.STRAW.get()), TextUtils.JEI("info.straw")),
			new InfoRecipe(new ItemStack(ModItems.HAM.get()), TextUtils.JEI("info.ham")),
			new InfoRecipe(new ItemStack(ModItems.SMOKED_HAM.get()), TextUtils.JEI("info.ham")),
			new InfoRecipe(new ItemStack(ModItems.FLINT_KNIFE.get()), TextUtils.JEI("info.knife")),
			new InfoRecipe(new ItemStack(ModItems.IRON_KNIFE.get()), TextUtils.JEI("info.knife")),
			new InfoRecipe(new ItemStack(ModItems.DIAMOND_KNIFE.get()), TextUtils.JEI("info.knife")),
			new InfoRecipe(new ItemStack(ModItems.NETHERITE_KNIFE.get()), TextUtils.JEI("info.knife")),
			new InfoRecipe(new ItemStack(ModItems.GOLDEN_KNIFE.get()), TextUtils.JEI("info.knife")),

			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_CABBAGES.get()), new ItemStack(ModItems.CABBAGE.get()), new ItemStack(ModItems.CABBAGE_LEAF.get())), TextUtils.JEI("info.wild_cabbages")),
			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_BEETROOTS.get()), new ItemStack(Items.BEETROOT)), TextUtils.JEI("info.wild_beetroots")),
			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_CARROTS.get()), new ItemStack(Items.CARROT)), TextUtils.JEI("info.wild_carrots")),
			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_ONIONS.get()), new ItemStack(ModItems.ONION.get())), TextUtils.JEI("info.wild_onions")),
			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_POTATOES.get()), new ItemStack(Items.POTATO)), TextUtils.JEI("info.wild_potatoes")),
			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_TOMATOES.get()), new ItemStack(ModItems.TOMATO.get())), TextUtils.JEI("info.wild_tomatoes")),
			new InfoRecipe(List.of(new ItemStack(ModItems.WILD_RICE.get()), new ItemStack(ModItems.RICE.get()), new ItemStack(ModItems.RICE_PANICLE.get())), TextUtils.JEI("info.wild_rice"))

		);
	}
}
