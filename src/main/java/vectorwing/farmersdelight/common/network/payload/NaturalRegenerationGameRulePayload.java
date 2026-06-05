package vectorwing.farmersdelight.common.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * <a href="https://www.curseforge.com/minecraft/mc-mods/appleskin">...</a>
 */
public record NaturalRegenerationGameRulePayload(boolean value) implements CustomPacketPayload
{
	public static boolean NATURAL_REGENERATION = true;
	public static final Identifier ID = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "natural_regeneration_gamerule_sync");
	public static final Type<NaturalRegenerationGameRulePayload> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, NaturalRegenerationGameRulePayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL,
		NaturalRegenerationGameRulePayload::value,
		NaturalRegenerationGameRulePayload::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
