package org.trypticon.dozenalwatchface.config;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.trypticon.dozenalwatchface.R;
import org.trypticon.dozenalwatchface.framework.WearableConfigListener;

/**
 * Configuration screen.
 */
public class DozenalConfigView extends LinearLayout {
    private WearableConfigListener listener;

    public DozenalConfigView(Context context, AttributeSet attributes) {
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

        ((TextView) findViewById(R.id.dozenal_config_textview_title)).setText(getContext().getString(R.string.dozenal_config_title));

        CheckBox dozenalTimeCheckBox = (CheckBox) findViewById(R.id.dozenal_config_time_checkbox);
        ((TextView) findViewById(R.id.dozenal_config_time_checkboxname))
                .setText(context.getString(R.string.dozenal_time_toggle_name));

        CheckBox dozenalCalendarCheckBox = (CheckBox) findViewById(R.id.dozenal_config_calendar_checkbox);
        ((TextView) findViewById(R.id.dozenal_config_calendar_checkboxname))
                .setText(context.getString(R.string.dozenal_calendar_toggle_name));

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
