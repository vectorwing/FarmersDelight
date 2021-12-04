package vectorwing.farmersdelight.crafting.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.setup.Configuration;

import java.util.Random;

/**
 * Credits to the Create team for the implementation of results with chances!
 */
public class ChanceResult
{
	public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1);

	private final ItemStack stack;
	private final float chance;

	public ChanceResult(ItemStack stack, float chance) {
		this.stack = stack;
		this.chance = chance;
	}

	public ItemStack getStack() {
		return stack;
	}

	public float getChance() {
		return chance;
	}

	public ItemStack rollOutput(Random rand, int fortuneLevel) {
		int outputAmount = stack.getCount();
		double fortuneBonus = Configuration.CUTTING_BOARD_FORTUNE_BONUS.get() * fortuneLevel;
		for (int roll = 0; roll < stack.getCount(); roll++)
			if (rand.nextFloat() > chance + fortuneBonus)
				outputAmount--;
		if (outputAmount == 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.copy();
		out.setCount(outputAmount);
		return out;
	}

	public JsonElement serialize() {
		JsonObject json = new JsonObject();

		ResourceLocation resourceLocation = stack.getItem().getRegistryName();
		json.addProperty("item", resourceLocation.toString());

		int count = stack.getCount();
		if (count != 1)
			json.addProperty("count", count);
		if (stack.hasTag())
			json.add("nbt", new JsonParser().parse(stack.getTag().toString()));
		if (chance != 1)
			json.addProperty("chance", chance);
		return json;
	}

	public static ChanceResult deserialize(JsonElement je) {
		if (!je.isJsonObject())
			throw new JsonSyntaxException("Must be a json object");

		JsonObject json = je.getAsJsonObject();
		String itemId = JSONUtils.getAsString(json, "item");
		int count = JSONUtils.getAsInt(json, "count", 1);
		float chance = JSONUtils.getAsFloat(json, "chance", 1);
		ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);

		if (JSONUtils.isValidPrimitive(json, "nbt")) {
			try {
				JsonElement element = json.get("nbt");
				itemstack.setTag(JsonToNBT.parseTag(
						element.isJsonObject() ? FarmersDelight.GSON.toJson(element) : JSONUtils.convertToString(element, "nbt")));
			}
			catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
		}

		return new ChanceResult(itemstack, chance);
	}

	public void write(PacketBuffer buf) {
		buf.writeItem(getStack());
		buf.writeFloat(getChance());
	}

	public static ChanceResult read(PacketBuffer buf) {
		return new ChanceResult(buf.readItem(), buf.readFloat());
	}
}
