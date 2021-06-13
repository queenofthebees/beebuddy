package nomble.beebuddy.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import nomble.beebuddy.duck.IFriendlyBee;
import nomble.beebuddy.duck.IFriendlyPlayer;

public class SitOnHeadGoal extends Goal{
    private final BeeEntity bee;
    private int tick = 0;

    public SitOnHeadGoal(BeeEntity b){
        bee = b;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean shouldContinue(){
        IFriendlyBee b = (IFriendlyBee)(Object)bee;
        return b.beebuddy$getFriendPlayer().map(f -> {
            BlockPos t = f.getBlockPos().up(2);
            Vec3d v = f.getVelocity();
            return f.world.getBlockState(t).isOf(Blocks.AIR)
                && v.getX() == 0D && v.getZ() == 0D
                && !((IFriendlyPlayer)(Object)f).beebuddy$hasHeadFriend();
        }).orElse(false) && !bee.hasAngerTime();
    }
    @Override
    public boolean canStart(){
        return shouldContinue()
            && (bee.getRandom().nextInt(bee.hasNectar() ? 20 : 100) == 0);
    }

    @Override
    public void start(){
        tick = 0;
    }
    @Override
    public void stop(){
        bee.getNavigation().stop();
    }

    @Override
    public void tick(){
        ((IFriendlyBee)(Object)bee).beebuddy$getFriendPlayer().ifPresent(f -> {
            BlockPos target = f.getBlockPos().up(2);
            if(bee.getBlockPos().getManhattanDistance(target) < 3){
                if(tick++ >= 40){
                    if(bee.hasNectar()){
                        ((IFriendlyBee)(Object)bee).callSetHasNectar(false);
                        f.removeStatusEffect(StatusEffects.POISON);
                        f.eatFood( bee.world
                                 , new ItemStack(Items.HONEY_BOTTLE));
                    }
                    tick = 0;
                    ((IFriendlyPlayer)(Object)f).beebuddy$makeHeadFriend(bee);
                }
            }
            else{
                double x = target.getX() + 0.5D;
                double y = target.getY();
                double z = target.getZ() + 0.5D;
                bee.getNavigation().startMovingTo(x, y, z, 0.5D);
            }
        });
    }
}
