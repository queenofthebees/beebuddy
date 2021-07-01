package nomble.beebuddy.duck;

import java.util.Optional;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;

public interface IFriendlyPlayer{
    public NbtCompound beebuddy$getHeadFriendNbt();
    public boolean beebuddy$hasHeadFriend();
    public void beebuddy$setHeadFriendNbt(NbtCompound t);
    public boolean beebuddy$makeHeadFriend(BeeEntity bee);
}
