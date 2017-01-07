/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.BoxInsetLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.trypticon.android.love39watchface.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom list view holding labeled previews to choose from, with some usability hacks
 * for wearable support.
 */
public class PreviewListView<S> extends ListView {
    private List<ListItem<S>> items = new ArrayList<>();

    private OnSettingChangedListener<S> onSettingChangedListener;

    public PreviewListView(Context context, AttributeSet attributes) {
        super(context, attributes);

        setChoiceMode(CHOICE_MODE_SINGLE);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView list, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    int position = pointToPosition(getWidth() / 2, getHeight() / 2);
                    if (position == -1) {
                        position = pointToPosition(getWidth() / 2, getHeight() / 2 + getDividerHeight());
                    }

                    if (position >= 0) {
                        scrollToItemAndSetChecked(position);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                PreviewListView.ListItem<S> item = (PreviewListView.ListItem<S>) getItemAtPosition(position);
                if (onSettingChangedListener != null) {
                    onSettingChangedListener.onSettingChanged(item.getSetting());
                }
            }
        });
    }

    public OnSettingChangedListener<S> getOnSettingChangedListener() {
        return onSettingChangedListener;
    }

    public void setOnSettingChangedListener(OnSettingChangedListener<S> onSettingChangedListener) {
        this.onSettingChangedListener = onSettingChangedListener;
    }

    public void scrollToItemAndSetChecked(S setting) {
        int size = items.size();
        for (int position = 0; position < size; position++) {
            ListItem item = items.get(position);
            if (item.getSetting().equals(setting)) {
                scrollToItemAndSetChecked(position);
                break;
            }
        }
    }

    public void scrollToItemAndSetChecked(int position) {
        View view = getChildAt(position - getFirstVisiblePosition());

        // Indirectly sets the item to checked
        performItemClick(view, position, getItemIdAtPosition(position));

        int margin = ((BoxInsetLayout.LayoutParams) getLayoutParams()).leftMargin;
        if (isAttachedToWindow()) {
            smoothScrollToPositionFromTop(position, margin);
        } else {
            setSelectionFromTop(position, margin);
        }
    }

    public List<ListItem<S>> getItems() {
        return items;
    }

    public void setItems(List<ListItem<S>> items) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        setAdapter(new PreviewListViewAdapter(getContext(), items));
    }


    public static class ListItem<S> {
        private final S setting;
        private final Drawable drawable;
        private final String labelText;

        public ListItem(S setting, Drawable drawable, String labelText) {
            this.setting = setting;
            this.drawable = drawable;
            this.labelText = labelText;
        }

        public S getSetting() {
            return setting;
        }
    }

    private class PreviewListViewAdapter extends BaseAdapter implements ListAdapter {
        private final LayoutInflater inflater;

        private final List<ListItem<S>> items;

        private PreviewListViewAdapter(Context context, List<ListItem<S>> items) {
            inflater = LayoutInflater.from(context);
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView instanceof ViewHolder) {
                holder = (ViewHolder) convertView;
            } else {
                holder = new ViewHolder(getContext(), inflater.inflate(R.layout.config_list_item, parent, false));
            }

            Drawable drawable = items.get(position).drawable;
            int layoutWidth = parent.getWidth();
            int layoutHeight = (int) (layoutWidth * (float) drawable.getIntrinsicHeight() /
                    (float) drawable.getIntrinsicWidth());
            LinearLayout.LayoutParams holderLayoutParams =
                    new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
            LinearLayout.LayoutParams viewLayoutParams =
                    new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
            if (position == 0) {
                holderLayoutParams.height += 20 * getResources().getDisplayMetrics().density;
                viewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            } else if (position == getCount() - 1) {
                holderLayoutParams.height += 20 * getResources().getDisplayMetrics().density;
                viewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            } else {
                viewLayoutParams.gravity = Gravity.CENTER;
            }
            holder.setLayoutParams(holderLayoutParams);
            holder.view.setLayoutParams(viewLayoutParams);

            holder.image.setImageDrawable(drawable);
            holder.text.setText(items.get(position).labelText);
            return holder;
        }
    }

    @SuppressLint("ViewConstructor") // The tools can't construct this private class anyway.
    private static class ViewHolder extends LinearLayout implements Checkable {
        private final View view;
        private final ImageView image;
        private final TextView text;

        private ViewHolder(Context context, View view) {
            super(context);

            addView(view);

            this.view = view;
            image = (ImageView) view.findViewById(R.id.drawable);
            text = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public void setChecked(boolean checked) {
            image.setActivated(checked);
        }

        @Override
        public boolean isChecked() {
            return image.isActivated();
        }

        @Override
        public void toggle() {
            setChecked(!isChecked());
        }
    }

    public interface OnSettingChangedListener<S> {
        void onSettingChanged(S setting);
    }
}
