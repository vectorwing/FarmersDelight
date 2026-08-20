package vectorwing.farmersdelight.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public final class FDRecipeSyncEvents
{
    private FDRecipeSyncEvents() {}

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(
                ModRecipeTypes.COOKING.get(),
                ModRecipeTypes.CUTTING.get()
        );
    }
}
