package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.ConfigValues;
import me.voidxwalker.worldpreview.WorldPreview;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.concurrent.locks.LockSupport;

public class SleepBackground implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static int CLIENT_WORLD_TICK_COUNT = 0;
    private static boolean HAS_WORLD_PREVIEW = false;
    private static boolean CHECK_FREEZE_PREVIEW = false;
    private static boolean LOCK_FREEZE_PREVIEW = false;
    public static boolean LATEST_LOCK_FRAME = false;
    public static boolean LOCK_FILE_EXIST = false;
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

    public static void checkRenderWorldPreview() {
        if (!HAS_WORLD_PREVIEW || !WorldPreview.inPreview) return;

        boolean windowFocused = Display.isActive(), windowHovered = isHoveredWindow();
        int renderTimes = SleepBackground.LOCK_FILE_EXIST ? ConfigValues.NONE_PLAYING_FRAME_RATE.getRenderTimes() : ConfigValues.WORLD_PREVIEW_RENDER_TIMES.getRenderTimes();
        if (windowFocused || windowHovered
                || ++LOADING_SCREEN_RENDER_COUNT >= renderTimes) {
            LOADING_SCREEN_RENDER_COUNT = 0;
            if (windowFocused || windowHovered) {
                if (CHECK_FREEZE_PREVIEW) {
                    LOCK_FREEZE_PREVIEW = WorldPreview.freezePreview;
                }
                CHECK_FREEZE_PREVIEW = true;
            }
            WorldPreview.freezePreview = LOCK_FREEZE_PREVIEW;
        } else {
            CHECK_FREEZE_PREVIEW = false;
            WorldPreview.freezePreview = true;
        }
    }
}
