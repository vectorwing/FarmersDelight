package vectorwing.farmersdelight.utils;

import net.minecraft.block.WoodType;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModAtlases
{
	public static final RenderMaterial BLANK_CANVAS_SIGN_MATERIAL = new RenderMaterial(Atlases.SIGN_SHEET, new ResourceLocation(FarmersDelight.MODID, "entity/signs/canvas"));

	public static final Map<DyeColor, RenderMaterial> DYED_CANVAS_SIGN_MATERIALS =
			Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::getSignMaterial));


	public static RenderMaterial getSignMaterial(DyeColor dyeType) {
		return new RenderMaterial(Atlases.SIGN_SHEET, new ResourceLocation(FarmersDelight.MODID, "entity/signs/canvas_" + dyeType.getName()));
	}
}
