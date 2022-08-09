package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.config.ConfigValues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow @Nullable public ClientWorld world;

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void onRender(CallbackInfo ci) {
        SleepBackground.LATEST_LOCK_FRAME = !SleepBackground.shouldRenderInBackground();
    }

    @Inject(method = "method_6648", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) {
            ci.cancel();
            Display.processMessages();
        }
    }

    private int lockCheck = 0;

    @Inject(method = "tick", at = @At("RETURN"))
    public void onTick(CallbackInfo ci) {
        SleepBackground.CLIENT_WORLD_TICK_COUNT = this.world == null ? 0 :
                Math.min(SleepBackground.CLIENT_WORLD_TICK_COUNT + 1, ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks());

        if (++lockCheck >= ConfigValues.NONE_PLAYING_TICK_INTERVAL.getTickInterval()) {
            SleepBackground.LOCK_FILE_EXIST = new File(FileUtils.getUserDirectory(), "sleepbg.lock").exists();
            lockCheck = 0;
        }
    }

}
