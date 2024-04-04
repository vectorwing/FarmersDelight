package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.world.filter.BiomeTagFilter;

import java.util.function.Supplier;

public class ModPlacementModifiers
{
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, FarmersDelight.MODID);

	public static final Supplier<PlacementModifierType<BiomeTagFilter>> BIOME_TAG = PLACEMENT_MODIFIERS.register("biome_tag", () -> typeConvert(BiomeTagFilter.CODEC));

	private static <P extends PlacementModifier> PlacementModifierType<P> typeConvert(Codec<P> codec) {
		return () -> codec;
	}
}
