package nomble.beebuddy.entity.ai.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.BeeEntity;
import nomble.beebuddy.duck.IFriendlyBee;

import java.util.EnumSet;

public class BeeSitGoal extends Goal {
    private final BeeEntity bee;

    public BeeSitGoal(BeeEntity b) {
        bee = b;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
    }

    @Override
    public boolean shouldContinue() {
        return ((IFriendlyBee) bee).beebuddy$isSitting();
    }

    @Override
    public boolean canStart() {
        return !bee.isInsideWaterOrBubbleColumn() && shouldContinue();
    }

    @Override
    public void start() {
        bee.getNavigation().stop();
    }
}
