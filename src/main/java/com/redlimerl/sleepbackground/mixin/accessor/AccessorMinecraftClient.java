package com.redlimerl.sleepbackground.mixin.accessor;

import net.minecraft.class_4117;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {

    @Accessor("field_19944")
    class_4117 getWindowForAccess();

}
