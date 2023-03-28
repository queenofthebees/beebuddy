package nomble.beebuddy.mixin.invoker;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.charset.CoderMalfunctionError;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public interface BeeWanderAroundGoalInvoker {
    @Invoker("<init>")
    static BeeEntity.BeeWanderAroundGoal beebuddy$make(BeeEntity main) {
        throw new CoderMalfunctionError(new Exception("mixins broke :("));
    }
}
