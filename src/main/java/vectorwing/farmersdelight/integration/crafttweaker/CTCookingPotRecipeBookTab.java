package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.BracketEnum;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;


@Document("mods/FarmersDelight/CookingPotRecipeBookTab")
@ZenRegister
@BracketEnum("farmersdelight:cooking_pot_recipe_book_tab")
@NativeTypeRegistration(zenCodeName = "mods.farmersdelight.CookingPotRecipeBookTab", value = CookingPotRecipeBookTab.class)
public class CTCookingPotRecipeBookTab
{
}
