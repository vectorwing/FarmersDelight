package vectorwing.farmersdelight.utils.advancement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class PlayerInteractedEntityTrigger implements ICriterionTrigger<PlayerInteractedEntityTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "player_interacted_entity");

	@Override
	public ResourceLocation getId() { return ID; }

	@Override
	public PlayerInteractedEntityTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
	{
		return new PlayerInteractedEntityTrigger.Instance(new ResourceLocation(JSONUtils.getString(json, "player_interacted_entity")), ItemPredicate.deserialize(json.get("item")));
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
	{

	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
	{

	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
	{

	}

	public static class Instance extends CriterionInstance
	{
		private final ResourceLocation multiblock;
		private final ItemPredicate item;

		public Instance(ResourceLocation multiblock, ItemPredicate hammer)
		{
			super(PlayerInteractedEntityTrigger.ID);
			this.multiblock = multiblock;
			this.item = hammer;
		}

		public boolean test(ItemStack hammer)
		{
			//return this.multiblock.equals(multiblock.getUniqueName())&&this.hammer.test(hammer);
			return false;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonobject = new JsonObject();
			jsonobject.addProperty("multiblock", this.multiblock.toString());
			jsonobject.add("item", this.item.serialize());
			return jsonobject;
		}
	}
}
