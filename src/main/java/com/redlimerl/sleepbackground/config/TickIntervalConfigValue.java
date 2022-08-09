package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class TickIntervalConfigValue extends ConfigValue {

    private int tickInterval;

    public TickIntervalConfigValue(String keyName, int defaultValue, String comment) {
        super(keyName, comment);
        this.tickInterval = defaultValue;
    }

    public int getTickInterval() {
        return this.isEnable() ? tickInterval : 1;
    }

    @Override
    protected void loadToInit(JsonObject configObject) {
        if (configObject.has("tick_interval")) {
            this.tickInterval = configObject.get("tick_interval").getAsInt();
            if (this.tickInterval < 1) throw new IllegalArgumentException("The Tick Interval should always be 1 or over");
        }
    }

    @Override
    protected void writeToJson(JsonObject configObject) {
        configObject.addProperty("tick_interval", this.tickInterval);
    }
}
