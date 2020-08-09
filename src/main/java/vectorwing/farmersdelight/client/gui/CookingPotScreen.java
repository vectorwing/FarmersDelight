package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SoupItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.container.CookingPotContainer;
import vectorwing.farmersdelight.utils.Text;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends ContainerScreen<CookingPotContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(FarmersDelight.MODID, "textures/gui/cooking_pot.png");

    public CookingPotScreen(CookingPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void func_230430_a_(MatrixStack p_230450_1_, final int mouseX, final int mouseY, float partialTicks) {
        super.func_230430_a_(p_230450_1_, mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(p_230450_1_, mouseX, mouseY);
    }

    protected void renderHoveredToolTip(MatrixStack ms, int mouseX, int mouseY) {
        if (this.field_230706_i_ != null && this.field_230706_i_.player != null && this.field_230706_i_.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            if (this.hoveredSlot.slotNumber == 6) {
                List<ITextComponent> tooltip = new ArrayList<>();

                ItemStack meal = this.hoveredSlot.getStack();
                tooltip.add(meal.getItem().getName().func_230531_f_().func_240701_a_(meal.getRarity().color));

                ITextComponent container = null;
                if (meal.hasContainerItem()) {
                    container = meal.getContainerItem().getItem().getName();
                } else if (meal.getItem() instanceof SoupItem) {
                    container = Items.BOWL.getItem().getName();
                }

                if (container != null) {
                    tooltip.add(Text.getTranslation("container.cooking_pot.served_on", container).func_230531_f_().func_240701_a_(TextFormatting.GRAY));
                }

                this.func_238654_b_(ms, tooltip, mouseX, mouseY);
            } else {
                this.func_230457_a_(ms, this.hoveredSlot.getStack(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void func_230451_b_(MatrixStack ms, int mouseX, int mouseY) {
        super.func_230451_b_(ms, mouseX, mouseY);
        this.field_230712_o_.func_238421_b_(ms, this.playerInventory.getDisplayName().getString(), 8.0f, (float) (this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void func_230450_a_(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        // Render UI background
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.field_230706_i_ == null)
            return;

        this.field_230706_i_.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int x = (this.field_230708_k_ - this.xSize) / 2;
        int y = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(ms, x, y, 0, 0, this.xSize, this.ySize);

        // Render heat indicator
        if (this.container.isHeated()) {
            this.func_238474_b_(ms, x + 47, y + 55, 176, 0, 17, 15);
        }

        // Render progress arrow
        int l = this.container.getCookProgressionScaled();
        this.func_238474_b_(ms, this.guiLeft + 89, this.guiTop + 25, 176, 15, l + 1, 17);
    }
}
