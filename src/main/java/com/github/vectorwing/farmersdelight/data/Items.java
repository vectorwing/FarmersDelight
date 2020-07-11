package com.github.vectorwing.farmersdelight.data;

import com.github.vectorwing.farmersdelight.FarmersDelight;
import com.github.vectorwing.farmersdelight.init.ItemInit;
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
		singleTexture(ItemInit.TOMATO_SAUCE.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
				"layer0", new ResourceLocation(FarmersDelight.MODID, "items/tomato_sauce"));
	}
}