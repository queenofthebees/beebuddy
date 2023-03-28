package nomble.beebuddy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import nomble.beebuddy.item.NectarItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class BeeBuddy implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("beebuddy");

    @Override
    public void onInitialize() {
        Collection<ItemStack> nectars = new ArrayList<>();

        for (String s : new String[]{"ace", "agender", "aro", "bi", "demiboy"
                , "demigirl", "gay", "genderfluid"
                , "genderqueer", "honeyed", "lesbian", "mlm"
                , "nonbinary", "omni", "pan", "poly"
                , "trans", "xenogender"}) {
            Item nectar = new NectarItem(s, new Item.Settings());


            Identifier id = new Identifier("beebuddy", s + "_nectar");


            nectars.add(nectar.getDefaultStack());

            // eh, we aren't really using item references to need to save em
            Registry.register(Registries.ITEM, id, nectar);
        }

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addAfter(Items.SPIDER_EYE, nectars);
        });



        LOGGER.info("bzzzz!");
    }
}
