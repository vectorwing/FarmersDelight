package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
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

import java.util.Random;

/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * https://www.curseforge.com/minecraft/mc-mods/appleskin
 */

// TODO: Fix this lmao

@OnlyIn(Dist.CLIENT)
public class NourishedHungerOverlay
{
	protected int foodIconsOffset;
	private static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation(FarmersDelight.MODID, "textures/gui/nourished.png");

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new NourishedHungerOverlay());
	}

//	@SubscribeEvent(priority = EventPriority.NORMAL)
//	public void onPreRender(RenderGameOverlayEvent.Pre event) {
//		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get())
//			return;
//		if (event.getType() != RenderGameOverlayEvent.ElementType.)
//			return;
//		if (event.isCanceled())
//			return;
//
//		foodIconsOffset = ForgeIngameGui.right_height;
//	}

//	@SubscribeEvent(priority = EventPriority.NORMAL)
//	public void onRender(RenderGameOverlayEvent.Post event) {
//		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get())
//			return;
//		if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD)
//			return;
//		if (event.isCanceled())
//			return;
//
//		Minecraft minecraft = Minecraft.getInstance();
//		Player player = minecraft.player;
//		FoodData stats = player.getFoodData();
//
//		int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;
//		int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset;
//
//		boolean isPlayerHealingWithSaturation =
//				player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
//						&& player.isHurt()
//						&& stats.getSaturationLevel() > 0.0F
//						&& stats.getFoodLevel() >= 20;
//
//		if (player.getEffect(ModEffects.NOURISHMENT.get()) != null) {
//			drawNourishedOverlay(stats, minecraft, event.getMatrixStack(), left, top, isPlayerHealingWithSaturation);
//		}
//	}

	public static void drawNourishedOverlay(FoodData stats, Minecraft mc, PoseStack matrixStack, int left, int top, boolean naturalHealing) {
		matrixStack.pushPose();
		matrixStack.translate(0, 0, 0.01);

		float saturation = stats.getSaturationLevel();
		int foodLevel = stats.getFoodLevel();
		int ticks = mc.gui.getGuiTicks();
		Random rand = new Random();
		rand.setSeed((long) (ticks * 312871));

//		mc.getTextureManager().bind(MOD_ICONS_TEXTURE);
		RenderSystem.enableBlend();

		for (int j = 0; j < 10; ++j) {
			int x = left - j * 8 - 9;
			int y = top;

			if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
				y = top + (rand.nextInt(3) - 1);
			}

			// Background texture
			mc.gui.blit(matrixStack, x, y, 0, 0, 11, 11);

			float effectiveHungerOfBar = (stats.getFoodLevel()) / 2.0F - j;
			int naturalHealingOffset = naturalHealing ? 18 : 0;

			// Gilded hunger icons
			if (effectiveHungerOfBar >= 1)
				mc.gui.blit(matrixStack, x, y, 18 + naturalHealingOffset, 0, 9, 9);
			else if (effectiveHungerOfBar >= .5)
				mc.gui.blit(matrixStack, x, y, 9 + naturalHealingOffset, 0, 9, 9);
		}

		RenderSystem.disableBlend();
		matrixStack.popPose();
//		mc.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
	}
}
