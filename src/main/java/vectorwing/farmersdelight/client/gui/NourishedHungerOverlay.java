package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModEffects;
import vectorwing.farmersdelight.setup.Configuration;

/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * https://www.curseforge.com/minecraft/mc-mods/appleskin
 */

@OnlyIn(Dist.CLIENT)
public class NourishedHungerOverlay
{
	protected int foodIconsOffset;
	private static final ResourceLocation modIcons = new ResourceLocation(FarmersDelight.MODID, "textures/gui/nourished.png");

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new NourishedHungerOverlay());
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPreRender(RenderGameOverlayEvent.Pre event) {
		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get())
			return;
		if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD)
			return;
		if (event.isCanceled())
			return;

		foodIconsOffset = ForgeIngameGui.right_height;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent.Post event) {
		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get())
			return;
		if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD)
			return;
		if (event.isCanceled())
			return;

		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		FoodStats stats = player.getFoodStats();

		int left = mc.getMainWindow().getScaledWidth() / 2 + 91;
		int top = mc.getMainWindow().getScaledHeight() - foodIconsOffset;

		boolean isPlayerHealingWithSaturation =
				player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)
						&& player.shouldHeal()
						&& stats.getSaturationLevel() > 0.0F
						&& stats.getFoodLevel() >= 20;

		if (player.getActivePotionEffect(ModEffects.NOURISHED.get()) != null && player.getActivePotionEffect(Effects.HUNGER) == null) {
			drawNourishedOverlay(stats.getFoodLevel(), mc, event.getMatrixStack(), left, top, isPlayerHealingWithSaturation);
		}
	}

	public static void drawNourishedOverlay(int foodLevel, Minecraft mc, MatrixStack matrixStack, int left, int top, boolean naturalHealing) {
		mc.getTextureManager().bindTexture(modIcons);

		for (int j = 0; j < 10; ++j) {
			int x = left - j * 8 - 9;
			int y = top;

			// Background texture
			mc.ingameGUI.blit(matrixStack, x, y, 0, 0, 11, 11);

			float effectiveHungerOfBar = (foodLevel) / 2.0F - j;
			int naturalHealingOffset = naturalHealing ? 18 : 0;

			// Gilded hunger icons
			if (effectiveHungerOfBar >= 1)
				mc.ingameGUI.blit(matrixStack, x, y, 18 + naturalHealingOffset, 0, 9, 9);
			else if (effectiveHungerOfBar >= .5)
				mc.ingameGUI.blit(matrixStack, x, y, 9 + naturalHealingOffset, 0, 9, 9);
		}

		mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
	}
}
