package org.trypticon.android.love39watchface.time;

import java.text.FieldPosition;

/**
 * Abstraction of date formatting.
 */
public abstract class DateFormat {
    public abstract void formatDate(Time time, StringBuffer buffer, FieldPosition fieldPosition);
}
