//package vectorwing.farmersdelight.common.networking;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.item.ItemStack;
//import vectorwing.farmersdelight.FarmersDelight;
//import vectorwing.farmersdelight.common.item.SkilletItem;
//
//import java.util.function.Supplier;
//
//public class ModNetworking
//{
//	private static final String PROTOCOL_VERSION = "1";
//	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//			new ResourceLocation(FarmersDelight.MODID, "main"),
//			() -> PROTOCOL_VERSION,
//			PROTOCOL_VERSION::equals,
//			PROTOCOL_VERSION::equals
//	);
//
//	public static void register() {
//		int i = 0;
//		INSTANCE.registerMessage(i++, FlipSkilletMessage.class, FlipSkilletMessage::encode, FlipSkilletMessage::new, FlipSkilletMessage::handle);
//	}
//
//	public static class FlipSkilletMessage
//	{
//		public FlipSkilletMessage(FriendlyByteBuf buf) {
//		}
//
//		public FlipSkilletMessage() {
//		}
//
//		public void handle(Supplier<NetworkEvent.Context> context) {
//			context.get().enqueueWork(() -> {
//				ServerPlayer player = context.get().getSender();
//				ItemStack stack = player.getUseItem();
//				if (stack.getItem() instanceof SkilletItem) {
//					CompoundTag tag = stack.getOrCreateTag();
//					if (!tag.contains("FlipTimeStamp")) {
//						tag.putLong("FlipTimeStamp", player.level().getGameTime());
//					}
//				}
//			});
//			context.get().setPacketHandled(true);
//		}
//
//		public void encode(FriendlyByteBuf buf) {
//		}
//	}
//}
