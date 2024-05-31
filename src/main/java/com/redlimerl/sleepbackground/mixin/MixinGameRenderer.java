package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Dynamic
    // class_9779 -> RenderTickCounter
    @Inject(method = {"render", "Lnet/minecraft/class_757;method_3192(Lnet/minecraft/class_9779;Z)V"}, at = @At("HEAD"), require = 1, cancellable = true)
    private void onRender(CallbackInfo callbackInfo) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            callbackInfo.cancel();
        }
        SleepBackground.checkRenderWorldPreview();
    }
}
