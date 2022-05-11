package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow private Profiler profiler;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;updateListenerPosition(Lnet/minecraft/client/render/Camera;)V"), cancellable = true)
    public void onRender(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderInBackground()) {
            this.profiler.pop();
            ci.cancel();
        }
    }
}
