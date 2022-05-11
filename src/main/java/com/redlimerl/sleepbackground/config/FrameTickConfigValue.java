package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class FrameTickConfigValue extends FrameLimitConfigValue {

    private int maxTicks;

    public FrameTickConfigValue(String keyName, int defaultFrameLimit, int defaultMaxTick) {
        super(keyName, defaultFrameLimit);
        this.maxTicks = defaultMaxTick;
    }

    @Override
    protected void loadToInit(JsonObject configObject) {
        super.loadToInit(configObject);
        if (configObject.has("max_ticks")) {
            this.maxTicks = configObject.get("max_ticks").getAsInt();
        }
    }

    @Override
    protected void writeToJson(JsonObject configObject) {
        super.writeToJson(configObject);
        configObject.addProperty("max_ticks", this.maxTicks);
    }

    public int getMaxTicks() {
        return this.isEnable() ? this.maxTicks : 0;
    }

    @Override
    public FrameTickConfigValue register() {
        super.register();
        return this;
    }
}
