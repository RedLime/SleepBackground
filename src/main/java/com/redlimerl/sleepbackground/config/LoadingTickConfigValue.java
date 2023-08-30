package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class LoadingTickConfigValue extends ConfigValue {

    private int tickInterval;

    public LoadingTickConfigValue(int defaultLimit, String comment) {
        super("loading_screen_tick_interval", comment);
        this.tickInterval = defaultLimit;
    }

    @Override
    public void loadToInit(JsonObject configObject) {
        if (configObject.has(this.getKeyName())) {
            this.tickInterval = configObject.get(this.getKeyName()).getAsInt();
            if (this.tickInterval < 1 || this.tickInterval > 16) throw new IllegalArgumentException("Loading screen tick interval must be >= 1ms and <= 16ms");
        }
    }

    @Override
    public void writeToJson(JsonObject configObject) {
        configObject.addProperty(this.getKeyName(), this.tickInterval);
    }

    public int getTickInterval() {
        return this.isEnable() ? tickInterval : 16;
    }
}
