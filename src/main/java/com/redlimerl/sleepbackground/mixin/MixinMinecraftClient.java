package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void onFocused(CallbackInfoReturnable<Integer> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();

        boolean isHovered = (GLFW.glfwGetWindowAttrib(window.getHandle(), 131083) != 0);

        if (!client.isWindowFocused() && !isHovered) {
            cir.setReturnValue(SleepBackground.BG_FRAME_RATE);
        }
    }

}
