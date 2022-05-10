package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
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

    @Shadow private Profiler profiler;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;updateListenerPosition(Lnet/minecraft/client/render/Camera;)V"), cancellable = true)
    public void onRender(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderInBackground()
            && (this.world != null || this.currentScreen instanceof LevelLoadingScreen)) {
            this.profiler.pop();
            ci.cancel();
        }
    }
}
