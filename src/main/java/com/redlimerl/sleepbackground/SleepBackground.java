package com.redlimerl.sleepbackground;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class SleepBackground implements ClientModInitializer {

    public static int BG_FRAME_RATE = 1;
    public static int LOADING_FRAME_RATE = 30;

    @Override
    public void onInitializeClient() {
        File bgConfigFile = FabricLoader.getInstance().getConfigDir().resolve("sleepbg.txt").toFile();
        try {
            if (bgConfigFile.exists()) {
                BG_FRAME_RATE = Integer.parseInt(FileUtils.readFileToString(bgConfigFile, StandardCharsets.UTF_8));
            } else {
                FileUtils.writeStringToFile(bgConfigFile, String.valueOf(BG_FRAME_RATE), StandardCharsets.UTF_8);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        File ldConfigFile = FabricLoader.getInstance().getConfigDir().resolve("sleepbg_loading_screen.txt").toFile();
        try {
            if (ldConfigFile.exists()) {
                LOADING_FRAME_RATE = Integer.parseInt(FileUtils.readFileToString(ldConfigFile, StandardCharsets.UTF_8));
            } else {
                FileUtils.writeStringToFile(ldConfigFile, String.valueOf(BG_FRAME_RATE), StandardCharsets.UTF_8);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static long lastRenderTime = 0;
    public static boolean shouldRenderInBackground() {
        long currentTime = Util.getMeasuringTimeMs();
        long timeSinceLastRender = currentTime - lastRenderTime;

        Integer targetFPS = getBackgroundFPS();
        if (targetFPS == null) return true;

        long frameTime = 1000 / targetFPS;

        if (timeSinceLastRender < frameTime)
            return false;

        lastRenderTime = currentTime;
        return true;
    }

    @Nullable
    private static Integer getBackgroundFPS() {
        MinecraftClient client = MinecraftClient.getInstance();
        boolean isInLoadingScreen = client.currentScreen instanceof LevelLoadingScreen;
        return (!client.isWindowFocused() && !isHoveredWindow() && (client.world != null || isInLoadingScreen))
                ? (isInLoadingScreen ? LOADING_FRAME_RATE : BG_FRAME_RATE) : null;
    }

    private static boolean isHoveredWindow() {
        Window window = MinecraftClient.getInstance().getWindow();
        return GLFW.glfwGetWindowAttrib(window.getHandle(), 131083) != 0;
    }
}
