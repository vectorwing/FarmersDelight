package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlacedBlockTrigger;
import net.minecraft.block.Blocks;
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
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
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
		Consumer<Advancement> consumer = (advancement) -> {
			if (!set.add(advancement.getId())) {
				throw new IllegalStateException("Duplicate advancement " + advancement.getId());
			} else {
				Path path1 = getPath(PATH, advancement);

				try	{
					IDataProvider.save((new GsonBuilder()).setPrettyPrinting().create(), cache, advancement.copy().serialize(), path1);
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
			Advancement huntAndGather = getAdvancement(farmersDelight, ModItems.FLINT_KNIFE.get(), "craft_knife", FrameType.TASK, true, true, false)
					.withCriterion("flint_knife", InventoryChangeTrigger.Instance.forItems(ModItems.FLINT_KNIFE.get()))
					.withCriterion("iron_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
					.withCriterion("diamond_knife", InventoryChangeTrigger.Instance.forItems(ModItems.DIAMOND_KNIFE.get()))
					.withCriterion("golden_knife", InventoryChangeTrigger.Instance.forItems(ModItems.GOLDEN_KNIFE.get())).withRequirementsStrategy(IRequirementsStrategy.OR)
					.register(consumer, getNameId("main/craft_knife"));

			Advancement dippingYourRoots = getAdvancement(huntAndGather, ModItems.RICE_PANICLE.get(), "plant_rice", FrameType.TASK, true, true, false)
					.withCriterion("plant_rice", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.RICE_CROP.get()))
					.register(consumer, getNameId("main/plant_rice"));

			Advancement graspingAtStraws = getAdvancement(huntAndGather, ModItems.STRAW.get(), "harvest_straw", FrameType.TASK, true, true, false)
					.withCriterion("harvest_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
					.register(consumer, getNameId("main/harvest_straw"));

			Advancement plantFood = getAdvancement(graspingAtStraws, ModItems.MULCH.get(), "get_mulch", FrameType.TASK, true, true, false)
					.withCriterion("get_mulch", InventoryChangeTrigger.Instance.forItems(ModItems.MULCH.get()))
					.register(consumer, getNameId("main/get_mulch"));

			// Cooking Branch
			Advancement bonfireLit = getAdvancement(farmersDelight, Blocks.CAMPFIRE, "place_campfire", FrameType.TASK, true, true, false)
					.withCriterion("campfire", PlacedBlockTrigger.Instance.placedBlock(Blocks.CAMPFIRE))
					.register(consumer, getNameId("main/place_campfire"));

			Advancement fireUpTheGrill = getAdvancement(bonfireLit, ModItems.STOVE.get(), "craft_stove", FrameType.TASK, true, true, false)
					.withCriterion("stove", InventoryChangeTrigger.Instance.forItems(ModItems.STOVE.get()))
					.register(consumer, getNameId("main/craft_stove"));

			Advancement dinnerIsServed = getAdvancement(fireUpTheGrill, ModItems.COOKING_POT.get(), "place_cooking_pot", FrameType.GOAL, true, true, false)
					.withCriterion("cooking_pot", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.COOKING_POT.get()))
					.register(consumer, getNameId("main/place_cooking_pot"));

			Advancement masterChef = getAdvancement(dinnerIsServed, ModItems.PASTA_WITH_MEATBALLS.get(), "master_chef", FrameType.CHALLENGE, true, true, false)
					.withCriterion("mixed_salad", ConsumeItemTrigger.Instance.forItem(ModItems.MIXED_SALAD.get()))
					.withCriterion("beef_stew", ConsumeItemTrigger.Instance.forItem(ModItems.BEEF_STEW.get()))
					.withCriterion("chicken_soup", ConsumeItemTrigger.Instance.forItem(ModItems.CHICKEN_SOUP.get()))
					.withCriterion("vegetable_soup", ConsumeItemTrigger.Instance.forItem(ModItems.VEGETABLE_SOUP.get()))
					.withCriterion("fish_stew", ConsumeItemTrigger.Instance.forItem(ModItems.FISH_STEW.get()))
					.withCriterion("fried_rice", ConsumeItemTrigger.Instance.forItem(ModItems.FRIED_RICE.get()))
					.withCriterion("pumpkin_soup", ConsumeItemTrigger.Instance.forItem(ModItems.PUMPKIN_SOUP.get()))
					.withCriterion("honey_glazed_ham", ConsumeItemTrigger.Instance.forItem(ModItems.HONEY_GLAZED_HAM.get()))
					.withCriterion("pasta_with_meatballs", ConsumeItemTrigger.Instance.forItem(ModItems.PASTA_WITH_MEATBALLS.get()))
					.withCriterion("pasta_with_mutton_chop", ConsumeItemTrigger.Instance.forItem(ModItems.PASTA_WITH_MUTTON_CHOP.get()))
					.withCriterion("vegetable_noodles", ConsumeItemTrigger.Instance.forItem(ModItems.VEGETABLE_NOODLES.get()))
					.withCriterion("steak_and_potatoes", ConsumeItemTrigger.Instance.forItem(ModItems.STEAK_AND_POTATOES.get()))
					.withCriterion("shepherds_pie", ConsumeItemTrigger.Instance.forItem(ModItems.SHEPHERDS_PIE.get()))
					.withCriterion("ratatouille", ConsumeItemTrigger.Instance.forItem(ModItems.RATATOUILLE.get()))
					.withCriterion("squid_ink_pasta", ConsumeItemTrigger.Instance.forItem(ModItems.SQUID_INK_PASTA.get()))
					.withCriterion("stuffed_pumpkin", ConsumeItemTrigger.Instance.forItem(ModItems.STUFFED_PUMPKIN.get()))
					.withRewards(AdvancementRewards.Builder.experience(200))
					.register(consumer, getNameId("main/master_chef"));
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
