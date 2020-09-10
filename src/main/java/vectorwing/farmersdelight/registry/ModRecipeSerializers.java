package vectorwing.farmersdelight.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.KnifeRepairRecipe;

public class ModRecipeSerializers {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersDelight.MODID);

	public static final RegistryObject<SpecialRecipeSerializer<KnifeRepairRecipe>> KNIFE_REPAIR = RECIPE_SERIALIZERS.register(
			"knife_repair", () -> new SpecialRecipeSerializer<>(KnifeRepairRecipe::new)
	);
}
