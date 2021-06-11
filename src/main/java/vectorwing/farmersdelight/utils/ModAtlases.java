package vectorwing.farmersdelight.utils;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;

public class ModAtlases
{
	public static final RenderMaterial CANVAS_SIGN_BLANK_MATERIAL = new RenderMaterial(Atlases.SIGN_ATLAS, new ResourceLocation(FarmersDelight.MODID, "entity/signs/canvas_blank"));

	public static RenderMaterial getSignMaterial(@Nullable DyeColor dyeType) {
		ResourceLocation location = new ResourceLocation(dyeType != null ? dyeType.name() : "blank");
		return new RenderMaterial(Atlases.SIGN_ATLAS, new ResourceLocation(location.getNamespace(), "entity/signs/canvas_" + location.getPath()));
	}
}
