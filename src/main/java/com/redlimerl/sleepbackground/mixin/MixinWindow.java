package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.class_4117;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_4117.class)
public class MixinWindow {

    @Inject(method = "method_18301", cancellable = true, at = @At("HEAD"))
    public void onSwap(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            ci.cancel();
            GLFW.glfwPollEvents();
        }
    }
}
