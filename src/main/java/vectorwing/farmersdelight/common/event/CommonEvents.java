package vectorwing.farmersdelight.common.event;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.network.payload.NaturalRegenerationGameRulePayload;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;
import vectorwing.farmersdelight.integration.jei.FDRecipes;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEvents
{
	private static boolean NATURAL_REGENERATION = true;

	@SubscribeEvent
	public static void handleVanillaSoupEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntity();

		if (Configuration.ENABLE_RABBIT_STEW_BUFF.get() && food.equals(Items.RABBIT_STEW)) {
			return;
		}

		if (Configuration.ENABLE_VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			ApplyStatusEffectsConsumeEffect soupEffects = FoodValues.ConsumableValues.VANILLA_SOUP_EFFECTS.get(food);

			if (soupEffects != null) {
				for (MobEffectInstance effect : soupEffects.effects()) {
					entity.addEffect(effect);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		sendPayload((ServerPlayer) event.getEntity(), new NaturalRegenerationGameRulePayload(NATURAL_REGENERATION));
	}

	@SubscribeEvent
	public void onServerWorldTick(ServerTickEvent.Post event) {
		boolean currentNaturalRegen = event.getServer().getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION);
		if (NATURAL_REGENERATION != currentNaturalRegen) {
			NATURAL_REGENERATION = currentNaturalRegen;

			NaturalRegenerationGameRulePayload payload = new NaturalRegenerationGameRulePayload(NATURAL_REGENERATION);
			for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
				sendPayload(player, payload);
			}
		}
	}

	@SubscribeEvent
	public void sendSyncedRecipes(OnDatapackSyncEvent event) {
		event.sendRecipes(ModRecipeTypes.COOKING.get(), ModRecipeTypes.CUTTING.get());
	}

	private static void sendPayload(ServerPlayer player, CustomPacketPayload payload) {
		if (!player.connection.hasChannel(payload.type().id())) {
			return;
		}
 		PacketDistributor.sendToPlayer(player, payload);
	}
}
