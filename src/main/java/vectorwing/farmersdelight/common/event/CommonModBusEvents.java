package vectorwing.farmersdelight.common.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonModBusEvents
{
	@SubscribeEvent
	public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
		if (DatagenModLoader.isRunningDataGen()) {
			return;
		}
		if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
			Configuration.SOUP_ITEM_LIST.get().forEach((key) -> {
				Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(key));
				event.modify(item, (builder) -> builder.set(DataComponents.MAX_STACK_SIZE, 16));
			});
		}
		if (Configuration.ENABLE_RABBIT_STEW_BUFF.get()) {
			event.modify(Items.RABBIT_STEW, (builder) -> builder.set(DataComponents.FOOD, FoodValues.RABBIT_STEW_BUFF));
		}
	}

	@SubscribeEvent
	public static void registryActions(RegisterEvent event) {
		// TODO: Move remapper somewhere else
//		if (event.getRegistry().equals(BuiltInRegistries.BLOCK)) {
//			addBlockAlias("basket", "bamboo_basket");
//		}
//		if (event.getRegistry().equals(BuiltInRegistries.ITEM)) {
//			addItemAlias("basket", "bamboo_basket");
//		}
//		if (event.getRegistryKey().equals(BuiltInRegistries.RECIPE_SERIALIZERS)) {
//			CraftingHelper.register(new ResourceLocation(MODID, "tool_action"), ToolActionIngredient.SERIALIZER);
//			CraftingHelper.register(VanillaCrateEnabledCondition.Serializer.INSTANCE);
//		}
	}

	public static void addBlockAlias(String oldName, String newName) {
		ModBlocks.BLOCKS.addAlias(RecipeUtils.FDLocation(oldName), RecipeUtils.FDLocation(newName));
	}

	public static void addItemAlias(String oldName, String newName) {
		ModItems.ITEMS.addAlias(RecipeUtils.FDLocation(oldName), RecipeUtils.FDLocation(newName));
	}
}
