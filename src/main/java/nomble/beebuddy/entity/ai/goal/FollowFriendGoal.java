package nomble.beebuddy.entity.ai.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import nomble.beebuddy.duck.IFriendlyBee;

import java.util.EnumSet;

public class FollowFriendGoal extends Goal {
    private final BeeEntity bee;
    private int tick = 0;

    public FollowFriendGoal(BeeEntity b) {
        bee = b;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        PlayerEntity f = ((IFriendlyBee) bee).beebuddy$getFriendPlayer()
                .orElse(null);
        if (f == null) {
            return false;
        }
        IFriendlyBee b = (IFriendlyBee) bee;
        return (bee.getNavigation().isIdle()
                && !b.beebuddy$getPollinateGoalRunning()
                && !b.beebuddy$getMoveToFlowerGoalRunning()
                && (bee.squaredDistanceTo(f) > 144D))
                || (bee.squaredDistanceTo(f) > 400D);
    }

    @Override
    public boolean shouldContinue() {
        IFriendlyBee ib = (IFriendlyBee) bee;
        return !ib.beebuddy$isSitting()
                && !bee.getNavigation().isIdle()
                && !bee.isLeashed()
                && ib.beebuddy$getFriendPlayer()
                .map(f -> bee.squaredDistanceTo(f) >= 9D)
                .orElse(false);
    }

    @Override
    public void start() {
        tick = 0;
    }

    @Override
    public void stop() {
        bee.getNavigation().resetRangeMultiplier();
        bee.getNavigation().stop();
    }

    @Override
    public void tick() {
        ((IFriendlyBee) bee).beebuddy$getFriendPlayer().ifPresent(f -> {
            bee.getLookControl().lookAt(f, 10F, (float) bee.getMaxLookPitchChange());
            if (tick++ % 10 == 0) {
                tick = 0;
                double s = f.getAttributeValue(EntityAttributes
                        .GENERIC_MOVEMENT_SPEED) + 1D;
                if (bee.squaredDistanceTo(f) > 256D) {
                    s *= 10D;
                }
                if (bee.squaredDistanceTo(f) < 1024D) {
                    bee.getNavigation().setRangeMultiplier(32F);
                    bee.getNavigation().startMovingTo(f, s);
                } else {
                    for (int i = 0; i < 10; i++) {
                        int dx = bee.getRandom().nextInt(5) - 2;
                        int dy = bee.getRandom().nextInt(2) + 1;
                        int dz = bee.getRandom().nextInt(5) - 2;
                        BlockPos o = new BlockPos(dx, dy, dz);
                        if (bee.world.isSpaceEmpty(bee, bee.getBoundingBox()
                                .offset(o))) {
                            BlockPos p = f.getBlockPos().add(o);
                            double x = (double) p.getX() + 0.5D;
                            double y = (double) p.getY() + 0.5D;
                            double z = (double) p.getZ() + 0.5D;
                            bee.getNavigation().stop();
                            bee.refreshPositionAndAngles(x, y, z, bee.getYaw()
                                    , bee.getPitch());
                            break;
                        }
                    }
                }
            }
        });
    }
}
