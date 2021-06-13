package nomble.beebuddy.duck;

import java.util.Optional;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.CompoundTag;

public interface IFriendlyPlayer{
    public CompoundTag beebuddy$getHeadFriendNbt();
    public boolean beebuddy$hasHeadFriend();
    public void beebuddy$setHeadFriendNbt(CompoundTag t);
    public boolean beebuddy$makeHeadFriend(BeeEntity bee);
}
