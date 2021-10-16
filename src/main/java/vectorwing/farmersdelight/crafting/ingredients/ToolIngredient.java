// TODO: Fix ToolIngredient! ToolTypes are gone, so now you gotta figure something else out!

//package vectorwing.farmersdelight.crafting.ingredients;
//
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import net.minecraft.MethodsReturnNonnullByDefault;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraftforge.common.crafting.CraftingHelper;
//import net.minecraftforge.common.crafting.IIngredientSerializer;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//
//@ParametersAreNonnullByDefault
//@MethodsReturnNonnullByDefault
//public class ToolIngredient extends Ingredient
//{
//	public static final Serializer SERIALIZER = new Serializer();
//
//	public final ToolType toolType;
//
//	public ToolIngredient(ToolType toolType) {
//		super(ForgeRegistries.ITEMS.getValues().stream()
//				.map(ItemStack::new)
//				.filter(stack -> stack.getToolTypes().contains(toolType))
//				.map(Ingredient.ItemValue::new));
//		this.toolType = toolType;
//	}
//
//	@Override
//	public boolean test(@Nullable ItemStack stack) {
//		return stack != null && stack.getToolTypes().contains(toolType);
//	}
//
//	@Override
//	public JsonElement toJson() {
//		JsonObject json = new JsonObject();
//		json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
//		json.addProperty("tool", toolType.getName());
//		return json;
//	}
//
//	@Override
//	public IIngredientSerializer<? extends Ingredient> getSerializer() {
//		return SERIALIZER;
//	}
//
//	public static class Serializer implements IIngredientSerializer<ToolIngredient>
//	{
//		@Override
//		public ToolIngredient parse(JsonObject json) {
//			return new ToolIngredient(ToolType.get(json.get("tool").getAsString()));
//		}
//
//		@Override
//		public ToolIngredient parse(FriendlyByteBuf buffer) {
//			return new ToolIngredient(ToolType.get(buffer.readUtf()));
//		}
//
//		@Override
//		public void write(FriendlyByteBuf buffer, ToolIngredient ingredient) {
//			buffer.writeUtf(ingredient.toolType.getName());
//		}
//	}
//}
