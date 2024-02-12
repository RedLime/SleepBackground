package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.config.ConfigValues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Shadow @Nullable public ClientWorld world;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        SleepBackground.LATEST_LOCK_FRAME = !SleepBackground.shouldRenderInBackground();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        SleepBackground.CLIENT_WORLD_TICK_COUNT = this.world == null ? 0 :
                Math.min(SleepBackground.CLIENT_WORLD_TICK_COUNT + 1, ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks());

        SleepBackground.checkLock();
    }


    @Inject(method = "drawProfilerResults", at = @At("HEAD"), cancellable = true, expect = 0, require = 0)
    private void onDraw(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }

    @ModifyArg(
            method = {
                    /* 1.18.2 */ "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/RegistryTracker$Modifiable;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V",
                    /* class_6904: SaveLoader */
                    /* 1.19-1.19.2 */ "Lnet/minecraft/client/MinecraftClient;startIntegratedServer(Ljava/lang/String;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/class_6904;)V",
                    /* 1.19.3-1.19.4 */ "Lnet/minecraft/client/MinecraftClient;startIntegratedServer(Ljava/lang/String;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/class_6904;Z)V"
            },
            at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"), require = 0
    )
    public long onSleep(long l){
        return ConfigValues.LOADING_TICK_INTERVAL.getTickInterval();
    }
}
