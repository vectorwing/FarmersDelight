
/*
Farmers Delight Cutting Board

An important thing to note is that if you are passing in a sound parameter, which is Optional, you need to specify the FULL ResourceLocation:

`"minecraft:block.gravel.break"` is the same as `"block.gravel.break"` since the default modid is `minecraft`, but if you want to reference custom or modded sounds, you'll need to specify the modid.

*/

<recipetype:farmersdelight:cutting>.addRecipe("cutting_board_test", <item:minecraft:gravel>, [<item:minecraft:flint>], <item:minecraft:string>, "minecraft:block.gravel.break");

<recipetype:farmersdelight:cutting>.addRecipe("wool_to_string", <tag:items:minecraft:wool>, [<item:minecraft:string> * 2], <item:minecraft:shears>);

<recipetype:farmersdelight:cutting>.remove(<item:minecraft:leather>);

