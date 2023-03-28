package nomble.beebuddy.mixin;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nomble.beebuddy.duck.IFriendlyBee;
import nomble.beebuddy.duck.IGoalRemover;
import nomble.beebuddy.entity.ai.goal.BeeSitGoal;
import nomble.beebuddy.entity.ai.goal.FollowFriendGoal;
import nomble.beebuddy.entity.ai.goal.SitOnHeadGoal;
import nomble.beebuddy.item.NectarItem;
import nomble.beebuddy.mixin.invoker.BeeWanderAroundGoalInvoker;
import nomble.beebuddy.mixin.invoker.MoveToFlowerGoalInvoker;
import nomble.beebuddy.mixin.invoker.PollinateGoalInvoker;
import nomble.beebuddy.mixin.invoker.StingGoalInvoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntityMixin
        implements IFriendlyBee {
    @Unique
    private static final TrackedData<Byte> beebuddy$TAMEFLAGS
            = DataTracker.registerData(BeeEntity.class
            , TrackedDataHandlerRegistry.BYTE);
    @Unique
    private static final TrackedData<Optional<UUID>> beebuddy$OWNER
            = DataTracker.registerData(BeeEntity.class
            , TrackedDataHandlerRegistry.OPTIONAL_UUID);
    @Unique
    private static final TrackedData<String> beebuddy$NECTAR
            = DataTracker.registerData(BeeEntity.class
            , TrackedDataHandlerRegistry.STRING);
    @Unique
    private Optional<PlayerEntity> beebuddy$friendCache = Optional.empty();
    @Shadow
    private BeeEntity.PollinateGoal pollinateGoal;
    @Shadow
    private BeeEntity.MoveToFlowerGoal moveToFlowerGoal;
    @Shadow
    private BlockPos hivePos;
    @Shadow
    private int ticksInsideWater;
    public BeeEntityMixin(EntityType<? extends PassiveEntity> t, World w) {
        super(t, w);
    }

    @Invoker
    public abstract void callSetHasNectar(boolean hasNectar);


    @Unique
    private boolean beebuddy$hasFriend() {
        return (this.dataTracker.get(beebuddy$TAMEFLAGS) & 4) != 0;
    }

    @Override
    @Unique
    public Optional<UUID> beebuddy$getFriend() {
        return this.dataTracker.get(beebuddy$OWNER);
    }

    @Override
    @Unique
    public Optional<PlayerEntity> beebuddy$getFriendPlayer() {
        if (beebuddy$friendCache.isEmpty()) {
            Optional<UUID> u = beebuddy$getFriend();
            beebuddy$friendCache = u.map(this.world::getPlayerByUuid);
        }
        return beebuddy$friendCache;
    }

    @Override
    @Unique
    public void beebuddy$setFriend(Optional<UUID> friend) {
        this.dataTracker.set(beebuddy$OWNER, friend);
    }


    @Override
    @Unique
    public boolean beebuddy$isSitting() {
        return (this.dataTracker.get(beebuddy$TAMEFLAGS) & 1) != 0;
    }

    @Unique
    private void beebuddy$setSitting(boolean b) {
        int s = this.dataTracker.get(beebuddy$TAMEFLAGS) & -2;
        s |= b ? 1 : 0;
        this.dataTracker.set(beebuddy$TAMEFLAGS, (byte) s);
    }

    @Override
    @Unique
    public String beebuddy$getNectarType() {
        return this.dataTracker.get(beebuddy$NECTAR);
    }

    @Override
    @Unique
    public void beebuddy$setNectarType(String type) {
        this.dataTracker.set(beebuddy$NECTAR, type);
    }

    @Override
    @Unique
    public boolean beebuddy$getPollinateGoalRunning() {
        return this.goalSelector.getRunningGoals()
                .map(PrioritizedGoal::getGoal)
                .filter(g -> g == pollinateGoal).count() > 0L;
    }

    @Override
    @Unique
    public boolean beebuddy$getMoveToFlowerGoalRunning() {
        return this.goalSelector.getRunningGoals()
                .map(PrioritizedGoal::getGoal)
                .filter(g -> g == moveToFlowerGoal).count() > 0L;
    }

    @Unique
    private void beebuddy$initTamedGoals() {
        BeeEntity us = (BeeEntity) (Object) this;

        this.goalSelector.add(0, new BeeSitGoal(us));
        this.goalSelector
                .add(1, StingGoalInvoker.beebuddy$make(us, us, 1.399999976158142D
                        , true));
        this.goalSelector.add(2, new FollowParentGoal(us, 1.25D));
        this.goalSelector.add(3, new FollowFriendGoal(us));
        this.goalSelector.add(4, new AnimalMateGoal(us, 1.0D));
        if (pollinateGoal == null) { // we can be called before initGoals's body
            pollinateGoal = PollinateGoalInvoker.beebuddy$make(us);
        }
        this.goalSelector.add(5, pollinateGoal);
        if (moveToFlowerGoal == null) {
            moveToFlowerGoal = MoveToFlowerGoalInvoker.beebuddy$make(us);
        }
        this.goalSelector.add(6, moveToFlowerGoal);
        this.goalSelector.add(7, new SitOnHeadGoal(us));
        this.goalSelector.add(7, BeeWanderAroundGoalInvoker.beebuddy$make(us));
        this.goalSelector.add(8, new LookAtEntityGoal(us, PlayerEntity.class
                , 4.0F));
        this.goalSelector.add(8, new SwimGoal(us));

        BeeEntityMixin mix = this;
        this.targetSelector.add(1, (new RevengeGoal(this) {
            @Override
            public boolean canStart() {
                if (!super.canStart()) {
                    return false;
                }
                if (us.getAttacker() != null) {
                    UUID u = us.getAttacker().getUuid();
                    return !u.equals(mix.beebuddy$getFriend());
                } else {
                    return true;
                }
            }

            @Override
            public boolean shouldContinue() {
                return us.hasAngerTime() && super.shouldContinue();
            }
        }).setGroupRevenge());
    }

    @Override
    @Unique
    public void beebuddy$friendify() {
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                .setBaseValue(42.0D);
        this.setHealth(42.0F);
        ((IGoalRemover) this.goalSelector).beebuddy$clear();
        ((IGoalRemover) this.targetSelector).beebuddy$clear();
        beebuddy$setSitting(true);
        beebuddy$initTamedGoals();
    }


    @Inject(method = "initGoals", at = @At("HEAD"), cancellable = true)
    private void changeGoalsIfFriend(CallbackInfo cbi) {
        if (beebuddy$hasFriend()) {
            beebuddy$initTamedGoals();
            cbi.cancel();
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initFriendTracker(CallbackInfo cbi) {
        this.dataTracker.startTracking(beebuddy$TAMEFLAGS, (byte) 0);
        this.dataTracker.startTracking(beebuddy$OWNER, Optional.empty());
        this.dataTracker.startTracking(beebuddy$NECTAR, "default");
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeFriendship(NbtCompound tag, CallbackInfo cbi) {
        beebuddy$getFriend().ifPresent(u -> tag.putUuid("Owner", u));
        tag.putBoolean("Sitting", beebuddy$isSitting());
        tag.putString("BeeBuddyNectar", beebuddy$getNectarType());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readFriendship(NbtCompound tag, CallbackInfo cbi) {
        byte tame = 0;
        UUID u = null;
        if (tag.containsUuid("Owner")) {
            u = tag.getUuid("Owner");
            tame |= 4;
        } else if (tag.contains("Owner")) {
            String s = tag.getString("Owner");
            u = ServerConfigHandler.getPlayerUuidByName(this.getServer(), s);
            tame |= 4;
        }

        if (tag.getBoolean("Sitting")) {
            tame |= 1;
        }
        beebuddy$setFriend(Optional.ofNullable(u));
        this.dataTracker.set(beebuddy$TAMEFLAGS, tame);
        if (tag.contains("BeeBuddyNectar")) {
            beebuddy$setNectarType(tag.getString("BeeBuddyNectar"));
        }

        if (!this.world.isClient) {
            ((IGoalRemover) this.goalSelector).beebuddy$clear();
            ((IGoalRemover) this.targetSelector).beebuddy$clear();
            initGoals();
        }
    }


    @Inject(method = "hasStung", at = @At("HEAD"), cancellable = true)
    private void cannotSting(CallbackInfoReturnable<Boolean> cbir) {
        if (beebuddy$hasFriend()) {
            cbir.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void standToDamage(DamageSource src, float a
            , CallbackInfoReturnable<Boolean> cbir) {
        if (!this.isInvulnerableTo(src)) {
            beebuddy$setSitting(false);
        }
    }


    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void friendIsHive(CallbackInfo cbi) {
        if (this.age % 20 == 0) {
            beebuddy$getFriendPlayer().ifPresent(f -> {
                hivePos = f.getBlockPos();
            });
        }
    }

    @Inject(method = "isHiveValid", at = @At("HEAD"), cancellable = true)
    private void friendIsValid(CallbackInfoReturnable<Boolean> cbir) {
        if (beebuddy$hasFriend()) {
            cbir.setReturnValue(hivePos != null);
        }
    }

    @Inject(method = "hasHive", at = @At("HEAD"), cancellable = true)
    private void dontEnterFriend(CallbackInfoReturnable<Boolean> cbir) {
        if (beebuddy$hasFriend()) {
            cbir.setReturnValue(false);
        }
    }


    @Inject(method = "createChild", at = @At("HEAD"), cancellable = true)
    private void makePrideChild(ServerWorld world, PassiveEntity entity
            , CallbackInfoReturnable<BeeEntity> cbir) {
        if (entity instanceof BeeEntity) {
            IFriendlyBee partner = (IFriendlyBee) entity;
            BeeEntity child = EntityType.BEE.create(world);
            IFriendlyBee fchild = (IFriendlyBee) child;

            String ours = beebuddy$getNectarType();
            String theirs = partner.beebuddy$getNectarType();
            fchild.beebuddy$setNectarType(this.random.nextBoolean() ? ours
                    : theirs);
            if (beebuddy$getFriend().equals(partner.beebuddy$getFriend())) {
                beebuddy$getFriend().ifPresent(u -> {
                    fchild.beebuddy$setFriend(Optional.of(u));
                    fchild.beebuddy$friendify();
                });
            }

            cbir.setReturnValue(child);
        }
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void doFluidLiquid(CallbackInfo cbi) {
        if (beebuddy$getNectarType().equals("genderfluid")) {
            ticksInsideWater = 0;
        }
    }


    @Override
    protected void tame(PlayerEntity player, Hand hand
            , CallbackInfoReturnable<ActionResult> cbir) {
        ItemStack stack = player.getStackInHand(hand);
        boolean friend = beebuddy$getFriend().map(player.getUuid()::equals)
                .orElse(false);
        if (!this.world.isClient) {
            if (stack.getItem() == Items.HONEY_BOTTLE) {
                if (friend && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    this.heal(this.getMaxHealth());
                    cbir.setReturnValue(ActionResult.SUCCESS);
                } else if (!friend && !beebuddy$hasFriend()) {
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                        ItemStack g = new ItemStack(Items.GLASS_BOTTLE);
                        if (stack.isEmpty()) {
                            player.setStackInHand(hand, g);
                        } else if (!player.getInventory().insertStack(g)) {
                            player.dropItem(g, false);
                        }
                    }
                    if (this.random.nextInt(4) == 0) {
                        beebuddy$setFriend(Optional.of(player.getUuid()));
                        if (player instanceof ServerPlayerEntity p) {
                            Criteria.TAME_ANIMAL
                                    .trigger(p, (BeeEntity) (Object) this);
                        }
                        beebuddy$friendify();
                        this.world.sendEntityStatus(this, (byte) 7);
                    } else {
                        this.world.sendEntityStatus(this, (byte) 6);
                    }
                    cbir.setReturnValue(ActionResult.SUCCESS);
                }
            } else if (stack.getItem() instanceof NectarItem n) {
                if (friend || !beebuddy$hasFriend()) {
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    beebuddy$setNectarType(n.getType());
                    cbir.setReturnValue(ActionResult.SUCCESS);
                }
            } else {
                beebuddy$setSitting(!beebuddy$isSitting());
                cbir.setReturnValue(ActionResult.SUCCESS);
            }
        } else if (friend || stack.getItem() == Items.HONEY_BOTTLE) {
            cbir.setReturnValue(ActionResult.CONSUME);
        } else if (stack.getItem() instanceof NectarItem) {
            cbir.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Override
    protected void doAceReproduction(PlayerEntity player, CallbackInfo cbi) {
        if (beebuddy$getNectarType().equals("ace")) {
            if (this.world instanceof ServerWorld) {
                BeeEntity us = (BeeEntity) (Object) this;
                us.breed((ServerWorld) this.world, us);
                us.setBaby(true);
            }
        }
    }
}
