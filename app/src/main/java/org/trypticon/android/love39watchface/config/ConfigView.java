package org.trypticon.android.love39watchface.config;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.WearableConfigListener;

/**
 * Configuration screen.
 */
public class ConfigView extends LinearLayout {
    private WearableConfigListener listener;

    public ConfigView(Context context, AttributeSet attributes) {
        super(context, attributes);
        try {
            listener = (WearableConfigListener) context;
        } catch (ClassCastException e) {
            // Ignore it, for design mode.
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Context context = getContext();

        ((TextView) findViewById(R.id.config_textview_title)).setText(getContext().getString(R.string.config_title));

        CheckBox dozenalTimeCheckBox = (CheckBox) findViewById(R.id.config_dozenal_time_checkbox);
        ((TextView) findViewById(R.id.config_dozenal_time_label))
                .setText(context.getString(R.string.config_dozenal_time_label_text));

        CheckBox dozenalCalendarCheckBox = (CheckBox) findViewById(R.id.config_dozenal_calendar_checkbox);
        ((TextView) findViewById(R.id.config_dozenal_calendar_label))
                .setText(context.getString(R.string.config_dozenal_calendar_label_text));

        dozenalTimeCheckBox.setChecked(PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(ConfigKeys.DOZENAL_TIME_KEY, false));
        dozenalCalendarCheckBox.setChecked(PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(ConfigKeys.DOZENAL_CALENDAR_KEY, false));

        dozenalTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                listener.onConfigCompleted(ConfigKeys.DOZENAL_TIME_KEY, checked, false);
            }
        });
        dozenalCalendarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                listener.onConfigCompleted(ConfigKeys.DOZENAL_CALENDAR_KEY, checked, false);
            }
        });

    }
}
