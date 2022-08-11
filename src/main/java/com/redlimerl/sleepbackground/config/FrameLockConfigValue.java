package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class FrameLockConfigValue extends FrameLimitConfigValue {

    private int tickInterval;
    private boolean enableWorldPreview;
    private int renderTimes;

    public FrameLockConfigValue(String keyName, int defaultValue, int defaultInterval, boolean enableWorldPreview, int defaultTimes, String comment) {
        super(keyName, defaultValue, comment);
        this.tickInterval = defaultInterval;
        this.renderTimes = defaultTimes;
        this.enableWorldPreview = enableWorldPreview;
    }

    public int getTickInterval() {
        return this.isEnable() ? tickInterval : 1;
    }

    public int getRenderTimes() {
        return this.isEnable() ? renderTimes : 1;
    }

    @Override
    public void loadToInit(JsonObject configObject) {
        super.loadToInit(configObject);
        if (configObject.has("tick_interval")) {
            this.tickInterval = configObject.get("tick_interval").getAsInt();
            if (this.tickInterval < 1) throw new IllegalArgumentException("The Tick Interval should always be 1 or over");
        }
        if (configObject.has("wp_render_times")) {
            this.renderTimes = configObject.get("wp_render_times").getAsInt();
            if (this.renderTimes < 1) throw new IllegalArgumentException("The Render times should always be 1 or over");
        }
        if (configObject.has("wp_render_times_enable")) {
            this.enableWorldPreview = configObject.get("wp_render_times_enable").getAsBoolean();
        }
    }

    @Override
    public void writeToJson(JsonObject configObject) {
        super.writeToJson(configObject);
        configObject.addProperty("tick_interval", this.tickInterval);
        configObject.addProperty("wp_render_times_enable", this.enableWorldPreview);
        configObject.addProperty("wp_render_times", this.renderTimes);
    }
}
