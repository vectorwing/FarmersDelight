package vectorwing.farmersdelight.common.mixin.datafix;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3818_3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(V3818_3.class)
public class V3818_3Mixin
{
	@ModifyArg(method = "lambda$registerTypes$0", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/DSL;optionalFields([Lcom/mojang/datafixers/util/Pair;)Lcom/mojang/datafixers/types/templates/TypeTemplate;"))
	private static Pair<String, TypeTemplate>[] addFDCustomComponents(Pair<String, TypeTemplate>[] components, @Local(argsOnly = true) Schema schema) {
		var list = new ArrayList<>(List.of(components));

		list.add(Pair.of("farmersdelight:meal", References.ITEM_STACK.in(schema)));
		list.add(Pair.of("farmersdelight:container", References.ITEM_STACK.in(schema)));

		return list.toArray(components);
	}
}