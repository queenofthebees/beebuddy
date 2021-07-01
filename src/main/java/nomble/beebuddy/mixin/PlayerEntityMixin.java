package nomble.beebuddy.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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
    private static final TrackedData<NbtCompound> beebuddy$HEADFRIEND
        = DataTracker.registerData( PlayerEntity.class
                                  , TrackedDataHandlerRegistry.TAG_COMPOUND);



    @Shadow
    private long shoulderEntityAddedTime;



    @Override
    @Unique
    public NbtCompound beebuddy$getHeadFriendNbt(){
        return (NbtCompound)this.dataTracker.get(beebuddy$HEADFRIEND);
    }
    @Override
    @Unique
    public boolean beebuddy$hasHeadFriend(){
        return !beebuddy$getHeadFriendNbt().isEmpty();
    }

    @Override
    @Unique
    public void beebuddy$setHeadFriendNbt(NbtCompound t){
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
        NbtCompound t = new NbtCompound();
        EntityInvoker inv = (EntityInvoker)(Object)bee;
        t.putString("id", inv.beebuddy$getSavedEntityId());
        bee.writeNbt(t);
        beebuddy$setHeadFriendNbt(t);
        bee.discard();
        this.shoulderEntityAddedTime = this.world.getTime();
        return true;
    }



    @Shadow
    private void dropShoulderEntity(NbtCompound entityNbt){}



    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initHeadTracker(CallbackInfo cbi){
        this.dataTracker.startTracking(beebuddy$HEADFRIEND, new NbtCompound());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeHeadFriend(NbtCompound tag, CallbackInfo cbi){
        if(beebuddy$hasHeadFriend()){
            tag.put("HeadFriend", beebuddy$getHeadFriendNbt());
        }
    }
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readHeadFriend(NbtCompound tag, CallbackInfo cbi){
        if(tag.contains("HeadFriend", 10)){
            beebuddy$setHeadFriendNbt(tag.getCompound("HeadFriend"));
        }
    }

    @Inject(method = "dropShoulderEntities", at = @At("TAIL"))
    private void dropHeadFriend(CallbackInfo cbi){
        if(shoulderEntityAddedTime + 20L < this.world.getTime()){
            dropShoulderEntity(beebuddy$getHeadFriendNbt());
            beebuddy$setHeadFriendNbt(new NbtCompound());
        }
    }
}
