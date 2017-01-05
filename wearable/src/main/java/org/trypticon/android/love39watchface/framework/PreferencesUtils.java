package org.trypticon.android.love39watchface.framework;

import android.content.SharedPreferences;

import com.google.android.gms.wearable.DataMap;

/**
 * Utilities for getting/settings preferences.
 */
public class PreferencesUtils {

    public static <E extends Enum<E>> E getEnum(SharedPreferences sharedPreferences, String key, Class<E> enumClass, E defaultValue) {
        String string = sharedPreferences.getString(key, null);
        if (string != null) {
            try {
                return Enum.valueOf(enumClass, string);
            } catch (IllegalArgumentException e) {
                // fall through to return default
            }
        }
        return defaultValue;
    }

    public static void putObject(SharedPreferences.Editor editor, String key, Object value) {
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Enum) {
            editor.putString(key, ((Enum) value).name());
        } else {
            throw new IllegalArgumentException("Argument type not supported yet: " + value);
        }
    }

    public static void putObject(DataMap dataMap, String key, Object value) {
        if (value instanceof Boolean) {
            dataMap.putBoolean(key, (Boolean) value);
        } else if (value instanceof Enum) {
            dataMap.putString(key, ((Enum) value).name());
        } else {
            throw new IllegalArgumentException("Argument type not supported yet: " + value);
        }
    }
}
