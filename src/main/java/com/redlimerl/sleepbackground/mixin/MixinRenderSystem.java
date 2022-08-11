package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(GL11.class)
public class MixinRenderSystem {

    @Inject(method = "glClear", at = @At("HEAD"), cancellable = true)
    private static void onClear(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "glPushMatrix", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onPush(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "glPopMatrix", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onPop(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "glEnable", at = @At("HEAD"), cancellable = true)
    private static void onCull(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "glFlush", at = @At("HEAD"), cancellable = true)
    private static void onTexture(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }
}
