package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.ConfigValues;
import me.voidxwalker.worldpreview.WorldPreview;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static int CLIENT_WORLD_TICK_COUNT = 0;
    private static boolean HAS_WORLD_PREVIEW = false;
    private static int LOADING_SCREEN_RENDER_COUNT = 0;

    @Override
    public void onInitializeClient() {
        SleepBackgroundConfig.init();

        if (FabricLoader.getInstance().getModContainer("worldpreview").isPresent()) {
            HAS_WORLD_PREVIEW = true;
        }
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

        if (!client.isWindowFocused() && !isHoveredWindow()) {

            if (client.world != null) {
                if (ConfigValues.WORLD_INITIAL_FRAME_RATE.getMaxTicks() > CLIENT_WORLD_TICK_COUNT)
                    return ConfigValues.WORLD_INITIAL_FRAME_RATE.getFrameLimit();

                return ConfigValues.BACKGROUND_FRAME_RATE.getFrameLimit();
            }

            else if (client.currentScreen instanceof LevelLoadingScreen)
                return ConfigValues.LOADING_SCREEN_FRAME_RATE.getFrameLimit();

            return null;
        }
        return null;
    }

    private static boolean isHoveredWindow() {
        Window window = MinecraftClient.getInstance().getWindow();
        return GLFW.glfwGetWindowAttrib(window.getHandle(), 131083) != 0;
    }

    public static void checkRenderWorldPreview() {
        if (!HAS_WORLD_PREVIEW || !WorldPreview.inPreview) return;

        if (++LOADING_SCREEN_RENDER_COUNT < ConfigValues.WORLD_PREVIEW_RENDER_TIMES.getRenderTimes()) {
            WorldPreview.freezePreview = true;
        } else {
            LOADING_SCREEN_RENDER_COUNT = 0;
            WorldPreview.freezePreview = false;
        }
    }
}
