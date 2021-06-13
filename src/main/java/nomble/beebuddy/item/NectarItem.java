package nomble.beebuddy.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import nomble.beebuddy.duck.IFriendlyBee;

public class NectarItem extends Item{
    private final String type;

    public NectarItem(String t, Item.Settings settings){
        super(settings);
        type = t;
    }

    public String getType(){
        return type;
    }
}
