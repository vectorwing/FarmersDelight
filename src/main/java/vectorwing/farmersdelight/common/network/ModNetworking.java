package vectorwing.farmersdelight.common.network;

import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.network.payload.FlipSkilletPayload;
import vectorwing.farmersdelight.common.network.payload.RichSoilBoostParticlesPayload;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class ModNetworking
{
	@SubscribeEvent
	public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(RichSoilBoostParticlesPayload.TYPE, RichSoilBoostParticlesPayload.STREAM_CODEC, ClientPayloadHandler::handleRichSoilBoostParticles);
        registrar.playToServer(FlipSkilletPayload.TYPE, FlipSkilletPayload.STREAM_CODEC, ServerPayloadHandler::handleFlipSkillet);
	}

	public static class ClientPayloadHandler
	{
		public static void handleRichSoilBoostParticles(RichSoilBoostParticlesPayload payload, IPayloadContext context) {
			BoneMealItem.addGrowthParticles(context.player().level(), payload.pos(), 15);
		}
	}

    public static class ServerPayloadHandler
    {
        public static void handleFlipSkillet(FlipSkilletPayload payload, IPayloadContext context) {
            ItemStack stack = context.player().getUseItem();
            if (stack.getItem() instanceof SkilletItem) {
                stack.set(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get(), context.player().level().getGameTime());
            }
        }
    }
}
