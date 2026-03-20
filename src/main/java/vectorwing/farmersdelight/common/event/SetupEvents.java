package vectorwing.farmersdelight.common.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import vectorwing.farmersdelight.common.crafting.condition.VanillaCrateEnabledCondition;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import static vectorwing.farmersdelight.FarmersDelight.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents
{
	@SubscribeEvent
	public static void registerRecipeElements(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
			addBlockAlias("basket", "bamboo_basket");
		}
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
			addItemAlias("basket", "bamboo_basket");
		}
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(new ResourceLocation(MODID, "tool_action"), ToolActionIngredient.SERIALIZER);
			CraftingHelper.register(VanillaCrateEnabledCondition.Serializer.INSTANCE);
		}
	}

	public static void addBlockAlias(String oldName, String newName) {
		((ForgeRegistry<?>) ForgeRegistries.BLOCKS).addAlias(RecipeUtils.FDLocation(oldName), RecipeUtils.FDLocation(newName));
	}

	public static void addItemAlias(String oldName, String newName) {
		((ForgeRegistry<?>) ForgeRegistries.ITEMS).addAlias(RecipeUtils.FDLocation(oldName), RecipeUtils.FDLocation(newName));
	}
}
