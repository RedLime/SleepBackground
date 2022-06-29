package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo callbackInfo) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            callbackInfo.cancel();
        }
        SleepBackground.checkRenderWorldPreview();
    }
}
