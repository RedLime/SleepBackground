package com.redlimerl.sleepbackground.config;

import me.contaria.speedrunapi.config.api.annotations.Config;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class WorldSetupConfigValue extends FrameLimitConfigValue {

    @Config.Numbers.Whole.Bounds(max = 100, enforce = Config.Numbers.EnforceBounds.MIN_ONLY)
    private int maxTicks;

    public WorldSetupConfigValue(int defaultFrameLimit, int defaultMaxTicks) {
        super(defaultFrameLimit);
        this.maxTicks = defaultMaxTicks;
    }

    public int getMaxTicks() {
        return this.isEnabled() ? this.maxTicks : 0;
    }
}
