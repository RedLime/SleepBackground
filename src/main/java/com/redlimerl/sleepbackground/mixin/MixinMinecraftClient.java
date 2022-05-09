package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow protected abstract int getFramerateLimit();

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getFramerateLimit()I"))
    public int onFocused(MinecraftClient client) {
        Window window = client.getWindow();
        boolean isHovered = (GLFW.glfwGetWindowAttrib(window.getHandle(), 131083) != 0);

        return (!client.isWindowFocused() && !isHovered) ? SleepBackground.BG_FRAME_RATE : this.getFramerateLimit();
    }

}
