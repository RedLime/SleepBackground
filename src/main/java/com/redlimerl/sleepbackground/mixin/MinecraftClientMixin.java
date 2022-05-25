package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import com.redlimerl.sleepbackground.config.ConfigValues;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow public ClientWorld world;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        SleepBackground.CLIENT_WORLD_TICK_COUNT = this.world == null ? 0 :
                Math.min(SleepBackground.CLIENT_WORLD_TICK_COUNT + 1, ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks());
    }
}
