package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "render", at = @At("HEAD"))
    private void updateLatestLockFrame(CallbackInfo ci) {
        SleepBackground.shouldNotRender = !SleepBackground.shouldRenderInBackground();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void updateClientWorldTickCount(CallbackInfo ci) {
        SleepBackground.tick();
    }

    @Inject(method = "drawProfilerResults", at = @At("HEAD"), cancellable = true, require = 0)
    private void cancelDrawProfilerResults(CallbackInfo ci) {
        if (SleepBackground.shouldNotRender) ci.cancel();
    }

    @ModifyArg(method = "startIntegratedServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"))
    private long modifyLoadingScreenTickInterval(long delay) {
        Integer tickInterval = SleepBackground.config.LOADING_SCREEN_TICK_INTERVAL.getTickInterval();
        return tickInterval != null ? tickInterval : delay;
    }
}
