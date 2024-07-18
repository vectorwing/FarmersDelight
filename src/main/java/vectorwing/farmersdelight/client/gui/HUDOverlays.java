package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;


/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * <a href="https://www.curseforge.com/minecraft/mc-mods/appleskin">...</a>
 */

public class HUDOverlays
{
	public static int healthIconsOffset;
	public static int foodIconsOffset;
	private static final ResourceLocation MOD_ICONS_TEXTURE = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/fd_icons.png");

	public static void register(RegisterGuiLayersEvent event) {
		event.registerBelow(
				VanillaGuiLayers.PLAYER_HEALTH,
				ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "health_offset"),
				(guiGraphics, deltaTracker) -> healthIconsOffset = Minecraft.getInstance().gui.leftHeight
		);
		event.registerBelow(
				VanillaGuiLayers.FOOD_LEVEL,
				ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "food_offset"),
				(guiGraphics, deltaTracker) -> foodIconsOffset = Minecraft.getInstance().gui.rightHeight
		);
		event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, ComfortOverlay.ID, new ComfortOverlay());
		event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, NourishmentOverlay.ID, new NourishmentOverlay());
	}

	public static abstract class BaseOverlay implements LayeredDraw.Layer
	{
		public abstract void render(Minecraft mc, Player player, GuiGraphics guiGraphics, int left, int right, int top, int guiTicks);

		@Override
		public final void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.player == null || !shouldRenderOverlay(minecraft, minecraft.player, guiGraphics, minecraft.gui.getGuiTicks()))
				return;

			int top = guiGraphics.guiHeight();
			int left = guiGraphics.guiWidth() / 2 - 91; // left of health bar
			int right = guiGraphics.guiWidth() / 2 + 91; // right of food bar

			render(minecraft, minecraft.player, guiGraphics, left, right, top, minecraft.gui.getGuiTicks());
		}

		public boolean shouldRenderOverlay(Minecraft minecraft, Player player, GuiGraphics guiGraphics, int guiTicks) {
			return !minecraft.options.hideGui && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer();
		}
	}

	public static class NourishmentOverlay extends BaseOverlay
	{
		public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "nourishment");

		@Override
		public void render(Minecraft minecraft, Player player, GuiGraphics guiGraphics, int left, int right, int top, int guiTicks) {
			FoodData stats = player.getFoodData();

			boolean isPlayerHealingWithSaturation =
					player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
							&& player.isHurt()
							&& stats.getFoodLevel() >= 18;

			if (player.getEffect(ModEffects.NOURISHMENT) != null) {
				drawNourishmentOverlay(stats, minecraft, guiGraphics, right, top - foodIconsOffset, isPlayerHealingWithSaturation);
			}
		}

		@Override
		public boolean shouldRenderOverlay(Minecraft mc, Player player, GuiGraphics guiGraphics, int guiTicks) {
			if (!super.shouldRenderOverlay(mc, player, guiGraphics, guiTicks))
				return false;

			return Configuration.NOURISHED_HUNGER_OVERLAY.get();
		}
	}

	public static class ComfortOverlay extends BaseOverlay
	{
		public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "comfort");

		@Override
		public void render(Minecraft minecraft, Player player, GuiGraphics guiGraphics, int left, int right, int top, int guiTicks) {
			FoodData stats = player.getFoodData();

			boolean isPlayerEligibleForComfort = stats.getSaturationLevel() == 0.0F
					&& player.isHurt()
					&& !player.hasEffect(MobEffects.REGENERATION);

			if (player.getEffect(ModEffects.COMFORT) != null && isPlayerEligibleForComfort) {
				drawComfortOverlay(player, minecraft, guiGraphics, left, top - healthIconsOffset);
			}
		}

		@Override
		public boolean shouldRenderOverlay(Minecraft mc, Player player, GuiGraphics guiGraphics, int guiTicks) {
			if (!super.shouldRenderOverlay(mc, player, guiGraphics, guiTicks))
				return false;

			return Configuration.COMFORT_HEALTH_OVERLAY.get();
		}
	}

	public static void drawNourishmentOverlay(FoodData foodData, Minecraft minecraft, GuiGraphics graphics, int right, int top, boolean naturalHealing) {
		float saturation = foodData.getSaturationLevel();
		int foodLevel = foodData.getFoodLevel();
		int ticks = minecraft.gui.getGuiTicks();
		Random rand = new Random();
		rand.setSeed(ticks * 312871);

		RenderSystem.enableBlend();

		for (int j = 0; j < 10; ++j) {
			int x = right - j * 8 - 9;
			int y = top;

			if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
				y = top + (rand.nextInt(3) - 1);
			}

			// Background texture
			graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 0, 9, 9);

			float effectiveHungerOfBar = (foodData.getFoodLevel()) / 2.0F - j;
			int naturalHealingOffset = naturalHealing ? 18 : 0;

			// Gilded hunger icons
			if (effectiveHungerOfBar >= 1)
				graphics.blit(MOD_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
			else if (effectiveHungerOfBar >= .5)
				graphics.blit(MOD_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
		}

		RenderSystem.disableBlend();
	}

	public static void drawComfortOverlay(Player player, Minecraft minecraft, GuiGraphics graphics, int left, int top) {
		int ticks = minecraft.gui.getGuiTicks();
		Random rand = new Random();
		rand.setSeed((long) (ticks * 312871));

		int health = Mth.ceil(player.getHealth());
		float absorb = Mth.ceil(player.getAbsorptionAmount());
		AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		float healthMax = (float) attrMaxHealth.getValue();

		int regen = -1;
		if (player.hasEffect(MobEffects.REGENERATION)) regen = ticks % 25;

		int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		int comfortSheen = ticks % 50;
		int comfortHeartFrame = comfortSheen % 2;
		int[] textureWidth = {5, 9};

		RenderSystem.enableBlend();

		int healthMaxSingleRow = Mth.ceil(Math.min(healthMax, 20) / 2.0F);
		int leftHeightOffset = ((healthRows - 1) * rowHeight); // This keeps the overlay on the bottommost row of hearts

		for (int i = 0; i < healthMaxSingleRow; ++i) {
			int column = i % 10;
			int x = left + column * 8;
			int y = top + leftHeightOffset;

			if (health <= 4) y += rand.nextInt(2);
			if (i == regen) y -= 2;

			if (column == comfortSheen / 2) {
				graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
			}
			if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
				graphics.blit(MOD_ICONS_TEXTURE, x + 5, y, 5, 9, 4, 9);
			}
		}

		RenderSystem.disableBlend();
	}
}
