package vectorwing.farmersdelight.common.crafting.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;

public class VanillaCrateEnabledCondition implements ICondition
{
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "vanilla_crates_enabled");
	public static final VanillaCrateEnabledCondition INSTANCE = new VanillaCrateEnabledCondition();

	private VanillaCrateEnabledCondition() {
	}

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		return Configuration.ENABLE_VANILLA_CROP_CRATES.get();
	}

	public static class Serializer implements IConditionSerializer<VanillaCrateEnabledCondition>
	{

		public static final Serializer INSTANCE = new Serializer();

		public Serializer() {
		}

		@Override
		public ResourceLocation getID() {
			return ID;
		}

		@Override
		public VanillaCrateEnabledCondition read(JsonObject json) {
			return new VanillaCrateEnabledCondition();
		}

		@Override
		public void write(JsonObject json, VanillaCrateEnabledCondition value) {
		}
	}
}