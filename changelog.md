# Changelog

## 0.2.1
(1.16.1)
- Netherite Knife added to knife tags
- Fixed Wild Crop hitbox
- Fixed weird drinking animation for milk bottle and hot cocoa on third person
- Added Soul Fire to tray_heat_sources

## 0.2.0 - Please Read!
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