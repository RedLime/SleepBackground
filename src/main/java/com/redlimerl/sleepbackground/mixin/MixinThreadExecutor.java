package com.redlimerl.sleepbackground.mixin;

import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ThreadExecutor.class)
public class MixinThreadExecutor {
    @Redirect(method = "method_20813", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;yield()V"), require = 0)
    private void noopYield() {
    }

    @Group(min = 1, max = 1)
    @ModifyArg(method = "method_20813", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/locks/LockSupport;parkNanos(Ljava/lang/Object;J)V"), require = 0)
    private long parkLonger(long original) {
        return 500000L;
    }

    // 1.14.3- parks inline
    @Group
    @Dynamic
    @ModifyArg(method = "runTasks(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/locks/LockSupport;parkNanos(Ljava/lang/Object;J)V"), require = 0)
    private long parkLonger1143(long original) {
        return 500000L;
    }
}
