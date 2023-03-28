package nomble.beebuddy.mixin.invoker;

import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.charset.CoderMalfunctionError;

@Mixin(BeeEntity.StingGoal.class)
public interface StingGoalInvoker {
    @Invoker("<init>")
    static BeeEntity.StingGoal beebuddy$make(BeeEntity main
            , PathAwareEntity e
            , double d, boolean b) {
        throw new CoderMalfunctionError(new Exception("mixins broke :("));
    }
}
