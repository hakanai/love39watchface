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
    private WearableConfigListener mListener;

    public DozenalConfigView(Context context, AttributeSet attributes) {
        super(context, attributes);
        try {
            mListener = (WearableConfigListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement WatchFaceConfigListener");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Context context = getContext();

        ((TextView) findViewById(R.id.dozenal_config_textview_title)).setText(getContext().getString(R.string.dozenal_config_title));

        CheckBox themeCheckBox = (CheckBox) findViewById(R.id.dozenal_config_checkbox);
        TextView themeTextView = (TextView)findViewById(R.id.dozenal_config_checkboxname);
        themeTextView.setText(context.getString(R.string.dozenal_toggle_name));

        boolean dozenalActive = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(ConfigKeys.DOZENAL_KEY, false);
        themeCheckBox.setChecked(dozenalActive);

        themeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                mListener.onConfigCompleted(ConfigKeys.DOZENAL_KEY, checked, false);
            }
        });

    }
}
