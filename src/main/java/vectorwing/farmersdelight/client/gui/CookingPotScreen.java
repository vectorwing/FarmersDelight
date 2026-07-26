package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends AbstractContainerScreen<CookingPotMenu>
{
	public CookingPotScreen(CookingPotMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		gui.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
		gui.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}
}
