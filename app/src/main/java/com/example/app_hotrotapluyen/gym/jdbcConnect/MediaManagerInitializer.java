package com.example.app_hotrotapluyen.gym.jdbcConnect;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class MediaManagerInitializer {

    private static boolean isMediaManagerInitialized = false;

    private MediaManagerInitializer() {
        // Private constructor to prevent instantiation
    }

    public static void initializeMediaManager(Context context) {
        if (!isMediaManagerInitialized) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dlpqr1jhm");
            config.put("api_key", "187745367395712");
            config.put("api_secret", "-_7wEP5n5Il_4lpiZRm2f1XgAxg");

            com.cloudinary.android.MediaManager.init(context, config);

            isMediaManagerInitialized = true;
        }
    }
}
