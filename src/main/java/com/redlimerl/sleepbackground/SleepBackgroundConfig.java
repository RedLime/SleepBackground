package com.redlimerl.sleepbackground;

import com.redlimerl.sleepbackground.config.*;
import me.contaria.speedrunapi.config.api.SpeedrunConfig;
import me.contaria.speedrunapi.config.api.annotations.Config;

@Config(init = Config.InitPoint.PRELAUNCH)
public class SleepBackgroundConfig implements SpeedrunConfig {

    @Config.Category("backgroundFrameRate")
    public final FrameLimitConfigValue BACKGROUND_FRAME_RATE = new FrameLimitConfigValue(1);

    @Config.Category("loadingScreenFrameRate")
    public final FrameLimitConfigValue LOADING_SCREEN_FRAME_RATE = new FrameLimitConfigValue(30);

    @Config.Category("worldSetupFrameRate")
    public final WorldSetupConfigValue WORLD_SETUP_FRAME_RATE = new WorldSetupConfigValue(10, 30);

    @Config.Category("lockedFrameRate")
    public final LockedInstanceConfigValue LOCKED_INSTANCE_FRAME_RATE = new LockedInstanceConfigValue(1, 20);

    @Config.Category("loadingScreenTickInterval")
    public final LoadingScreenTickConfigValue LOADING_SCREEN_TICK_INTERVAL = new LoadingScreenTickConfigValue(1);

    @Config.Category("logInterval")
    public final LogIntervalConfigValue LOG_INTERVAL = new LogIntervalConfigValue(500);

    {
        SleepBackground.config = this;
    }

    @Override
    public String modID() {
        return "sleepbackground";
    }
}
