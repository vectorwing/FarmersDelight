package vectorwing.farmersdelight.common.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;

public record FlipSkilletPayload() implements CustomPacketPayload
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "flip_skillet");
    public static final FlipSkilletPayload INSTANCE = new FlipSkilletPayload();
    public static final Type<FlipSkilletPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, FlipSkilletPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}