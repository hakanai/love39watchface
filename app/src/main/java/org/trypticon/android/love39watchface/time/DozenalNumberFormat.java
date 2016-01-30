package org.trypticon.android.love39watchface.time;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Formatter for dozenal numbers.
 */
class DozenalNumberFormat extends NumberFormat {

    @Override
    public StringBuffer format(double number, StringBuffer buffer, FieldPosition field) {
        throw new UnsupportedOperationException("Not supported yet. Maybe if I get bored?");
    }

    @Override
    public StringBuffer format(long number, StringBuffer result, FieldPosition field) {
        // Super-naive implementation for now.
        if (number < 0) {
            throw new UnsupportedOperationException("Unsupported value: " + number);
        }

        String noPadding = Long.toString(number, 12);
        int startOffset = result.length();

        int padSize = Math.max(0, getMinimumIntegerDigits() - noPadding.length());
        for (int i = 0; i < padSize; i++) {
            result.append('0');
        }
        result.append(noPadding);

        int length = result.length();
        for (int i = startOffset; i < length; i++) {
            switch (result.charAt(i)) {
                case 'a':
                    result.setCharAt(i, '\u218A');
                    break;
                case 'b':
                    result.setCharAt(i, '\u218B');
                    break;
            }
        }

        return result;
    }

    @Override
    public Number parse(String string, ParsePosition position) {
        throw new UnsupportedOperationException("Not supported yet. Maybe if I get bored?");
    }
}
