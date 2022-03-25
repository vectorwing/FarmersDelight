package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.advancement.CuttingBoardTrigger;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.TextUtils;

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
	public void run(HashCache cache) {
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> consumer = (advancement) -> {
			if (!set.add(advancement.getId())) {
				throw new IllegalStateException("Duplicate advancement " + advancement.getId());
			} else {
				Path path1 = getPath(PATH, advancement);

				try {
					DataProvider.save((new GsonBuilder()).setPrettyPrinting().create(), cache, advancement.deconstruct().serializeToJson(), path1);
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
			Advancement farmersDelight = Advancement.Builder.advancement()
					.display(ModItems.COOKING_POT.get(),
							TextUtils.getTranslation("advancement.root"),
							TextUtils.getTranslation("advancement.root.desc"),
							new ResourceLocation("minecraft:textures/block/bricks.png"),
							FrameType.TASK, false, false, false)
					.addCriterion("seeds", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}))
					.save(consumer, getNameId("main/root"));

			// Farming Branch
			Advancement huntAndGather = getAdvancement(farmersDelight, ModItems.FLINT_KNIFE.get(), "craft_knife", FrameType.TASK, true, true, false)
					.addCriterion("flint_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FLINT_KNIFE.get()))
					.addCriterion("iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
					.addCriterion("diamond_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DIAMOND_KNIFE.get()))
					.addCriterion("golden_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOLDEN_KNIFE.get()))
					.addCriterion("netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
					.requirements(RequirementsStrategy.OR)
					.save(consumer, getNameId("main/craft_knife"));

			Advancement graspingAtStraws = getAdvancement(huntAndGather, ModItems.STRAW.get(), "harvest_straw", FrameType.TASK, true, true, false)
					.addCriterion("harvest_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
					.save(consumer, getNameId("main/harvest_straw"));

			Advancement wildButcher = getAdvancement(huntAndGather, ModItems.HAM.get(), "get_ham", FrameType.TASK, true, true, false)
					.addCriterion("ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HAM.get()))
					.save(consumer, getNameId("main/get_ham"));

			Advancement dippingYourRoots = getAdvancement(graspingAtStraws, ModItems.RICE_PANICLE.get(), "plant_rice", FrameType.TASK, true, true, false)
					.addCriterion("plant_rice", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.RICE_CROP.get()))
					.save(consumer, getNameId("main/plant_rice"));

			Advancement cropRotation = getAdvancement(dippingYourRoots, ModItems.CABBAGE.get(), "plant_all_crops", FrameType.CHALLENGE, true, true, false)
					.addCriterion("wheat", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.WHEAT))
					.addCriterion("beetroot", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS))
					.addCriterion("carrot", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.CARROTS))
					.addCriterion("potato", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.POTATOES))
					.addCriterion("brown_mushroom", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.BROWN_MUSHROOM))
					.addCriterion("red_mushroom", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.RED_MUSHROOM))
					.addCriterion("sugar_cane", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.SUGAR_CANE))
					.addCriterion("melon", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM))
					.addCriterion("pumpkin", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM))
					.addCriterion("sweet_berries", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.SWEET_BERRY_BUSH))
					.addCriterion("cocoa", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.COCOA))
					.addCriterion("cabbage", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.CABBAGE_CROP.get()))
					.addCriterion("tomato", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.TOMATO_CROP.get()))
					.addCriterion("onion", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.ONION_CROP.get()))
					.addCriterion("rice", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.RICE_CROP.get()))
					.addCriterion("nether_wart", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART))
					.addCriterion("chorus_flower", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.CHORUS_FLOWER))
					.addCriterion("glow_berries", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.CAVE_VINES))
					.rewards(AdvancementRewards.Builder.experience(100))
					.save(consumer, getNameId("main/plant_all_crops"));

			Advancement plantFood = getAdvancement(dippingYourRoots, ModItems.RICH_SOIL.get(), "get_rich_soil", FrameType.GOAL, true, true, false)
					.addCriterion("get_rich_soil", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICH_SOIL.get()))
					.save(consumer, getNameId("main/get_rich_soil"));

			Advancement fungusAmongUs = getAdvancement(plantFood, ModItems.RED_MUSHROOM_COLONY.get(), "get_mushroom_colony", FrameType.TASK, true, true, false)
					.addCriterion("brown_mushroom_colony", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BROWN_MUSHROOM_COLONY.get()))
					.addCriterion("red_mushroom_colony", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RED_MUSHROOM_COLONY.get()))
					.requirements(RequirementsStrategy.OR)
					.save(consumer, getNameId("main/get_mushroom_colony"));

			Advancement cantTakeTheHeat = getAdvancement(huntAndGather, ModItems.NETHERITE_KNIFE.get(), "obtain_netherite_knife", FrameType.CHALLENGE, true, true, false)
					.addCriterion("obtain_netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
					.rewards(AdvancementRewards.Builder.experience(200))
					.save(consumer, getNameId("main/obtain_netherite_knife"));

			// Cooking Branch
			Advancement bonfireLit = getAdvancement(farmersDelight, Blocks.CAMPFIRE, "place_campfire", FrameType.TASK, true, true, false)
					.addCriterion("campfire", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.CAMPFIRE))
					.addCriterion("soul_campfire", PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.SOUL_CAMPFIRE))
					.requirements(RequirementsStrategy.OR)
					.save(consumer, getNameId("main/place_campfire"));

			Advancement fireUpTheGrill = getAdvancement(bonfireLit, ModItems.STOVE.get(), "place_stove", FrameType.TASK, true, true, false)
					.addCriterion("stove", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.STOVE.get()))
					.save(consumer, getNameId("main/place_stove"));

			Advancement dinnerIsServed = getAdvancement(fireUpTheGrill, ModItems.COOKING_POT.get(), "place_cooking_pot", FrameType.GOAL, true, true, false)
					.addCriterion("cooking_pot", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.COOKING_POT.get()))
					.save(consumer, getNameId("main/place_cooking_pot"));

			Advancement portableCooking = getAdvancement(fireUpTheGrill, ModItems.SKILLET.get(), "use_skillet", FrameType.TASK, true, true, false)
					.addCriterion("skillet", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.SKILLET.get()))
					.save(consumer, getNameId("main/use_skillet"));

			Advancement sizzlingHot = getAdvancement(portableCooking, ModItems.SKILLET.get(), "place_skillet", FrameType.TASK, true, true, false)
					.addCriterion("skillet", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.SKILLET.get()))
					.save(consumer, getNameId("main/place_skillet"));

			Advancement cupOfHappiness = getAdvancement(dinnerIsServed, ModItems.HOT_COCOA.get(), "drink_hot_cocoa", FrameType.TASK, true, true, false)
					.addCriterion("hot_cocoa", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.HOT_COCOA.get()))
					.save(consumer, getNameId("main/drink_hot_cocoa"));

			Advancement warmAndCozy = getAdvancement(dinnerIsServed, ModItems.CHICKEN_SOUP.get(), "eat_comfort_food", FrameType.TASK, true, true, false)
					.addCriterion("comfort", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(ModEffects.COMFORT.get())))
					.save(consumer, getNameId("main/eat_comfort_food"));

			Advancement wellServed = getAdvancement(warmAndCozy, ModItems.STEAK_AND_POTATOES.get(), "eat_nourishing_food", FrameType.TASK, true, true, false)
					.addCriterion("nourished", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.effects().and(ModEffects.NOURISHMENT.get())))
					.save(consumer, getNameId("main/eat_nourishing_food"));

			Advancement gloriousFeast = getAdvancement(wellServed, ModItems.ROAST_CHICKEN_BLOCK.get(), "place_feast", FrameType.TASK, true, true, false)
					.addCriterion("roast_chicken", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.ROAST_CHICKEN_BLOCK.get()))
					.addCriterion("stuffed_pumpkin", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.STUFFED_PUMPKIN_BLOCK.get()))
					.addCriterion("honey_glazed_ham", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get()))
					.addCriterion("shepherds_pie", PlacedBlockTrigger.TriggerInstance.placedBlock(ModBlocks.SHEPHERDS_PIE_BLOCK.get()))
					.requirements(RequirementsStrategy.OR)
					.save(consumer, getNameId("main/place_feast"));

			Advancement watchYourFingers = getAdvancement(fireUpTheGrill, ModItems.CUTTING_BOARD.get(), "use_cutting_board", FrameType.TASK, true, true, false)
					.addCriterion("cutting_board", CuttingBoardTrigger.Instance.simple())
					.save(consumer, getNameId("main/use_cutting_board"));

			Advancement masterChef = getAdvancement(gloriousFeast, ModItems.HONEY_GLAZED_HAM.get(), "master_chef", FrameType.CHALLENGE, true, true, false)
					.addCriterion("mixed_salad", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.MIXED_SALAD.get()))
					.addCriterion("beef_stew", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BEEF_STEW.get()))
					.addCriterion("chicken_soup", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.CHICKEN_SOUP.get()))
					.addCriterion("vegetable_soup", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.VEGETABLE_SOUP.get()))
					.addCriterion("fish_stew", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.FISH_STEW.get()))
					.addCriterion("fried_rice", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.FRIED_RICE.get()))
					.addCriterion("pumpkin_soup", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.PUMPKIN_SOUP.get()))
					.addCriterion("baked_cod_stew", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BAKED_COD_STEW.get()))
					.addCriterion("noodle_soup", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.NOODLE_SOUP.get()))
					.addCriterion("pasta_with_meatballs", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.PASTA_WITH_MEATBALLS.get()))
					.addCriterion("pasta_with_mutton_chop", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.PASTA_WITH_MUTTON_CHOP.get()))
					.addCriterion("roasted_mutton_chops", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.ROASTED_MUTTON_CHOPS.get()))
					.addCriterion("vegetable_noodles", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.VEGETABLE_NOODLES.get()))
					.addCriterion("steak_and_potatoes", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.STEAK_AND_POTATOES.get()))
					.addCriterion("ratatouille", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.RATATOUILLE.get()))
					.addCriterion("squid_ink_pasta", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.SQUID_INK_PASTA.get()))
					.addCriterion("grilled_salmon", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.GRILLED_SALMON.get()))
					.addCriterion("roast_chicken", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.ROAST_CHICKEN.get()))
					.addCriterion("stuffed_pumpkin", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.STUFFED_PUMPKIN.get()))
					.addCriterion("honey_glazed_ham", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.HONEY_GLAZED_HAM.get()))
					.addCriterion("shepherds_pie", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.SHEPHERDS_PIE.get()))
					.addCriterion("bacon_and_eggs", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BACON_AND_EGGS.get()))
					.rewards(AdvancementRewards.Builder.experience(200))
					.save(consumer, getNameId("main/master_chef"));
		}

		protected static Advancement.Builder getAdvancement(Advancement parent, ItemLike display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
			return Advancement.Builder.advancement().parent(parent).display(display,
					TextUtils.getTranslation("advancement." + name),
					TextUtils.getTranslation("advancement." + name + ".desc"),
					null, frame, showToast, announceToChat, hidden);
		}

		private String getNameId(String id) {
			return FarmersDelight.MODID + ":" + id;
		}
	}
}
