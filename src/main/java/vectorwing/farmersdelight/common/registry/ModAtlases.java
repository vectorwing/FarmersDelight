package vectorwing.farmersdelight.common.registry;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModAtlases
{
	public static final Material BLANK_CANVAS_SIGN_MATERIAL = new Material(Sheets.SIGN_SHEET, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/canvas"));
	public static final Material BLANK_HANGING_CANVAS_SIGN_MATERIAL = new Material(Sheets.SIGN_SHEET, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/hanging/canvas"));

	public static final Map<DyeColor, Material> DYED_CANVAS_SIGN_MATERIALS =
			Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::createCanvasSignMaterial));
	public static final Map<DyeColor, Material> DYED_HANGING_CANVAS_SIGN_MATERIALS =
			Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::createHangingCanvasSignMaterial));

	public static Material createCanvasSignMaterial(DyeColor dyeType) {
		return new Material(Sheets.SIGN_SHEET, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/canvas_" + dyeType.getName()));
	}

	public static Material createHangingCanvasSignMaterial(DyeColor dyeType) {
		return new Material(Sheets.SIGN_SHEET, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/hanging/canvas_" + dyeType.getName()));
	}

	public static Material getCanvasSignMaterial(@Nullable DyeColor dyeColor) {
		return dyeColor != null ? ModAtlases.DYED_CANVAS_SIGN_MATERIALS.get(dyeColor) : ModAtlases.BLANK_CANVAS_SIGN_MATERIAL;
	}

	public static Material getHangingCanvasSignMaterial(@Nullable DyeColor dyeColor) {
		return dyeColor != null ? ModAtlases.DYED_HANGING_CANVAS_SIGN_MATERIALS.get(dyeColor) : ModAtlases.BLANK_HANGING_CANVAS_SIGN_MATERIAL;
	}
}
