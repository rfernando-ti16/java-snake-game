package com.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.config.ConfigManager;

public class Messages {
    private static final String BUNDLE_NAME = "com.i18n.messages";

    private Messages() {
    }

    public static String getString(String key) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, 
                                    ConfigManager.getInstance().getLocale());
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            e.printStackTrace();
            return '!' + key + '!';
        }
    }
}
