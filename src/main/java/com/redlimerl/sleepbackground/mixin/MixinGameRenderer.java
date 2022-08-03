package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.config.ConfigValues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo callbackInfo) {
        if (ConfigValues.USE_LEGACY_METHOD.isEnable() && !SleepBackground.shouldRenderInBackground()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderWorld", at = @At("HEAD"), cancellable = true)
    private void onRenderWorld(CallbackInfo callbackInfo) {
        if (MinecraftClient.getInstance().getOverlay() instanceof SplashScreen) {
            callbackInfo.cancel();
        }
    }
}
