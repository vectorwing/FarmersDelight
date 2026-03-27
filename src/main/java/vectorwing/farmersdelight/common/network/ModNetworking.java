package vectorwing.farmersdelight.common.network;

import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.network.payload.RichSoilBoostParticlesPayload;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class ModNetworking
{
	@SubscribeEvent
	public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(RichSoilBoostParticlesPayload.TYPE, RichSoilBoostParticlesPayload.STREAM_CODEC, ClientPayloadHandler::handleRichSoilBoostParticles);
	}

	public static class ClientPayloadHandler
	{
		public static void handleRichSoilBoostParticles(RichSoilBoostParticlesPayload payload, IPayloadContext context) {
			BoneMealItem.addGrowthParticles(context.player().level(), payload.pos(), 15);
		}
	}
}
