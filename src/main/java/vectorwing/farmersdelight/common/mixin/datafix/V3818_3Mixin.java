package vectorwing.farmersdelight.common.mixin.datafix;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3818_3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.SequencedMap;
import java.util.function.Supplier;

@Mixin(V3818_3.class)
public class V3818_3Mixin
{
	/**
	 * Adds Farmer's Delight's custom data components to the V3818_3 DATA_COMPONENTS schema so that
	 * old-world item data (e.g. the cooking pot's stored meal/container) migrates correctly.
	 * <p>
	 * In 1.21.1 this was done by injecting into {@code lambda$registerTypes$0} and modifying the
	 * {@code Pair[]} passed to {@code DSL.optionalFields(...)}. As of 1.21.x the schema now builds a
	 * mutable {@link SequencedMap} via {@code components(Schema)} and wraps it with
	 * {@code DSL.optionalFieldsLazy(Map)}, so we instead append to that returned map.
	 */
	@ModifyReturnValue(method = "components(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/SequencedMap;", at = @At("RETURN"))
	private static SequencedMap<String, Supplier<TypeTemplate>> addFDCustomComponents(SequencedMap<String, Supplier<TypeTemplate>> components, @Local(argsOnly = true) Schema schema) {
		components.put("farmersdelight:meal", () -> References.ITEM_STACK.in(schema));
		components.put("farmersdelight:container", () -> References.ITEM_STACK.in(schema));
		return components;
	}
}
