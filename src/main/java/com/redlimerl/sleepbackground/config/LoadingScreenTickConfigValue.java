package com.redlimerl.sleepbackground.config;

import me.contaria.speedrunapi.config.api.annotations.Config;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class LoadingScreenTickConfigValue extends ConfigValue {

    @Config.Numbers.Whole.Bounds(min = 1, max = 16)
    private int tickInterval;

    public LoadingScreenTickConfigValue(int defaultTickInterval) {
        this.tickInterval = defaultTickInterval;
    }

    public Integer getTickInterval() {
        return this.isEnabled() ? this.tickInterval : null;
    }
}
