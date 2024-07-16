package vectorwing.farmersdelight.integration.jei.category;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DecompositionRecipeCategory implements IRecipeCategory<DecompositionDummy>
{
	public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "decomposition");
	private static final int slotSize = 22;

	private final Component title;
	private final IDrawable background;
	private final IDrawable slotIcon;
	private final IDrawable icon;
	private final ItemStack organicCompost;
	private final ItemStack richSoil;

	public DecompositionRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.decomposition");
		ResourceLocation backgroundImage = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/jei/decomposition.png");
		background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
		organicCompost = new ItemStack(ModBlocks.ORGANIC_COMPOST.get());
		richSoil = new ItemStack(ModItems.RICH_SOIL.get());
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, richSoil);
		slotIcon = helper.createDrawable(backgroundImage, 119, 0, slotSize, slotSize);
	}

	@Override
	public RecipeType<DecompositionDummy> getRecipeType() {
		return FDRecipeTypes.DECOMPOSITION;
	}

	@Override
	public Component getTitle() {
		return this.title;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DecompositionDummy recipe, IFocusGroup focusGroup) {
		List<ItemStack> accelerators = new ArrayList<>();
		BuiltInRegistries.BLOCK.getTag(ModTags.COMPOST_ACTIVATORS).ifPresent(s -> s.forEach(f -> accelerators.add(new ItemStack(f.value()))));

		builder.addSlot(RecipeIngredientRole.INPUT, 9, 26).addItemStack(organicCompost);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 93, 26).addItemStack(richSoil);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 54).addItemStacks(accelerators);
	}

	@Override
	public void draw(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		this.slotIcon.draw(guiGraphics, 63, 53);
	}

	@Override
	public List<Component> getTooltipStrings(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ClientRenderUtils.isCursorInsideBounds(40, 38, 11, 11, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".light"));
		}
		if (ClientRenderUtils.isCursorInsideBounds(53, 38, 11, 11, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".fluid"));
		}
		if (ClientRenderUtils.isCursorInsideBounds(67, 38, 11, 11, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".accelerators"));
		}
		return Collections.emptyList();
	}

	private static MutableComponent translateKey(@Nonnull String suffix) {
		return Component.translatable(FarmersDelight.MODID + ".jei.decomposition" + suffix);
	}
}
