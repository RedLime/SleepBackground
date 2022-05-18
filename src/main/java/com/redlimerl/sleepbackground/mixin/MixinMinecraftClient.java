package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.config.ConfigValues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.MetricsData;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow @Nullable public ClientWorld world;

    @Shadow public abstract Profiler getProfiler();

    @Shadow @Final public MetricsData metricsData;

    @Shadow private long lastMetricsSampleTime;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;updateListenerPosition(Lnet/minecraft/client/render/Camera;)V", shift = At.Shift.AFTER), cancellable = true)
    public void onRender(CallbackInfo ci) {
        if (!SleepBackground.shouldRenderInBackground()) {
            GLFW.glfwPollEvents();
            this.getProfiler().pop();
            skipAllProfiler();
            ci.cancel();
        }
        SleepBackground.checkRenderWorldPreview();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void onTick(CallbackInfo ci) {
        SleepBackground.CLIENT_WORLD_TICK_COUNT = this.world == null ? 0 :
                Math.min(SleepBackground.CLIENT_WORLD_TICK_COUNT + 1, ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks());
    }

    @SuppressWarnings("ALL")
    private void skipAllProfiler() {
        long m = System.nanoTime();
        this.metricsData.pushSample(m - this.lastMetricsSampleTime);
        this.lastMetricsSampleTime = m;
    }

}
