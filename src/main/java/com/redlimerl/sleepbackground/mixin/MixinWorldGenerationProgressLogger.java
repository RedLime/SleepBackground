package com.redlimerl.sleepbackground.mixin;

import com.redlimerl.sleepbackground.config.ConfigValues;
import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldGenerationProgressLogger.class)
public class MixinWorldGenerationProgressLogger {

    @Shadow private long nextMessageTime;

    /**
     * @author DuncanRuns, jojoe77777
     */
    @Inject(method = "setChunkStatus", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", remap = false))
    public void injectMessageTime(ChunkPos pos, ChunkStatus status, CallbackInfo ci) {
        nextMessageTime -= (500 - ConfigValues.LOG_INTERVAL.getLogInterval());
    }
}
