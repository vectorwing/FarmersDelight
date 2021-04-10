package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.advancement.CuttingBoardTrigger;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModEffects;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.TextUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Advancements extends AdvancementProvider
{
	private final Path PATH;
	public static final Logger LOGGER = LogManager.getLogger();

	public Advancements(DataGenerator generatorIn) {
		super(generatorIn);
		PATH = generatorIn.getOutputFolder();
	}

	@Override
	public void act(DirectoryCache cache) {
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (advancement) -> {
			if (!set.add(advancement.getId())) {
				throw new IllegalStateException("Duplicate advancement " + advancement.getId());
			} else {
				Path path1 = getPath(PATH, advancement);

				try {
					IDataProvider.save((new GsonBuilder()).setPrettyPrinting().create(), cache, advancement.copy().serialize(), path1);
				}
				catch (IOException ioexception) {
					LOGGER.error("Couldn't save advancement {}", path1, ioexception);
				}
			}
		};

		new FarmersDelightAdvancements().accept(consumer);
	}

	private static Path getPath(Path pathIn, Advancement advancementIn) {
		return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
	}

	public static class FarmersDelightAdvancements implements Consumer<Consumer<Advancement>>
	{
		@Override
		@SuppressWarnings("unused")
		public void accept(Consumer<Advancement> consumer) {
			Advancement farmersDelight = Advancement.Builder.builder()
					.withDisplay(ModItems.COOKING_POT.get(),
							TextUtils.getTranslation("advancement.root"),
							TextUtils.getTranslation("advancement.root.desc"),
							new ResourceLocation("minecraft:textures/block/bricks.png"),
							FrameType.TASK, false, false, false)
					.withCriterion("seeds", InventoryChangeTrigger.Instance.forItems(new IItemProvider[]{}))
					.register(consumer, getNameId("main/root"));

			// Farming Branch
			Advancement huntAndGather = getAdvancement(farmersDelight, ModItems.FLINT_KNIFE.get(), "craft_knife", FrameType.TASK, true, true, false)
					.withCriterion("flint_knife", InventoryChangeTrigger.Instance.forItems(ModItems.FLINT_KNIFE.get()))
					.withCriterion("iron_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
					.withCriterion("diamond_knife", InventoryChangeTrigger.Instance.forItems(ModItems.DIAMOND_KNIFE.get()))
					.withCriterion("golden_knife", InventoryChangeTrigger.Instance.forItems(ModItems.GOLDEN_KNIFE.get()))
					.withCriterion("netherite_knife", InventoryChangeTrigger.Instance.forItems(ModItems.NETHERITE_KNIFE.get()))
					.withRequirementsStrategy(IRequirementsStrategy.OR)
					.register(consumer, getNameId("main/craft_knife"));

			Advancement graspingAtStraws = getAdvancement(huntAndGather, ModItems.STRAW.get(), "harvest_straw", FrameType.TASK, true, true, false)
					.withCriterion("harvest_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
					.register(consumer, getNameId("main/harvest_straw"));

			Advancement wildButcher = getAdvancement(huntAndGather, ModItems.HAM.get(), "get_ham", FrameType.TASK, true, true, false)
					.withCriterion("ham", InventoryChangeTrigger.Instance.forItems(ModItems.HAM.get()))
					.register(consumer, getNameId("main/get_ham"));

			Advancement dippingYourRoots = getAdvancement(graspingAtStraws, ModItems.RICE_PANICLE.get(), "plant_rice", FrameType.TASK, true, true, false)
					.withCriterion("plant_rice", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.RICE_CROP.get()))
					.register(consumer, getNameId("main/plant_rice"));

			Advancement cropRotation = getAdvancement(dippingYourRoots, ModItems.CABBAGE.get(), "plant_all_crops", FrameType.CHALLENGE, true, true, false)
					.withCriterion("wheat", PlacedBlockTrigger.Instance.placedBlock(Blocks.WHEAT))
					.withCriterion("beetroot", PlacedBlockTrigger.Instance.placedBlock(Blocks.BEETROOTS))
					.withCriterion("carrot", PlacedBlockTrigger.Instance.placedBlock(Blocks.CARROTS))
					.withCriterion("potato", PlacedBlockTrigger.Instance.placedBlock(Blocks.POTATOES))
					.withCriterion("brown_mushroom", PlacedBlockTrigger.Instance.placedBlock(Blocks.BROWN_MUSHROOM))
					.withCriterion("red_mushroom", PlacedBlockTrigger.Instance.placedBlock(Blocks.RED_MUSHROOM))
					.withCriterion("sugar_cane", PlacedBlockTrigger.Instance.placedBlock(Blocks.SUGAR_CANE))
					.withCriterion("melon", PlacedBlockTrigger.Instance.placedBlock(Blocks.MELON_STEM))
					.withCriterion("pumpkin", PlacedBlockTrigger.Instance.placedBlock(Blocks.PUMPKIN_STEM))
					.withCriterion("sweet_berries", PlacedBlockTrigger.Instance.placedBlock(Blocks.SWEET_BERRY_BUSH))
					.withCriterion("cocoa", PlacedBlockTrigger.Instance.placedBlock(Blocks.COCOA))
					.withCriterion("cabbage", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.CABBAGE_CROP.get()))
					.withCriterion("tomato", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.TOMATO_CROP.get()))
					.withCriterion("onion", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.ONION_CROP.get()))
					.withCriterion("rice", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.RICE_CROP.get()))
					.withCriterion("nether_wart", PlacedBlockTrigger.Instance.placedBlock(Blocks.NETHER_WART))
					.withCriterion("chorus_flower", PlacedBlockTrigger.Instance.placedBlock(Blocks.CHORUS_FLOWER))
					.withRewards(AdvancementRewards.Builder.experience(100))
					.register(consumer, getNameId("main/plant_all_crops"));

			Advancement plantFood = getAdvancement(dippingYourRoots, ModItems.RICH_SOIL.get(), "get_rich_soil", FrameType.GOAL, true, true, false)
					.withCriterion("get_rich_soil", InventoryChangeTrigger.Instance.forItems(ModItems.RICH_SOIL.get()))
					.register(consumer, getNameId("main/get_rich_soil"));

			Advancement fungusAmongUs = getAdvancement(plantFood, ModItems.RED_MUSHROOM_COLONY.get(), "get_mushroom_colony", FrameType.TASK, true, true, false)
					.withCriterion("brown_mushroom_colony", InventoryChangeTrigger.Instance.forItems(ModItems.BROWN_MUSHROOM_COLONY.get()))
					.withCriterion("red_mushroom_colony", InventoryChangeTrigger.Instance.forItems(ModItems.RED_MUSHROOM_COLONY.get()))
					.withRequirementsStrategy(IRequirementsStrategy.OR)
					.register(consumer, getNameId("main/get_mushroom_colony"));

			Advancement cantTakeTheHeat = getAdvancement(huntAndGather, ModItems.NETHERITE_KNIFE.get(), "obtain_netherite_knife", FrameType.CHALLENGE, true, true, false)
					.withCriterion("obtain_netherite_knife", InventoryChangeTrigger.Instance.forItems(ModItems.NETHERITE_KNIFE.get()))
					.withRewards(AdvancementRewards.Builder.experience(200))
					.register(consumer, getNameId("main/obtain_netherite_knife"));

			// Cooking Branch
			Advancement bonfireLit = getAdvancement(farmersDelight, Blocks.CAMPFIRE, "place_campfire", FrameType.TASK, true, true, false)
					.withCriterion("campfire", PlacedBlockTrigger.Instance.placedBlock(Blocks.CAMPFIRE))
					.withCriterion("soul_campfire", PlacedBlockTrigger.Instance.placedBlock(Blocks.SOUL_CAMPFIRE))
					.withRequirementsStrategy(IRequirementsStrategy.OR)
					.register(consumer, getNameId("main/place_campfire"));

			Advancement fireUpTheGrill = getAdvancement(bonfireLit, ModItems.STOVE.get(), "place_stove", FrameType.TASK, true, true, false)
					.withCriterion("stove", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.STOVE.get()))
					.register(consumer, getNameId("main/place_stove"));

			Advancement dinnerIsServed = getAdvancement(fireUpTheGrill, ModItems.COOKING_POT.get(), "place_cooking_pot", FrameType.GOAL, true, true, false)
					.withCriterion("cooking_pot", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.COOKING_POT.get()))
					.register(consumer, getNameId("main/place_cooking_pot"));

			Advancement cupOfHappiness = getAdvancement(dinnerIsServed, ModItems.HOT_COCOA.get(), "drink_hot_cocoa", FrameType.TASK, true, true, false)
					.withCriterion("hot_cocoa", ConsumeItemTrigger.Instance.forItem(ModItems.HOT_COCOA.get()))
					.register(consumer, getNameId("main/drink_hot_cocoa"));

			Advancement warmAndCozy = getAdvancement(dinnerIsServed, ModItems.CHICKEN_SOUP.get(), "eat_comfort_food", FrameType.TASK, true, true, false)
					.withCriterion("comfort", EffectsChangedTrigger.Instance.forEffect(MobEffectsPredicate.any().addEffect(ModEffects.COMFORT.get())))
					.register(consumer, getNameId("main/eat_comfort_food"));

			Advancement wellServed = getAdvancement(warmAndCozy, ModItems.STEAK_AND_POTATOES.get(), "eat_nourishing_food", FrameType.TASK, true, true, false)
					.withCriterion("nourished", EffectsChangedTrigger.Instance.forEffect(MobEffectsPredicate.any().addEffect(ModEffects.NOURISHED.get())))
					.register(consumer, getNameId("main/eat_nourishing_food"));

			Advancement gloriousFeast = getAdvancement(wellServed, ModItems.ROAST_CHICKEN_BLOCK.get(), "place_feast", FrameType.TASK, true, true, false)
					.withCriterion("roast_chicken", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.ROAST_CHICKEN_BLOCK.get()))
					.withCriterion("stuffed_pumpkin", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.STUFFED_PUMPKIN_BLOCK.get()))
					.withCriterion("honey_glazed_ham", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get()))
					.withCriterion("shepherds_pie", PlacedBlockTrigger.Instance.placedBlock(ModBlocks.SHEPHERDS_PIE_BLOCK.get()))
					.withRequirementsStrategy(IRequirementsStrategy.OR)
					.register(consumer, getNameId("main/place_feast"));

			Advancement watchYourFingers = getAdvancement(fireUpTheGrill, ModItems.CUTTING_BOARD.get(), "use_cutting_board", FrameType.TASK, true, true, false)
					.withCriterion("cutting_board", CuttingBoardTrigger.Instance.simple())
					.register(consumer, getNameId("main/use_cutting_board"));

			Advancement masterChef = getAdvancement(gloriousFeast, ModItems.HONEY_GLAZED_HAM.get(), "master_chef", FrameType.CHALLENGE, true, true, false)
					.withCriterion("mixed_salad", ConsumeItemTrigger.Instance.forItem(ModItems.MIXED_SALAD.get()))
					.withCriterion("beef_stew", ConsumeItemTrigger.Instance.forItem(ModItems.BEEF_STEW.get()))
					.withCriterion("chicken_soup", ConsumeItemTrigger.Instance.forItem(ModItems.CHICKEN_SOUP.get()))
					.withCriterion("vegetable_soup", ConsumeItemTrigger.Instance.forItem(ModItems.VEGETABLE_SOUP.get()))
					.withCriterion("fish_stew", ConsumeItemTrigger.Instance.forItem(ModItems.FISH_STEW.get()))
					.withCriterion("fried_rice", ConsumeItemTrigger.Instance.forItem(ModItems.FRIED_RICE.get()))
					.withCriterion("pumpkin_soup", ConsumeItemTrigger.Instance.forItem(ModItems.PUMPKIN_SOUP.get()))
					.withCriterion("baked_cod_stew", ConsumeItemTrigger.Instance.forItem(ModItems.BAKED_COD_STEW.get()))
					.withCriterion("noodle_soup", ConsumeItemTrigger.Instance.forItem(ModItems.NOODLE_SOUP.get()))
					.withCriterion("pasta_with_meatballs", ConsumeItemTrigger.Instance.forItem(ModItems.PASTA_WITH_MEATBALLS.get()))
					.withCriterion("pasta_with_mutton_chop", ConsumeItemTrigger.Instance.forItem(ModItems.PASTA_WITH_MUTTON_CHOP.get()))
					.withCriterion("roasted_mutton_chops", ConsumeItemTrigger.Instance.forItem(ModItems.ROASTED_MUTTON_CHOPS.get()))
					.withCriterion("vegetable_noodles", ConsumeItemTrigger.Instance.forItem(ModItems.VEGETABLE_NOODLES.get()))
					.withCriterion("steak_and_potatoes", ConsumeItemTrigger.Instance.forItem(ModItems.STEAK_AND_POTATOES.get()))
					.withCriterion("ratatouille", ConsumeItemTrigger.Instance.forItem(ModItems.RATATOUILLE.get()))
					.withCriterion("squid_ink_pasta", ConsumeItemTrigger.Instance.forItem(ModItems.SQUID_INK_PASTA.get()))
					.withCriterion("grilled_salmon", ConsumeItemTrigger.Instance.forItem(ModItems.GRILLED_SALMON.get()))
					.withCriterion("roast_chicken", ConsumeItemTrigger.Instance.forItem(ModItems.ROAST_CHICKEN.get()))
					.withCriterion("stuffed_pumpkin", ConsumeItemTrigger.Instance.forItem(ModItems.STUFFED_PUMPKIN.get()))
					.withCriterion("honey_glazed_ham", ConsumeItemTrigger.Instance.forItem(ModItems.HONEY_GLAZED_HAM.get()))
					.withCriterion("shepherds_pie", ConsumeItemTrigger.Instance.forItem(ModItems.SHEPHERDS_PIE.get()))
					.withRewards(AdvancementRewards.Builder.experience(200))
					.register(consumer, getNameId("main/master_chef"));
		}

		protected static Advancement.Builder getAdvancement(Advancement parent, IItemProvider display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
			return Advancement.Builder.builder().withParent(parent).withDisplay(display,
					TextUtils.getTranslation("advancement." + name),
					TextUtils.getTranslation("advancement." + name + ".desc"),
					null, frame, showToast, announceToChat, hidden);
		}

		private String getNameId(String id) {
			return FarmersDelight.MODID + ":" + id;
		}
	}
}
