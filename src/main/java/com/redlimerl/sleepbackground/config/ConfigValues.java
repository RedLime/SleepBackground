package com.redlimerl.sleepbackground.config;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class ConfigValues {

    public static final HashSet<ConfigValue> ALL_CONFIGS = Sets.newHashSet();

    public static final FrameLimitConfigValue BACKGROUND_FRAME_RATE =
            new FrameLimitConfigValue("background", 1, "It works when instance is in the background after joined the world.");

    public static final FrameLockConfigValue NONE_PLAYING_FRAME_RATE =
            new FrameLockConfigValue("lock_instance", 1, 20, true, 10, "It works when instance is in the background with sleepbg.lock file is exist in user directory at every interval ticks. (for macros option)");

    public static final FrameLimitConfigValue LOADING_SCREEN_FRAME_RATE =
            new FrameLimitConfigValue("loading_screen", 30, "It works when instance is in the world loading screen. minimum (fps_limit) is 15.") {
                @Override
                public void loadToInit(JsonObject configObject) {
                    super.loadToInit(configObject);
                    Integer fps = this.getFrameLimit();
                    if (fps != null && fps < 15) {
                        throw new IllegalArgumentException("loading_screen fps limit is must be 15 or over");
                    }
                }
            };

    public static final FrameTickConfigValue WORLD_INITIAL_FRAME_RATE =
            new FrameTickConfigValue("world_setup", 10, 30, "same with (background) config but for (max_ticks) ticks after the joined the world.");

    public static final PollingRateConfigValue POLLING_RATE_LIMIT = new PollingRateConfigValue("polling_rate_limit", "Limits input detection when the instance is in the background. When enabled, it will be make problem if you are using macros.");

    static {
        ALL_CONFIGS.add(BACKGROUND_FRAME_RATE);
        ALL_CONFIGS.add(LOADING_SCREEN_FRAME_RATE);
        ALL_CONFIGS.add(WORLD_INITIAL_FRAME_RATE);
        ALL_CONFIGS.add(NONE_PLAYING_FRAME_RATE);
        ALL_CONFIGS.add(POLLING_RATE_LIMIT);
    }
}
