package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class LogIntervalConfigValue extends ConfigValue {

    private int logInterval;

    public LogIntervalConfigValue(int defaultLimit, String comment) {
        super("log_interval", comment);
        this.logInterval = defaultLimit;
    }

    @Override
    public void loadToInit(JsonObject configObject) {
        if (configObject.has(this.getKeyName())) {
            this.logInterval = configObject.get(this.getKeyName()).getAsInt();
            if (this.logInterval < 50 || this.logInterval > 500) throw new IllegalArgumentException("Logging interval must be >= 50ms and <= 500ms");
        }
    }

    @Override
    public void writeToJson(JsonObject configObject) {
        configObject.addProperty(this.getKeyName(), this.logInterval);
    }

    public int getLogInterval() {
        return this.isEnable() ? logInterval : 500;
    }
}
