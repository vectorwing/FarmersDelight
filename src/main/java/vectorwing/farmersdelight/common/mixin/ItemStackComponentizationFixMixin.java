package vectorwing.farmersdelight.common.mixin;

import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings({"rawtypes"})
@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin
{
	@Inject(method = "fixItemStack", at = @At("HEAD"))
	private static void fixCustomStacks(ItemStackComponentizationFix.ItemStackData data, Dynamic tag, CallbackInfo ci) {
		if (data.is("farmersdelight:cooking_pot")) {
			data.fixSubTag("BlockEntityTag", false, subTag -> {
				// WORKING
				data.setComponent("farmersdelight:container", subTag.get("Container"));

				// NOT WORKING?
				List<Dynamic<?>> list = subTag.get("Inventory").get("Items")
						.asList(listTag -> listTag.remove("Slot"));
				if (!list.isEmpty()) {
					data.setComponent("farmersdelight:meal", list.getFirst());
				}
				return subTag.remove("Container").remove("Inventory");
			});
			data.removeTag("BlockEntityTag");
		}
	}
}