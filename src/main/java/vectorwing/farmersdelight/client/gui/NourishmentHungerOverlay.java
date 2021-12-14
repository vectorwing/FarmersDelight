package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;

/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * https://www.curseforge.com/minecraft/mc-mods/appleskin
 */

@OnlyIn(Dist.CLIENT)
public class NourishmentHungerOverlay
{
	public static int foodIconsOffset;
	private static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation(FarmersDelight.MODID, "textures/gui/nourished.png");

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new NourishmentHungerOverlay());

		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FOOD_LEVEL_ELEMENT, "Farmer's Delight Nourishment", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
			Minecraft mc = Minecraft.getInstance();
			boolean isMounted = mc.player != null && mc.player.getVehicle() instanceof LivingEntity;
			if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
				renderNourishmentOverlay(gui, mStack);
			}
		});
	}

	public static void renderNourishmentOverlay(ForgeIngameGui gui, PoseStack poseStack) {
		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get()) {
			return;
		}

		foodIconsOffset = gui.right_height;
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;

		if (player == null) {
			return;
		}

		FoodData stats = player.getFoodData();
		int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset + 10;
		int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;

		boolean isPlayerHealingWithSaturation =
				player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
						&& player.isHurt()
						&& stats.getSaturationLevel() > 0.0F
						&& stats.getFoodLevel() >= 20;

		if (player.getEffect(ModEffects.NOURISHMENT.get()) != null) {
			drawNourishmentOverlay(stats, minecraft, poseStack, left, top, isPlayerHealingWithSaturation);
		}
	}

	public static void drawNourishmentOverlay(FoodData stats, Minecraft mc, PoseStack matrixStack, int left, int top, boolean naturalHealing) {
		float saturation = stats.getSaturationLevel();
		int foodLevel = stats.getFoodLevel();
		int ticks = mc.gui.getGuiTicks();
		Random rand = new Random();
		rand.setSeed((long) (ticks * 312871));

		RenderSystem.setShaderTexture(0, MOD_ICONS_TEXTURE);
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
		RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
	}
}
