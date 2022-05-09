package com.redlimerl.sleepbackground;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class SleepBackground implements ClientModInitializer {

    public static int BG_FRAME_RATE = 1;

    @Override
    public void onInitializeClient() {
        File file = FabricLoader.getInstance().getConfigDir().resolve("sleepbg.txt").toFile();
        try {
            if (file.exists()) {
                BG_FRAME_RATE = Integer.parseInt(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            } else {
                FileUtils.writeStringToFile(file, String.valueOf(BG_FRAME_RATE), StandardCharsets.UTF_8);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
