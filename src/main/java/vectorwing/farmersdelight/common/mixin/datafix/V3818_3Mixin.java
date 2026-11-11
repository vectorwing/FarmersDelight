package vectorwing.farmersdelight.common.mixin.datafix;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3818_3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(V3818_3.class)
public class V3818_3Mixin
{
	@ModifyArg(method = "lambda$registerTypes$0", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/DSL;optionalFieldsLazy(Ljava/util/Map;)Lcom/mojang/datafixers/types/templates/TypeTemplate;"))
	private static Map<String, Supplier<TypeTemplate>> addFDCustomComponents(Map<String, Supplier<TypeTemplate>> fields, @Local(argsOnly = true) Schema schema) {
		var map = new HashMap<>(fields);

		map.put("farmersdelight:meal", () -> References.ITEM_STACK.in(schema));
		map.put("farmersdelight:container", () -> References.ITEM_STACK.in(schema));

		return map;
	}
}