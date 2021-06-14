package nomble.beebuddy.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

import nomble.beebuddy.duck.IFriendlyPlayer;
import nomble.beebuddy.mixin.invoker.EntityInvoker;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
                                        implements IFriendlyPlayer{
    public PlayerEntityMixin(EntityType<? extends LivingEntity> t, World w){
        super(t, w);
    }



    @Unique
    private static final TrackedData<CompoundTag> beebuddy$HEADFRIEND
        = DataTracker.registerData( PlayerEntity.class
                                  , TrackedDataHandlerRegistry.TAG_COMPOUND);



    @Shadow
    private long shoulderEntityAddedTime;



    @Override
    @Unique
    public CompoundTag beebuddy$getHeadFriendNbt(){
        return (CompoundTag)this.dataTracker.get(beebuddy$HEADFRIEND);
    }
    @Override
    @Unique
    public boolean beebuddy$hasHeadFriend(){
        return !beebuddy$getHeadFriendNbt().isEmpty();
    }

    @Override
    @Unique
    public void beebuddy$setHeadFriendNbt(CompoundTag t){
        this.dataTracker.set(beebuddy$HEADFRIEND, t);
    }
    @Override
    @Unique
    public boolean beebuddy$makeHeadFriend(BeeEntity bee){
        if(this.hasVehicle() || !this.onGround || this.isTouchingWater()){
            return false;
        }
        if(beebuddy$hasHeadFriend()){
            return false;
        }
        CompoundTag t = new CompoundTag();
        EntityInvoker inv = (EntityInvoker)(Object)bee;
        t.putString("id", inv.beebuddy$getSavedEntityId());
        bee.toTag(t);
        beebuddy$setHeadFriendNbt(t);
        bee.remove();
        this.shoulderEntityAddedTime = this.world.getTime();
        return true;
    }



    @Shadow
    private void dropShoulderEntity(CompoundTag entityNbt){}



    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initHeadTracker(CallbackInfo cbi){
        this.dataTracker.startTracking(beebuddy$HEADFRIEND, new CompoundTag());
    }

    @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
    private void writeHeadFriend(CompoundTag tag, CallbackInfo cbi){
        if(beebuddy$hasHeadFriend()){
            tag.put("HeadFriend", beebuddy$getHeadFriendNbt());
        }
    }
    @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
    private void readHeadFriend(CompoundTag tag, CallbackInfo cbi){
        if(tag.contains("HeadFriend", 10)){
            beebuddy$setHeadFriendNbt(tag.getCompound("HeadFriend"));
        }
    }

    @Inject(method = "dropShoulderEntities", at = @At("TAIL"))
    private void dropHeadFriend(CallbackInfo cbi){
        if(shoulderEntityAddedTime + 20L < this.world.getTime()){
            dropShoulderEntity(beebuddy$getHeadFriendNbt());
            beebuddy$setHeadFriendNbt(new CompoundTag());
        }
    }
}
