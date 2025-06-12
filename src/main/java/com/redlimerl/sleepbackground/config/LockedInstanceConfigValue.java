package com.redlimerl.sleepbackground.config;

import me.contaria.speedrunapi.config.api.annotations.Config;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class LockedInstanceConfigValue extends FrameLimitConfigValue {

    @Config.Numbers.Whole.Bounds(min = 1, max = 100, enforce = Config.Numbers.EnforceBounds.MIN_ONLY)
    private int tickInterval;

    public LockedInstanceConfigValue(int defaultFrameLimit, int defaultTickInterval) {
        super(defaultFrameLimit);
        this.tickInterval = defaultTickInterval;
    }

    public int getTickInterval() {
        return this.isEnabled() ? this.tickInterval : 1;
    }
}
