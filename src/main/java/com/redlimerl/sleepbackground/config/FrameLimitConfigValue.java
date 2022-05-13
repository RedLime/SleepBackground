package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class FrameLimitConfigValue extends ConfigValue {

    private int frameLimit;

    public FrameLimitConfigValue(String keyName, int defaultLimit, String comment) {
        super(keyName, comment);
        this.frameLimit = defaultLimit;
    }

    @Override
    protected void loadToInit(JsonObject configObject) {
        if (configObject.has("fps_limit")) {
            this.frameLimit = configObject.get("fps_limit").getAsInt();
            if (this.frameLimit < 1) throw new IllegalArgumentException("The FPS limit should always be 1 or over");
        }
    }

    @Override
    protected void writeToJson(JsonObject configObject) {
        configObject.addProperty("fps_limit", this.frameLimit);
    }

    @Nullable
    public Integer getFrameLimit() {
        return this.isEnable() ? frameLimit : null;
    }
}
