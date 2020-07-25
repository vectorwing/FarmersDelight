package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlacedBlockTrigger;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.init.ModItems;
import vectorwing.farmersdelight.utils.Text;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider
{
	private final Path PATH;
	public static final Logger LOGGER = LogManager.getLogger();

	public Advancements(DataGenerator generatorIn)
	{
		super(generatorIn);
		PATH = generatorIn.getOutputFolder();
	}

	public void act(DirectoryCache cache) throws IOException
	{
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (p_204017_3_) -> {
			if (!set.add(p_204017_3_.getId())) {
				throw new IllegalStateException("Duplicate advancement " + p_204017_3_.getId());
			} else {
				Path path1 = getPath(PATH, p_204017_3_);

				try	{
					IDataProvider.save((new GsonBuilder()).setPrettyPrinting().create(), cache, p_204017_3_.copy().serialize(), path1);
				} catch (IOException ioexception) {
					LOGGER.error("Couldn't save advancement {}", path1, ioexception);
				}
			}
		};

		new FarmersDelightAdvancements().accept(consumer);
	}

	private static Path getPath(Path pathIn, Advancement advancementIn)
	{
		return pathIn.resolve("data/"+advancementIn.getId().getNamespace()+"/advancements/"+advancementIn.getId().getPath()+".json");
	}

	public static class FarmersDelightAdvancements implements Consumer<Consumer<Advancement>>
	{
		public void accept(Consumer<Advancement> consumer)
		{
			Advancement farmersDelight = Advancement.Builder.builder()
					.withDisplay(ModItems.COOKING_POT.get(),
							Text.getTranslation("advancement.root"),
							Text.getTranslation("advancement.root.desc"),
							new ResourceLocation("minecraft:textures/block/bricks.png"),
							FrameType.TASK, false, false, false)
					.withCriterion("seeds", InventoryChangeTrigger.Instance.forItems(Items.WHEAT_SEEDS))
					.register(consumer, getNameId("main/root"));

			// Farming Branch
			Advancement huntAndGather = getAdvancement(farmersDelight, ModItems.FLINT_KNIFE.get(), "craft_flint_knife", FrameType.TASK, true, true, false)
					.withCriterion("flint_knife", InventoryChangeTrigger.Instance.forItems(ModItems.FLINT_KNIFE.get()))
					.register(consumer, getNameId("main/craft_flint_knife"));

			Advancement dippingYourRoots = getAdvancement(huntAndGather, ModItems.RICE_PANICLE.get(), "plant_rice", FrameType.TASK, true, true, false)
					.withCriterion("plant_rice", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.RICE_CROP.get()))
					.register(consumer, getNameId("main/plant_rice"));

			// Cooking Branch
			Advancement fireUpTheGrill = getAdvancement(farmersDelight, ModItems.STOVE.get(), "craft_stove", FrameType.TASK, true, true, false)
					.withCriterion("stove", InventoryChangeTrigger.Instance.forItems(ModItems.STOVE.get()))
					.register(consumer, getNameId("main/craft_stove"));

			Advancement dinnerIsServed = getAdvancement(fireUpTheGrill, ModItems.COOKING_POT.get(), "place_cooking_pot", FrameType.GOAL, true, true, false)
					.withCriterion("cooking_pot", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.COOKING_POT.get()))
					.register(consumer, getNameId("main/place_cooking_pot"));

			Advancement animalCare = getAdvancement(dinnerIsServed, ModItems.COOKING_POT.get(), "feed_dog_food", FrameType.TASK, true, true, false)
					.withCriterion("dog_food", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.COOKING_POT.get()))
					.register(consumer, getNameId("main/feed_dog_food"));
		}

		protected static Advancement.Builder getAdvancement(Advancement parent, IItemProvider display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden)
		{
			return Advancement.Builder.builder().withParent(parent).withDisplay(display,
					Text.getTranslation("advancement." + name),
					Text.getTranslation("advancement." + name + ".desc"),
					null, frame, showToast, announceToChat, hidden);
		}

		private String getNameId(String id) {
			return FarmersDelight.MODID + ":" + id;
		}
	}
}
