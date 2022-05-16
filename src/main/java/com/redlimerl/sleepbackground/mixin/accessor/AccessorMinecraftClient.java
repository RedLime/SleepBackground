package com.redlimerl.sleepbackground.mixin.accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {

    @Accessor("window")
    Window getWindowForAccess();

}
