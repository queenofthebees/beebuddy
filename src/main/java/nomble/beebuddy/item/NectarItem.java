package nomble.beebuddy.item;

import net.minecraft.item.Item;

public class NectarItem extends Item {
    private final String type;

    public NectarItem(String t, Item.Settings settings) {
        super(settings);
        type = t;
    }

    public String getType() {
        return type;
    }
}
