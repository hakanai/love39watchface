package org.trypticon.dozenalwatchface;

import java.text.DateFormatSymbols;
import java.util.Locale;

/**
 * Formatter for dozenal dates.
 */
class DozenalDateFormat {
    private final DozenalNumberFormat dayOfMonthFormat = new DozenalNumberFormat(2);
    private DateFormatSymbols symbols;

    DozenalDateFormat(Locale locale) {
        symbols = DateFormatSymbols.getInstance(locale);
    }

    String formatDate(DozenalTime time) {
        StringBuilder builder = new StringBuilder(16);

        //TODO: Can we use DateFormat directly somehow? I think we'd have to make a custom Calendar?

        int dayOfWeek = time.getDayOfWeek();
        if (dayOfWeek > 0) {
            dayOfWeek++; // skip Monday
        }
        builder.append(symbols.getShortWeekdays()[dayOfWeek + 1]);
        builder.append(' ');

        builder.append(dayOfMonthFormat.format(time.getDayOfMonth()));
        builder.append(' ');

        int month = time.getMonth();
        String monthString;
        if (month == 13) {
            monthString = "Int"; //TODO: Localise this
        } else {
            monthString = symbols.getShortMonths()[(month + 1) % 12];
        }
        builder.append(monthString);

        return builder.toString();
    }
}
