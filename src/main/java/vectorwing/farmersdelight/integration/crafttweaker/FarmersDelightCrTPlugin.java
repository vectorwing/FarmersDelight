package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.plugin.CraftTweakerPlugin;
import com.blamejared.crafttweaker.api.plugin.ICraftTweakerPlugin;
import com.blamejared.crafttweaker.api.plugin.IRecipeComponentRegistrationHandler;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.integration.crafttweaker.handlers.RecipeHandlerUtils;

@CraftTweakerPlugin(FarmersDelight.MODID + ":crafttweaker_plugin")
public class FarmersDelightCrTPlugin implements ICraftTweakerPlugin {

    public static final Logger LOGGER_CT = CraftTweakerAPI.getLogger("FarmersDelight");

    @Override
    public void registerRecipeComponents(IRecipeComponentRegistrationHandler handler) {
        handler.registerRecipeComponent(RecipeHandlerUtils.SOUND_COMPONENT);
        handler.registerRecipeComponent(RecipeHandlerUtils.CONTAINER_COMPONENT);
        handler.registerRecipeComponent(RecipeHandlerUtils.TOOL_COMPONENT);
        handler.registerRecipeComponent(RecipeHandlerUtils.COOKING_TAB_COMPONENT);
    }
}
