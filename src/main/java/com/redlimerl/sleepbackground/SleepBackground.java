package com.redlimerl.sleepbackground;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {

    public static int BG_FRAME_RATE = 1;

    @Override
    public void onInitializeClient() {
        File file = FabricLoader.getInstance().getConfigDir().resolve("sleepbg.txt").toFile();
        try {
            if (file.exists()) {
                BG_FRAME_RATE = Integer.parseInt(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            } else {
                FileUtils.writeStringToFile(file, String.valueOf(BG_FRAME_RATE), StandardCharsets.UTF_8);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static long lastRenderTime = 0;
    public static boolean shouldRenderInBackground() {
        long currentTime = Util.getMeasuringTimeMs();
        long timeSinceLastRender = currentTime - lastRenderTime;

        if (!shouldRenderInBackground(timeSinceLastRender))
            return false;

        lastRenderTime = currentTime;
        return true;
    }

    private static boolean shouldRenderInBackground(long timeSinceLastRender) {
        Integer fpsOverride = getBackgroundFPS();
        if (fpsOverride == null) {
            return true;
        }

        long frameTime = (1000 / fpsOverride);
        boolean shouldSkipRender = (timeSinceLastRender < frameTime);
        if (!shouldSkipRender)
            return true;

        idle(frameTime);
        return false;
    }

    private static void idle(long waitMillis) {
        waitMillis = Math.min(waitMillis, 30L);
        LockSupport.parkNanos("waiting to render", waitMillis * 1000000L);
    }

    @Nullable
    private static Integer getBackgroundFPS() {
        MinecraftClient client = MinecraftClient.getInstance();
        return (!client.isWindowFocused() && !isHoveredWindow())
         ? BG_FRAME_RATE : null;
    }

    private static boolean isHoveredWindow() {
        Window window = MinecraftClient.getInstance().getWindow();
        return GLFW.glfwGetWindowAttrib(window.getHandle(), 131083) != 0;
    }
}
