package vectorwing.farmersdelight.common.registry;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModAtlases
{
	public static final Material BLANK_CANVAS_SIGN_MATERIAL = new Material(Sheets.SIGN_SHEET, new ResourceLocation(FarmersDelight.MODID, "entity/signs/canvas"));

	public static final Map<DyeColor, Material> DYED_CANVAS_SIGN_MATERIALS =
			Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::getSignMaterial));


	public static Material getSignMaterial(DyeColor dyeType) {
		return new Material(Sheets.SIGN_SHEET, new ResourceLocation(FarmersDelight.MODID, "entity/signs/canvas_" + dyeType.getName()));
	}
}
