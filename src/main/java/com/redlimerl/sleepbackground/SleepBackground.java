package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.ConfigValues;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static int CLIENT_WORLD_TICK_COUNT = 0;
    public static boolean LATEST_LOCK_FRAME = false;
    public static boolean LOCK_FILE_EXIST = false;

    @Override
    public void onInitializeClient() {
        SleepBackgroundConfig.init();
    }

    private static long lastRenderTime = 0;
    public static boolean shouldRenderInBackground() {
        long currentTime = System.currentTimeMillis();
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
     * For decrease CPU usage
     * From mangohand's idle method
     */
    private static void idle(long waitMillis) {
        waitMillis = Math.min(waitMillis, 30L);
        LockSupport.parkNanos("waiting to render", waitMillis * 1000000L);
    }

    @Nullable
    private static Integer getBackgroundFPS() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!Display.isActive() && !isHoveredWindow()) {

            if (client.world != null) {
                if (SleepBackground.LOCK_FILE_EXIST) {
                    Integer value = ConfigValues.NONE_PLAYING_FRAME_RATE.getFrameLimit();
                    if (value != null) return value;
                }

                if (ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks() > CLIENT_WORLD_TICK_COUNT) {
                    Integer value = ConfigValues.WORLD_INITIAL_FRAME_RATE.getFrameLimit();
                    if (value != null) return value;
                }

                return ConfigValues.BACKGROUND_FRAME_RATE.getFrameLimit();
            }

            else if (client.currentScreen instanceof ProgressScreen) {
                return ConfigValues.LOADING_SCREEN_FRAME_RATE.getFrameLimit();
            }

            return null;
        }
        return null;
    }

    private static boolean isHoveredWindow() {
        return Mouse.isInsideWindow();
    }

    private static int lockTick = 0;
    public static void checkLock() {
        if (ConfigValues.NONE_PLAYING_FRAME_RATE.isEnable() && ++lockTick >= ConfigValues.NONE_PLAYING_FRAME_RATE.getTickInterval()) {
            SleepBackground.LOCK_FILE_EXIST = new File(FileUtils.getUserDirectory(), "sleepbg.lock").exists();
            lockTick = 0;
        }
    }
}
