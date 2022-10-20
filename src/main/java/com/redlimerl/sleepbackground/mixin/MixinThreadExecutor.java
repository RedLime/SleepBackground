package com.redlimerl.sleepbackground.mixin;

import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.concurrent.locks.LockSupport;

@Mixin(ThreadExecutor.class)
public class MixinThreadExecutor {

    /**
     * @author MangoHands
     * @reason wait for tasks
     */
    @Overwrite
    protected void waitForTasks() {
        LockSupport.parkNanos("waiting for tasks", 500000L);
    }

}
