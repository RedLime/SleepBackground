package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class RenderTimesConfigValue extends ConfigValue{

    private int renderTimes;

    public RenderTimesConfigValue(String keyName, int defaultValue, String comment) {
        super(keyName, comment);
        this.renderTimes = defaultValue;
    }

    public int getRenderTimes() {
        return this.isEnable() ? renderTimes : 1;
    }

    @Override
    protected void loadToInit(JsonObject configObject) {
        if (configObject.has("render_times")) {
            this.renderTimes = configObject.get("render_times").getAsInt();
            if (this.renderTimes < 1) throw new IllegalArgumentException("The Render times should always be 1 or over");
        }
    }

    @Override
    protected void writeToJson(JsonObject configObject) {
        configObject.addProperty("render_times", this.renderTimes);
    }
}
