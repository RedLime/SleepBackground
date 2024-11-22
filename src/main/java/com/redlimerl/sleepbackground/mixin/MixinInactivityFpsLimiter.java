package com.redlimerl.sleepbackground.mixin;

import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(InactivityFpsLimiter.class)
public abstract class MixinInactivityFpsLimiter {
    @ModifyVariable(method = "update", ordinal = 0, at = @At(value = "STORE", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/InactivityFpsLimit;AFK:Lnet/minecraft/client/option/InactivityFpsLimit;")))
    public long disableInactivityFpsLimiter(long original) {
        return 0; // override last input time
    }
}
