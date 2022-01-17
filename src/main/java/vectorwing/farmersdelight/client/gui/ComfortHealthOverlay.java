package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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

@OnlyIn(Dist.CLIENT)
public class ComfortHealthOverlay
{
	protected int healthIconsOffset;
	private static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation(FarmersDelight.MODID, "textures/gui/fd_icons.png");

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new ComfortHealthOverlay());
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPreRender(RenderGameOverlayEvent.Pre event) {
		if (!Configuration.COMFORT_HEALTH_OVERLAY.get())
			return;
		if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH)
			return;
		if (event.isCanceled())
			return;

		healthIconsOffset = ForgeIngameGui.left_height;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent.Post event) {
		if (!Configuration.COMFORT_HEALTH_OVERLAY.get())
			return;
		if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH)
			return;
		if (event.isCanceled())
			return;

		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity player = minecraft.player;
		FoodStats stats = player.getFoodData();

		int left = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;
		int top = minecraft.getWindow().getGuiScaledHeight() - healthIconsOffset;

		boolean isPlayerEligibleForComfort = stats.getSaturationLevel() == 0.0F
				&& player.isHurt()
				&& !player.hasEffect(Effects.REGENERATION);

		if (player.getEffect(ModEffects.COMFORT.get()) != null && isPlayerEligibleForComfort) {
			drawComfortOverlay(player, minecraft, event.getMatrixStack(), left, top);
		}
	}

	public static void drawComfortOverlay(PlayerEntity player, Minecraft minecraft, MatrixStack matrixStack, int left, int top) {
		int ticks = minecraft.gui.getGuiTicks();
		Random rand = new Random();
		rand.setSeed((long) (ticks * 312871));

		int health = MathHelper.ceil(player.getHealth());
		float absorb = MathHelper.ceil(player.getAbsorptionAmount());
		ModifiableAttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		float healthMax = (float) attrMaxHealth.getValue();

		int regen = -1;
		if (player.hasEffect(Effects.REGENERATION)) regen = ticks % 25;

		int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		int comfortSheen = ticks % 50;
		int comfortHeartFrame = comfortSheen % 2;
		int[] textureWidth = {5, 9};

		minecraft.getTextureManager().bind(MOD_ICONS_TEXTURE);
		RenderSystem.enableBlend();

		int totalHealth = MathHelper.ceil((healthMax + absorb) / 2.0F);
		for (int i = totalHealth - 1; i >= 0; --i) {
			int column = i % 10;
			int row = MathHelper.ceil((float) (i + 1) / 10.0F) - 1;
			int x = left + column * 8;
			int y = top - row * rowHeight;

			if (health <= 4) y += rand.nextInt(2);
			if (i == regen) y -= 2;


			if (column == comfortSheen / 2) {
				minecraft.gui.blit(matrixStack, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
			}
			if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
				minecraft.gui.blit(matrixStack, x + 5, y, 5, 9, 4, 9);
			}
		}

		RenderSystem.disableBlend();
		minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
	}
}
