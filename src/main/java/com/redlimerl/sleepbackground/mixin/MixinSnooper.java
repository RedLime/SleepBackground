package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.SleepBackground;
import net.minecraft.util.snooper.Snooper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snooper.class)
public class MixinSnooper {

    @Inject(method = "addCpuInfo", at = @At("HEAD"), cancellable = true)
    private void onUpdate(CallbackInfo ci) {
        if (SleepBackground.LATEST_LOCK_FRAME) ci.cancel();
    }
}
