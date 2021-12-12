package vectorwing.farmersdelight.common.crafting.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;

public class VanillaCrateEnabledCondition implements ICondition
{
	private final ResourceLocation location;

	public VanillaCrateEnabledCondition(ResourceLocation location) {
		this.location = location;
	}

	@Override
	public ResourceLocation getID() {
		return this.location;
	}

	@Override
	public boolean test() {
		return Configuration.ENABLE_VANILLA_CROP_CRATES.get();
	}

	public static class Serializer implements IConditionSerializer<VanillaCrateEnabledCondition>
	{
		private final ResourceLocation location;

		public Serializer() {
			this.location = new ResourceLocation(FarmersDelight.MODID, "vanilla_crates_enabled");
		}

		@Override
		public ResourceLocation getID() {
			return this.location;
		}

		@Override
		public VanillaCrateEnabledCondition read(JsonObject json) {
			return new VanillaCrateEnabledCondition(this.location);
		}

		@Override
		public void write(JsonObject json, VanillaCrateEnabledCondition value) {
		}
	}
}