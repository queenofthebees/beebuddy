package nomble.beebuddy.mixin.invoker;

import java.nio.charset.CoderMalfunctionError;

import net.minecraft.entity.passive.BeeEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.MoveToFlowerGoal.class)
public interface MoveToFlowerGoalInvoker{
    @Invoker("<init>")
    public static BeeEntity.MoveToFlowerGoal beebuddy$make(BeeEntity main){
        throw new CoderMalfunctionError(new Exception("mixins broke :("));
    }
}
