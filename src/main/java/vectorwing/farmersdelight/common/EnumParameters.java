package vectorwing.farmersdelight.common;

import net.minecraft.client.model.HumanoidModel;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import vectorwing.farmersdelight.client.renderer.SkilletItemRenderer;

public class EnumParameters
{
	// TODO: Is this proxy still needed?
//	public static final EnumProxy<RecipeBookCategories> PROXY_COOKING_SEARCH = new EnumProxy<>(
//		RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.COMPASS))
//	);
//	public static final EnumProxy<RecipeBookCategories> PROXY_COOKING_MEALS = new EnumProxy<>(
//		RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModItems.VEGETABLE_NOODLES.get()))
//	);
//	public static final EnumProxy<RecipeBookCategories> PROXY_COOKING_DRINKS = new EnumProxy<>(
//		RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModItems.APPLE_CIDER.get()))
//	);
//	public static final EnumProxy<RecipeBookCategories> PROXY_COOKING_MISC = new EnumProxy<>(
//		RecipeBookCategories.class, (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(ModItems.DUMPLINGS.get()), new ItemStack(ModItems.TOMATO_SAUCE.get()))
//	);
	public static final EnumProxy<HumanoidModel.ArmPose> PROXY_SKILLET_FLIP = new EnumProxy<>(
		HumanoidModel.ArmPose.class, false, new SkilletItemRenderer.ArmPoseTransformer()
	);
}
