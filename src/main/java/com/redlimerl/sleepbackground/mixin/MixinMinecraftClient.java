package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Final
    @Shadow public GameOptions options;

    @Final
    @Shadow private Window window;

    @Inject(method = "onWindowFocusChanged", at = @At("RETURN"))
    public void onFocused(boolean focused, CallbackInfo ci) {
        this.window.setFramerateLimit(focused ? this.options.maxFps : SleepBackground.BG_FRAME_RATE);
    }

}
