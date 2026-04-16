package vectorwing.farmersdelight.common.mixin.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V1460;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(V1460.class)
public abstract class V1460Mixin extends Schema
{
	@Shadow
	protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {}

	public V1460Mixin(int versionKey, Schema parent) {
		super(versionKey, parent);
	}

	@Inject(method = "registerBlockEntities", at = @At("RETURN"))
	private void registerFarmersDelightBlockEntities(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir) {
		var map = cir.getReturnValue();

		farmersdelight$registerHandlerInventory(schema, map, farmersdelight$id("stove"));
		farmersdelight$registerHandlerInventory(schema, map, farmersdelight$id("cutting_board"));
		farmersdelight$registerHandlerInventory(schema, map, farmersdelight$id("skillet"));

		schema.register(map, farmersdelight$id("cooking_pot"), p_17586_ ->
				DSL.optionalFields(
						"Inventory", DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))),
						"Container", References.ITEM_STACK.in(schema)
				));

		registerInventory(schema, map, farmersdelight$id("cabinet"));
		registerInventory(schema, map, farmersdelight$id("basket"));
	}

	@Unique
	private static String farmersdelight$id(String path) {
		return FarmersDelight.MODID + ":" + path;
	}

	@Unique
	private static void farmersdelight$registerHandlerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
		schema.register(map, name, string -> DSL.optionalFields("Inventory", DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))));
	}
}
