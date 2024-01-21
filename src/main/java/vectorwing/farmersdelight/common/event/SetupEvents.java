package vectorwing.farmersdelight.common.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import vectorwing.farmersdelight.common.crafting.condition.VanillaCrateEnabledCondition;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

import static vectorwing.farmersdelight.FarmersDelight.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents
{
	@SubscribeEvent
	public static void registerRecipeElements(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(new ResourceLocation(MODID, "tool_action"), ToolActionIngredient.SERIALIZER);
			CraftingHelper.register(VanillaCrateEnabledCondition.Serializer.INSTANCE);
		}
	}
}
