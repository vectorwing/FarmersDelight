import crafttweaker.api.recipe.replacement.type.ManagerFilteringRule;
import crafttweaker.api.recipe.replacement.Replacer;
import crafttweaker.api.recipe.type.Recipe;
import crafttweaker.api.world.Container;
import crafttweaker.api.util.random.Percentaged;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.ingredient.IIngredient;
import stdlib.List;

Replacer.create().filter(ManagerFilteringRule.of(<recipetype:farmersdelight:cooking>)).replace<IItemStack>(<recipecomponent:farmersdelight:recipe_component/cooking_pot_container>, <item:minecraft:oak_leaves>, <item:minecraft:stone>).execute();

Replacer.create().replace<IIngredient>(<recipecomponent:farmersdelight:recipe_component/cutting_board_tool>, <tag:items:forge:tools/knives>, <tag:items:forge:tools/axes>).execute();

// Replacer.create().filter(ManagerFilteringRule.of(<recipetype:farmersdelight:cutting>)).replace<List<Percentaged<IItemStack>>>(<recipecomponent:crafttweaker:output/chanced_items>, [<item:minecraft:white_dye>*2 % 100], [<item:minecraft:white_dye> % 60, <item:minecraft:white_dye> % 30]).execute();