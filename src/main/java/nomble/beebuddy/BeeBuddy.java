package nomble.beebuddy;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import nomble.beebuddy.item.NectarItem;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class BeeBuddy implements ModInitializer{
    public static final Logger LOGGER = LogManager.getLogger("beebuddy");

    @Override
    public void onInitialize(){
        for(String s : new String[] { "ace", "aro", "bi", "gay", "lesbian"
                                    , "nonbinary", "trans", "pan"          }){
            Item it = new NectarItem(s, new Item.Settings()
                                                .group(ItemGroup.MISC));
            Identifier id = new Identifier("beebuddy", s + "_nectar");
            // eh, we aren't really using item references to need to save em
            Registry.register(Registry.ITEM, id, it);
        }

        LOGGER.info("bzzzz!");
    }
}
