package vectorwing.farmersdelight.integration.jei.decomposition;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DecompositionRecipeCategory implements IRecipeCategory<DecompositionDummy> {
    public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "decomposition");
    private static final int slotSize = 22;

    private final String title;
    private final IDrawable background;
    private final IDrawable slotIcon;
    private final IDrawable icon;
    private final ItemStack organicCompost;
    private final ItemStack richSoil;

    public DecompositionRecipeCategory(IGuiHelper helper) {
        title = I18n.get(FarmersDelight.MODID + ".jei.decomposition");
        ResourceLocation backgroundImage = new ResourceLocation(FarmersDelight.MODID, "textures/gui/jei/decomposition.png");
        background = helper.createDrawable(backgroundImage, 0, 0, 118, 80);
        organicCompost = new ItemStack(ModBlocks.ORGANIC_COMPOST.get());
        richSoil = new ItemStack(ModItems.RICH_SOIL.get());
        icon = helper.createDrawableIngredient(richSoil);
        slotIcon = helper.createDrawable(backgroundImage, 119, 0, slotSize, slotSize);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends DecompositionDummy> getRecipeClass() {
        return DecompositionDummy.class;
    }

    @Override
    public String getTitle() {
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
    public void setIngredients(DecompositionDummy decompositionRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(ImmutableList.of(Ingredient.of(organicCompost)));
        ingredients.setOutput(VanillaTypes.ITEM, richSoil);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DecompositionDummy decompositionRecipe, IIngredients iIngredients) {
        List<ItemStack> accelerators = ModTags.COMPOST_ACTIVATORS.getValues().stream().map(ItemStack::new).collect(Collectors.toList());
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        // Draw decomposing block
        itemStacks.init(0, false, 8, 25);
        itemStacks.set(0, organicCompost);

        // Draw decomposition result
        itemStacks.init(1, false, 92, 25);
        itemStacks.set(1, richSoil);

        // Draw accelerators
        itemStacks.init(2, false, 63, 53);
        itemStacks.set(2, accelerators);
    }

    @Override
    public void draw(DecompositionDummy recipe, MatrixStack ms, double mouseX, double mouseY) {
        this.slotIcon.draw(ms, 63, 53);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(DecompositionDummy recipe, double mouseX, double mouseY) {
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
    private static TranslationTextComponent translateKey(@Nonnull String suffix) {
        return new TranslationTextComponent(FarmersDelight.MODID + ".jei.decomposition" + suffix);
    }

}

