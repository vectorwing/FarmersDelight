# Changelog

## 0.2.4b
- Fix: Rice and Tomato Seeds had inverted tags;
- Fix: Game crash when using a Bucket on the bottom half of a rice crop;
- Fix: Cutting Board now uses the proper Forge Tag for shears;
- Fix: Wild Crops couldn't be sheared by modded shears;
- Change the amount of rice dropped from the crop, to match the redesign ratios;
- Add Rice Bale as an ingredient for Horse Feed;
- Make Raw Pasta edible... at a risk;
- Add: Spanish translation (thanks to tild09!)
- Update the looks on the mod's logo.

## 0.2.4a
- Fix: Wrong Pair import when getting Dog Food tooltip.

## 0.2.4
- The Rice crop has been redesigned!
  - The top half of the crop can now be harvested individually. The bottom half will stay planted, and eventually regrow a top half;
  - Bottom half yields a single pile of Rice when broken;
  - Top half is a standard crop, which yields Rice Panicles when fully grown, or Rice/Straw when harvested with a Knife;
  - Fully compatible with vanilla pistons, mechanical automation methods, Quark's simple harvesting, Immersive Engineering's Garden Cloche, and Botany Pots;
  - Existing Rice crops planted in the world will remain as the old form for compatibility. Simply harvest them and replant rice to get the new form!
- Wild Rice now generates in Swamps, Jungles and Bamboo Jungles:
  - Somewhat more common than other wild crops, due to the rarity of these biomes;
  - Yields a single pile of Rice, and can be sheared.
- Seed-bearing Wild Crops (cabbage, beet and tomato) have a small chance of dropping an edible produce when harvested;
- Wild Crop bone meal action temporarily removed due to an exploit;
- Fixed Dog Food:
  - When fed to your pet wolf, it fully heals them and grants them Speed, Strength and Resistance;
- Added Horse Feed:
  - When fed to any tamed ridable mob (horse, donkey, llama etc.), it fully heals them and grants them Speed II and Jump Boost;
  - Does not work for Pigs and Striders, as they cannot be tamed;
- When players have the Nourished effect, the hunger bar will look gilded and shiny:
  - The gilded coating will visually "peel in half" when the player is spending saturation to heal damage;
  - This overlay can be disabled in the new "farmnersdelight-client.toml" config;
- If a bell has ropes hanging from under it, players can ring the bell by right-clicking the rope column (24 blocks max.);
- Some JEI quality-of-life features:
  - Press "uses" (U) on a Stove to see the Campfire Cooking recipe tab;
  - Press "uses" (U) on a container item (e.g. Bowl) to see which Cooking Pot recipes it can be served on;
  - Cooking Pot recipes now have a "Move Items" transfer button when checking recipes on it;
- Graphics updates for some blocks and items;
- Resource pack makers: I was previously using 'textures/blocks' instead of 'textures/block'. This version fixes it, so you may have to change it too.
- Translation updates:
  - Updated Chinese (thanks to WuzgXY-GitHub, TUsama and ChillirCR!);
  - Updated German (thanks to SebastianD334 and Moralle!);
  - Added Korean (thanks to qkrehf2!);
  - Added Russian (thanks to GrayPix and Kanspirians!);
  - Added French (thanks to Derrias!);
  - Added Japanese (thanks to FEMT1915!).

## 0.2.3
1.16.3+
- Re-implemented world generation to the new .json system:
  - Configs for wild patches have been changed, and are using new defaults;
  - Beach wild patches (Wild Cabbage and Sea Beet) now use a different weight pattern, so values are similar to other crops;
  - These two should change themselves on all instances, but we advise all players and modpack makers to reset their configs once, if migrating.
  
1.15.2+
- Adds Compost Heaps to village generation
  - These structures house heaps of Organic Compost, which farmers lay down to decompose into Rich Soil. Each biome has a different composting strategy based on their biome and climate!
  - Some of them have small farms mixed in, or examples of other mechanics;
  - They often have a few blocks of Rich Soil ready for taking!
- Adds 3 new meals: Cooked Rice, Grilled Salmon and Baked Cod Stew;
- Adds Tatami, a traditional flooring based on straw and canvas:
  - Tatami Blocks place as a "half-mat" by default. Placing another tatami against them will complete a "full-mat" pair! You can sneak-place to suppress pairing;
  - Tatami Mats are carpet-thin, come in Full and Half, and can be interchanged to and fro via crafting. They have a simpler placement and behave like carpets.
  - Thanks to Vazkii for sharing some placement code with me! A similar block to Tatami should soon be in Quark as well!
- Rename my "unofficial" Forge tool tags to be fully plural;
- Improves configs and integration options for modpack makers
- Prettifies the mod's meta files, and the info on the Mods page of Minecraft
- Makes Wild Patches non-replaceable like Tall Grass, closes issue #70
- Fixes containers vanishing if used to right-click an empty pot, closes issue #71
- Removes knives from being a crafting ingredient, delegating it to the Cutting Board if needed
  - Fixes Grindstone repairing for knives, and some duplication cases with other mods
- More extensions to IE and MCAbnormals integration;
- Straw harvesting now checks "farmersdelight:straw_harvesters" for items that can harvest straw from grassy plants.

## 0.2.2
1.16.1+
- Added Crimson and Warped Pantries;
- Added Netherite tools to tags, so they can be used on cutting boards.
1.15.2+
- Fix server crash when crafting any recipes using knives;
- Fix server crash when trying to load knife enchantments;
- (Grimmauld) Fix shears' default dispenser behaviour being overriden, and enhances behaviour compatibilities overall;
- Cooking Pot can now throw containers high enough to enter a hopper to its side;
- Add more compostables;
- Add several new salvaging recipes using the Cutting Board;
- Cabbage can now be portioned into 2 Cabbage Leaves for efficiency;
- Fixes Rich Soil Farmland decaying into normal Dirt if not hydrated;
- Adds up license definition in mods.toml;
- Mod Integrations, Volume 1:
    - Botany Pots: crops and soils;
    - Immersive Engineering: Garden Cloche integration;
    - MCAbnormals: meals for the Cooking Pot;
    - Create: milling and mixing for some items.

## 0.2.1
1.16.1+
- Netherite Knife added to knife tags;
- Added Soul Fire to tray_heat_sources.
1.15.2+
- Add the ability to smelt metal Knives for nuggets;
- Fix Wild Crops occasionally spawning themselves as items spontaneously;
- Fix drinking animation for Milk Bottle and Hot Cocoa on third person.

## 0.2.0 - Please Read!
1.16.1+
- Added Netherite Knife;
- Added Nether Salad;
1.15.2+
- Mulch has been renamed and remapped to Rich Soil - a more sensible name for its function.
  - BACKUP YOUR WORLDS!! The mod will try to remap existing mulch, so you won't lose your progress.
  - Some errors from other mods on world load might interrupt the remapping. If anything wrong happens, address the errors and try again with a backup.
  - Treat this as a breaking change. While existing mulch will usually be remapped safely, it's better to start a new world if you're not sure.
- Adds the Cutting Board - a hard surface to split items apart using certain tools
  - Tool items can be carved into the board for display by sneak-right-clicking;
  - Knives can portion meats and some fruits, to be more efficient when cooking meals;
  - Pickaxes can separate brick blocks back into individual bricks;
  - Axes can strip logs and harvest the removed tree bark, useful for composting;
    - With this change, Knives are no longer able to strip logs for bark;
  - Shears can salvage Saddles. Maybe more later!
  - Current recipes are proofs of concept; we plan to add more in the future!
- Adds Pantries - decorative storage blocks for every wood type;
- Adds Pies - placeable desserts made from often overlooked sweet ingredients!
  - Apple Pie, Sweet Berry Cheesecake and Chocolate Pie;
  - They have 4 slices, can be eaten by hand, or sliced off with Knives;
  - All pies provide a brief boost in Speed;
  - Pumpkin Pie compatibility planned for the future.
- Pumpkins can now be cut into 4 edible slices, to contrast with a Melon's 9 slices;
- Several tagging changes, both mod-scope and Forge-scope, to allow more customization;
- Many blocks in Farmer's Delight now send comparator signals;
- Flammable blocks and items are now fuels!
- New food effect: Comfort
  - Makes the user immune to cold and sickness (Hunger, Slowness and Weakness), and instantly heals these effects when obtained;
  - Provided by soups and stews, which are traditionally warm and wintry foods;
- Small tweaks to Tomato crop harvesting;
- Adds a few strong effects to vanilla soup items, to make them more interesting to consume;
- Wild Crops can now be multiplied by bone-mealing, at a small chance of success;
- General asset improvements.

## 0.1.5
- Cooking Pot now allows optional container overrides at recipe .json level:
  - Stuffed Pumpkin now uses that to have Pumpkins as containers;
  - All vanilla bowl foods now use it to have bowls as containers (they have no container in code);
  - Foods without containers in code or in .json just drop to output (e.g. Dumplings);
- Spent some time "sharpening" the Knives:
  - Fixed duplication bug when repairing them;
  - Knives are now able to receive a selection of weapon and tool enchantments;
  - Knives are now effective tools for cloth, cake and some plant materials;
  - Knives now have an exclusive enchantment: Backstabbing!
    - Multiplies damage when striking a mob from behind: 1.4x, 1.8x and 2.2x at levels I, II and III respectively;
  - Knives now lose only 1 durability point when striking mobs;
- Stove now has a cozy flame animation;
- Fixed Sea Beets dropping an unfarmable Beetroot instead of their seeds.

## 0.1.4a
- Fixes Crates lacking hardness and loot tables. Woops!

## 0.1.4
- Several fixes to the Cooking Pot:
  - It can now be automated: top side receives ingredients, sides can add containers and remove servings;
  - General performance fixes
  - Placeholder icon disappears if slot is filled
  - Better management of recipes and items
  - Now emits bubbling sounds! :D
- Added new foods and meals to make;
- Added Hot Cocoa, which removes 1 random negative effect when drinked;
- Farmer Villagers now buy the mod's basic crops (configurable);
- Bees will now seek wild crops, and can enter Love Mode with them;
- Added Crates for the basic crops, and reworked the Rice Bale
- Eating hearty meals now gives you the Nourished buff:
  - Constantly removes exhaustion unless you're spending saturation to heal damage;
- Mixed Salad now gives a short burst of Regeneration

## 0.1.3
- Adds Organic Compost, a nice sink for all these monster drops you have:
  - Organic Compost, when placed, will slowly decompose to Mulch over time;
  - Does so quicker if away from light, and close to mushrooms;
- Adds Mulch, a fertile soil. Can be tilled, cannot be trampled, and boosts crops planted on it;
  - Mushrooms planted on Mulch will begin growing Colonies of up to 5 mushrooms in the same spot;
- Rice now drops Rice Panicles if harvested without a knife. They can be crafted alone to get Rice;
- Adds Rice Bales to mass-store panicles;
- Knives can now strip logs to obtain Tree Bark, but will spend twice the durability to do so;
- Fixed a few bugs with the Stove;
- Fixed multiple bugs with the Cooking Pot;
- Added some utility recipes.

## 0.1.2
- Adds some Forge Tags to multiple items, so you can interchange with other food mods;
- Crafting and cooking recipes from this mod now use tags for most items as well;
- Minor fixes.

## 0.1.1
- Fixes conflict with other mods upon injecting chest loot tables;
- Returns Canvas to a 2x2 recipe instead of 3x3;
- Add basic tags to FD crops, seeds and milk.

## 0.1.0
- Initial release
- Adds the Stove - Upgraded, retrievable Campfire with six cooking slots;
- Adds the Cooking Pot - Portable workstation for preparing and mass-storing container foods (meals);
- Adds the Basket - Barrel that catches items landing on its open face, and can face any direction;
- Adds Knives - scavenging and cooking tools for retrieving more loot from plants and animals;
    - Knives can cut Cake into Slices of Cake - 1 for each remaining bite;
    - Slices of Cake can be crafted back into a Cake;
- Adds 4 new crops: Cabbage, Tomato, Onion and Rice;
- Generates Wild Patches for added crops and vanilla crops, spawning across the world on occasion;
- Adds multiple new foods to prepare using vanilla and Farmer's Delight ingredients;
- Adds Straw - a building material with a few current uses, and future ones planned;
- Adds Ropes - non-solid, deployable ladder blocks;
- Adds Safety Net - negates fall damage and bounces entities slightly;
- Adds JEI integration.
