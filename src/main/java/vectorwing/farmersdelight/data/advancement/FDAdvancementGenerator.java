package vectorwing.farmersdelight.data.advancement;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.advancement.CuttingBoardTrigger;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.registry.ModEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Optional;
import java.util.function.Consumer;

public class FDAdvancementGenerator implements AdvancementProvider.AdvancementGenerator
{
	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
		AdvancementHolder farmersDelight = Advancement.Builder.advancement()
				.display(ModItems.COOKING_POT.get(),
						TextUtils.getTranslation("advancement.root"),
						TextUtils.getTranslation("advancement.root.desc"),
						new ResourceLocation("minecraft:textures/block/bricks.png"),
						FrameType.TASK, false, false, false)
				.addCriterion("seeds", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}))
				.save(consumer, getNameId("main/root"));

		// Harvesting Branch
		AdvancementHolder huntAndGather = getAdvancement(farmersDelight, ModItems.FLINT_KNIFE.get(), "craft_knife", FrameType.TASK, true, true, false)
				.addCriterion("flint_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FLINT_KNIFE.get()))
				.addCriterion("iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.addCriterion("diamond_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DIAMOND_KNIFE.get()))
				.addCriterion("golden_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOLDEN_KNIFE.get()))
				.addCriterion("netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, getNameId("main/craft_knife"));

		AdvancementHolder graspingAtStraws = getAdvancement(huntAndGather, ModItems.STRAW.get(), "harvest_straw", FrameType.TASK, true, false, false)
				.addCriterion("harvest_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.save(consumer, getNameId("main/harvest_straw"));

		AdvancementHolder advancedComposting = getAdvancement(graspingAtStraws, ModItems.ORGANIC_COMPOST.get(), "place_organic_compost", FrameType.TASK, true, false, false)
				.addCriterion("place_organic_compost", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.ORGANIC_COMPOST.get()))
				.save(consumer, getNameId("main/place_organic_compost"));

		AdvancementHolder plantFood = getAdvancement(advancedComposting, ModItems.RICH_SOIL.get(), "get_rich_soil", FrameType.GOAL, true, true, false)
				.addCriterion("get_rich_soil", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICH_SOIL.get()))
				.save(consumer, getNameId("main/get_rich_soil"));

		AdvancementHolder wildButcher = getAdvancement(huntAndGather, ModItems.HAM.get(), "get_ham", FrameType.TASK, true, false, false)
				.addCriterion("ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HAM.get()))
				.addCriterion("smoked_ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SMOKED_HAM.get()))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, getNameId("main/get_ham"));

		AdvancementHolder watchYourFingers = getAdvancement(huntAndGather, ModItems.CUTTING_BOARD.get(), "use_cutting_board", FrameType.TASK, true, false, false)
				.addCriterion("cutting_board", CuttingBoardTrigger.TriggerInstance.simple())
				.save(consumer, getNameId("main/use_cutting_board"));

		AdvancementHolder cantTakeTheHeat = getAdvancement(watchYourFingers, ModItems.NETHERITE_KNIFE.get(), "obtain_netherite_knife", FrameType.CHALLENGE, true, true, false)
				.addCriterion("obtain_netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
				.rewards(AdvancementRewards.Builder.experience(200))
				.save(consumer, getNameId("main/obtain_netherite_knife"));

		// Farming Branch
		AdvancementHolder cropsOfTheWild = getAdvancement(farmersDelight, ModItems.WILD_ONIONS.get(), "get_fd_seed", FrameType.TASK, true, true, false)
				.addCriterion("cabbage_seeds", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CABBAGE_SEEDS.get()))
				.addCriterion("tomato_seeds", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO_SEEDS.get()))
				.addCriterion("onion", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ONION.get()))
				.addCriterion("rice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE.get()))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, getNameId("main/get_fd_seed"));

		AdvancementHolder fungusAmongUs = getAdvancement(cropsOfTheWild, ModItems.RED_MUSHROOM_COLONY.get(), "get_mushroom_colony", FrameType.TASK, true, false, false)
				.addCriterion("brown_mushroom_colony", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BROWN_MUSHROOM_COLONY.get()))
				.addCriterion("red_mushroom_colony", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RED_MUSHROOM_COLONY.get()))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, getNameId("main/get_mushroom_colony"));

		AdvancementHolder dippingYourRoots = getAdvancement(cropsOfTheWild, ModItems.RICE.get(), "plant_rice", FrameType.TASK, true, false, false)
				.addCriterion("plant_rice", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.RICE_CROP.get()))
				.save(consumer, getNameId("main/plant_rice"));

		AdvancementHolder tallmato = getAdvancement(cropsOfTheWild, ModItems.TOMATO.get(), "harvest_ropelogged_tomato", FrameType.TASK, true, false, false)
				.addCriterion("harvest_ropelogged_tomato", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
						LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(ModBlocks.TOMATO_CROP.get()).setProperties(
								StatePropertiesPredicate.Builder.properties()
										.hasProperty(TomatoVineBlock.VINE_AGE, 0)
										.hasProperty(TomatoVineBlock.ROPELOGGED, true)
						)),
						ItemPredicate.Builder.item())
				)
				.save(consumer, getNameId("main/harvest_ropelogged_tomato"));

		AdvancementHolder booHiss = getAdvancement(tallmato, ModItems.ROTTEN_TOMATO.get(), "hit_raider_with_rotten_tomato", FrameType.TASK, true, true, false)
				.addCriterion("hit_raider_with_rotten_tomato", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(
						Optional.of(DamagePredicate.Builder.damageInstance()
								.type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of(ModEntityTypes.ROTTEN_TOMATO.get()))).build()),
						Optional.of(EntityPredicate.Builder.entity().of(EntityTypeTags.RAIDERS).build())))
				.save(consumer, getNameId("main/hit_raider_with_rotten_tomato"));

		AdvancementHolder cropRotation = getAdvancement(dippingYourRoots, ModItems.CABBAGE.get(), "plant_all_crops", FrameType.CHALLENGE, true, true, false)
				.addCriterion("wheat", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.WHEAT))
				.addCriterion("beetroot", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS))
				.addCriterion("carrot", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.CARROTS))
				.addCriterion("potato", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.POTATOES))
				.addCriterion("cabbage", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.CABBAGE_CROP.get()))
				.addCriterion("tomato", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.BUDDING_TOMATO_CROP.get()))
				.addCriterion("onion", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.ONION_CROP.get()))
				.addCriterion("rice", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.RICE_CROP.get()))
				.addCriterion("melon", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM))
				.addCriterion("pumpkin", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM))
				.addCriterion("sweet_berries", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.SWEET_BERRY_BUSH))
				.addCriterion("sugar_cane", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.SUGAR_CANE))
				.addCriterion("kelp", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.KELP))
				.addCriterion("cocoa", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.COCOA))
				.addCriterion("nether_wart", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART))
				.addCriterion("chorus_flower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.CHORUS_FLOWER))
				.addCriterion("brown_mushroom", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.BROWN_MUSHROOM))
				.addCriterion("red_mushroom", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.RED_MUSHROOM))
				.addCriterion("glow_berries", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.CAVE_VINES))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(consumer, getNameId("main/plant_all_crops"));

		// Cooking Branch
		AdvancementHolder bonfireLit = getAdvancement(farmersDelight, Blocks.CAMPFIRE, "place_campfire", FrameType.TASK, true, true, false)
				.addCriterion("campfire", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.CAMPFIRE))
				.addCriterion("soul_campfire", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.SOUL_CAMPFIRE))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, getNameId("main/place_campfire"));

		AdvancementHolder portableCooking = getAdvancement(bonfireLit, ModItems.SKILLET.get(), "use_skillet", FrameType.TASK, true, false, false)
				.addCriterion("skillet", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.SKILLET.get()))
				.save(consumer, getNameId("main/use_skillet"));

		AdvancementHolder sizzlingHot = getAdvancement(portableCooking, ModItems.SKILLET.get(), "place_skillet", FrameType.TASK, true, false, false)
				.addCriterion("skillet", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.SKILLET.get()))
				.save(consumer, getNameId("main/place_skillet"));

		AdvancementHolder dinnerIsServed = getAdvancement(bonfireLit, ModItems.COOKING_POT.get(), "place_cooking_pot", FrameType.GOAL, true, true, false)
				.addCriterion("cooking_pot", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.COOKING_POT.get()))
				.save(consumer, getNameId("main/place_cooking_pot"));

		AdvancementHolder comforting = getAdvancement(dinnerIsServed, ModItems.BAKED_COD_STEW.get(), "eat_comfort_food", FrameType.TASK, true, false, false)
				.addCriterion("comfort", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(ModEffects.COMFORT.get())))
				.save(consumer, getNameId("main/eat_comfort_food"));

		AdvancementHolder nourishing = getAdvancement(comforting, ModItems.STEAK_AND_POTATOES.get(), "eat_nourishing_food", FrameType.TASK, true, false, false)
				.addCriterion("nourishment", EffectsChangedTrigger.TriggerInstance.hasEffects(MobEffectsPredicate.Builder.effects().and(ModEffects.NOURISHMENT.get())))
				.save(consumer, getNameId("main/eat_nourishing_food"));

		AdvancementHolder gloriousFeast = getAdvancement(nourishing, ModItems.ROAST_CHICKEN_BLOCK.get(), "place_feast", FrameType.TASK, true, true, false)
				.addCriterion("roast_chicken", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.ROAST_CHICKEN_BLOCK.get()))
				.addCriterion("stuffed_pumpkin", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.STUFFED_PUMPKIN_BLOCK.get()))
				.addCriterion("honey_glazed_ham", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get()))
				.addCriterion("shepherds_pie", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.SHEPHERDS_PIE_BLOCK.get()))
				.addCriterion("rice_roll_medley", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get()))
				.requirements(AdvancementRequirements.Strategy.OR)
				.save(consumer, getNameId("main/place_feast"));

		AdvancementHolder masterChef = getAdvancement(gloriousFeast, ModItems.HONEY_GLAZED_HAM.get(), "master_chef", FrameType.CHALLENGE, true, true, false)
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
				.addCriterion("mushroom_rice", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.MUSHROOM_RICE.get()))
				.rewards(AdvancementRewards.Builder.experience(200))
				.save(consumer, getNameId("main/master_chef"));
	}

	protected static Advancement.Builder getAdvancement(AdvancementHolder parent, ItemLike display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(parent).display(display,
				TextUtils.getTranslation("advancement." + name),
				TextUtils.getTranslation("advancement." + name + ".desc"),
				null, frame, showToast, announceToChat, hidden);
	}

	private String getNameId(String id) {
		return FarmersDelight.MODID + ":" + id;
	}
}
