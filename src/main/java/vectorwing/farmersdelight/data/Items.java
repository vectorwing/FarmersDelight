package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModItems;

public class Items extends ItemModelProvider
{

	public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, FarmersDelight.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		ResourceLocation sauceName = ModItems.TOMATO_SAUCE.get().getRegistryName();
		if (sauceName != null)
			singleTexture(sauceName.getPath(), new ResourceLocation("item/handheld"),
					"layer0", new ResourceLocation(FarmersDelight.MODID, "items/tomato_sauce"));
	}
}