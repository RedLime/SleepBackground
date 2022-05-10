package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;updateListenerPosition(Lnet/minecraft/client/render/Camera;)V"), cancellable = true)
    public void onInput(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderInBackground()
            && (this.world != null || this.currentScreen instanceof LevelLoadingScreen)) {
            ci.cancel();
        }
    }

/*
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V"))
    public void onFocused(GameRenderer instance, float tickDelta, long startTime, boolean tick) {
        if (SleepBackground.shouldRenderInBackground()) {
            instance.render(tickDelta, startTime, tick);
        } else {
            SleepBackground.SHOULD_CHECK_INPUT = true;
        }
    }

    @Inject(method = "handleInputEvents", at = @At("HEAD"), cancellable = true)
    public void onInput(CallbackInfo ci) {
        if (SleepBackground.SHOULD_CHECK_INPUT) {
            ci.cancel();
            SleepBackground.SHOULD_CHECK_INPUT = false;
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void onTick(CallbackInfo ci) {
        System.out.println("tick" + System.currentTimeMillis());
    }*/

}
