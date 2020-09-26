package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;
import vectorwing.farmersdelight.utils.tags.ModTags;
import net.minecraft.item.Items;

public class ItemTags extends ItemTagsProvider {
	public ItemTags(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerTags() {
		getBuilder(ModTags.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get());
		getBuilder(ModTags.STRAW_HARVESTERS).add(ModTags.KNIVES);
		getBuilder(ModTags.COMFORT_FOODS).add(Items.MUSHROOM_STEM, Items.BEETROOT_SOUP, Items.RABBIT_STEW);
		getBuilder(ModTags.WOLF_PREY).add(ForgeTags.RAW_CHICKEN).add(Items.MUTTON, Items.RABBIT);
	}
}
