package vectorwing.farmersdelight.common.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import vectorwing.farmersdelight.common.item.SkilletItem;

import java.util.function.Supplier;

public class ModNetworking {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("farmersdelight:channel"),
            () -> "1", s -> s.equals("1"), s -> s.equals("1"));

    static {
        CHANNEL.registerMessage(0, FlipSkilletMessage.class,
                FlipSkilletMessage::encode, FlipSkilletMessage::new, FlipSkilletMessage::handle);
    }


    public static void init() {
    }

    public static class FlipSkilletMessage {

        private static void handle(FlipSkilletMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
            contextSupplier.get().enqueueWork(() -> {
                Player player = contextSupplier.get().getSender();
                ItemStack stack = player.getUseItem();
                if (stack.getItem() instanceof SkilletItem) {
                    CompoundTag tag = stack.getOrCreateTag();
                    if (!tag.contains("FlipTimeStamp")) {
                        tag.putLong("FlipTimeStamp", player.level.getGameTime());
                    }
                }
            });
        }

        public FlipSkilletMessage(FriendlyByteBuf buf) {
        }

        public FlipSkilletMessage() {
        }

        public void encode(FriendlyByteBuf buf) {

        }

    }
}
