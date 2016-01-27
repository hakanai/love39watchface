package org.trypticon.android.love39watchface.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.wearable.view.ActionPage;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.ustwo.clockwise.WatchShape;

import org.trypticon.android.love39watchface.DateStyle;
import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.TimeStyle;
import org.trypticon.android.love39watchface.framework.WearableConfigListener;

/**
 * Configuration screen.
 */
public class ConfigView extends FrameLayout {
    private WearableConfigListener listener;
    private WatchShape watchShape;

    private boolean dozenalTime;
    private boolean dozenalCalendar;

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

        final Context context = getContext();

        requestApplyInsets();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        dozenalTime = sharedPreferences.getBoolean(ConfigKeys.DOZENAL_TIME_KEY, false);
        dozenalCalendar = sharedPreferences.getBoolean(ConfigKeys.DOZENAL_CALENDAR_KEY, false);

        GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ConfigGridPagerAdapter());

        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        watchShape = insets.isRound() ? WatchShape.CIRCLE : WatchShape.SQUARE;
        return super.onApplyWindowInsets(insets);
    }

    private class ConfigGridPagerAdapter extends GridPagerAdapter {
        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int row) {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int row, int column) {
            View view;
            if (column < 2) {
                Context context = getContext();
                View pageView = LayoutInflater.from(getContext()).inflate(R.layout.config_list_page, container, false);
                PreviewListView list = (PreviewListView) pageView.findViewById(R.id.list);

                if (column == 0) {
                    list.setItems(new PreviewListView.ListItem[]{
                            new PreviewListView.ListItem(
                                    SampleDrawable.createForTime(context, watchShape, TimeStyle.CLASSIC),
                                    getContext().getString(R.string.config_time_classic)),
                            new PreviewListView.ListItem(
                                    SampleDrawable.createForTime(context, watchShape, TimeStyle.DOZENAL),
                                    getContext().getString(R.string.config_time_dozenal))
                    });
                    list.setSelection(dozenalTime ? 1 : 0);
                    list.setItemChecked(dozenalTime ? 1 : 0, true);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dozenalTime = position == 1;
                        }
                    });
                } else {
                    list.setItems(new PreviewListView.ListItem[] {
                            new PreviewListView.ListItem(
                                    SampleDrawable.createForDate(context, watchShape, DateStyle.CLASSIC),
                                    getContext().getString(R.string.config_calendar_classic)),
                            new PreviewListView.ListItem(
                                    SampleDrawable.createForDate(context, watchShape, DateStyle.DOZENAL),
                                    getContext().getString(R.string.config_calendar_dozenal))
                    });
                    list.setSelection(dozenalCalendar ? 1 : 0);
                    list.setItemChecked(dozenalCalendar ? 1 : 0, true);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dozenalCalendar = position == 1;
                        }
                    });
                }

                view = pageView;
            } else {
                ActionPage actionPage = (ActionPage) LayoutInflater.from(getContext()).inflate(R.layout.config_confirmation, container, false);
                actionPage.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onConfigCompleted(ConfigKeys.DOZENAL_TIME_KEY, dozenalTime, false);
                        listener.onConfigCompleted(ConfigKeys.DOZENAL_CALENDAR_KEY, dozenalCalendar, false);

                        Context context = getContext();
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                });
                view = actionPage;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int row, int column, Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
