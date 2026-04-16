package vectorwing.farmersdelight.common.mixin.datafix;

import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"rawtypes"})
@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin
{
	@Inject(method = "fixItemStack", at = @At("HEAD"))
	private static void fixCustomStacks(ItemStackComponentizationFix.ItemStackData data, Dynamic tag, CallbackInfo ci) {
		if (data.is("farmersdelight:cooking_pot")) {
			data.fixSubTag("BlockEntityTag", false, subTag -> {
				Optional<? extends Dynamic<?>> container = subTag.get("Container").result();
				container.ifPresent(dynamic -> {
					Dynamic<?> result = dynamic.set("count", dynamic.createInt(dynamic.get("Count").asInt(1))).remove("Count");
					data.setComponent("farmersdelight:container", result);
				});

				Optional<? extends Dynamic<?>> inventory = subTag.get("Inventory").result();
				if (inventory.isPresent()) {
					List<Dynamic<?>> list = inventory.get().get("Items").asList(dynamic ->
							dynamic.set("count", dynamic.createInt(dynamic.get("Count").asInt(2))).remove("Count").remove("Slot"));
					if (!list.isEmpty()) {
						data.setComponent("farmersdelight:meal", list.getFirst());
					}
				}

				return subTag.remove("Container").remove("Inventory");
			});
		}
	}
}