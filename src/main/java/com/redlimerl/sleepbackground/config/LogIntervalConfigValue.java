package com.redlimerl.sleepbackground.config;

import me.contaria.speedrunapi.config.api.annotations.Config;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class LogIntervalConfigValue extends ConfigValue {

    @Config.Numbers.Whole.Bounds(min = 50, max = 500)
    private int logInterval;

    public LogIntervalConfigValue(int defaultLogInterval) {
        this.logInterval = defaultLogInterval;
    }

    public Integer getLogInterval() {
        return this.isEnabled() ? this.logInterval : null;
    }
}
