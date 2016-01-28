package org.trypticon.android.love39watchface.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.trypticon.android.love39watchface.R;

/**
 * Custom list view holding labeled previews to choose from, with some usability hacks
 * for wearable support.
 */
public class PreviewListView extends ListView {
    private ListItem[] items = new ListItem[0];

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
                        smoothScrollToPosition(position);
                        View view = getChildAt(position - getFirstVisiblePosition());
                        performItemClick(view, position, getItemIdAtPosition(position));
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public ListItem[] getItems() {
        return items.clone();
    }

    public void setItems(ListItem[] items) {
        this.items = items.clone();
        setAdapter(new PreviewListViewAdapter(getContext(), items));
    }


    public static class ListItem {
        private final Drawable drawable;
        private final String labelText;
        public ListItem(Drawable drawable, String labelText) {
            this.drawable = drawable;
            this.labelText = labelText;
        }
    }

    private class PreviewListViewAdapter extends BaseAdapter implements ListAdapter {
        private final LayoutInflater inflater;

        private final ListItem[] items;

        private PreviewListViewAdapter(Context context, ListItem[] items) {
            inflater = LayoutInflater.from(context);
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView instanceof ViewHolder) {
                holder = (ViewHolder) convertView;
            } else {
                holder = new ViewHolder(getContext(), inflater.inflate(R.layout.config_list_item, parent, false));
            }

            // Deliberately making this a square.
            LinearLayout.LayoutParams holderLayoutParams =
                    new LinearLayout.LayoutParams(parent.getWidth(), parent.getWidth());
            LinearLayout.LayoutParams viewLayoutParams =
                    new LinearLayout.LayoutParams(parent.getWidth(), parent.getWidth());
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

            holder.image.setImageDrawable(items[position].drawable);
            holder.text.setText(items[position].labelText);
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
}
