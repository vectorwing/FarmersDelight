package vectorwing.farmersdelight.utils;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class ForgeTags {
    // GENERAL GROUPS
    public static final ITag.INamedTag<Item> CROPS = forgeTag("crops");
    public static final ITag.INamedTag<Item> SEEDS = forgeTag("seeds");
    public static final ITag.INamedTag<Item> MILK = forgeTag("milk");
    public static final ITag.INamedTag<Item> BREAD = forgeTag("bread");
    public static final ITag.INamedTag<Item> VEGETABLES = forgeTag("vegetables");
    public static final ITag.INamedTag<Item> SALAD_INGREDIENTS = forgeTag("salad_ingredients");

    // TYPES OF ITEMS
    public static final ITag.INamedTag<Item> CROPS_CABBAGE = forgeTag("crops/cabbage");
    public static final ITag.INamedTag<Item> CROPS_TOMATO = forgeTag("crops/tomato");
    public static final ITag.INamedTag<Item> CROPS_ONION = forgeTag("crops/onion");
    public static final ITag.INamedTag<Item> CROPS_RICE = forgeTag("crops/rice");

    public static final ITag.INamedTag<Item> KNIVES = forgeTag("knives");

    public static final ResourceLocation HEAT_SOURCES_TAG = new ResourceLocation(FarmersDelight.MODID, "heat_sources");

    public static final ITag.INamedTag<Item> WOLF_PREY = ItemTags.makeWrapperTag(new ResourceLocation(FarmersDelight.MODID, "wolf_prey").getPath());

    private static ITag.INamedTag<Item> forgeTag(String path) {
        return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).getPath());
    }

    public static void register() {
    }
}
