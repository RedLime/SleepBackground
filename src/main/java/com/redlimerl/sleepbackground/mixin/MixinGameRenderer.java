package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.class_4218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_4218.class)
public class MixinGameRenderer {

    @Inject(method = "method_19061", at = @At("HEAD"), cancellable = true)
    private void onRender(CallbackInfo callbackInfo) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            callbackInfo.cancel();
        }
        SleepBackground.checkRenderWorldPreview();
    }

    @Inject(method = "method_19073", at = @At("HEAD"), cancellable = true)
    public void onRenderStream(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }
}
