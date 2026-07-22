package vectorwing.farmersdelight.common.registry;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModAtlases
{
	public static final SpriteId BLANK_CANVAS_SIGN_MATERIAL = new SpriteId(Sheets.SIGN_SHEET, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/canvas"));
	public static final SpriteId BLANK_HANGING_CANVAS_SIGN_MATERIAL = new SpriteId(Sheets.SIGN_SHEET, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/hanging/canvas"));

	public static final Map<DyeColor, SpriteId> DYED_CANVAS_SIGN_MATERIALS =
			Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::createCanvasSignMaterial));
	public static final Map<DyeColor, SpriteId> DYED_HANGING_CANVAS_SIGN_MATERIALS =
			Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), ModAtlases::createHangingCanvasSignMaterial));

	public static SpriteId createCanvasSignMaterial(DyeColor dyeType) {
		return new SpriteId(Sheets.SIGN_SHEET, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/canvas_" + dyeType.getName()));
	}

	public static SpriteId createHangingCanvasSignMaterial(DyeColor dyeType) {
		return new SpriteId(Sheets.SIGN_SHEET, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "entity/signs/hanging/canvas_" + dyeType.getName()));
	}

	public static SpriteId getCanvasSignMaterial(@Nullable DyeColor dyeColor) {
		return dyeColor != null ? ModAtlases.DYED_CANVAS_SIGN_MATERIALS.get(dyeColor) : ModAtlases.BLANK_CANVAS_SIGN_MATERIAL;
	}

	public static SpriteId getHangingCanvasSignMaterial(@Nullable DyeColor dyeColor) {
		return dyeColor != null ? ModAtlases.DYED_HANGING_CANVAS_SIGN_MATERIALS.get(dyeColor) : ModAtlases.BLANK_HANGING_CANVAS_SIGN_MATERIAL;
	}
}

