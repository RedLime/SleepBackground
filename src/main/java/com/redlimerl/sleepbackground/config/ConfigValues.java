package com.redlimerl.sleepbackground.config;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class ConfigValues {

    public static final HashSet<ConfigValue> ALL_CONFIGS = Sets.newHashSet();

    public static final FrameLimitConfigValue BACKGROUND_FRAME_RATE =
            new FrameLimitConfigValue("background", 1, "It works when instance is in the background after joined the world.");


    public static final FrameTickConfigValue WORLD_INITIAL_FRAME_RATE =
            new FrameTickConfigValue("world_setup", 10, 30, "same with (background) config but for (max_ticks) ticks after the joined the world.");

    public static final FrameLimitConfigValue DISPLAY_UPDATE_RATE = new FrameLimitConfigValue("display_rate", 15, "display rate for updating the window and polling input devices when in background.");

    static {
        ALL_CONFIGS.add(BACKGROUND_FRAME_RATE);
        ALL_CONFIGS.add(WORLD_INITIAL_FRAME_RATE);
        ALL_CONFIGS.add(DISPLAY_UPDATE_RATE);
    }
}
