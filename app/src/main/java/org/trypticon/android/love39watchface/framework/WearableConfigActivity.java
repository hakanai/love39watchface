package org.trypticon.android.love39watchface.framework;

import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.ustwo.clockwise.WearableAPIHelper;

/**
 * Activity which handles config requests on the wearable.
 */
public class WearableConfigActivity extends Activity implements WearableConfigListener {
    private static final String TAG = WearableConfigActivity.class.getSimpleName();

    private static final String DATA_PATH_CONFIG_UPDATE_WEARABLE = "/config_update/wearable";
    private static final String DATA_KEY_CONFIG_PREFS = "prefs";
    private static final String DATA_KEY_CONFIG_TIMESTAMP = "timestamp";

    private static final String COMPONENT_NAME = "android.support.wearable.watchface.extra.WATCH_FACE_COMPONENT";
    private static final String CONFIG = "config";

    private WearableAPIHelper wearableAPIHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note that it is possible to load different resources by examining or using the
        // ComponentName -- it is the name of the watch face Service the user has requested to configure.
        ComponentName componentName = getIntent().getParcelableExtra(COMPONENT_NAME);

        String resourceName = CONFIG;
        final int layoutId = getResources().getIdentifier(resourceName, "layout", componentName.getPackageName());
        if (layoutId <= 0) {
            Log.e(TAG, "Could not find resource id for: " + resourceName);
            finish();
            return;
        }

        setContentView(layoutId);

        wearableAPIHelper = new WearableAPIHelper(this, new WearableAPIHelper.WearableAPIHelperListener() {
            @Override
            public void onWearableAPIConnected(GoogleApiClient apiClient) {
            }

            @Override
            public void onWearableAPIConnectionSuspended(int cause) {
            }

            @Override
            public void onWearableAPIConnectionFailed(ConnectionResult result) {
            }
        });
    }


    @Override
    public void onConfigCompleted(String key, Object value, boolean finish) {
        // Store the value locally.
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        putObject(editor, key, value);
        editor.apply();

        // Send the changed preference to the companion. The path indicates the source of the change.
        DataMap prefsDataMap = new DataMap();
        putObject(prefsDataMap, key, value);

        // We have to make the data map unique to ensure Wear API sends it to the wearable. This is required because
        // it is valid for the companion app to send the same config change multiple times if the wearable was
        // alternately changing the config value to something else.
        DataMap dataMap = new DataMap();
        dataMap.putDataMap(DATA_KEY_CONFIG_PREFS, prefsDataMap);
        dataMap.putLong(DATA_KEY_CONFIG_TIMESTAMP, System.currentTimeMillis());

        wearableAPIHelper.putDataMap(DATA_PATH_CONFIG_UPDATE_WEARABLE, dataMap, null);

        if (finish) {
            finish();
        }
    }

    private static void putObject(SharedPreferences.Editor editor, String key, Object value) {
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            throw new IllegalArgumentException("Argument type not supported yet: " + value);
        }
    }

    private static void putObject(DataMap dataMap, String key, Object value) {
        if (value instanceof Boolean) {
            dataMap.putBoolean(key, (Boolean) value);
        } else {
            throw new IllegalArgumentException("Argument type not supported yet: " + value);
        }
    }
}
