package com.redlimerl.sleepbackground;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {

    private static int BG_FRAME_RATE = 1;
    private static int LOADING_FRAME_RATE = 30;
    private static final Logger LOGGER = LogManager.getLogger();

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
                FileUtils.writeStringToFile(ldConfigFile, String.valueOf(LOADING_FRAME_RATE), StandardCharsets.UTF_8);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (BG_FRAME_RATE < 1 || LOADING_FRAME_RATE < 1) {
            throw new IllegalArgumentException("The FPS limit should always be 1 or over");
        }

        LOGGER.info("FPS limit in the background has been initalized.");
        LOGGER.info("Normal FPS : " + BG_FRAME_RATE);
        LOGGER.info("Loading Screen FPS : " + LOADING_FRAME_RATE);
    }

    private static long lastRenderTime = 0;
    public static boolean shouldRenderInBackground() {
        long currentTime = Util.getMeasuringTimeMs();
        long timeSinceLastRender = currentTime - lastRenderTime;

        Integer targetFPS = getBackgroundFPS();
        if (targetFPS == null) return true;

        long frameTime = 1000 / targetFPS;

        if (timeSinceLastRender < frameTime) {
            idle(frameTime);
            return false;
        }

        lastRenderTime = currentTime;
        return true;
    }

    /**
     * From mangohand's idle method
     */
    private static void idle(long waitMillis) {
        waitMillis = Math.min(waitMillis, 30L);
        LockSupport.parkNanos("waiting to render", waitMillis * 1000000L);
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
