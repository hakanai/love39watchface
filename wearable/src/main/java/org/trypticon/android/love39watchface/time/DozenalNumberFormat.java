/*
 * Copyright © 2016-2018 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.time;

import android.icu.text.NumberFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * Formatter for dozenal numbers.
 */
// Never serialised.
@SuppressWarnings("serial")
class DozenalNumberFormat extends NumberFormat {
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '\u218A', '\u218B'
    };
    private static final int MAX_LENGTH = 23; // Max chars in result (conservative)

    private final StringBuilder tempBuilder = new StringBuilder(MAX_LENGTH);

    DozenalNumberFormat() {
        tempBuilder.setLength(MAX_LENGTH);
    }

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

        long value = number;

        boolean negative = false;
        if (value < 0) {
            negative = true;
        } else {
            value = -value;
        }

        int cursor = MAX_LENGTH;

        do {
            long q = value / 12;
            --cursor;
            tempBuilder.setCharAt(cursor, DIGITS[(int) (12 * q - value)]);
            value = q;
        } while (value != 0);

        if (negative) {
            --cursor;
            tempBuilder.setCharAt(cursor, '-');
        }

        int resultOffset = cursor;
        int resultLength = MAX_LENGTH - cursor;

        int padSize = Math.max(0, getMinimumIntegerDigits() - resultLength);
        for (int i = 0; i < padSize; i++) {
            result.append('0');
        }
        result.append(tempBuilder, resultOffset, MAX_LENGTH);

        return result;
    }

    @Override
    public StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new UnsupportedOperationException("Not supported yet. Maybe if I get bored?");
    }

    @Override
    public StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new UnsupportedOperationException("Not supported yet. Maybe if I get bored?");
    }

    @Override
    public StringBuffer format(android.icu.math.BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new UnsupportedOperationException("Not supported yet. Maybe if I get bored?");
    }

    @Override
    public Number parse(String string, ParsePosition position) {
        throw new UnsupportedOperationException("Not supported yet. Maybe if I get bored?");
    }
}
