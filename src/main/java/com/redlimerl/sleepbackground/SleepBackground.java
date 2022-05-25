package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.ConfigValue;
import com.redlimerl.sleepbackground.config.ConfigValues;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static int CLIENT_WORLD_TICK_COUNT = 0;
    private static long lastRenderTime = 0;
    private static long lastPollTime = 0;

    @Override
    public void onInitializeClient() {
        SleepBackgroundConfig.init();
    }

    public static boolean shouldRenderInBackground() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRender = currentTime - lastRenderTime;

        @Nullable Integer targetFPS = getBackgroundFPS();
        if (targetFPS == null) return true;

        long frameTime = 1000 / targetFPS;

        if (timeSinceLastRender < frameTime) {
            idle(frameTime);
            return false;
        }

        lastRenderTime = currentTime;
        return true;
    }

    public static boolean shouldPollMouse() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastPoll = currentTime - lastPollTime;
        long pollTime = 1000 / ConfigValues.DISPLAY_UPDATE_RATE.getFrameLimit();
        if (timeSinceLastPoll < pollTime) {
            return false;
        }
        lastPollTime = currentTime;
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

        if (!Display.isActive() && !Mouse.isInsideWindow()) {
            if (client.world != null) {
                if (ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks() > CLIENT_WORLD_TICK_COUNT)
                    return ConfigValues.WORLD_INITIAL_FRAME_RATE.getFrameLimit();

                return ConfigValues.BACKGROUND_FRAME_RATE.getFrameLimit();
            }

            return null;
        }
        return null;
    }
}