package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;

/**
 * Credits to squeek502 (AppleSkin) for the implementation reference.
 */
public class HUDOverlays
{
	public static int healthIconsOffset;
	public static int foodIconsOffset;
	private static final Identifier MOD_ICONS_TEXTURE = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/fd_icons.png");
	private static final int MOD_ICONS_TEXTURE_WIDTH = 256;
	private static final int MOD_ICONS_TEXTURE_HEIGHT = 256;

	public static void register(RegisterGuiLayersEvent event) {
		event.registerBelow(
				VanillaGuiLayers.PLAYER_HEALTH,
				Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "health_offset"),
				(guiGraphics, deltaTracker) -> healthIconsOffset = Minecraft.getInstance().gui.hud.leftHeight
		);
		event.registerBelow(
				VanillaGuiLayers.FOOD_LEVEL,
				Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "food_offset"),
				(guiGraphics, deltaTracker) -> foodIconsOffset = Minecraft.getInstance().gui.hud.rightHeight
		);
		event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, ComfortOverlay.ID, new ComfortOverlay());
		event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, NourishmentOverlay.ID, new NourishmentOverlay());
	}

	public static abstract class BaseOverlay implements GuiLayer
	{
		public abstract void render(Minecraft minecraft, Player player, GuiGraphicsExtractor guiGraphics, int left, int right, int top, int guiTicks);

		@Override
		public final void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.player == null || !shouldRenderOverlay(minecraft, minecraft.player, guiGraphics, minecraft.gui.hud.getGuiTicks())) {
				return;
			}

			int top = guiGraphics.guiHeight();
			int left = guiGraphics.guiWidth() / 2 - 91;
			int right = guiGraphics.guiWidth() / 2 + 91;

			render(minecraft, minecraft.player, guiGraphics, left, right, top, minecraft.gui.hud.getGuiTicks());
		}

		public boolean shouldRenderOverlay(Minecraft minecraft, Player player, GuiGraphicsExtractor guiGraphics, int guiTicks) {
			return !minecraft.gui.hud.isHidden() && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer();
		}
	}

	public static class NourishmentOverlay extends BaseOverlay
	{
		public static final Identifier ID = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "nourishment");

		@Override
		public void render(Minecraft minecraft, Player player, GuiGraphicsExtractor guiGraphics, int left, int right, int top, int guiTicks) {
			FoodData stats = player.getFoodData();

			boolean isPlayerHealingWithSaturation =
					player.isHurt()
							&& stats.getSaturationLevel() > 0.0;

			if (player.getEffect(ModEffects.NOURISHMENT) != null) {
				drawNourishmentOverlay(stats, minecraft, guiGraphics, right, top - foodIconsOffset, isPlayerHealingWithSaturation);
			}
		}

		@Override
		public boolean shouldRenderOverlay(Minecraft minecraft, Player player, GuiGraphicsExtractor guiGraphics, int guiTicks) {
			return super.shouldRenderOverlay(minecraft, player, guiGraphics, guiTicks)
					&& Configuration.ENABLE_NOURISHMENT_HUNGER_OVERLAY.get();
		}
	}

	public static class ComfortOverlay extends BaseOverlay
	{
		public static final Identifier ID = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "comfort");

		@Override
		public void render(Minecraft minecraft, Player player, GuiGraphicsExtractor guiGraphics, int left, int right, int top, int guiTicks) {
			FoodData stats = player.getFoodData();

			boolean isPlayerEligibleForComfort = stats.getSaturationLevel() == 0.0F
					&& player.isHurt()
					&& !player.hasEffect(MobEffects.REGENERATION);

			if (player.getEffect(ModEffects.COMFORT) != null && isPlayerEligibleForComfort) {
				drawComfortOverlay(player, minecraft, guiGraphics, left, top - healthIconsOffset);
			}
		}

		@Override
		public boolean shouldRenderOverlay(Minecraft minecraft, Player player, GuiGraphicsExtractor guiGraphics, int guiTicks) {
			return super.shouldRenderOverlay(minecraft, player, guiGraphics, guiTicks)
					&& Configuration.ENABLE_COMFORT_HEALTH_OVERLAY.get();
		}
	}

	public static void drawNourishmentOverlay(FoodData foodData, Minecraft minecraft, GuiGraphicsExtractor graphics, int right, int top, boolean naturalHealing) {
		float saturation = foodData.getSaturationLevel();
		int foodLevel = foodData.getFoodLevel();
		int ticks = minecraft.gui.hud.getGuiTicks();
		Random random = new Random();
		random.setSeed(ticks * 312871L);

		for (int j = 0; j < 10; ++j) {
			int x = right - j * 8 - 9;
			int y = top;

			if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
				y = top + (random.nextInt(3) - 1);
			}

			blitIcon(graphics, x, y, 0, 0, 9, 9);

			float effectiveHungerOfBar = foodData.getFoodLevel() / 2.0F - j;
			int naturalHealingOffset = naturalHealing ? 18 : 0;

			if (effectiveHungerOfBar >= 1) {
				blitIcon(graphics, x, y, 18 + naturalHealingOffset, 0, 9, 9);
			} else if (effectiveHungerOfBar >= .5) {
				blitIcon(graphics, x, y, 9 + naturalHealingOffset, 0, 9, 9);
			}
		}
	}

	public static void drawComfortOverlay(Player player, Minecraft minecraft, GuiGraphicsExtractor graphics, int left, int top) {
		int ticks = minecraft.gui.hud.getGuiTicks();
		Random random = new Random();
		random.setSeed(ticks * 312871L);

		int health = Mth.ceil(player.getHealth());
		float absorb = Mth.ceil(player.getAbsorptionAmount());
		AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		float healthMax = attrMaxHealth == null ? player.getMaxHealth() : (float) attrMaxHealth.getValue();

		int regen = -1;
		if (player.hasEffect(MobEffects.REGENERATION)) {
			regen = ticks % 25;
		}

		int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		int comfortSheen = ticks % 50;
		int comfortHeartFrame = comfortSheen % 2;
		int[] textureWidth = {5, 9};

		int healthMaxSingleRow = Mth.ceil(Math.min(healthMax, 20) / 2.0F);
		int leftHeightOffset = (healthRows - 1) * rowHeight;

		for (int i = 0; i < healthMaxSingleRow; ++i) {
			int column = i % 10;
			int x = left + column * 8;
			int y = top + leftHeightOffset;

			if (health <= 4) {
				y += random.nextInt(2);
			}
			if (i == regen) {
				y -= 2;
			}

			if (column == comfortSheen / 2) {
				blitIcon(graphics, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
			}
			if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
				blitIcon(graphics, x + 5, y, 5, 9, 4, 9);
			}
		}
	}

	private static void blitIcon(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int width, int height) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, MOD_ICONS_TEXTURE, x, y, u, v, width, height, MOD_ICONS_TEXTURE_WIDTH, MOD_ICONS_TEXTURE_HEIGHT);
	}
}
