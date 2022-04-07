package vectorwing.farmersdelight.integration.jei.category;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DecompositionRecipeCategory implements IRecipeCategory<DecompositionDummy>
{
	public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "decomposition");
	private static final int slotSize = 22;

	private final Component title;
	private final IDrawable background;
	private final IDrawable slotIcon;
	private final IDrawable icon;
	private final ItemStack organicCompost;
	private final ItemStack richSoil;

	public DecompositionRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.decomposition");
		ResourceLocation backgroundImage = new ResourceLocation(FarmersDelight.MODID, "textures/gui/jei/decomposition.png");
		background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
		organicCompost = new ItemStack(ModBlocks.ORGANIC_COMPOST.get());
		richSoil = new ItemStack(ModItems.RICH_SOIL.get());
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM, richSoil);
		slotIcon = helper.createDrawable(backgroundImage, 119, 0, slotSize, slotSize);
	}

	@Override
	public ResourceLocation getUid() {
		return this.getRecipeType().getUid();
	}

	@Override
	public Class<? extends DecompositionDummy> getRecipeClass() {
		return this.getRecipeType().getRecipeClass();
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
	public void setRecipe(IRecipeLayoutBuilder recipeLayout, DecompositionDummy decompositionRecipe, IFocusGroup iIngredients) {
		List<ItemStack> accelerators = ForgeRegistries.BLOCKS.tags().getTag(ModTags.COMPOST_ACTIVATORS).stream().map(ItemStack::new).collect(Collectors.toList());

		recipeLayout.addSlot(RecipeIngredientRole.INPUT, 9, 26).addItemStack(organicCompost);
		recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, 93, 26).addItemStack(richSoil);
		recipeLayout.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 54).addItemStacks(accelerators);
	}

	@Override
	public void draw(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, PoseStack ms, double mouseX, double mouseY) {
		this.slotIcon.draw(ms, 63, 53);
	}

	@Override
	public List<Component> getTooltipStrings(DecompositionDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (inIconAt(40, 38, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".light"));
		}
		if (inIconAt(53, 38, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".fluid"));
		}
		if (inIconAt(67, 38, mouseX, mouseY)) {
			return ImmutableList.of(translateKey(".accelerators"));
		}
		return Collections.emptyList();
	}

	private static boolean inIconAt(int iconX, int iconY, double mouseX, double mouseY) {
		final int icon_size = 11;
		return iconX <= mouseX && mouseX < iconX + icon_size && iconY <= mouseY && mouseY < iconY + icon_size;
	}

	private static TranslatableComponent translateKey(@Nonnull String suffix) {
		return new TranslatableComponent(FarmersDelight.MODID + ".jei.decomposition" + suffix);
	}
}

