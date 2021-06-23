package vectorwing.farmersdelight.crafting.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToolIngredient extends Ingredient
{
    public static final Serializer SERIALIZER = new Serializer();

    public final ToolType toolType;

    public ToolIngredient(ToolType toolType) {
        super(ForgeRegistries.ITEMS.getValues().stream()
                .map(ItemStack::new)
                .filter(stack -> stack.getToolTypes().contains(toolType))
                .map(Ingredient.SingleItemList::new));
        this.toolType = toolType;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return stack != null && stack.getToolTypes().contains(toolType) || super.test(stack);
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
        json.addProperty("tool", toolType.getName());
        return json;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements IIngredientSerializer<ToolIngredient>
    {
        @Override
        public ToolIngredient parse(JsonObject json) {
            return new ToolIngredient(ToolType.get(json.get("tool").getAsString()));
        }

        @Override
        public ToolIngredient parse(PacketBuffer buffer) {
            return new ToolIngredient(ToolType.get(buffer.readString()));
        }

        @Override
        public void write(PacketBuffer buffer, ToolIngredient ingredient) {
            buffer.writeString(ingredient.toolType.getName());
        }
    }
}
