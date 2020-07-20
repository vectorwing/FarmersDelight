package vectorwing.farmersdelight.data;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public class Items extends ItemModelProvider {

	public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, FarmersDelight.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		singleTexture(ModItems.TOMATO_SAUCE.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
				"layer0", new ResourceLocation(FarmersDelight.MODID, "items/tomato_sauce"));
	}
}