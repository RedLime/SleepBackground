package com.redlimerl.sleepbackground.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.redlimerl.sleepbackground.SleepBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(GlStateManager.class)
public class MixinRenderSystem {

    @Inject(method = "clear", at = @At("HEAD"), cancellable = true)
    private static void onClear(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "pushMatrix", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onPush(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "popMatrix", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onPop(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "translatef", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onTranslate(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "enableCull", at = @At("HEAD"), cancellable = true)
    private static void onCull(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "enableTexture", at = @At("HEAD"), cancellable = true)
    private static void onTexture(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "fogDensity", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onFogDensity(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @Inject(method = "fogMode(Lcom/mojang/blaze3d/platform/GlStateManager$FogMode;)V", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private static void onFogMode(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }
}
