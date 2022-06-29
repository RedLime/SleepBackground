package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Framebuffer.class)
public class MixinFramebuffer {

    @Inject(method = "bind", at = @At("HEAD"), cancellable = true)
    private void onBegin(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            ci.cancel();
        }
    }

    @Inject(method = "endWrite", at = @At("HEAD"), cancellable = true)
    private void onEnd(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            ci.cancel();
        }
    }

    @Inject(method = "draw(II)V", at = @At("HEAD"), cancellable = true)
    private void onDraw(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            ci.cancel();
        }
    }
}
