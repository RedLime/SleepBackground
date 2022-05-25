package com.redlimerl.sleepbackground.config;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigValue {
    private final String keyName;
    private final String comment;
    private final boolean defaultEnable;
    private boolean enable = true;

    public ConfigValue(String keyName, String comment, boolean defaultEnable) {
        this.keyName = keyName;
        this.comment = comment;
        this.defaultEnable = defaultEnable;
    }

    public ConfigValue(String keyName, String comment) {
        this(keyName, comment, true);
    }

    public final String getKeyName() {
        return keyName;
    }

    public final String getComment() {
        return comment;
    }

    public final boolean isEnable() {
        return enable;
    }

    @Nullable
    private JsonObject getJsonObjectFromConfig(JsonObject jsonObject) {
        if (jsonObject.has(this.getKeyName()) && jsonObject.get(this.getKeyName()).isJsonObject()) {
            return jsonObject.getAsJsonObject(this.getKeyName());
        }
        return null;
    }

    public final void load(JsonObject jsonObject) {
        JsonObject configObject = this.getJsonObjectFromConfig(jsonObject);
        if (configObject == null) {
            this.enable = this.defaultEnable;
            return;
        }

        try {
            if (configObject.has("enable"))
                this.enable = configObject.get("enable").getAsBoolean();
            this.loadToInit(configObject);
        } catch (Throwable e) {
            this.enable = false;
            e.printStackTrace();
        }
    }

    protected abstract void loadToInit(JsonObject configObject);

    public final void writeToJsonObject(JsonObject jsonObject) {
        JsonObject configObject = new JsonObject();

        configObject.addProperty("_description", this.comment);
        configObject.addProperty("enable", this.enable);
        this.writeToJson(configObject);

        jsonObject.add(this.getKeyName(), configObject);
    }

    protected abstract void writeToJson(JsonObject configObject);

    @Override
    public final String toString() {
        return "ConfigValue{" +
                "keyName='" + keyName + '\'' +
                '}';
    }
}
