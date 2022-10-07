package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;

public class PollingRateConfigValue extends ConfigValue {

    public PollingRateConfigValue(String keyName, String comment) {
        super(keyName, comment, false);
    }

    @Override
    protected void loadToInit(JsonObject configObject) {

    }

    @Override
    protected void writeToJson(JsonObject configObject) {

    }
}
