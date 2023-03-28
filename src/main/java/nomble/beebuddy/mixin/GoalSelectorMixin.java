package nomble.beebuddy.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import nomble.beebuddy.duck.IGoalRemover;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(GoalSelector.class)
public abstract class GoalSelectorMixin implements IGoalRemover {
    @Shadow
    @Final
    private Set<PrioritizedGoal> goals;

    @Override
    public void beebuddy$clear() {
        goals.clear();
    }
}
