package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    @Inject(method = "swapBuffers", cancellable = true, at = @At("HEAD"))
    private void onSwap(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            ci.cancel();
            GLFW.glfwPollEvents();
        }
    }
}
